package engine.environment.manager;

import engine.environment.active.ActiveEnvironmentVariables;
import engine.environment.active.ActiveEnvironmentVariablesImpl;
import engine.property.PropertyDefinition;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EnvironmentVariablesManagerImpl implements EnvironmentVariablesManager {

    private Map<String, PropertyDefinition> name2PropertyDef;

    public EnvironmentVariablesManagerImpl() {
        this.name2PropertyDef = new HashMap<>();
    }

    @Override
    public void addEnvironmentVariable(PropertyDefinition propertyDefinition) {
        name2PropertyDef.put(propertyDefinition.getName(), propertyDefinition);
    }

    @Override
    public ActiveEnvironmentVariables createActiveEnvironment() {
        return new ActiveEnvironmentVariablesImpl();
    }

    @Override
    public Collection<PropertyDefinition> getEnvVariables() {
        return this.name2PropertyDef.values();
    }
}
