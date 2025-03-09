package dnd;

public class Spiderweb implements Effect {
    private int effectDuration = 2;

    @Override
    public void onHit(Dude target) {}

    @Override
    public void onTurnStart(Dude target) {
        if (effectDuration > 0) {
            target.actionPoints -= 1;
            effectDuration--;
            System.out.println(target.getName() + " on kinni! -1 action point.");
        }
    }

    @Override
    public void onTurnEnd(Dude target) {}

    @Override
    public int requiredActionPoints() { return 1; }
    @Override
    public boolean isExpired() { return effectDuration <= 0; }
}
