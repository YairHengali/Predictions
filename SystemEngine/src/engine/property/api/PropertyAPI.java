package engine.property.api;

import engine.property.PropertyType;

public interface PropertyAPI {
    Object getValue();
    void setValue(Object value);
    PropertyType getType();
}
