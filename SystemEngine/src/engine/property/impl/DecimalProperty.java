package engine.property.impl;

import engine.range.Range;
import engine.property.PropertyDefinition;
import engine.property.PropertyType;
import engine.property.api.PropertyInstance;

import java.util.Random;

public class DecimalProperty extends PropertyInstance {

    private final Range range;

    public DecimalProperty(PropertyDefinition propertyDefinition) {
        super(propertyDefinition.getName(), propertyDefinition.getType());
        this.range = propertyDefinition.getValueRange();

        if(propertyDefinition.isInitializedRandomly()){
            Random random = new Random();
            if (range != null)
            {
                int from = range.getFrom().intValue();
                int to = range.getTo().intValue();
                this.value = String.valueOf(random.nextInt(to - from + 1) + from);
            }
            else
            {
                this.value = String.valueOf(random.nextInt());
            }
        }
        else {
            this.value = propertyDefinition.getInitValue(); //TODO: MAYBE TRYPARSE HERE OR NOT NEEDED (ANYWAY WILL FIND OUT?)
        }
    }

    public DecimalProperty(String name, PropertyType type, Range range) {
        super(name, type);
        this.range = range;
    }

//    public int getValue() {
//        return this.value;
//    }

    public void setValue(Integer value) {
        if (range == null || (value <= range.getTo().intValue() && value >= range.getFrom().intValue()))
            this.value = value.toString();
        else if(value > range.getTo().intValue())
        {
            this.value = String.valueOf(range.getTo().intValue());
        }
        else if(value < range.getFrom().intValue())
        {
            this.value = String.valueOf(range.getFrom().intValue());
        }
    }
}
