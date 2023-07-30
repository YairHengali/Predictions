package engine;

import java.util.*;

public class Entity {
    private final String name;
    private int population;
    private final Map<String, Property<?>> name2property = new HashMap<>(); //MAYBE MAP (NAME : PROPERTY)

    public Entity(String name, int population) {
        this.name = name;
        this.population = population;
    }

    public <T> void addProperty(String name, T value, Range valueRange)
    {
        Property<T> newProperty = new Property<>(name, value, valueRange);
        name2property.put(name, newProperty);
    }

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
                ", name2property=" + name2property +
                '}';
    }
}
