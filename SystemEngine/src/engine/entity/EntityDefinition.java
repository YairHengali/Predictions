package engine.entity;
import engine.property.PropertyDefinition;

import java.util.*;

public class EntityDefinition {
    private final String name;
    private int population = 0;
    private final Map<String, PropertyDefinition> name2propertyDef = new HashMap<>(); //MAYBE MAP (NAME : PROPERTY)

    public EntityDefinition(String name, int population) {
        this.population = population;
        this.name = name;
    }

    public void addPropertyDefinition(PropertyDefinition propertyDefinitionToAdd){
        name2propertyDef.put(propertyDefinitionToAdd.getName(), propertyDefinitionToAdd);
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
