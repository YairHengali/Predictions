package engine.action.api;

import java.io.Serializable;

public abstract class AbstractAction implements Action, Serializable {
    private final ActionType actionType;
    protected final String mainEntityName;
    protected final String secondaryEntityName;


    protected AbstractAction(ActionType actionType, String mainEntityName, String secondaryEntityName) {
        this.actionType = actionType;
        this.mainEntityName = mainEntityName;
        this.secondaryEntityName = secondaryEntityName;
    }

    @Override
    public String getMainEntityName() {return mainEntityName;}

    @Override
    public String getSecondaryEntityName() {return secondaryEntityName;}

    @Override
    public ActionType getActionType() { return actionType; }
}
