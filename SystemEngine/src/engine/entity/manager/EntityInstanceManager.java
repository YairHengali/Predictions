package engine.entity.manager;

import engine.entity.EntityDefinition;
import engine.entity.EntityInstance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface EntityInstanceManager {
    EntityInstance create(EntityDefinition entityDefinition);
    Map<String, List<EntityInstance>> getInstancesLists();
    void killEntity(int id);
}
