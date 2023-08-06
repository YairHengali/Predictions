package engine.action.api;

import engine.entity.EntityInstance;

import java.util.List;

public abstract class AbstractAction implements Action {
    ActionType actionType;
    protected List<EntityInstance> mainEntityList;

    protected AbstractAction(ActionType actionType, List<EntityInstance> mainEntityList) {
        this.actionType = actionType;
        this.mainEntityList = mainEntityList;
    }

    @Override
    public ActionType getActionType() { return actionType; }
}
