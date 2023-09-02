package engine.property.api;

import engine.property.PropertyType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class PropertyInstance implements Serializable {
    protected String value;
    private final String name;
    private final PropertyType type;

    private Map<Integer, String> tick2value = new HashMap<>();
//    private Integer lastTickModified = 0;


    public PropertyInstance(String name, PropertyType type) {
        this.name = name;
        this.type = type;
    }

    public PropertyType getType(){
        return this.type;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public int getLastTickModified() {
        Optional<Integer> lastModifiedTick = tick2value.keySet().stream().max(Integer::compare);
        return lastModifiedTick.get();
    }

    public void setLastTickModified(Integer currTick) {
        if(currTick != null){
            this.tick2value.put(currTick, this.value);
        }
    }}
