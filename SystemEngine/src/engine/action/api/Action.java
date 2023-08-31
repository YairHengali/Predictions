package engine.action.api;

import engine.context.Context;

public interface Action {
    void Run(Context context);
    ActionType getActionType();
    String getMainEntityName();
    String getSecondaryEntityName();
}
