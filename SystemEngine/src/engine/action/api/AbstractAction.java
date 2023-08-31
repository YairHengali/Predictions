package engine.action.api;

import engine.action.impl.condition.api.Condition;
import engine.world.factory.SecondaryEntityDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractAction implements Action, Serializable {
    private final ActionType actionType;
    protected final String mainEntityName;
    protected final SecondaryEntityDetails secondaryEntityDetails;



    protected AbstractAction(ActionType actionType, String mainEntityName, SecondaryEntityDetails secondaryEntityDetails) {
        this.actionType = actionType;
        this.mainEntityName = mainEntityName;
        this.secondaryEntityDetails = secondaryEntityDetails;
    }

    @Override
    public String getMainEntityName() {return mainEntityName;}

    @Override
    public ActionType getActionType() { return actionType; }

    @Override
    public SecondaryEntityDetails getSecondaryEntityDetails(){ return secondaryEntityDetails;}

}
