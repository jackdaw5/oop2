package dnd;

public interface Effect {
    void onHit(Dude target);
    void onTurnStart(Dude target);
    void onTurnEnd(Dude target);
    int requiredActionPoints();
    boolean isExpired();
}
