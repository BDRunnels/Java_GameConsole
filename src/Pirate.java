import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import pirate.Feature;
import pirate.Loot;
import pirate.Weapon;

public final class Pirate extends Combatant {

    // private final String name;
    // private final Map<String, Integer> gameData;
    private final List<Town> townsVisited = new LinkedList<>();
    private List<Loot> loot;
    private List<Combatant> opponents;
    private List<Feature> features;
    // private Weapon currentWeapon;
    
    public Pirate(String name) {
        super(name, Map.of("level", 0, "townIndex", 0));
        visitTown();
    }
    // -----------------------------
    // {
    //     gameData = new HashMap<>(Map.of(
    //         "health", 100,
    //         "score", 0,
    //         "level", 0,
    //         "townIndex", 0
    //     ));
    //     visitTown();
    // }

    // public Weapon getCurrentWeapon() {
    //     return currentWeapon;
    // }

    // public void setCurrentWeapon(Weapon currentWeapon) {
    //     this.currentWeapon = currentWeapon;
    // }

    // int value(String name) {
    //     return gameData.get(name);
    // }

    // @Override
    // public String name() {
    //     return name;
    // }

    // private void setValue(String name, int value) {
    //     gameData.put(name, value);
    // }

    // private void adjustValue(String name, int adjValue) {
    //     gameData.compute(name, (k,v) -> v += adjValue);
    // }

    // private void adjustHealth(int adjValue) {
    //     int health = value("health");
    //     health += adjValue;
    //     health = (health < 0) ? 0 : ((health > 100) ? 100 : health);
    //     setValue("health", health);
    // }

    boolean useWeapon() {
        // System.out.println("Used the " + super.getCurrentWeapon());
        // return visitNextTown();
        int count = opponents.size();
        if (count > 0) {
            int opponentIndex = count - 1;
            if (count > 1) {
                opponentIndex = new Random().nextInt(count + 1);
            }
            Combatant combatant = opponents.get(opponentIndex);
            if (super.useWeapon(combatant)) {
                opponents.remove(opponentIndex);
            } else {
                return combatant.useWeapon(this);
            }
        }
        return false;
    }

    boolean visitTown() {
        
        List<Town> levelTowns = PirateGame.getTowns(value("level"));
        if (levelTowns == null) return true;
        Town town = levelTowns.get(value("townIndex"));
        if (town != null) {
            townsVisited.add(town);
            loot = town.loot();
            opponents = town.opponents();
            features = town.features();
            return false;
        }
        return true;
    }

    boolean hasExperiences() {
        return (features != null && features.size() > 0);
    }

    boolean hasOpponents() {
        return (opponents != null && opponents.size() > 0);
    }

    public String information() {

        var current = ((LinkedList<Town>) townsVisited).getLast();
        String[] simpleName = new String[townsVisited.size()];
        Arrays.setAll(simpleName, i -> townsVisited.get(i).name());
        return "---> " + current +
            "\n" + super.information() +
            "\n\ttownsVisited = " + Arrays.toString(simpleName);
    }

    boolean findLoot() {
        if (loot.size() > 0) {
            Loot item = loot.remove(0);
            System.out.println("Found " + item + "!");
            adjustValue("score", item.getWorth());
            System.out.println(name() + "'s score is now " + value("score"));
        }
        if (loot.size() == 0) {
            return visitNextTown();
        }
        return false;
    }

    boolean experienceFeature() {
        if (features.size() > 0) {
            Feature item = features.remove(0);
            System.out.println("Ran into " + item + "!");
            adjustHealth(item.getHealthPoints());
            System.out.println(name() + "'s health is now " + value("health"));
        }
        return (value("health") <= 0);
    }

    private boolean visitNextTown() {
        int townIndex = value("townIndex");
        var towns = PirateGame.getTowns(value("level"));
        if (towns == null) return true;

        if (townIndex >= (towns.size() - 1)) {
            System.out.println("Leveled up! Bonus: 500 points");
            adjustValue("score", 500);
            adjustValue("level", 1);
            setValue("townIndex", 0);
        } else {
            System.out.println("Sailing to next town! Bonus: 50 points");
            adjustValue("townIndex", 1);
            adjustValue("score", 50);
        }
        return visitTown();
    }
}