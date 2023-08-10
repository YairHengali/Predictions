package engine.action.impl.condition.impl;

import engine.YairExpression;
import engine.action.api.ActionType;
import engine.action.impl.condition.ConditionOp;
import engine.action.impl.condition.api.Condition;
import engine.context.Context;
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
    public void Run(Context context) throws Exception {//TODO: EXCEPTION IF property from unmatching type
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
        YairExpression valueToCompareAsExpression = new YairExpression(valueToCompareExpression,  context.getActiveEnvironmentVariables(), context.getPrimaryEntityInstance());
        String valueToCompare = valueToCompareAsExpression.praseExpressionToValueString();

        switch (propertyToEvaluate.getType()) {
            case BOOLEAN:
                result = operator.eval( ((BooleanProperty) propertyToEvaluate).getValue(), valueToCompare, PropertyType.STRING);
                break;
            case DECIMAL:
                result = operator.eval( ((DecimalProperty) propertyToEvaluate).getValue(), valueToCompare, PropertyType.DECIMAL);
                break;
            case FLOAT:
                result = operator.eval( ((FloatProperty) propertyToEvaluate).getValue(), valueToCompare, PropertyType.FLOAT);
                break;
            case STRING:
                result = operator.eval( ((StringProperty) propertyToEvaluate).getValue(), valueToCompare, PropertyType.STRING);
                break;
        }

//        switch (propertyToEvaluate.getType()) {
//            case BOOLEAN:
//                result = operator.eval( ((BooleanProperty) propertyToEvaluate).getValue(), valueToCompareExpression, PropertyType.STRING);
//                break;
//            case DECIMAL:
//                result = operator.eval( ((DecimalProperty) propertyToEvaluate).getValue(), valueToCompareExpression, PropertyType.DECIMAL);
//                break;
//            case FLOAT:
//                result = operator.eval( ((FloatProperty) propertyToEvaluate).getValue(), valueToCompareExpression, PropertyType.FLOAT);
//                break;
//            case STRING:
//                result = operator.eval( ((StringProperty) propertyToEvaluate).getValue(), valueToCompareExpression, PropertyType.STRING);
//                break;
//        }

        return result;
    }

    public String getPropertyName() {
        return propertyName;
    }
}