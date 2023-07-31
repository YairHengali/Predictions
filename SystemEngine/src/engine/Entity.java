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
    public Entity(String name) {
        this.name = name;
    }

    public <T> void addProperty(String name, PropertyType type, T value, Range valueRange, boolean isInitializedRandomly)
    {
        Property<T> newProperty = new Property<>(name,type, value, valueRange, isInitializedRandomly);
        name2property.put(name, newProperty);
    }

    public <T> void addProperty2(Property<T> propertyToAdd) //TODO: DECIDE WHICH OF THEM BETTER
    {
        name2property.put(propertyToAdd.getName(), propertyToAdd);
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
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Entity:\n");
        stringBuilder.append("name='").append(name).append("'\n");
        stringBuilder.append("population=").append(population).append("\n");
        if (!name2property.isEmpty())
        {
            stringBuilder.append("Properties:\n");
            for (Map.Entry<String, Property<?>> entry : name2property.entrySet()) {
                stringBuilder.append(entry.getValue()).append("\n");
            }
        }

        return stringBuilder.toString();
    }
}
