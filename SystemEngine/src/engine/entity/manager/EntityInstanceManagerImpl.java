package engine.entity.manager;

import engine.entity.EntityDefinition;
import engine.entity.EntityInstance;
import engine.property.PropertyDefinition;
import engine.property.api.PropertyInstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityInstanceManagerImpl implements EntityInstanceManager{
    private int count;
    private Map<String, List<EntityInstance>> name2EntitiesIns;


    public EntityInstanceManagerImpl() {
        count = 0;
        name2EntitiesIns = new HashMap<>();
    }

    @Override
    public EntityInstance create(EntityDefinition entityDefinition) {

        count++;
        EntityInstance newEntityInstance = new EntityInstance(entityDefinition, count);

        if(!name2EntitiesIns.containsKey(entityDefinition.getName())) {
            name2EntitiesIns.put(entityDefinition.getName(),new ArrayList<>());
        }
        name2EntitiesIns.get(entityDefinition.getName()).add(newEntityInstance);
        return newEntityInstance;
    }

    @Override
    public Map<String, List<EntityInstance>> getInstancesLists() {
        return name2EntitiesIns;
    }

    @Override
    public void killEntity(int id) {
        //TODO: kill the entity
    }
}
