package engine.action.impl;

import engine.EntityDef;
import engine.entity.EntityInstance;
import engine.expression.Expression;
import engine.property.Property;
import engine.action.api.Action;

import java.util.List;

public class Increase implements Action {
    List<EntityInstance> mainEntityList;
    String propertyName;
    int amountToIncrease; //TODO: Expression????

//    public Increase(Entity mainEntity, String propertyName, int amountToIncrease){
//        this.mainEntity = mainEntity;
//        if (this.mainEntity.getPropertyByName(propertyName).getValue() instanceof Number)//IN real TIME WILL BE OBJECT, PROBLEM
//        {
//            this.property = (Property<Number>) this.mainEntity.getPropertyByName(propertyName);
//        }
//        this.amountToIncrease = amountToIncrease; //TODO: NEED TO UNDERSTAND IN CASE OF VALUE THAT DEPENDS ENVIRONMENT
//    }
    public Increase(List<EntityInstance> mainEntityList, String propertyName, int amountToIncrease){ //TODO: EXCEPTION IF property from non-Number type
        this.mainEntityList = mainEntityList;
        this.propertyName = propertyName;
        this.amountToIncrease = amountToIncrease; //TODO: NEED TO UNDERSTAND IN CASE OF VALUE THAT DEPENDS ENVIRONMENT
    }


    @Override
    public void Run() {
//        mainEntity.getPropertyByName(propertyName).setValue(mainEntity.getPropertyByName(propertyName).getValue() + amountToIncrease);
        for (EntityInstance entityInstance : mainEntityList) {
            entityInstance.getPropertyByName(propertyName).getValue() += amountToIncrease;
        }

        //IF ENTITY.NAME == entityName
        //FOREACH PROPERTY : GET PROPERTIES()
        //IF PROPERTY.NAME == propertyName
    }
//    @Override
//    public void Run() {
////        mainEntity.getPropertyByName(propertyName).setValue(mainEntity.getPropertyByName(propertyName).getValue() + amountToIncrease);
//        propertyValue = property.getValue();
//        if (propertyValue instanceof Integer) {
//            int intValue = propertyValue.intValue();
//            property.setValue(intValue + amountToIncrease.intValue());//TODO: NO!! EXCEPTION IF OUT OF RANGE!
//        } else if (propertyValue instanceof Float) {
//            float floatValue = propertyValue.floatValue();
//            property.setValue(floatValue + amountToIncrease.floatValue());
//        }
//        else{
//            //NEED TO DO SOMETHING?
//            }
//
//            //IF ENTITY.NAME == entityName
//                //FOREACH PROPERTY : GET PROPERTIES()
//                    //IF PROPERTY.NAME == propertyName
//    }
}
