package engine.entity;

import engine.property.PropertyDefinition;
import engine.property.api.PropertyAPI;
import engine.property.impl.DecimalProperty;

import java.util.HashMap;
import java.util.Map;

public class EntityInstance {

    private final String name;
    private final Map<String, PropertyAPI> name2property = new HashMap<>();

    public EntityInstance(EntityDefinition entityDefinition) {
        this.name = entityDefinition.getName();
        for (PropertyDefinition def : entityDefinition.getName2propertyDef().values()) {

            switch (def.getType()){
                case BOOLEAN:
                    //this.name2property.put(def.getName(), new BooleanProperty(def));
                    break;
                case DECIMAL:
                    this.name2property.put(def.getName(), new DecimalProperty(def));
                    break;
                case FLOAT:
                    //this.name2property.put(def.getName(), new FloatProperty(def));
                    break;
                case STRING:
                    //this.name2property.put(def.getName(), new StringProperty(def));
                    break;
            }
        }
    }

    public PropertyAPI getPropertyByName(String propertyName) {
        return name2property.get(propertyName);
    }
}
