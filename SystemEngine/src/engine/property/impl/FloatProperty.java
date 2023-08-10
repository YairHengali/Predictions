package engine.property.impl;

import engine.range.Range;
import engine.property.PropertyDefinition;
import engine.property.PropertyType;
import engine.property.api.PropertyInstance;

import java.util.Random;

public class FloatProperty extends PropertyInstance {

    private float value; //TODO: Maybe Expression??????
    private Range range;

    public FloatProperty(PropertyDefinition propertyDefinition) {
        super(propertyDefinition.getName(), propertyDefinition.getType());
        this.range = propertyDefinition.getValueRange();

        if(propertyDefinition.isInitializedRandomly()){
            Random random = new Random();
            if (range != null)
            {
                this.value = random.nextFloat() * (range.getTo().floatValue() - range.getFrom().floatValue() + 1) + range.getFrom().floatValue();
            }
            else
            {
                this.value = random.nextFloat();
            }
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
        if (range == null || (value <= range.getTo().floatValue() && value >= range.getFrom().floatValue()))
            this.value = value;
    }
}
