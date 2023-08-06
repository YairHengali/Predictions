package engine.entity;
import engine.property.PropertyDefinition;

import java.util.*;

public class EntityDefinition {
    private final String name;
    private final int population;
    private final Map<String, PropertyDefinition> name2propertyDef = new HashMap<>();

    public EntityDefinition(String name, int population) {
        this.population = population;
        this.name = name;
    }

    public void addPropertyDefinition(PropertyDefinition propertyDefinitionToAdd){
        name2propertyDef.put(propertyDefinitionToAdd.getName(), propertyDefinitionToAdd);
    }

    public PropertyDefinition getPropertyDefinitionByName(String propertyName){
        return name2propertyDef.get(propertyName);
    }

    public String getName() {
        return name;
    }

    public int getPopulation() {
        return population;
    }

    public Map<String, PropertyDefinition> getName2propertyDef() {
        return name2propertyDef;
    }
}
