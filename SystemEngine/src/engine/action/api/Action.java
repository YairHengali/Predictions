package engine.action.api;

import engine.context.Context;

public interface Action {
    void Run(Context context) throws Exception;
    ActionType getActionType();
//    EntityDefinition getContextEntity();
    String getMainEntityName();
}
