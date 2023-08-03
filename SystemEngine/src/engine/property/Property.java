package engine.property;

//public class integerProperty extends Property {
//    int value;
//    private Range valueRange = null;
//
//    public int getValue() {
//        return value;
//    }
//
//    public void setValue(int value) {
//        this.value = value;
//    }
//
//    public Range getValueRange() {
//        return valueRange;
//    }
//
//    public void setValueRange(Range valueRange) {
//        this.valueRange = valueRange;
//    }
//}
//
//public class booleanProperty extends Property {
//    boolean value;
//
//    public boolean isValue() {
//        return value;
//    }
//
//    public void setValue(boolean value) {
//        this.value = value;
//    }
//}
//
//public class decimalProperty extends Property {
//    float value;
//    private Range valueRange = null;
//
//    public float getValue() {
//        return value;
//    }
//
//    public void setValue(float value) {
//        this.value = value;
//    }
//
//    public Range getValueRange() {
//        return valueRange;
//    }
//
//    public void setValueRange(Range valueRange) {
//        this.valueRange = valueRange;
//    }
//}
//
//public class stringProperty extends Property {
//    String value;
//
//    public String getValue() {
//        return value;
//    }
//
//    public void setValue(String value) {
//        this.value = value;
//    }
//}

import engine.Range;

public class Property <T> {
    private final String name;
    private final PropertyType type;
    private T value;
    private final Range valueRange;
    private final boolean isInitializedRandomly;


    public Property(String name, PropertyType type, T value, Range valueRange, boolean isInitializedRandomly) {
        this.name = name;
        this.type = type;
        this.value = value;//TODO: Exception if out of range / invalid format
        this.valueRange = valueRange;
        this.isInitializedRandomly = isInitializedRandomly;
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    } //TODO: Exception if out of range / invalid format

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Property:\n");
        stringBuilder.append("name='").append(name).append("'\n");
        stringBuilder.append("type=").append(type).append("\n");
        stringBuilder.append("value=").append(value).append("\n");
        if (valueRange != null){
            stringBuilder.append("valueRange=").append(valueRange).append("\n");}
        stringBuilder.append("is initialized randomly=").append(isInitializedRandomly).append("\n");
        return stringBuilder.toString();
    }
}

