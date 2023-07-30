package engine;

import engine.action.Action;
import engine.action.Increase;

import java.util.ArrayList;
import java.util.Collection;

public class Rule {
    private final String name;
    private final int howManyTicksForActivation = 1;
    private final float probabilityForActivation = 1;

    private final Collection<Action> Actions = new ArrayList<>();

    public Rule(String name) {
        this.name = name;
    }

    public void addIncreaseAction(Entity mainEntity, Property<Number> property, Number amountToIncrease)
    {
        Action action = new Increase(mainEntity, property, amountToIncrease);
        Actions.add(action);
    }
}
