package engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Entity {
    private String name;
    private int population;
    private List<Property> properties = new ArrayList<>();

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
