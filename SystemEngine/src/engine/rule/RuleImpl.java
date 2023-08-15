package engine.rule;

import engine.action.api.Action;
import engine.action.api.ClacType;
import engine.action.impl.Calculation;
import engine.action.impl.Increase;
import engine.context.Context;
import engine.context.ContextImpl;
import engine.entity.EntityInstance;
import engine.entity.manager.EntityInstanceManager;
import engine.environment.active.ActiveEnvironmentVariables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class RuleImpl implements Rule {
    private final String name;
    private int howManyTicksForActivation = 1;
    private double probabilityForActivation = 1;
    private final Collection<Action> actions = new ArrayList<>();

    public RuleImpl(String name, Integer howManyTicksForActivation, Double probabilityForActivation) {
        this.name = name;
        if (howManyTicksForActivation != null) {
            this.howManyTicksForActivation = howManyTicksForActivation;
        }
        if (probabilityForActivation != null) {
            this.probabilityForActivation = probabilityForActivation;
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean isActive(int currTick) {
        if(currTick % howManyTicksForActivation == 0){ // TODO: need to check the first tick number (0 or 1)
            Random random = new Random();
            return random.nextDouble() < this.probabilityForActivation;
        }
        return false;
    }

    @Override
    public int getTicksForActivations() {
        return this.howManyTicksForActivation;
    }

    @Override
    public double getProbForActivations() {
        return this.probabilityForActivation;
    }

    @Override
    public Collection<Action> getActions() {
        return this.actions;
    }

    public void addAction(Action action) {
        actions.add(action);
    }

    @Override
    public void runRule(EntityInstanceManager manager, ActiveEnvironmentVariables activeEnvironmentVariables)
    {
        for (Action action: actions) {
            for (EntityInstance entityInstance : manager.getInstancesListByName(action.getMainEntityName()))
            {
                Context context = new ContextImpl(entityInstance, manager, activeEnvironmentVariables);
                try {
                    action.Run(context);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            manager.killEntities(); //TODO: MAYBE AFTER THIS LOOP
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("RuleImpl:\n");
        stringBuilder.append("name='").append(name).append("'\n");
        stringBuilder.append("howManyTicksForActivation=").append(howManyTicksForActivation).append("\n");
        stringBuilder.append("probabilityForActivation=").append(probabilityForActivation).append("\n");
        stringBuilder.append("number of actions=").append(actions.size()).append("\n");

        if(!actions.isEmpty()) {
            stringBuilder.append("actions names:").append("\n");
            for (Action action : actions) {
                stringBuilder.append(action.getClass().getSimpleName()).append("\n");// TODO: actions names (?? Increase, Decrease ??)
            }
        }

        return stringBuilder.toString();
    }
}
