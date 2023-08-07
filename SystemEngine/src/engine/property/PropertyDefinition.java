package engine.property;

import engine.Range;

public class PropertyDefinition {
    private final String name;
    private final PropertyType type;
    private final Range valueRange;
    private final boolean isInitializedRandomly;
    private final String initValue;




    public PropertyDefinition(String name, PropertyType type, Range valueRange, boolean isInitializedRandomly, String initValue) {
        this.name = name;
        this.type = type;
        this.valueRange = valueRange;
        this.isInitializedRandomly = isInitializedRandomly;
        this.initValue = initValue;
    }

    public String getName() {
        return name;
    }

    public PropertyType getType() {
        return type;
    }

    public Range getValueRange() {
        return valueRange;
    }

    public boolean isInitializedRandomly() {
        return isInitializedRandomly;
    }

    public String getInitValue() {
        return initValue;
    }
}
