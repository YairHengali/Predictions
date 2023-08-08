package engine.context;

import engine.entity.EntityInstance;
import engine.entity.manager.EntityInstanceManager;
import engine.environment.active.ActiveEnvironmentVariables;
import engine.property.api.PropertyInstance;

public class ContextImpl implements Context{
    private EntityInstance primaryEntityInstance;
    private EntityInstanceManager entityInstanceManager;
    private ActiveEnvironmentVariables activeEnvironment;

    public ContextImpl(EntityInstance primaryEntityInstance, EntityInstanceManager entityInstanceManager, ActiveEnvironmentVariables activeEnvironment) {
        this.primaryEntityInstance = primaryEntityInstance;
        this.entityInstanceManager = entityInstanceManager;
        this.activeEnvironment = activeEnvironment;
    }

    @Override
    public EntityInstance getPrimaryEntityInstance() {
        return this.primaryEntityInstance;
    }

//    @Override
//    public void removeEntity(EntityInstance entityInstance) {
//    }
    @Override
    public PropertyInstance getEnvironmentVariable(String name) {
        return activeEnvironment.getPropertyInstance(name);
    }

    //////

    @Override
    public EntityInstanceManager getEntityInstanceManager() {
        return entityInstanceManager;
    }

    @Override
    public ActiveEnvironmentVariables getActiveEnvironmentVariables()
    {return activeEnvironment;}
}
