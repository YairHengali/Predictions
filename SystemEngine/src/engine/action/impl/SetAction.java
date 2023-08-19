package engine.action.impl;

import engine.action.api.AbstractAction;
import engine.action.api.ActionType;
import engine.context.Context;
import engine.expression.Expression;
import engine.property.api.PropertyInstance;
import engine.property.impl.BooleanProperty;
import engine.property.impl.DecimalProperty;
import engine.property.impl.FloatProperty;
import engine.property.impl.StringProperty;

public class SetAction extends AbstractAction {
    private final String propertyName;
    private final String valueExpression;
    public SetAction(String mainEntityName,String propertyName, String valueExpression){
        super(ActionType.SET, mainEntityName);
        this.propertyName = propertyName;
        this.valueExpression = valueExpression;
    }

    @Override
    public void Run(Context context) {
        Expression valueAsExpression = new Expression(valueExpression, context.getActiveEnvironmentVariables(), context.getPrimaryEntityInstance());

        PropertyInstance entityPropertyInstance = context.getPrimaryEntityInstance().getPropertyByName(propertyName);

        String valueFromExpression = valueAsExpression.praseExpressionToValueString(entityPropertyInstance.getType());

        try {
            if (entityPropertyInstance instanceof DecimalProperty) {
                ((DecimalProperty) entityPropertyInstance).setValue(Integer.parseInt(valueFromExpression));
            }
            else if (entityPropertyInstance instanceof FloatProperty) {
                ((FloatProperty) entityPropertyInstance).setValue(Float.parseFloat(valueFromExpression));
            }
            else if (entityPropertyInstance instanceof BooleanProperty) {
                if (valueFromExpression.equals("true") || valueFromExpression.equals("false"))
                {
                    ((BooleanProperty) entityPropertyInstance).setValue(Boolean.valueOf(valueFromExpression));
                }
                else{
                    throw new IllegalArgumentException("Error in Set action! Can not set the value " + valueFromExpression + " to the boolean property: " + entityPropertyInstance.getName());
                }
            }
            else if (entityPropertyInstance instanceof StringProperty) {
                ((StringProperty) entityPropertyInstance).setValue(valueFromExpression);
            }
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Error in Set action! Can not set the value " + valueFromExpression + " to the " + entityPropertyInstance.getType() + " property: " + entityPropertyInstance.getName());
        }

    }
}
