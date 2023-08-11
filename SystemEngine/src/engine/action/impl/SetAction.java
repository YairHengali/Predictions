package engine.action.impl;

import engine.YairExpression;
import engine.action.api.AbstractAction;
import engine.action.api.ActionType;
import engine.context.Context;
import engine.property.api.PropertyInstance;
import engine.property.impl.BooleanProperty;
import engine.property.impl.DecimalProperty;
import engine.property.impl.FloatProperty;
import engine.property.impl.StringProperty;

public class SetAction extends AbstractAction {
    String propertyName;
    String valueExpression;
    public SetAction(String mainEntityName,String propertyName, String valueExpression){
        super(ActionType.SET, mainEntityName);
        this.propertyName = propertyName;
        this.valueExpression = valueExpression;
    }

    @Override
    public void Run(Context context) throws Exception {//TODO: EXCEPTION IF property from unmatching type
        YairExpression valueAsExpression = new YairExpression(valueExpression, context.getActiveEnvironmentVariables(), context.getPrimaryEntityInstance());
        String valueFromExpression = valueAsExpression.praseExpressionToValueString();

        PropertyInstance entityPropertyInstance = context.getPrimaryEntityInstance().getPropertyByName(propertyName);

        try {
            if (entityPropertyInstance instanceof DecimalProperty) {
                ((DecimalProperty) entityPropertyInstance).setValue(Integer.parseInt(valueFromExpression));
            }
            else if (entityPropertyInstance instanceof FloatProperty) {
                ((FloatProperty) entityPropertyInstance).setValue(Float.parseFloat(valueFromExpression));
            }
            else if (entityPropertyInstance instanceof BooleanProperty) {
                ((BooleanProperty) entityPropertyInstance).setValue(Boolean.valueOf(valueFromExpression));
            }
            else if (entityPropertyInstance instanceof StringProperty) {
                ((StringProperty) entityPropertyInstance).setValue(valueFromExpression);
            }
        } catch (NumberFormatException e) {//IF INTEGER / FLOAT PARSING UNSUCCESSFUL
            //TODO: HANDLE THE PARSING EXCEPTION
        } catch (IllegalArgumentException e) { //IF BOOLEAN PARSING UNSUCCESSFUL
            //TODO: HANDLE THE PARSING EXCEPTION
        }

    }
}
