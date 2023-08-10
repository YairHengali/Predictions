package engine.property.impl;

import engine.range.Range;
import engine.property.PropertyDefinition;
import engine.property.PropertyType;
import engine.property.api.PropertyInstance;

import java.util.Random;

public class DecimalProperty extends PropertyInstance {

    private int value; //TODO: Maybe Expression??????
    private Range range;

    public DecimalProperty(PropertyDefinition propertyDefinition) {
        super(propertyDefinition.getName(), propertyDefinition.getType());
        this.range = propertyDefinition.getValueRange();

        if(propertyDefinition.isInitializedRandomly()){
            Random random = new Random();
            if (range != null)
            {
                this.value = random.nextInt(range.getTo().intValue() - range.getFrom().intValue() + 1) + range.getFrom().intValue();
            }
            else
            {
                this.value = random.nextInt();
            }
        }
        else {
            this.value = Integer.parseInt(propertyDefinition.getInitValue());
        }
    }

    public DecimalProperty(String name, PropertyType type, Range range) {
        super(name, type);
        this.range = range;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        if (range == null || (value <= range.getTo().intValue() && value >= range.getFrom().intValue()))
            this.value = value;
    }
}
