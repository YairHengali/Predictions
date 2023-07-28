package engine;

import java.util.*;

public class Entity {
    private String name;
    private int population;
    private Map<String, Property<?>> name2property = new HashMap<>(); //MAYBE MAP (NAME : PROPERTY)

    public Property<?> getPropertyByName(String propertyName)
    {
        return name2property.get(propertyName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return Objects.equals(name, entity.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Entity{" +
                "name='" + name + '\'' +
                ", population=" + population +
                ", properties=" + properties +
                '}';
    }
}
