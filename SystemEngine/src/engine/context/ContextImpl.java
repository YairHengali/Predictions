package engine.context;

import engine.entity.EntityInstance;
import engine.entity.manager.EntityInstanceManager;
import engine.environment.active.ActiveEnvironmentVariables;
import engine.property.api.PropertyInstance;

import java.io.Serializable;

public class ContextImpl implements Context, Serializable {
    private final EntityInstance primaryEntityInstance;
    private final EntityInstanceManager entityInstanceManager;
    private final ActiveEnvironmentVariables activeEnvironment;

    public ContextImpl(EntityInstance primaryEntityInstance, EntityInstanceManager entityInstanceManager, ActiveEnvironmentVariables activeEnvironment) {
        this.primaryEntityInstance = primaryEntityInstance;
        this.entityInstanceManager = entityInstanceManager;
        this.activeEnvironment = activeEnvironment;
    }

    @Override
    public EntityInstance getPrimaryEntityInstance() {
        return this.primaryEntityInstance;
    }

    @Override
    public PropertyInstance getEnvironmentVariable(String name) {
        return activeEnvironment.getEvnVariable(name);
    }

    @Override
    public EntityInstanceManager getEntityInstanceManager() {
        return entityInstanceManager;
    }

    @Override
    public ActiveEnvironmentVariables getActiveEnvironmentVariables()
    {return activeEnvironment;}
}
