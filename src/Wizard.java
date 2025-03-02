public class Wizard extends Dude {
    public Wizard(String name) {
        super(name, 8, 10, 80, 4);
    }

    @Override
    protected Effect chooseEffect() {
        return new Firebolt(15);
    }
}
