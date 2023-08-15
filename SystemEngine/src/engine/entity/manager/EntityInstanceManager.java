package engine.entity.manager;

import engine.entity.EntityDefinition;
import engine.entity.EntityInstance;

import java.util.List;

public interface EntityInstanceManager {
    void createEntityInstances(EntityDefinition entityDefinition);

    void killEntity(EntityInstance entityInstance);

    void killEntities();

    List<EntityInstance> getInstancesListByName(String entityName);
}
