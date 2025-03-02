public class WeaponAttack implements Effect {
    private int damage;

    public WeaponAttack(int damage) {
        this.damage = damage;
    }

    @Override
    public void onHit(Dude target) {
        target.health -= damage;
        System.out.println(target.getName() + " kaotab " + damage + " HP!");
    }

    @Override
    public void onTurnStart(Dude target) {}
    @Override
    public void onTurnEnd(Dude target) {}
    @Override
    public int requiredActionPoints() { return 2; }
    @Override
    public boolean isExpired() { return true; }
}
