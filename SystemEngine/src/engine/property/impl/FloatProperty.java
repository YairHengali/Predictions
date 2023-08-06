package engine.property.impl;

import engine.Range;
import engine.property.PropertyDefinition;
import engine.property.PropertyType;
import engine.property.api.PropertyInstance;

public class FloatProperty extends PropertyInstance {

    private float value; //TODO: Maybe Expression??????
    private Range range;

    public FloatProperty(PropertyDefinition propertyDefinition) {
        super(propertyDefinition.getName(), propertyDefinition.getType());
        this.range = propertyDefinition.getValueRange();

        if(propertyDefinition.isInitializedRandomly()){
            // TODO: implement randomly
        }
        else {
            this.value = Float.parseFloat(propertyDefinition.getInitValue());
        }
    }

    public FloatProperty(String name, PropertyType type, Range range) {
        super(name, type);
        this.range = range;
    }

    public float getValue() {
        return this.value;
    }

    public void setValue(float value) {
        if (range == null || (value <= range.getTo() && value >= range.getFrom()))
            this.value = value;
    }
}
