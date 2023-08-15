package engine.action.api;

import java.io.Serializable;

public abstract class AbstractAction implements Action, Serializable {
    private final ActionType actionType;
    protected final String mainEntityName;


    protected AbstractAction(ActionType actionType, String mainEntityName) {
        this.actionType = actionType;
        this.mainEntityName = mainEntityName;
    }

    @Override
    public String getMainEntityName() {return mainEntityName;}

    @Override
    public ActionType getActionType() { return actionType; }
}
