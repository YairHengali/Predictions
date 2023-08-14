package engine.world;

import engine.entity.EntityDefinition;
import engine.entity.manager.EntityInstanceManager;
import engine.entity.manager.EntityInstanceManagerImpl;
import engine.environment.active.ActiveEnvironmentVariables;
import engine.environment.manager.EnvironmentVariablesManager;
//import engine.environment.manager.EnvironmentVariablesManager;
import engine.environment.manager.EnvironmentVariablesManagerImpl;
import engine.property.PropertyDefinition;
import engine.rule.Rule;

import java.util.*;

public class World {

    private int currentNumberOfTicks = 0;
    private long startTime; // TODO: create StopWatch class

    private final Map<String, EntityDefinition> name2EntitiesDef = new HashMap<>();
    //private final Map<String, PropertyDefinition> name2EnvironmentVariablesDef = new HashMap<>();
    private final Map<String, Rule> name2Rule = new HashMap<>();

    EntityInstanceManager entityInstanceManager;
    ActiveEnvironmentVariables activeEnvironmentVariables;
    EnvironmentVariablesManager environmentVariablesManager = new EnvironmentVariablesManagerImpl(); //TODO: Needed? (added c'tor)

    public EntityInstanceManager getEntityInstanceManager() {
        return entityInstanceManager;
    }

    ///////// Termination conditions:
    private Integer maxNumberOfTicks = 100; // = null TODO: initialized only for testing
    private Long SecondsToTerminate = 10000L;// = null TODO: initialized only for testing

    //private Set<String> methodsNames;

    public World() {
//        methodsNames = new HashSet<>();
//        methodsNames.add("environment"); //TODO: MAYBE ENUM
//        methodsNames.add("random"); //TODO: MAYBE ENUM
    }

    public int getMaxNumberOfTicks() {
        return maxNumberOfTicks;
    }

    public long getSecondsToTerminate() {
        return SecondsToTerminate;
    }

    public void setMaxNumberOfTicks(int maxNumberOfTicks) {
        this.maxNumberOfTicks = maxNumberOfTicks;
    }

    public void setSecondsToTerminate(long secondsToTerminate) {
        SecondsToTerminate = secondsToTerminate;
    }



//    public TerminationReason runMainLoop()
//    {
//        return runLoop();
//    }
    public void runInitIteration() //Tick0
    {
        currentNumberOfTicks = 0;
        entityInstanceManager = new EntityInstanceManagerImpl();
        for(EntityDefinition entityDefinition : name2EntitiesDef.values())
        {
            entityInstanceManager.createEntityInstances(entityDefinition);
        }

        activeEnvironmentVariables = this.environmentVariablesManager.createActiveEnvironment();
        //activeEnvironmentVariables = new ActiveEnvironmentVariablesImpl();
//        for(PropertyDefinition envVarDef : name2EnvironmentVariablesDef.values())
//        {
//            activeEnvironmentVariables.createEvnVariableFromDef(envVarDef);
//        }
        for(PropertyDefinition envVarDef : this.environmentVariablesManager.getEnvironmentVariables())
        {
            activeEnvironmentVariables.createEvnVariableFromDef(envVarDef);
        }
    }

    public TerminationReason runMainLoop() //TICK 1 and up...;
    {
        this.startTime = System.currentTimeMillis();
        currentNumberOfTicks = 1;

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
        if(currentNumberOfTicks >= maxNumberOfTicks)
        {
            return TerminationReason.MAXTICKSREACHED;
        }
        else
        {
            return TerminationReason.SECONDSREACHED;
        }
    }

    public void addEnvironmentVariableDef(PropertyDefinition envVarDefinitionToAdd)
    {
        this.environmentVariablesManager.addEnvironmentVariable(envVarDefinitionToAdd);
        //name2EnvironmentVariablesDef.put(EnvVarDefinitionToAdd.getName(), EnvVarDefinitionToAdd);
    }

    public PropertyDefinition getEnvironmentVariableDefByName(String name)
    {
        try {
            return this.environmentVariablesManager.getEnvironmentVariableByName(name);
        }
        catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    public void addEntityDefinition(EntityDefinition entityDefinitionToAdd){
        name2EntitiesDef.put(entityDefinitionToAdd.getName(), entityDefinitionToAdd);
    }
    public EntityDefinition getEntityDefinitionByName(String entityName){
        return name2EntitiesDef.get(entityName);
    }

    public void addRule(Rule ruleToAdd)
    {
        name2Rule.put(ruleToAdd.getName(), ruleToAdd);
    }

    public Rule getRuleByName(String ruleName)
    {
        return name2Rule.get(ruleName);
    }

    public boolean isTermination(){
        if (this.SecondsToTerminate != null && this.maxNumberOfTicks != null)
        {
            return ((System.currentTimeMillis()-this.startTime)/1000 >= this.SecondsToTerminate) ||
                    (this.currentNumberOfTicks >= this.maxNumberOfTicks);
        }
        else if (this.SecondsToTerminate != null)
        {
            return ((System.currentTimeMillis()-this.startTime)/1000 >= this.SecondsToTerminate);
        }
        else // this.maxNumberOfTicks != null
        {
            return (this.currentNumberOfTicks >= this.maxNumberOfTicks);
        }

    }

//    public Object environment(String varName) throws Exception {
//        try{
//            return this.activeEnvironmentVariables.getEvnVariable(varName).getValue();
//        }
//        catch (IllegalArgumentException e){
//            throw new RuntimeException(e);
//        }
//    }
//
//    public int random(String argValue) throws NumberFormatException{
//        try {
//            int val = Integer.parseInt(argValue);
//            Random random = new Random();
//            return random.nextInt(val) + 1;
//        }
//        catch (NumberFormatException e){
//            throw e;
//        }
//    }

    public Collection<Rule> getRules()
    {
        return name2Rule.values();
    }

    public Collection<PropertyDefinition> getEnvironmentVariablesDefinitions()
    {
        return this.environmentVariablesManager.getEnvironmentVariables();
    }


    public Collection<EntityDefinition> getEntitiesDefinitions()
    {
        return name2EntitiesDef.values();
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
