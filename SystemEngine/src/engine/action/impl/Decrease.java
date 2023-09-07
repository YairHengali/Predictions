package engine.action.impl;

import engine.action.api.AbstractAction;
import engine.action.api.ActionType;
import engine.context.Context;
import engine.entity.EntityInstance;
import engine.expression.Expression;
import engine.expression.Expression2;
import engine.property.api.PropertyInstance;
import engine.property.impl.DecimalProperty;
import engine.property.impl.FloatProperty;
import engine.world.factory.SecondaryEntityDetails;

public class Decrease extends AbstractAction {
    private final String propertyName;
    private final String byExpression;

    public Decrease(String mainEntityName, SecondaryEntityDetails secondaryEntityDetails, String propertyName, String byExpression) {
        super(ActionType.DECREASE, mainEntityName, secondaryEntityDetails);
        this.propertyName = propertyName;
        this.byExpression = byExpression;
    }

//    @Override
//    public void Run(Context context) {
//        Expression expression = new Expression(byExpression, context.getActiveEnvironmentVariables(), context.getPrimaryEntityInstance());
//        PropertyInstance entityPropertyInstance = context.getPrimaryEntityInstance().getPropertyByName(propertyName);
//        String valueFromExpression = expression.praseExpressionToValueString(entityPropertyInstance.getType());
//
//        try {
//            if (entityPropertyInstance instanceof DecimalProperty) {
//                ((DecimalProperty) entityPropertyInstance).setValue( (Integer.parseInt(entityPropertyInstance.getValue()) - Integer.parseInt(valueFromExpression)) , context.getCurrentTick());
//            } else if (entityPropertyInstance instanceof FloatProperty) {
//                ((FloatProperty) entityPropertyInstance).setValue( (Float.parseFloat(entityPropertyInstance.getValue()) - Float.parseFloat(valueFromExpression)) , context.getCurrentTick());
//            }
//        } catch (NumberFormatException e)
//        {
//            throw new NumberFormatException("can not subtract float to integer in action Decrease\n" + e.getMessage());
//        }
//    }


    @Override
    public void Run(Context context) {
        Expression2 byAsExpression = new Expression2(byExpression, context);
        //USING PROTECTED METHOD:
        EntityInstance mainEntity = getMainEntityInstance(context);

        PropertyInstance mainEntityPropertyInstance = mainEntity.getPropertyByName(propertyName);

        String byFromExpression = byAsExpression.praseExpressionToValueString(mainEntityPropertyInstance.getType());


        try {
            if (mainEntityPropertyInstance instanceof DecimalProperty) {
                ((DecimalProperty) mainEntityPropertyInstance).setValue( (Integer.parseInt(mainEntityPropertyInstance.getValue()) - Integer.parseInt(byFromExpression)) , context.getCurrentTick());
            } else if (mainEntityPropertyInstance instanceof FloatProperty) {
                ((FloatProperty) mainEntityPropertyInstance).setValue( (Float.parseFloat(mainEntityPropertyInstance.getValue()) - Float.parseFloat(byFromExpression)) , context.getCurrentTick());
            }
        }catch (NumberFormatException e)
        {
            throw new NumberFormatException("can not add float to integer in action Increase\n" + e.getMessage());
        }
    }
}
