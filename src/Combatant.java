import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import pirate.Weapon;

public sealed abstract class Combatant implements Player permits Pirate, Islander, Soldier{

    private final String name;
    private final Map<String, Integer> gameData;
    private Weapon currentWeapon;
    
    public Combatant(String name) {
        this.name = name;
    }

    public Combatant(String name, Map<String, Integer> gameData) {
        this.name = name;
        if (gameData != null) {
            this.gameData.putAll(gameData);
        }
    }
    // ----------------------------- INSTANCE INTIALIZER
    {
        gameData = new HashMap<>(Map.of(
            "health", 100,
            "score", 0
        ));
    }

    public Weapon getCurrentWeapon() {
        return currentWeapon;
    }

    public void setCurrentWeapon(Weapon currentWeapon) {
        this.currentWeapon = currentWeapon;
    }

    int value(String name) {
        return gameData.get(name);
    }

    @Override
    public String name() {
        return name;
    }

    protected void setValue(String name, int value) {
        gameData.put(name, value);
    }

    protected void adjustValue(String name, int adjValue) {
        gameData.compute(name, (k,v) -> v += adjValue);
    }

    protected void adjustHealth(int adjValue) {
        int health = value("health");
        health += adjValue;
        health = (health < 0) ? 0 : ((health > 100) ? 100 : health);
        setValue("health", health);
    }

    boolean useWeapon(Combatant opponent) {
        System.out.print(name + " used the " + currentWeapon);
        if (new Random().nextBoolean()) {
            System.out.println(" and HIT *** " + opponent.name + "! ***");
            opponent.adjustHealth(-currentWeapon.getHitPoints());
            System.out.printf("%s's health = %d, %s's health = %d%n", 
                name, value("health"), 
                opponent.name(), opponent.value("health"));
            adjustValue("score", 50);
        } else {
            System.out.println(" and missed!");
        }
        return (opponent.value("health") <= 0); // if opponent is killed, will return true
    }

    @Override
    public String toString() {
        return name;
    }

    public String information() {
        return name + " " + gameData;
    }
}