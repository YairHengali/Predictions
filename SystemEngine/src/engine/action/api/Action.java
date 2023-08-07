package engine.action.api;

import engine.context.Context;
import engine.entity.EntityDefinition;
import engine.entity.manager.EntityInstanceManager;

public interface Action {
    void Run(EntityInstanceManager manager) throws Exception;
    ActionType getActionType();
//    EntityDefinition getContextEntity();
}
