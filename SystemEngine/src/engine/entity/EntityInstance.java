package engine.entity;

import engine.property.PropertyDefinition;
import engine.property.api.PropertyInstance;
import engine.property.impl.BooleanProperty;
import engine.property.impl.DecimalProperty;
import engine.property.impl.FloatProperty;
import engine.property.impl.StringProperty;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class EntityInstance implements Serializable {

    private final int id;
    private final String name;
    private final Map<String, PropertyInstance> name2property = new HashMap<>();

    public EntityInstance(EntityDefinition entityDefinition, int id) {
        this.id = id;
        this.name = entityDefinition.getName();

        for (PropertyDefinition def : entityDefinition.getName2propertyDef().values()) {
            try {
                switch (def.getType()) {
                    case BOOLEAN:
                        this.name2property.put(def.getName(), new BooleanProperty(def));
                        break;
                    case DECIMAL:
                        this.name2property.put(def.getName(), new DecimalProperty(def));
                        break;
                    case FLOAT:
                        this.name2property.put(def.getName(), new FloatProperty(def));
                        break;
                    case STRING:
                        this.name2property.put(def.getName(), new StringProperty(def));
                        break;
                }
            }catch(IllegalArgumentException e)
            {
                throw new IllegalArgumentException("Error, cannot assign the value: " + def.getInitValue() + " to the property: "+ def.getName() + " which is of type: " + def.getType() + " in the entity: " + entityDefinition.getName());
            }
            catch(RuntimeException e)
            {
                throw new IllegalArgumentException("Error, the init value of the property: " + def.getName() + ", in the entity " + entityDefinition.getName() + " is out of range");
            }
        }
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public PropertyInstance getPropertyByName(String propertyName) {
        return name2property.get(propertyName);
    }
}
