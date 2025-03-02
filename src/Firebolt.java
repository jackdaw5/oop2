public class Firebolt implements Effect {
    private int damage;
    private boolean hasBurnedAlready = false;

    public Firebolt(int damage) {
        this.damage = damage;
    }

    @Override
    public void onHit(Dude target) {}

    @Override
    public void onTurnStart(Dude target) {}

    @Override
    public void onTurnEnd(Dude target) {
        if (!hasBurnedAlready) {
            target.health -= damage;
            hasBurnedAlready = true;
            System.out.println(target.getName() + " põleb ja kaotab " + damage + " HP!");
        }
    }

    @Override
    public int requiredActionPoints() { return 2; }
    @Override
    public boolean isExpired() { return hasBurnedAlready; }
}
