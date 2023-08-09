package engine.action.impl.condition.impl;

import engine.action.api.AbstractAction;
import engine.action.api.Action;
import engine.action.api.ActionType;
import engine.action.impl.condition.api.Condition;
import engine.context.Context;

import java.util.ArrayList;
import java.util.List;

public abstract class ConditionImpl extends AbstractAction implements Condition {

    protected List<Action> thenActions;
    protected List<Action> elseActions;

    protected ConditionImpl(ActionType actionType, String mainEntityName) {
        super(actionType, mainEntityName);
        thenActions = new ArrayList<>();
        elseActions = new ArrayList<>();
    }

    @Override
    public void addActionToThen(Action actionToAdd) {
        this.thenActions.add(actionToAdd);
    }

    @Override
    public void addActionToElse(Action actionToAdd) {
        this.elseActions.add(actionToAdd);
    }

    protected void invokeThenActions(Context context){
        for (Action action: thenActions) {
            try {
                action.Run(context);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }
    protected void invokeElseActions(Context context){
        for (Action action: elseActions) {
            try {
                action.Run(context);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
