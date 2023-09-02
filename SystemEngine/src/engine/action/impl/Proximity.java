package engine.action.impl;

import engine.action.api.AbstractAction;
import engine.action.api.Action;
import engine.action.api.ActionType;
import engine.context.Context;
import engine.expression.Expression;
import engine.property.PropertyType;
import engine.property.api.PropertyInstance;
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
        Expression ofAsExpression = new Expression(ofExpression, context.getActiveEnvironmentVariables(), context.getPrimaryEntityInstance());
        String ofFromExpression = ofAsExpression.praseExpressionToValueString(PropertyType.FLOAT);

//TODO: FIGURE OUT HOW TO GET THE SECOND ENTITY

//        if (context.getEntityInstanceManager().isEnt1NearEnt2(context.getPrimaryEntityInstance(), ENT2, ofFromExpression)) {
//            //INVOKE ACTIONS:
//            for (Action action : thenActions) {
//                try {
//                    action.Run(context);
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }

    }

    public void addActionToThen(Action actionToAdd) {
        this.thenActions.add(actionToAdd);
    }

}
