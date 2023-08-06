package engine.action.impl;

import engine.action.api.AbstractAction;
import engine.action.api.ActionType;
import engine.entity.EntityInstance;
import engine.action.api.Action;
import engine.property.api.PropertyInstance;
import engine.property.impl.DecimalProperty;
import engine.property.impl.FloatProperty;

import java.util.List;

public class Increase extends AbstractAction {
    //List<EntityInstance> mainEntityList;
    String propertyName;
    Number amountToIncrease; //TODO: Expression????

//    public Increase(List<EntityInstance> mainEntityList, String propertyName, float amountToIncrease){
//        this.mainEntityList = mainEntityList;
//
//        if (this.mainEntityList.getPropertyByName(propertyName).getValue() instanceof Number)//IN real TIME WILL BE OBJECT, PROBLEM
//        {
//            this.property = (Property<Number>) this.mainEntity.getPropertyByName(propertyName);
//        }
//        this.amountToIncrease = amountToIncrease; //TODO: NEED TO UNDERSTAND IN CASE OF VALUE THAT DEPENDS ENVIRONMENT
//    }


    public Increase(List<EntityInstance> mainEntityList, String propertyName, Number amountToIncrease) { //TODO: EXCEPTION IF property from non-Number type
        super(ActionType.INCREASE, mainEntityList);
        this.propertyName = propertyName;
        this.amountToIncrease = amountToIncrease; //TODO: NEED TO UNDERSTAND IN CASE OF VALUE THAT DEPENDS ENVIRONMENT
    }


    @Override
    public void Run() throws Exception {
        for (EntityInstance entityInstance : mainEntityList) {
            PropertyInstance currentEntityPropertyInstance = entityInstance.getPropertyByName(propertyName);
            if (currentEntityPropertyInstance instanceof DecimalProperty) //TODO: || (currentEntityPropertyInstance instanceof (FloatProperty))))
            {
                ((DecimalProperty) currentEntityPropertyInstance).setValue(((DecimalProperty) currentEntityPropertyInstance).getValue() + amountToIncrease.intValue());//TODO: VALIDATE IF INT OR FLOAT
            } else if (currentEntityPropertyInstance instanceof FloatProperty) //TODO: || (currentEntityPropertyInstance instanceof (FloatProperty))))
            {
                ((FloatProperty) currentEntityPropertyInstance).setValue(((FloatProperty) currentEntityPropertyInstance).getValue() + amountToIncrease.floatValue());//TODO: VALIDATE IF INT OR FLOAT
            } else {
                throw new Exception("Invalid Property type, need to be Numeric");
            }
        }
    }
}
