package engine.rule;

import engine.action.api.Action;
import engine.entity.manager.EntityInstanceManager;

import java.util.Collection;

public interface Rule {
    String getName();
    boolean isActive(int currTick);
    int getTicksForActivations();
    double getProbForActivations();
    Collection<Action> actionsToPerform();
    void addAction(Action actionToAdd);
    public void runRule(EntityInstanceManager manager);

}
