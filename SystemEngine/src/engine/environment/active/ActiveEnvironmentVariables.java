package engine.environment.active;

import engine.property.PropertyDefinition;
import engine.property.api.PropertyInstance;

public interface ActiveEnvironmentVariables {
    PropertyInstance getEvnVariable(String name);
//    void addPropertyInstance(PropertyInstance propertyToAdd);

    void createEvnVariableFromDef(PropertyDefinition EvnVarDef);
}
