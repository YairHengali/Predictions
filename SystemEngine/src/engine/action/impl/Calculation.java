package engine.action.impl;

import engine.YairExpression;
import engine.action.api.AbstractAction;
import engine.action.api.ActionType;
import engine.action.api.ClacType;
import engine.context.Context;
import engine.property.api.PropertyInstance;
import engine.property.impl.DecimalProperty;
import engine.property.impl.FloatProperty;

public class Calculation extends AbstractAction {
    String propertyName;
    String arg1Expression;
    String arg2Expression;
    ClacType calcType;

    public Calculation(String mainEntityName, String propertyName, String arg1Expression, String arg2Expression, ClacType calcType) {
        super(ActionType.CALCULATION, mainEntityName);
        this.propertyName = propertyName;
        this.arg1Expression = arg1Expression;
        this.arg2Expression = arg2Expression;
        this.calcType = calcType;
    }

    @Override
    public void Run(Context context) throws Exception {//TODO: ADD EXCEPTIONS WHERE NEEDED (for example dividing by zero and unmatching type)

            YairExpression arg1AsExpression = new YairExpression(arg1Expression, context.getActiveEnvironmentVariables(), context.getPrimaryEntityInstance());
            YairExpression arg2AsExpression = new YairExpression(arg2Expression, context.getActiveEnvironmentVariables(), context.getPrimaryEntityInstance());

            String value1FromExpression = arg1AsExpression.praseExpressionToValueString();
            String value2FromExpression = arg2AsExpression.praseExpressionToValueString();

            PropertyInstance currentEntityPropertyInstance = context.getPrimaryEntityInstance().getPropertyByName(propertyName);

            if (currentEntityPropertyInstance instanceof DecimalProperty)
            {
                switch (calcType) {
                    case MULTIPLY:
                        ((DecimalProperty) currentEntityPropertyInstance).setValue(Integer.parseInt(value1FromExpression) * Integer.parseInt(value2FromExpression));
                        break;
                    case DIVIDE:
                        ((DecimalProperty) currentEntityPropertyInstance).setValue(Integer.parseInt(value1FromExpression) / Integer.parseInt(value2FromExpression));
                        break;
                }
            }
            else if (currentEntityPropertyInstance instanceof FloatProperty)
            {
                switch (calcType) {
                    case MULTIPLY:
                        ((FloatProperty) currentEntityPropertyInstance).setValue(Float.parseFloat(value1FromExpression) * Float.parseFloat(value2FromExpression));//TODO: VALIDATE IF ARGUMENTS INT OR FLOAT
                        break;
                    case DIVIDE:
                        ((FloatProperty) currentEntityPropertyInstance).setValue(Float.parseFloat(value1FromExpression) / Float.parseFloat(value2FromExpression));
                        break;
                }
            }
    }
}





