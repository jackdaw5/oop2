package dnd;

public class Fighter extends Dude {
    public Fighter(String name) {
        super(name, 5, 15, 100, 3);
    }

    @Override
    protected Effect chooseEffect() {
        return new WeaponAttack(20);
    }
}
