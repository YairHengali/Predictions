package engine.property.api;

import engine.property.PropertyType;

import java.io.Serializable;

public abstract class PropertyInstance implements Serializable {
    protected String value;
    private final String name;
    private final PropertyType type;

    public PropertyInstance(String name, PropertyType type) {
        this.name = name;
        this.type = type;
    }

    public PropertyType getType(){
        return this.type;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
