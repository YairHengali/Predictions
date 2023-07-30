package engine;

import java.util.*;

public class World {

    int currentNumberOfTicks = 0;
    // TODO: current Seconds of the simulation

    /////////Termination conditions:
    int maxNumberOfTicks;
    // TODO: max Seconds of the simulation(Termination conditions)

    private final Map<String, Entity> name2Entity = new HashMap<>();
    private final Map<String, Rule> name2Rule = new HashMap<>();
    private final Map<String, Property<?>> name2EnvironmentVariables = new HashMap<>();

    //TODO: ADD


    public <T> void addEnvironmentVariable(String name, T value, Range valueRange)
    {
        Property<T> newEnvVar = new Property<>(name, value, valueRange);
        name2EnvironmentVariables.put(name, newEnvVar);
    }

    public Property<?> getEnvironmentVariableByName(String EnvironmentVariableName)
    {
        return name2EnvironmentVariables.get(EnvironmentVariableName);
    }

    public void addRule(String name)
    {
        Rule newRule = new Rule(name);
        name2Rule.put(name, newRule);
    }

    public Rule getRuleByName(String ruleName)
    {
        return name2Rule.get(ruleName);
    }

    public void addEntity(String name, int population)
    {
        Entity newEntity = new Entity(name, population);
        name2Entity.put(name, newEntity);
    }

    public Entity getEntityByName(String entityName)
    {
        return name2Entity.get(entityName);
    }

    @Override
    public String toString() {
        return "World{" +
                "maxNumberOfTicks=" + maxNumberOfTicks +
                ", name2Entity=" + name2Entity +
                ", name2Rule=" + name2Rule +
                '}';
    }
}
