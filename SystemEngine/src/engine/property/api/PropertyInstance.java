package engine.property.api;

import engine.property.PropertyType;

import java.io.Serializable;

public abstract class PropertyInstance implements Serializable {
    protected String value;
    private final String name;
    private final PropertyType type;
    private int lastTickModified = 0;


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

    public int getLastTickModified() {
        return lastTickModified;
    }

    public void setLastTickModified(int lastTickModified) {
        this.lastTickModified = lastTickModified;
    }}
