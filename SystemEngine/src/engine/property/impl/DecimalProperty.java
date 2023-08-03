package engine.property.impl;

import engine.Range;
import engine.property.PropertyDefinition;
import engine.property.PropertyType;
import engine.property.api.PropertyAPI;

public class DecimalProperty implements PropertyAPI {
    private String name;
    private PropertyType type;
    private int value;
    private Range range;

    public DecimalProperty(String name, PropertyType type, int value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }
    public DecimalProperty(PropertyDefinition propertyDefinition) {
        this.name = propertyDefinition.getName();
        this.type = propertyDefinition.getType();
        this.range = propertyDefinition.getValueRange();

        if(propertyDefinition.isInitializedRandomly()){
            // TODO: implement randomly
        }
        else {
            this.value = Integer.parseInt(propertyDefinition.getInitValue());
        }


    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public void setValue(Object value) {
        if(value instanceof Integer)
        {
            this.value = (int)value;
        }
    }

    @Override
    public PropertyType getType(){
        return this.type;
    }
}
