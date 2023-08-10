package engine.property.impl;

import engine.property.PropertyDefinition;
import engine.property.PropertyType;
import engine.property.api.PropertyInstance;

import java.util.Random;

public class BooleanProperty extends PropertyInstance {

    public BooleanProperty(PropertyDefinition propertyDefinition) {
        super(propertyDefinition.getName(), propertyDefinition.getType());

        if(propertyDefinition.isInitializedRandomly()){
            Random random = new Random();
            this.value = String.valueOf(random.nextBoolean());
        }
        else {
            this.value = propertyDefinition.getInitValue();
        }
    }

    public BooleanProperty(String name, PropertyType type) {
        super(name, type);
    }

//    public boolean getValue() {
//        return this.value;
//    }

    public void setValue(Boolean value) {
            this.value = value.toString();
    }
}
