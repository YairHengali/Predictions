package engine.environment.active;

import engine.property.api.PropertyInstance;

public interface ActiveEnvironmentVariables {
    PropertyInstance getPropertyInstance(String name);
    void addPropertyInstance(PropertyInstance propertyToAdd);
}
