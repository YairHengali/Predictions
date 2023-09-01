package engine.action.impl;

import engine.action.api.AbstractAction;
import engine.action.api.ActionType;
import engine.context.Context;
import engine.expression.Expression;
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

    @Override
    public void Run(Context context) {
        Expression expression = new Expression(byExpression, context.getActiveEnvironmentVariables(), context.getPrimaryEntityInstance());
        PropertyInstance entityPropertyInstance = context.getPrimaryEntityInstance().getPropertyByName(propertyName);
        String valueFromExpression = expression.praseExpressionToValueString(entityPropertyInstance.getType());

        try {
            if (entityPropertyInstance instanceof DecimalProperty) {
                ((DecimalProperty) entityPropertyInstance).setValue( (Integer.parseInt(entityPropertyInstance.getValue()) - Integer.parseInt(valueFromExpression)) , context.getCurrentTick());
            } else if (entityPropertyInstance instanceof FloatProperty) {
                ((FloatProperty) entityPropertyInstance).setValue( (Float.parseFloat(entityPropertyInstance.getValue()) - Float.parseFloat(valueFromExpression)) , context.getCurrentTick());
            }
        } catch (NumberFormatException e)
        {
            throw new NumberFormatException("can not subtract float to integer in action Decrease\n" + e.getMessage());
        }
    }
}
