package engine;

import engine.action.api.Action;
import engine.action.api.ClacType;
import engine.action.impl.Calculation;
import engine.action.impl.Increase;
import engine.entity.EntityInstance;
import engine.property.Property;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Rule {
    private final String name;
    private int howManyTicksForActivation = 1;
    private double probabilityForActivation = 1;
    private final Collection<String> ActionsNames= new ArrayList<>();
    private final Collection<Action> Actions = new ArrayList<>();

    public Rule(String name, Integer howManyTicksForActivation, Double probabilityForActivation) {
        this.name = name;
        if (howManyTicksForActivation != null) {
            this.howManyTicksForActivation = howManyTicksForActivation;
        }
        if (probabilityForActivation != null) {
            this.probabilityForActivation = probabilityForActivation;
        }
    }

    public void addIncreaseAction(List<EntityInstance> mainEntityList, String propertyName, Number amountToIncrease)
    {
        Action action = new Increase(mainEntityList, propertyName, amountToIncrease);
        Actions.add(action);
    }

    public void addCalculationAction(List<EntityInstance> mainEntityList, String propertyName, Number argument1, Number argument2, ClacType calcType)
    {
        Action action = new Calculation(mainEntityList, propertyName, argument1, argument2, calcType);
        Actions.add(action);
    }

    public void runRule()
    {
        for (Action action: Actions) {
            action.Run();
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Rule:\n");
        stringBuilder.append("name='").append(name).append("'\n");
        stringBuilder.append("howManyTicksForActivation=").append(howManyTicksForActivation).append("\n");
        stringBuilder.append("probabilityForActivation=").append(probabilityForActivation).append("\n");
        stringBuilder.append("number of actions=").append(Actions.size()).append("\n");

        if(!Actions.isEmpty()) {
            stringBuilder.append("actions names:").append("\n");
            for (Action action : Actions) {
                stringBuilder.append(action.getClass().getSimpleName()).append("\n");// TODO: Actions names (?? Increase, Decrease ??)
            }
        }

        return stringBuilder.toString();
    }
}
