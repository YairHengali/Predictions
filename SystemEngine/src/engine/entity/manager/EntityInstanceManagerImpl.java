package engine.entity.manager;

import engine.entity.EntityDefinition;
import engine.entity.EntityInstance;
import engine.property.PropertyType;
import engine.property.api.PropertyInstance;
import engine.property.impl.BooleanProperty;
import engine.property.impl.FloatProperty;
import engine.property.impl.StringProperty;

import java.io.Serializable;
import java.util.*;

public class EntityInstanceManagerImpl implements EntityInstanceManager, Serializable {
    private int count;
    private final Map<String, List<EntityInstance>> name2EntInstancesList;
    private final Map<String, EntityDefinition> name2EntitiesDef = new HashMap<>();


    private final Set<EntityInstance> EntitiesToKill;


    private final Map<EntityInstance, String> EntityInstance2DerivedName;
    private final Set<String> EntitiesToCreate;

    public EntityInstanceManagerImpl(Collection<EntityDefinition> entityDefinitionCollection) {
        count = 0;
        name2EntInstancesList = new HashMap<>();
        EntitiesToKill = new HashSet<>();
        entityDefinitionCollection.forEach(entityDefinition -> this.name2EntitiesDef.put(entityDefinition.getName(), entityDefinition.clone()));


        EntityInstance2DerivedName = new HashMap<>();
        EntitiesToCreate = new HashSet<>();
    }

    @Override
    public void createEntitiesInstances() {
        for(EntityDefinition entityDefinition: this.name2EntitiesDef.values()){
            for (int i = 0; i < entityDefinition.getPopulation(); i++) {
                EntityInstance newEntityInstance = new EntityInstance(entityDefinition, count);
                count++;

                if(!name2EntInstancesList.containsKey(entityDefinition.getName())) {
                    name2EntInstancesList.put(entityDefinition.getName(),new ArrayList<>());
                }
                name2EntInstancesList.get(entityDefinition.getName()).add(newEntityInstance);
            }
        }

    }

    @Override
    public void killEntity(EntityInstance entityInstance) {
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

    @Override
    public List<EntityInstance> getInstancesListByName(String entityName) {
        return name2EntInstancesList.get(entityName);
    }

    @Override
    public void createAnInstance(String name) {

    }

    @Override
    public EntityDefinition getEntityDefByName(String entityName) {
        return name2EntitiesDef.get(entityName);
    }



    ///////FOR REPLACE ACTION:

    @Override
    public void createScratchEntity(String entityName) {
        EntitiesToCreate.add(entityName);
    }
    @Override
    public void createScratchEntities() {
        for (String entityName : EntitiesToCreate) {
            EntityInstance newEntity = new EntityInstance(name2EntitiesDef.get(entityName), count);
            count++;

            name2EntInstancesList.get(entityName).add(newEntity);
        }
        EntitiesToCreate.clear();
    }
    @Override
    public void createDerivedEntity(EntityInstance entityInstance, String EntityToCreate){
        EntityInstance2DerivedName.put(entityInstance, EntityToCreate);
    }
    @Override
    public void createDerivedEntities()
    {
        for (Map.Entry<EntityInstance, String> entry : EntityInstance2DerivedName.entrySet()) {
            createDerivedEntityInstance(entry.getKey(), entry.getValue());

            //KILL THE MAIN ENTITY:
            name2EntInstancesList.get(entry.getKey().getName()).remove(entry.getKey());
        }

        EntityInstance2DerivedName.clear();
    }

    private void createDerivedEntityInstance(EntityInstance entityInstance, String derivedEntityName){
        EntityInstance derivedEntity = new EntityInstance(name2EntitiesDef.get(derivedEntityName), count);
        count++;

        for (PropertyInstance propertyInstance: entityInstance.getProperties()) {
            String propertyName = propertyInstance.getName();
            PropertyType propertyType = propertyInstance.getType();

            PropertyInstance propertyAtDerived = derivedEntity.getPropertyByName(propertyName);
            if (propertyAtDerived != null && propertyAtDerived.getType() == propertyType){
                switch (propertyType) { //TODO: LOOKS UGLY AND MIGHT BE BETTER TO IMPLEMENT SET VALUE IN PROPERTY INSTANCE..
                    case BOOLEAN:
                        if (propertyAtDerived instanceof BooleanProperty) {
                            ((BooleanProperty) propertyAtDerived).setValue( Boolean.valueOf(propertyInstance.getValue()) , null);
                        }
                        break;
                    case FLOAT:
                        if (propertyAtDerived instanceof FloatProperty) {
                            ((FloatProperty) propertyAtDerived).setValue(Float.parseFloat(propertyInstance.getValue()), null);
                    }
                        break;
                    case STRING:
                        if (propertyAtDerived instanceof StringProperty) {
                            ((StringProperty) propertyAtDerived).setValue(propertyInstance.getValue(), null);
                        }
                        break;
                }
            }
        }
    }
}
