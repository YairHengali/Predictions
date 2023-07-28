package engine.action;

import engine.Entity;
import engine.Property;

public class Increase implements Action{
    Entity mainEntity;
    Property<Number> property;
    int amountToIncrease;

    public Increase(Entity mainEntity, String propertyName, int amountToIncrease){
        this.mainEntity = mainEntity;
        if (this.mainEntity.getPropertyByName(propertyName).getValue() instanceof Number)
        {
            this.property = (Property<Number>) this.mainEntity.getPropertyByName(propertyName);
        }
        this.amountToIncrease = amountToIncrease; //TODO: NEED TO UNDERSTAND IN CASE OF VALUE THAT DEPENDS ENVIRONMENT
    }

    @Override
    public void Run() {
        mainEntity.getPropertyByName(propertyName).setValue(mainEntity.getPropertyByName(propertyName).getValue() + amountToIncrease);

            //IF ENTITY.NAME == entityName
                //FOREACH PROPERTY : GET PROPERTIES()
                    //IF PROPERTY.NAME == propertyName


    }
}
