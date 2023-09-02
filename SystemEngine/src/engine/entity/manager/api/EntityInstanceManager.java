package engine.entity.manager.api;

import engine.entity.EntityDefinition;
import engine.entity.EntityInstance;

import java.util.List;

public interface EntityInstanceManager {
    void createEntitiesInstancesAndLocate(int rowSize, int colSize);

    void killEntity(EntityInstance entityInstance);

    void killEntities();

    List<EntityInstance> getInstancesListByName(String entityName);

    void createAnInstance(String name); //TODO!!!

    EntityDefinition getEntityDefByName(String entityName);


    /////FOR REPLACE:
    void createScratchEntity(String entityName);

    void createScratchEntities();

    void createDerivedEntity(EntityInstance entityInstance, String EntityToCreate);

    void createDerivedEntities();

    void makeMoveToAllEntities();
}
