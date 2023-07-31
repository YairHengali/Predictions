package engine.action;

import engine.Entity;
import engine.Property;

public class Increase implements Action{
    Entity mainEntity;
    Property<Number> property;
    Number amountToIncrease;

//    public Increase(Entity mainEntity, String propertyName, int amountToIncrease){
//        this.mainEntity = mainEntity;
//        if (this.mainEntity.getPropertyByName(propertyName).getValue() instanceof Number)//IN real TIME WILL BE OBJECT, PROBLEM
//        {
//            this.property = (Property<Number>) this.mainEntity.getPropertyByName(propertyName);
//        }
//        this.amountToIncrease = amountToIncrease; //TODO: NEED TO UNDERSTAND IN CASE OF VALUE THAT DEPENDS ENVIRONMENT
//    }
    public Increase(Entity mainEntity, Property<Number> property, Number amountToIncrease){ //TODO: EXCEPTION IF property from non-Number type
        this.mainEntity = mainEntity;
        this.property = property;
        this.amountToIncrease = amountToIncrease; //TODO: NEED TO UNDERSTAND IN CASE OF VALUE THAT DEPENDS ENVIRONMENT
    }

    @Override
    public void Run() {
//        mainEntity.getPropertyByName(propertyName).setValue(mainEntity.getPropertyByName(propertyName).getValue() + amountToIncrease);
        Number propertyValue = property.getValue();
        if (propertyValue instanceof Integer) {
            int intValue = propertyValue.intValue();
            property.setValue(intValue + amountToIncrease.intValue());//TODO: NO!! EXCEPTION IF OUT OF RANGE!
        } else if (propertyValue instanceof Float) {
            float floatValue = propertyValue.floatValue();
            property.setValue(floatValue + amountToIncrease.floatValue());
        }
        else{
            //NEED TO DO SOMETHING?
            }

            //IF ENTITY.NAME == entityName
                //FOREACH PROPERTY : GET PROPERTIES()
                    //IF PROPERTY.NAME == propertyName
    }
}
