import pirate.Weapon;

public class Main {
    public static void main(String[] args)  {
        
        // var console = new GameConsole<>(new ShooterGame("The Shootout Game"));

        // int playerIndex = console.addPlayer();
        // console.playGame(playerIndex);

        Weapon weapon = Weapon.getWeaponByChar('P');
        System.out.println("weapon = " + weapon + ", hitPoints = " + weapon.getHitPoints() + ", minLevel = " + weapon.getMinLevel());

        var list = Weapon.getWeaponsByLevel(1);
        list.forEach(System.out::println);

        Pirate bryan = new Pirate("Bryan");
        System.out.println(bryan);

        PirateGame.getTowns(0).forEach(t -> System.out.println(t.information()));
        System.out.println("-".repeat(50));
        PirateGame.getTowns(1).forEach(t -> System.out.println(t.information()));

        ///////
                // TESTING TOWN
        // Town bridgeTown = new Town("Bridgetown", "Barbados", 0);
        // System.out.println(bridgeTown);
        // System.out.println(bridgeTown.information());
        System.out.println("-".repeat(50));
        var console = new GameConsole<>(new PirateGame("The Pirate Game"));
        int playerIndex = console.addPlayer();
        console.playGame(playerIndex);
    }
}
