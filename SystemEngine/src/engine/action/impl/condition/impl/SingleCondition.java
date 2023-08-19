package engine.action.impl.condition.impl;


import engine.action.impl.condition.ConditionOp;
import engine.action.impl.condition.api.Condition;
import engine.context.Context;
import engine.expression.Expression;
import engine.property.PropertyType;
import engine.property.api.PropertyInstance;

public class SingleCondition extends ConditionImpl implements Condition
{
    final String propertyName;
    final ConditionOp operator;
    final String valueToCompareExpression;

    public SingleCondition(String mainEntityName, String propertyName, ConditionOp conditionOperator, String valueToCompareExpression) {
        super(mainEntityName);
        this.propertyName = propertyName;
        this.operator = conditionOperator;
        this.valueToCompareExpression = valueToCompareExpression;
    }

    @Override
    public void Run(Context context) {
        if (evaluateCondition(context)) {
            invokeThenActions(context);
        }
        else {
            invokeElseActions(context);
        }
    }

    @Override
    public boolean evaluateCondition(Context context) {
        boolean result = false;

        PropertyInstance propertyToEvaluate = context.getPrimaryEntityInstance().getPropertyByName(this.propertyName);
        //figuring value out of expression
        Expression valueToCompareAsExpression = new Expression(valueToCompareExpression,  context.getActiveEnvironmentVariables(), context.getPrimaryEntityInstance());
        String valueToCompare = valueToCompareAsExpression.praseExpressionToValueString(propertyToEvaluate.getType());

        try {
            switch (propertyToEvaluate.getType()) {
                case BOOLEAN:
                    result = operator.eval(propertyToEvaluate.getValue(), valueToCompare, PropertyType.BOOLEAN);
                    break;
                case DECIMAL:
                    result = operator.eval(propertyToEvaluate.getValue(), valueToCompare, PropertyType.DECIMAL);
                    break;
                case FLOAT:
                    result = operator.eval(propertyToEvaluate.getValue(), valueToCompare, PropertyType.FLOAT);
                    break;
                case STRING:
                    result = operator.eval(propertyToEvaluate.getValue(), valueToCompare, PropertyType.STRING);
                    break;
            }
        }catch (ClassCastException | NumberFormatException e)
        {
            throw new IllegalArgumentException("not-matching argument to single condition action, the property " + propertyToEvaluate.getName() + " is of type: " + propertyToEvaluate.getType() + " and the value to compare is: " + valueToCompare);
        }
        catch (IllegalArgumentException e)
        {
            throw new IllegalArgumentException("not-matching argument to single condition action, cannot perform bt or lt comparison on the property " + propertyToEvaluate.getName() + " because it is of type: " + propertyToEvaluate.getType());
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException("not-matching argument to single condition action, the property " + propertyToEvaluate.getName() + " is of type: " + propertyToEvaluate.getType() + " and the value to compare is: " + valueToCompare);
        }
        return result;
    }
}
