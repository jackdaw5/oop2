public class Knockdown implements Effect {
    @Override
    public void onHit(Dude target) {
        target.actionPoints = 0;
        System.out.println(target.getName() + " ei saa järgmises käigus tegutseda!");
    }

    @Override
    public void onTurnStart(Dude target) {}
    @Override
    public void onTurnEnd(Dude target) {}
    @Override
    public int requiredActionPoints() { return 3; }
    @Override
    public boolean isExpired() { return true; }
}
