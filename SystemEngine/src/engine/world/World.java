package engine.world;

import engine.entity.EntityDefinition;
import engine.entity.manager.EntityInstanceManager;
import engine.entity.manager.EntityInstanceManagerImpl;
import engine.environment.active.ActiveEnvironmentVariables;
import engine.environment.active.ActiveEnvironmentVariablesImpl;
import engine.property.PropertyDefinition;
import engine.rule.Rule;

import java.util.*;

public class World {

    private int currentNumberOfTicks = 0;
    private long startTime; // TODO: create StopWatch class

    private final Map<String, EntityDefinition> name2EntitiesDef = new HashMap<>();
    private final Map<String, PropertyDefinition> name2EnvironmentVariablesDef = new HashMap<>();
    private final Map<String, Rule> name2Rule = new HashMap<>();

    EntityInstanceManager entityInstanceManager;
    ActiveEnvironmentVariables activeEnvironmentVariables;


    ///////// Termination conditions:
    private int maxNumberOfTicks = 100;
    private long SecondsToTerminate = 10000;

    private Set<String> methodsNames;

    public World() {
        methodsNames = new HashSet<>();
        methodsNames.add("environment"); //TODO: MAYBE ENUM
        methodsNames.add("random"); //TODO: MAYBE ENUM
    }


    public void runMainLoop()
    {
        runTick0();
        runLoop();
    }
    private void runTick0()
    {
        entityInstanceManager = new EntityInstanceManagerImpl();
        for(EntityDefinition entityDefinition : name2EntitiesDef.values())
        {
            entityInstanceManager.createEntityInstances(entityDefinition);
        }

        activeEnvironmentVariables = new ActiveEnvironmentVariablesImpl();
        for(PropertyDefinition envVarDef : name2EnvironmentVariablesDef.values())
        {
            activeEnvironmentVariables.createEvnVariableFromDef(envVarDef);
        }

        this.startTime = System.currentTimeMillis();
        currentNumberOfTicks++;
    }

    private void runLoop() //TICK 1 and up...;
    {
        while (!isTermination())
        {
            for (Rule rule: name2Rule.values()) { //TODO: MAYBE NEED TO BE ORDERED (LIST OF RULES) - page 13
                if (rule.isActive(this.currentNumberOfTicks))
                {
                    rule.runRule(this.entityInstanceManager, this.activeEnvironmentVariables);
                }
            }
            currentNumberOfTicks++;
        }
    }

    public void addEnvironmentVariableDef(PropertyDefinition EnvVarDefinitionToAdd)
    {
        name2EnvironmentVariablesDef.put(EnvVarDefinitionToAdd.getName(), EnvVarDefinitionToAdd);
    }

    public PropertyDefinition getEnvironmentVariableDefByName(String name)
    {
        return name2EnvironmentVariablesDef.get(name);
    }

    public void addEntityDefinition(EntityDefinition entityDefinitionToAdd){
        name2EntitiesDef.put(entityDefinitionToAdd.getName(), entityDefinitionToAdd);
    }
    public EntityDefinition getEntityDefinitionByName(String entityName){
        return name2EntitiesDef.get(entityName);
    }

    public void addRule(Rule ruleToAdd)//TODO: Decide which better^^
    {
        name2Rule.put(ruleToAdd.getName(), ruleToAdd);
    }

    public Rule getRuleByName(String ruleName)
    {
        return name2Rule.get(ruleName);
    }

    public boolean isTermination(){
        return ((System.currentTimeMillis()-this.startTime)/1000 >= this.SecondsToTerminate) ||
                (this.currentNumberOfTicks >= this.maxNumberOfTicks);
    }

    public Object environment(String varName) throws Exception {
        if(this.name2EnvironmentVariablesDef.containsKey(varName)) {
            return this.name2EnvironmentVariablesDef.get(varName);
        }
        throw new Exception("Environment Variable Not Found!");
    }

    public int random(String argValue) throws NumberFormatException{
        try {
            int val = Integer.parseInt(argValue);
            Random random = new Random();
            return random.nextInt(val) + 1;
        }
        catch (NumberFormatException e){
            throw e;
        }
    }


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
//            for (Map.Entry<String, RuleImpl> entry : name2Rule.entrySet()) {
//                stringBuilder.append(entry.getValue()).append("\n");
//            }
//        }
//        return stringBuilder.toString();
//    }

}
