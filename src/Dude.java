import java.util.*;

public abstract class Dude {
    protected String name;
    protected int accuracy, armor, health, maxHealth, actionPoints, maxActionPoints;
    protected List<Effect> activeEffects = new ArrayList<>();

    public Dude(String name, int accuracy, int armor, int health, int actionPoints) {
        this.name = name;
        this.accuracy = accuracy;
        this.armor = armor;
        this.health = this.maxHealth = health;
        this.actionPoints = this.maxActionPoints = actionPoints;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public void takeTurn(Dude attackTarget) {
        System.out.println("\n" + name + " teeb oma käigu!");

        for (Effect effect : activeEffects) {
            effect.onTurnStart(this);
        }

        activeEffects.removeIf(Effect::isExpired);

        Effect chosenEffect = chooseEffect();
        if (chosenEffect != null && actionPoints >= chosenEffect.requiredActionPoints()) {
            actionPoints -= chosenEffect.requiredActionPoints();
            if (attackHits(attackTarget)) {
                System.out.println(name + " ründab " + attackTarget.getName() + " oskusel: " + chosenEffect.getClass().getSimpleName());
                chosenEffect.onHit(attackTarget);
                attackTarget.activeEffects.add(chosenEffect);
            } else {
                System.out.println(name + " rünnak ei tabanud!");
            }
        } else {
            System.out.println(name + " ei saa rünnata (pole piisavalt action pointe).");
        }

        for (Effect effect : activeEffects) {
            effect.onTurnEnd(this);
        }

        actionPoints = maxActionPoints;
    }

    protected boolean attackHits(Dude target) {
        int diceRoll = new Random().nextInt(20) + 1;  // d20 veeretus (1-20)
        return diceRoll + accuracy >= target.armor;
    }

    public String getName() { return name; }

    protected abstract Effect chooseEffect();
}
