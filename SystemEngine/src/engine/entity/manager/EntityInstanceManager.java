package engine.entity.manager;

import engine.entity.EntityDefinition;
import engine.entity.EntityInstance;

import java.util.List;
import java.util.Map;

public interface EntityInstanceManager {
    void createEntityInstances(EntityDefinition entityDefinition);
    Map<String, List<EntityInstance>> getInstancesLists();
    void killEntity(EntityInstance entityInstance);


    //////////////////////////////// trying run on instances from outside

    List<EntityInstance> getInstancesListByName(String entityName);
}
