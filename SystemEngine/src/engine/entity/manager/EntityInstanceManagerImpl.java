package engine.entity.manager;

import engine.entity.EntityDefinition;
import engine.entity.EntityInstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityInstanceManagerImpl implements EntityInstanceManager{
    private int count;
    private Map<String, List<EntityInstance>> name2EntInstancesList;


    public EntityInstanceManagerImpl() {
        count = 0;
        name2EntInstancesList = new HashMap<>();
    }

    @Override
    public EntityInstance create(EntityDefinition entityDefinition) {

        count++;
        EntityInstance newEntityInstance = new EntityInstance(entityDefinition, count);

        if(!name2EntInstancesList.containsKey(entityDefinition.getName())) {
            name2EntInstancesList.put(entityDefinition.getName(),new ArrayList<>());
        }
        name2EntInstancesList.get(entityDefinition.getName()).add(newEntityInstance);
        return newEntityInstance;
    }

    @Override
    public Map<String, List<EntityInstance>> getInstancesLists() {
        return name2EntInstancesList;
    }

    @Override
    public void killEntity(EntityInstance entityInstance) { //TODO: IF Preffered can change to map of maps by Ids2Instances..
        List<EntityInstance> sameEntityInstancesList = name2EntInstancesList.get(entityInstance.getName());
        for (int i = 0; i < sameEntityInstancesList.size(); i++) {
            if (sameEntityInstancesList.get(i).getId() == entityInstance.getId()) {
                sameEntityInstancesList.remove(i); //VALIDATE THAT REMOVE FROM ORIGINAL LIST
                break;
            }
        };
    }


    ///////////////////////////


    @Override
    public List<EntityInstance> getInstancesListByName(String entityName) {
        return name2EntInstancesList.get(entityName);
    }
}
