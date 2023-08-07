package engine.environment.active;

import engine.property.api.PropertyInstance;

import java.util.HashMap;
import java.util.Map;

public class ActiveEnvironmentVariablesImpl implements ActiveEnvironmentVariables {
    private final Map<String, PropertyInstance> envVariables;

    public ActiveEnvironmentVariablesImpl(){
        this.envVariables = new HashMap<>();
    }
    @Override
    public PropertyInstance getPropertyInstance(String name) {
        if(this.envVariables.containsKey(name))
            return this.envVariables.get(name);
        throw new IllegalArgumentException("Can't find environment variable: " + name);
    }

    @Override
    public void addPropertyInstance(PropertyInstance propertyToAdd) {
        this.envVariables.put(propertyToAdd.getName(),propertyToAdd);

    }
}
