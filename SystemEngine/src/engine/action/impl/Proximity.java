package engine.action.impl;

import engine.action.api.AbstractAction;
import engine.action.api.Action;
import engine.action.api.ActionType;
import engine.context.Context;
import engine.context.ContextImpl;
import engine.entity.EntityInstance;
import engine.expression.Expression;
import engine.expression.Expression2;
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
        //BEFORE getMainEntityInstance(context) FUNCTION:
//        Expression ofAsExpression = new Expression(ofExpression, context.getActiveEnvironmentVariables(), context.getPrimaryEntityInstance());
//        String ofFromExpression = ofAsExpression.praseExpressionToValueString(PropertyType.FLOAT);
//
//        for (EntityInstance targetEntity : context.getEntityInstanceManager().getInstancesListByName(targetEntityName)) {
//            int depth = Float.valueOf(ofFromExpression).intValue(); //MAYBE ROUND UP/DOWN ACCORDINGLY
//            if (context.getEntityInstanceManager().isEnt1NearEnt2(context.getPrimaryEntityInstance(), targetEntity, depth)) {
//                //INVOKE ACTIONS:
//                for (Action action : thenActions) {
//                    try {
//                        action.Run(new ContextImpl(context.getPrimaryEntityInstance(), targetEntity, context.getEntityInstanceManager(), context.getActiveEnvironmentVariables(), context.getCurrentTick()));
//                    } catch (Exception e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            }
//            break;
//        }

        Expression2 ofAsExpression = new Expression2(ofExpression, context);
        String ofFromExpression = ofAsExpression.praseExpressionToValueString(PropertyType.FLOAT);

        EntityInstance sourceEntityInstance = getMainEntityInstance(context);

        for (EntityInstance targetEntity : context.getEntityInstanceManager().getInstancesListByName(targetEntityName)) {
            int depth = Float.valueOf(ofFromExpression).intValue(); //MAYBE ROUND UP/DOWN ACCORDINGLY
            if (context.getEntityInstanceManager().isEnt1NearEnt2(sourceEntityInstance, targetEntity, depth)) {
                //INVOKE ACTIONS:
                for (Action action : thenActions) {
                    try {
                        action.Run(new ContextImpl(sourceEntityInstance, targetEntity, context.getEntityInstanceManager(), context.getActiveEnvironmentVariables(), context.getCurrentTick()));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            break;
        }

    }

    public void addActionToThen(Action actionToAdd) {
        this.thenActions.add(actionToAdd);
    }

}
