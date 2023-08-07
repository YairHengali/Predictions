package engine.property.impl;

import engine.property.PropertyDefinition;
import engine.property.PropertyType;
import engine.property.api.PropertyInstance;

import java.util.Random;

public class BooleanProperty extends PropertyInstance {

    private boolean value;

    public BooleanProperty(PropertyDefinition propertyDefinition) {
        super(propertyDefinition.getName(), propertyDefinition.getType());

        if(propertyDefinition.isInitializedRandomly()){
            Random random = new Random();
            this.value = random.nextBoolean();
        }
        else {
            this.value = Boolean.parseBoolean(propertyDefinition.getInitValue());
        }
    }

    public BooleanProperty(String name, PropertyType type) {
        super(name, type);
    }

    public boolean getValue() {
        return this.value;
    }

    public void setValue(boolean value) {
            this.value = value;
    }
}
