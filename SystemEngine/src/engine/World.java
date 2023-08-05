package engine;

import engine.entity.EntityDefinition;
import engine.entity.EntityInstance;
import engine.property.api.PropertyInstance;

import java.util.*;

public class World {

    private int currentNumberOfTicks = 0;
    private long startTime; // TODO: create StopWatch class

    ///////// Termination conditions:
    private int maxNumberOfTicks;
    private long SecondsToTerminate;
    private final Map<String, EntityDefinition> name2EntitiesDef = new HashMap<>();
    private final Map<String, List<EntityInstance>> name2EntitiesIns = new HashMap<>();
    private final Map<String, Rule> name2Rule = new HashMap<>();
    private final Map<String, PropertyInstance> name2EnvironmentVariables = new HashMap<>();
    private Set<String> methodsNames;

    public World() {
        this.startTime = System.currentTimeMillis();
        methodsNames = new HashSet<>();
        methodsNames.add("environment"); //TODO: MAYBE ENUM
        methodsNames.add("random"); //TODO: MAYBE ENUM
    }

//    public World(int maxNumberOfTicks, long maxNumOfSeconds) {
//        this.SecondsToTerminate = maxNumOfSeconds;
//        this.startTime = System.currentTimeMillis();
//        methodsNames = new HashSet<>();
//    }
//
//    public <T> void addEnvironmentVariable(String name, PropertyType type, T value, Range valueRange)
//    {
//        Property<T> newEnvVar = new Property<>(name, type, value, valueRange, false);
//        name2EnvironmentVariables.put(name, newEnvVar);
//    }
//
//    public Property<?> getEnvironmentVariableByName(String EnvironmentVariableName)
//    {
//        return name2EnvironmentVariables.get(EnvironmentVariableName);
//    }

    public void addEntityDefinition(EntityDefinition entityDefinitionToAdd){
        name2EntitiesDef.put(entityDefinitionToAdd.getName(), entityDefinitionToAdd);
    }

    public void addRule(String name, Integer howManyTicksForActivation, Double probabilityForActivation)
    {
        Rule newRule = new Rule(name, howManyTicksForActivation, probabilityForActivation);
        name2Rule.put(name, newRule);
    }

//    public void addRule(Rule ruleToAdd)//TODO: Decide which better^^
//    {
//        name2Rule.put(name, ruleToAdd);
//    }



    public Rule getRuleByName(String ruleName)
    {
        return name2Rule.get(ruleName);
    }


    public void createEntityInstances()
    {
        for(EntityDefinition entityDefinition : name2EntitiesDef.values()) {
            List<EntityInstance> newList = new ArrayList<>(entityDefinition.getPopulation());
            for (int i = 0; i < entityDefinition.getPopulation(); i++) {
                newList.set(i, new EntityInstance(entityDefinition));
            }
            this.name2EntitiesIns.put(entityDefinition.getName(), newList );
        }
    }

//    public EntityDef getEntityByName(String entityName, int entityNum)
//    {
//        return name2Entities.get(entityName).get(entityNum);
//    }

    //@Override
//    public String toString() {
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("World:\n");
//        stringBuilder.append("maxNumberOfTicks=").append(maxNumberOfTicks).append("\n");
//        if (!name2Entities.isEmpty())
//        {
//            stringBuilder.append("Entities:\n");
//            for (Map.Entry<String, List<EntityDef>> entry : name2Entities.entrySet()) {
//                String EntityName = entry.getKey();
//                List<EntityDef> entities = entry.getValue();
//
//                for (EntityDef entity : entities) {
//                    stringBuilder.append(entity).append("\n");
//                }
//            }
//        }
//        if (!name2Rule.isEmpty())
//        {
//            stringBuilder.append("Rules:\n");
//            for (Map.Entry<String, Rule> entry : name2Rule.entrySet()) {
//                stringBuilder.append(entry.getValue()).append("\n");
//            }
//        }
//        return stringBuilder.toString();
//    }
    public boolean isTermination(){
        return ((System.currentTimeMillis()-this.startTime)/1000 >= this.SecondsToTerminate) ||
                (this.currentNumberOfTicks >= this.maxNumberOfTicks);
    }
    public Object environment(String varName) throws Exception {
        if(this.name2EnvironmentVariables.containsKey(varName)) {
            return this.name2EnvironmentVariables.get(varName);
        }
        throw new Exception("Environment Variable Not Found!");
    }

    public int random(String argValue) throws NumberFormatException{
        try {
            int val = Integer.parseInt(argValue);
            Random random = new Random();
            return random.nextInt(val+1);
        }
        catch (NumberFormatException e){
            throw e;
        }
    }


}
