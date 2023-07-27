package engine;

public class integerProperty extends Property {
    int value;
}

public class booleanProperty extends Property {
    boolean value;
}

public class decimalProperty extends Property {
    float value;
}

public class stringProperty extends Property {
    String value;
}

public class Property {
    private eType type = null;
    private object value;
    private String name = "";
    private Range valueRange = null;


    @Override
    public String toString() {
        return "Property{" +
                "type=" + type +
                ", name=" + name +
                ", valueRange=" + valueRange +
                '}';
    }
}

