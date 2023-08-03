package engine;

import engine.property.Property;
import engine.property.PropertyType;

import java.util.*;

public class EntityDef {
    private final String name;
    private int population;
    private final Collection<Map<String, Property>> ListOfName2property = new ArrayList<>(); //MAYBE MAP (NAME : PROPERTY)

    public EntityDef(String name, int population) {
        this.name = name;
        this.population = population;
        for (int i = 0; i < population; i++) {
            ListOfName2property.add(new HashMap<>());
        }
    }
    public EntityDef(String name) {
        this.name = name;
    }


//
//    public void addProperty(String name, PropertyType type, Range valueRange, boolean isInitializedRandomly, String initValue)
//    {
//        Property newProperty = null;
//        switch (type) {
//            case PropertyType.BOOLEAN:
//                    newProperty = new booleanProperty(name, type, valueRange, isInitializedRandomly, initValue);
//                for (Map<String, Property> map : ListOfName2property) {
//                    map.put(name, newProperty);
//                }
//                break;
//            case PropertyType.DECIMAL:
//                newProperty = new decimalProperty(name, type, valueRange, isInitializedRandomly, initValue);
//                for (Map<String, Property> map : ListOfName2property) {
//                    map.put(name, newProperty);
//                }
//                break;
//            case PropertyType.FLOAT:
//                newProperty = new floatProperty(name, type, valueRange, isInitializedRandomly, initValue);
//                for (Map<String, Property> map : ListOfName2property) {
//                    map.put(name, newProperty);
//                }
//                break;
//            case PropertyType.STRING:
//                newProperty = new stringProperty(name, type, valueRange, isInitializedRandomly, initValue);
//                for (Map<String, Property> map : ListOfName2property) {
//                    map.put(name, newProperty);
//                }
//                break;
//            default:
//                break;
//        }
//    }
//
//    public <T> void addProperty2(Property<T> propertyToAdd) //TODO: DECIDE WHICH OF THEM BETTER
//    {
//        name2property.put(propertyToAdd.getName(), propertyToAdd);
//    }
//
//    public Property getPropertyByName(String propertyName)
//    {
//        return name2property.get(propertyName);
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityDef entity = (EntityDef) o;
        return Objects.equals(name, entity.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

//    @Override
//    public String toString() {
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("Entity:\n");
//        stringBuilder.append("name='").append(name).append("'\n");
//        stringBuilder.append("population=").append(population).append("\n");
//        if (!name2property.isEmpty())
//        {
//            stringBuilder.append("Properties:\n");
//            for (Map.Entry<String, Property<?>> entry : name2property.entrySet()) {
//                stringBuilder.append(entry.getValue()).append("\n");
//            }
//        }
//
//        return stringBuilder.toString();
//    }
}
