package engine.entity;
import engine.property.PropertyDefinition;

import java.util.*;

public class EntityDefinition {
    private final String name;
    private int population = 0;
    private final Map<String, PropertyDefinition> name2property = new HashMap<>(); //MAYBE MAP (NAME : PROPERTY)

    public EntityDefinition(String name, int population) {
        this.population = population;
        this.name = name;
    }

    public void addPropertyDefinition(PropertyDefinition propertyToAdd){
        name2property.put(propertyToAdd.getName(), propertyToAdd);
    }

    public String getName() {
        return name;
    }

    public int getPopulation() {
        return population;
    }

    public Map<String, PropertyDefinition> getName2property() {
        return name2property;
    }
}
