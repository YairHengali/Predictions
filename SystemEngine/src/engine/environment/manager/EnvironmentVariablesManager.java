package engine.environment.manager;

import engine.environment.active.ActiveEnvironmentVariables;
import engine.property.PropertyDefinition;

import java.util.Collection;

public interface EnvironmentVariablesManager {
    void addEnvironmentVariable(PropertyDefinition propertyDefinition);
    ActiveEnvironmentVariables createActiveEnvironment();
    Collection<PropertyDefinition> getEnvironmentVariables();
    PropertyDefinition getEnvironmentVariableByName(String name) throws IllegalArgumentException;
}
