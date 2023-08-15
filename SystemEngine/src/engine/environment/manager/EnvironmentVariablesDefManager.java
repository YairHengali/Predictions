package engine.environment.manager;

import engine.property.PropertyDefinition;

import java.util.Collection;

public interface EnvironmentVariablesDefManager {
    void addEnvironmentVariable(PropertyDefinition propertyDefinition);
    Collection<PropertyDefinition> getEnvironmentVariables();
    PropertyDefinition getEnvironmentVariableByName(String name) throws IllegalArgumentException;
}
