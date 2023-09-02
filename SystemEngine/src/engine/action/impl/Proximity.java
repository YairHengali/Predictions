package engine.action.impl;

import engine.action.api.AbstractAction;
import engine.action.api.Action;
import engine.action.api.ActionType;
import engine.context.Context;
import engine.world.factory.SecondaryEntityDetails;

import java.util.ArrayList;
import java.util.List;

public class Proximity extends AbstractAction {
    private final String ofExpression;
    private final String targetEntityName;
    protected final List<Action> thenActions;
    public Proximity(String mainEntityName, SecondaryEntityDetails secondaryEntityDetails, String targetEntityName, String ofExpression){
        super(ActionType.PROXIMITY, mainEntityName, secondaryEntityDetails);
        this.ofExpression = ofExpression;
        this.targetEntityName = targetEntityName;
        thenActions = new ArrayList<>();

    }
    @Override
    public void Run(Context context) {

    }

    public void addActionToThen(Action actionToAdd) {
        this.thenActions.add(actionToAdd);
    }
}
