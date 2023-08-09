package engine.action.impl;

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
    public void Run(Context context) throws Exception {
            PropertyInstance currentEntityPropertyInstance = context.getPrimaryEntityInstance().getPropertyByName(propertyName);
            if (currentEntityPropertyInstance instanceof DecimalProperty) //TODO: || (currentEntityPropertyInstance instanceof (FloatProperty))))
            {
                switch (calcType) {
                    case MULTIPLY:
                        ((DecimalProperty) currentEntityPropertyInstance).setValue((arg1Expression.intValue() * arg2Expression.intValue()));//TODO: VALIDATE IF ARGUMENTS INT OR FLOAT
                        break;
                    case DIVIDE:
                        ((DecimalProperty) currentEntityPropertyInstance).setValue((arg1Expression.intValue() / arg2Expression.intValue()));//TODO: VALIDATE IF ARGUMENTS INT OR FLOAT
                        break;
                }
            }
            else if (currentEntityPropertyInstance instanceof FloatProperty)
            {
                switch (calcType) {
                    case MULTIPLY:
                        ((FloatProperty) currentEntityPropertyInstance).setValue((arg1Expression.floatValue() * arg2Expression.floatValue()));//TODO: VALIDATE IF ARGUMENTS INT OR FLOAT
                        break;
                    case DIVIDE:
                        ((FloatProperty) currentEntityPropertyInstance).setValue((arg1Expression.floatValue() / arg2Expression.floatValue()));//TODO: VALIDATE IF ARGUMENTS INT OR FLOAT
                        break;
                }
            }
    }
}





