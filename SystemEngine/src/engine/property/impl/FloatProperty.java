package engine.property.impl;

import engine.range.Range;
import engine.property.PropertyDefinition;
import engine.property.PropertyType;
import engine.property.api.PropertyInstance;

import java.util.Random;

public class FloatProperty extends PropertyInstance {

    private final Range range;

    public FloatProperty(PropertyDefinition propertyDefinition) {
        super(propertyDefinition.getName(), propertyDefinition.getType());
        this.range = propertyDefinition.getValueRange();

        if(propertyDefinition.isInitializedRandomly()){
            Random random = new Random();
            if (range != null)
            {
                this.value = String.valueOf(random.nextFloat() * (range.getTo().floatValue() - range.getFrom().floatValue() + 1) + range.getFrom().floatValue());
            }
            else
            {
                this.value = String.valueOf(random.nextFloat());
            }
        }
        else {
            this.value = propertyDefinition.getInitValue();
        }
    }

    public FloatProperty(String name, PropertyType type, Range range) {
        super(name, type);
        this.range = range;
    }

//    public float getValue() {
//        return this.value;
//    }

    public void setValue(Float value) {
        if (range == null || (value <= range.getTo().floatValue() && value >= range.getFrom().floatValue()))
            this.value = value.toString();
        else if(value > range.getTo().floatValue())
        {
            this.value = String.valueOf(range.getTo().floatValue());
        }
        else if(value < range.getFrom().floatValue())
        {
            this.value = String.valueOf(range.getFrom().floatValue());
        }
    }
}
