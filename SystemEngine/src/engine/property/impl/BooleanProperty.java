package engine.property.impl;

import engine.property.PropertyDefinition;
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
            if (!(propertyDefinition.getInitValue().equals("true") || propertyDefinition.getInitValue().equals("false"))) {
                throw new IllegalArgumentException("Error, cannot insert: " + propertyDefinition.getInitValue() + " to a boolean property");
            }
            this.value = propertyDefinition.getInitValue();
        }
    }

    public void setValue(Boolean value) {
            this.value = value.toString();
    }
}
