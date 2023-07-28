package engine;

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

public class Property <T> {
    //private eType type = null;
    private T value;
    private String name = "";
    private Range valueRange = null;


    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Property{" +
                "type=" + type +
                ", name=" + name +
                ", valueRange=" + valueRange +
                '}';
    }

}

