package engine.action.impl;

import engine.action.api.AbstractAction;
import engine.action.api.Action;
import engine.action.api.ActionType;
import engine.action.api.ClacType;
import engine.entity.EntityInstance;
import engine.property.api.PropertyInstance;
import engine.property.impl.DecimalProperty;
import engine.property.impl.FloatProperty;

import java.util.List;

public class Calculation extends AbstractAction {
    //List<EntityInstance> mainEntityList;
    String propertyName;
    Number argument1;
    Number argument2;
    ClacType calcType;

    public Calculation(List<EntityInstance> mainEntityList, String propertyName, Number argument1, Number argument2, ClacType calcType) {
        super(ActionType.CALCULATION, mainEntityList);
        this.propertyName = propertyName;
        this.argument1 = argument1;
        this.argument2 = argument2;
        this.calcType = calcType;
    }

    @Override
    public void Run() throws Exception {
        for (EntityInstance entityInstance : mainEntityList) {
            PropertyInstance currentEntityPropertyInstance = entityInstance.getPropertyByName(propertyName);
            if (currentEntityPropertyInstance instanceof DecimalProperty) //TODO: || (currentEntityPropertyInstance instanceof (FloatProperty))))
            {
                switch (calcType) {
                    case MULTIPLY:
                        ((DecimalProperty) currentEntityPropertyInstance).setValue((argument1.intValue() * argument2.intValue()));//TODO: VALIDATE IF ARGUMENTS INT OR FLOAT
                        break;
                    case DIVIDE:
                        ((DecimalProperty) currentEntityPropertyInstance).setValue((argument1.intValue() / argument2.intValue()));//TODO: VALIDATE IF ARGUMENTS INT OR FLOAT
                        break;
                }
            }
            else if (currentEntityPropertyInstance instanceof FloatProperty)
            {
                switch (calcType) {
                    case MULTIPLY:
                        ((FloatProperty) currentEntityPropertyInstance).setValue((argument1.floatValue() * argument2.floatValue()));//TODO: VALIDATE IF ARGUMENTS INT OR FLOAT
                        break;
                    case DIVIDE:
                        ((FloatProperty) currentEntityPropertyInstance).setValue((argument1.floatValue() / argument2.floatValue()));//TODO: VALIDATE IF ARGUMENTS INT OR FLOAT
                        break;
                }
            }
            else
            {
                throw new Exception("Invalid Property type, need to be Numeric");
            }
        }
    }
}





