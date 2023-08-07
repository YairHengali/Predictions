package engine.action.impl.condition.api;

import engine.action.api.Action;
import engine.entity.EntityInstance;
import engine.property.api.PropertyInstance;

public interface Condition {
    boolean evaluateCondition();
    void addActionToThen(Action actionToAdd);
    void addActionToElse(Action actionToAdd);

}
