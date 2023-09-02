package engine.action.impl.condition.impl;


import engine.action.impl.condition.ConditionOp;
import engine.action.impl.condition.api.Condition;
import engine.context.Context;
import engine.expression.Expression;
import engine.property.PropertyType;
import engine.property.api.PropertyInstance;
import engine.world.factory.SecondaryEntityDetails;

public class SingleCondition extends ConditionImpl implements Condition
{
    final String firstArgExpression; //TODO: DOES IT CAN BY PF ANY TYPE? OR ONLY PROPERTY
    final ConditionOp operator;
    final String secondArgExpression;

    public SingleCondition(String mainEntityName, SecondaryEntityDetails secondaryEntityDetails, String firstArgExpression, ConditionOp conditionOperator, String secondArgExpression) {
        super(mainEntityName, secondaryEntityDetails);
        this.firstArgExpression = firstArgExpression;
        this.operator = conditionOperator;
        this.secondArgExpression = secondArgExpression;
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

        Expression firstArgAsExpression = new Expression(firstArgExpression, context.getActiveEnvironmentVariables(), context.getPrimaryEntityInstance());
        String propertyName = firstArgAsExpression.praseExpressionToValueString(PropertyType.STRING);//TODO: NEED TO FIND ITS TYPE, AND THIS WILL BE THE REQUIRED TYPE FOR OTHER VALUETOCOMPARE

        PropertyInstance propertyToEvaluate = context.getPrimaryEntityInstance().getPropertyByName(propertyName);
        //figuring value out of expression
        Expression secondArgAsExpression = new Expression(secondArgExpression,  context.getActiveEnvironmentVariables(), context.getPrimaryEntityInstance());
        String secondArg = secondArgAsExpression.praseExpressionToValueString(propertyToEvaluate.getType());

        try {
            switch (propertyToEvaluate.getType()) {
                case BOOLEAN:
                    result = operator.eval(propertyToEvaluate.getValue(), secondArg, PropertyType.BOOLEAN);
                    break;
                case DECIMAL:
                    result = operator.eval(propertyToEvaluate.getValue(), secondArg, PropertyType.DECIMAL);
                    break;
                case FLOAT:
                    result = operator.eval(propertyToEvaluate.getValue(), secondArg, PropertyType.FLOAT);
                    break;
                case STRING:
                    result = operator.eval(propertyToEvaluate.getValue(), secondArg, PropertyType.STRING);
                    break;
            }
        }catch (ClassCastException | NumberFormatException e)
        {
            throw new IllegalArgumentException("not-matching argument to single condition action, the property " + propertyToEvaluate.getName() + " is of type: " + propertyToEvaluate.getType() + " and the value to compare is: " + secondArg);
        }
        catch (IllegalArgumentException e)
        {
            throw new IllegalArgumentException("not-matching argument to single condition action, cannot perform bt or lt comparison on the property " + propertyToEvaluate.getName() + " because it is of type: " + propertyToEvaluate.getType());
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException("not-matching argument to single condition action, the property " + propertyToEvaluate.getName() + " is of type: " + propertyToEvaluate.getType() + " and the value to compare is: " + secondArg);
        }
        return result;
    }
}
