public class Main {
    public static void main(String[] args) {
        Dude fighter = new Fighter("Ragnar");
        Dude wizard = new Wizard("Merlin");

        System.out.println("Lahing algab: " + fighter.getName() + " vs " + wizard.getName());

        while (fighter.isAlive() && wizard.isAlive()) {
            fighter.takeTurn(wizard);
            if (!wizard.isAlive()) {
                System.out.println("\n" + wizard.getName() + " on langenud! " + fighter.getName() + " võitis!");
                break;
            }

            wizard.takeTurn(fighter);
            if (!fighter.isAlive()) {
                System.out.println("\n" + fighter.getName() + " on langenud! " + wizard.getName() + " võitis!");
            }
        }
    }
}
