package engine.environment.active;

import engine.property.PropertyDefinition;
import engine.property.api.PropertyInstance;

import java.util.Collection;
import java.util.List;

public interface ActiveEnvironmentVariables {
    PropertyInstance getEvnVariable(String name);
//    void addPropertyInstance(PropertyInstance propertyToAdd);
    Collection<PropertyInstance> getEvnVariables();
    void createEvnVariableFromDef(PropertyDefinition EvnVarDef);
}
