package engine.action.api;

import engine.entity.EntityInstance;

import java.io.Serializable;
import java.util.List;

public abstract class AbstractAction implements Action, Serializable {
    private final ActionType actionType;
    protected String mainEntityName;


    protected AbstractAction(ActionType actionType, String mainEntityName) {
        this.actionType = actionType;
        this.mainEntityName = mainEntityName;
    }

    @Override
    public String getMainEntityName() {return mainEntityName;}

    @Override
    public ActionType getActionType() { return actionType; }
}
