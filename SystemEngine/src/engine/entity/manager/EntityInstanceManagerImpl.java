package engine.entity.manager;

import engine.entity.EntityDefinition;
import engine.entity.EntityInstance;

import java.util.*;

public class EntityInstanceManagerImpl implements EntityInstanceManager{
    private int count;
    private Map<String, List<EntityInstance>> name2EntInstancesList;

    private Set<EntityInstance>  EntitiesToKill;

    public EntityInstanceManagerImpl() {
        count = 0;
        name2EntInstancesList = new HashMap<>();
        EntitiesToKill = new HashSet<>();
    }

    @Override
    public void createEntityInstances(EntityDefinition entityDefinition) {
        for (int i = 0; i < entityDefinition.getPopulation(); i++) {
            EntityInstance newEntityInstance = new EntityInstance(entityDefinition, count);
            count++;

            if(!name2EntInstancesList.containsKey(entityDefinition.getName())) {
                name2EntInstancesList.put(entityDefinition.getName(),new ArrayList<>());
            }
            name2EntInstancesList.get(entityDefinition.getName()).add(newEntityInstance);
        }
    }

    @Override
    public Map<String, List<EntityInstance>> getInstancesLists() {
        return name2EntInstancesList;
    }

//    @Override
//    public void killEntity(EntityInstance entityInstance) { //TODO: IF Preffered can change to map of maps by Ids2Instances..
//        List<EntityInstance> sameEntityInstancesList = name2EntInstancesList.get(entityInstance.getName());
//        for (int i = 0; i < sameEntityInstancesList.size(); i++) {
//            if (sameEntityInstancesList.get(i).getId() == entityInstance.getId()) {
//                name2EntInstancesList.get(entityInstance.getName()).remove(i); //Throws EXCEPTION BECAUSE WE ITERRATE THIS.
//                break;
//            }
//        };
//    }

    @Override
    public void killEntity(EntityInstance entityInstance) { //TODO: IF Preffered can change to map of maps by Ids2Instances..
        EntitiesToKill.add(entityInstance);
    }

    @Override
    public void killEntities()
    {
        for (EntityInstance entityInstance : EntitiesToKill) {
            name2EntInstancesList.get(entityInstance.getName()).remove(entityInstance);
        }
        EntitiesToKill.clear();
    }

    ///////////////////////////


    @Override
    public List<EntityInstance> getInstancesListByName(String entityName) {
        return name2EntInstancesList.get(entityName);
    }
}
