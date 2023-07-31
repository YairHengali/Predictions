package engine;

import java.util.*;

public class World {

    int currentNumberOfTicks = 0;
    // TODO: current Seconds of the simulation

    /////////Termination conditions:
    int maxNumberOfTicks;
    // TODO: max Seconds of the simulation(Termination conditions)

    private final Map<String, List<Entity>> name2Entities = new HashMap<>();
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
        name2Entities.computeIfAbsent(name, k -> new ArrayList<>(population)); // creates only if noe exists

        for (int i = 0; i < population; i++) {
            name2Entities.get(name).set(i, new Entity(name));
        }
    }

    public Entity getEntityByName(String entityName, int entityNum)
    {
        return name2Entities.get(entityName).get(entityNum);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("World:\n");
        stringBuilder.append("maxNumberOfTicks=").append(maxNumberOfTicks).append("\n");
        if (!name2Entities.isEmpty())
        {
            stringBuilder.append("Entities:\n");
            for (Map.Entry<String, List<Entity>> entry : name2Entities.entrySet()) {
                String EntityName = entry.getKey();
                List<Entity> entities = entry.getValue();

                for (Entity entity : entities) {
                    stringBuilder.append(entity).append("\n");
                }
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
