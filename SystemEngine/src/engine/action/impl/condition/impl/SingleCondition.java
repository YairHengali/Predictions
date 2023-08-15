package engine.action.impl.condition.impl;


import engine.action.api.ActionType;
import engine.action.impl.condition.ConditionOp;
import engine.action.impl.condition.api.Condition;
import engine.context.Context;
import engine.expression.Expression;
import engine.property.PropertyType;
import engine.property.api.PropertyInstance;
import engine.property.impl.BooleanProperty;
import engine.property.impl.DecimalProperty;
import engine.property.impl.FloatProperty;
import engine.property.impl.StringProperty;

public class SingleCondition extends ConditionImpl implements Condition
{
    String propertyName;
    ConditionOp operator;
    String valueToCompareExpression;

    public SingleCondition(String mainEntityName, String propertyName, ConditionOp conditionOperator, String valueToCompareExpression) {
        super(ActionType.CONDITION, mainEntityName);
        this.propertyName = propertyName;
        this.operator = conditionOperator;
        this.valueToCompareExpression = valueToCompareExpression;
    }

    @Override
    public void Run(Context context) throws Exception {//TODO: EXCEPTION IF property from unmatching type -Vaildate that done correctly
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
        String valueToCompare = valueToCompareAsExpression.praseExpressionToValueString();

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
        }catch (Exception e)
        {
            throw new RuntimeException("not-matching argument to single condition action " + e.getMessage());
        }

        return result;
    }

    public String getPropertyName() {
        return propertyName;
    }
}
