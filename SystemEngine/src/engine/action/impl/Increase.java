package engine.action.impl;

import engine.action.api.AbstractAction;
import engine.action.api.ActionType;
import engine.context.Context;
import engine.expression.Expression;
import engine.expression.Expression2;
import engine.property.api.PropertyInstance;
import engine.property.impl.DecimalProperty;
import engine.property.impl.FloatProperty;
import engine.world.factory.SecondaryEntityDetails;

public class Increase extends AbstractAction {
    private final String propertyName;
    private final String byExpression;

    public Increase(String mainEntityName, SecondaryEntityDetails secondaryEntityDetails, String propertyName, String byExpression) {
        super(ActionType.INCREASE, mainEntityName, secondaryEntityDetails);
        this.propertyName = propertyName;
        this.byExpression = byExpression;
    }

    @Override
    public void Run(Context context) {
        Expression expression = new Expression(byExpression, context.getActiveEnvironmentVariables(), context.getPrimaryEntityInstance());
        PropertyInstance entityPropertyInstance = context.getPrimaryEntityInstance().getPropertyByName(propertyName);
        String valueFromExpression = expression.praseExpressionToValueString(entityPropertyInstance.getType());


        try {
            if (entityPropertyInstance instanceof DecimalProperty) {
                ((DecimalProperty) entityPropertyInstance).setValue( (Integer.parseInt(entityPropertyInstance.getValue()) + Integer.parseInt(valueFromExpression)) , context.getCurrentTick());
            } else if (entityPropertyInstance instanceof FloatProperty) {
                ((FloatProperty) entityPropertyInstance).setValue( (Float.parseFloat(entityPropertyInstance.getValue()) + Float.parseFloat(valueFromExpression)) , context.getCurrentTick());
            }
        }catch (NumberFormatException e)
        {
            throw new NumberFormatException("can not add float to integer in action Increase\n" + e.getMessage());
        }
    }


    public void RunEx2(Context context) {
        Expression2 byAsExpression = new Expression2(byExpression, context);
        PropertyInstance mainEntityPropertyInstance;
        if (mainEntityName.equals(context.getPrimaryEntityInstance().getName()))
            mainEntityPropertyInstance = context.getPrimaryEntityInstance().getPropertyByName(propertyName); //TODO: EX2 - HOW TO DECIDE WHICH IS THE PRIMARY?
        else if (mainEntityName.equals(context.getSecondaryEntityInstance().getName()))
            mainEntityPropertyInstance = context.getSecondaryEntityInstance().getPropertyByName(propertyName);
        else {
            throw new RuntimeException("The entity: " + mainEntityName + " is not in the context of the action"); //NEEDED? OR ALREADY CHECKED IN XML PARSING?
        }

        String byFromExpression = byAsExpression.praseExpressionToValueString(mainEntityPropertyInstance.getType());


        try {
            if (mainEntityPropertyInstance instanceof DecimalProperty) {
                ((DecimalProperty) mainEntityPropertyInstance).setValue( (Integer.parseInt(mainEntityPropertyInstance.getValue()) + Integer.parseInt(byFromExpression)) , context.getCurrentTick());
            } else if (mainEntityPropertyInstance instanceof FloatProperty) {
                ((FloatProperty) mainEntityPropertyInstance).setValue( (Float.parseFloat(mainEntityPropertyInstance.getValue()) + Float.parseFloat(byFromExpression)) , context.getCurrentTick());
            }
        }catch (NumberFormatException e)
        {
            throw new NumberFormatException("can not add float to integer in action Increase\n" + e.getMessage());
        }
    }
}
