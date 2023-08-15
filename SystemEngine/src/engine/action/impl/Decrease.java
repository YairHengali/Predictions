package engine.action.impl;

import engine.YairExpression;
import engine.action.api.AbstractAction;
import engine.action.api.ActionType;
import engine.context.Context;
import engine.property.api.PropertyInstance;
import engine.property.impl.DecimalProperty;
import engine.property.impl.FloatProperty;

public class Decrease extends AbstractAction {
    private final String propertyName;
    private final String byExpression;

    public Decrease(String mainEntityName, String propertyName, String byExpression) {
        super(ActionType.DECREASE, mainEntityName);
        this.propertyName = propertyName;
        this.byExpression = byExpression;
    }

    @Override
    public void Run(Context context) throws Exception { //TODO: ADD EXCEPTIONS WHERE NEEDED
        YairExpression expression = new YairExpression(byExpression, context.getActiveEnvironmentVariables(), context.getPrimaryEntityInstance());
        String valueFromExpression = expression.praseExpressionToValueString();
        PropertyInstance entityPropertyInstance = context.getPrimaryEntityInstance().getPropertyByName(propertyName);

        try {
            if (entityPropertyInstance instanceof DecimalProperty) {
                ((DecimalProperty) entityPropertyInstance).setValue(Integer.parseInt(entityPropertyInstance.getValue()) - Integer.parseInt(valueFromExpression));
            } else if (entityPropertyInstance instanceof FloatProperty) {
                ((FloatProperty) entityPropertyInstance).setValue(Float.parseFloat(entityPropertyInstance.getValue()) - Float.parseFloat(valueFromExpression));
            }
        } catch (NumberFormatException e)
        {
            throw new NumberFormatException("can not subtract float to integer in action Decrease" + e.getMessage());
        }
    }
}
