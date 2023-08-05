package engine.property.impl;

import engine.Range;
import engine.property.PropertyDefinition;
import engine.property.api.PropertyInstance;

public class DecimalProperty extends PropertyInstance {

    private int value; //TODO: Maybe Expression??????
    private Range range;

    public DecimalProperty(PropertyDefinition propertyDefinition) {
        super(propertyDefinition.getName(), propertyDefinition.getType());
        this.range = propertyDefinition.getValueRange();

        if(propertyDefinition.isInitializedRandomly()){
            // TODO: implement randomly
        }
        else {
            this.value = Integer.parseInt(propertyDefinition.getInitValue());
        }


    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        if (range == null || (value <= range.getTo() && value >= range.getFrom()))
            this.value = value;
    }
}
