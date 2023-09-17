package engine.entity.manager.api;

import engine.entity.EntityDefinition;
import engine.entity.EntityInstance;

import java.util.List;
import java.util.stream.Stream;

public interface EntityInstanceManager {
    Stream<EntityInstance> getAllEntitiesInstances();

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

    /////FOR PROXIMITY:
    boolean isEnt1NearEnt2(EntityInstance entityInstance1, EntityInstance entityInstance2, int depth);

    double getAvgOfUnmodifiedTicksOfProperty(String entityName, String propertyName, int lastTick);

    double getAverageValueOfProperty(String entityName, String propertyName);
}
