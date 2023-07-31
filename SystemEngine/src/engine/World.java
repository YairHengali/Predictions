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

    public <T> void addEnvironmentVariable(String name, PropertyType type, T value, Range valueRange)
    {
        Property<T> newEnvVar = new Property<>(name, type, value, valueRange, false);
        name2EnvironmentVariables.put(name, newEnvVar);
    }

    public Property<?> getEnvironmentVariableByName(String EnvironmentVariableName)
    {
        return name2EnvironmentVariables.get(EnvironmentVariableName);
    }

    public void addRule(String name, int howManyTicksForActivation, float probabilityForActivation)
    {
        Rule newRule = new Rule(name, howManyTicksForActivation, probabilityForActivation);
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
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("World:\n");
        stringBuilder.append("maxNumberOfTicks=").append(maxNumberOfTicks).append("\n");
        if (!name2Entity.isEmpty())
        {
            stringBuilder.append("Entities:\n");
            for (Map.Entry<String, Entity> entry : name2Entity.entrySet()) {
                stringBuilder.append(entry.getValue()).append("\n");
            }
        }
        if (!name2Rule.isEmpty())
        {
            stringBuilder.append("Rules:\n");
            for (Map.Entry<String, Rule> entry : name2Rule.entrySet()) {
                stringBuilder.append(entry.getValue()).append("\n");
            }
        }
        return stringBuilder.toString();
    }
}
