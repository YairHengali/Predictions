package engine.action.impl;

import engine.YairExpression;
import engine.action.api.AbstractAction;
import engine.action.api.ActionType;
import engine.context.Context;
import engine.entity.EntityInstance;
import engine.property.api.PropertyInstance;
import engine.property.impl.DecimalProperty;
import engine.property.impl.FloatProperty;

import java.util.List;

public class Increase extends AbstractAction {
    String propertyName;
    String byExpression; //TODO: Expression instead of string????

    public Increase(String mainEntityName, String propertyName, String byExpression) {
        super(ActionType.INCREASE, mainEntityName);
        this.propertyName = propertyName;
        this.byExpression = byExpression; //TODO: NEED TO UNDERSTAND IN CASE OF VALUE THAT DEPENDS ENVIRONMENT
    }


    @Override
    public void Run(Context context) throws Exception { //TODO: ADD EXCEPTIONS WHERE NEEDED
        YairExpression expression = new YairExpression(byExpression, context.getActiveEnvironmentVariables(), context.getPrimaryEntityInstance());
        String valueFromExpression = expression.praseExpressionToValueString();

        PropertyInstance entityPropertyInstance = context.getPrimaryEntityInstance().getPropertyByName(propertyName);

        if (entityPropertyInstance instanceof DecimalProperty)
        {
            ((DecimalProperty) entityPropertyInstance).setValue(Integer.parseInt(entityPropertyInstance.getValue()) + Integer.parseInt(valueFromExpression));
        }
        else if (entityPropertyInstance instanceof FloatProperty)
        {
            ((FloatProperty) entityPropertyInstance).setValue(Float.parseFloat(entityPropertyInstance.getValue()) + Float.parseFloat(valueFromExpression));//TODO: VALIDATE IF INT OR FLOAT
        }
    }
}
