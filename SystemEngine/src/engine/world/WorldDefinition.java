package engine.world;

import engine.entity.EntityDefinition;
import engine.environment.manager.EnvironmentVariablesDefManager;
import engine.environment.manager.EnvironmentVariablesDefManagerImpl;
import engine.property.PropertyDefinition;
import engine.rule.Rule;

import java.io.Serializable;
import java.util.*;

public class WorldDefinition implements Serializable {
    private Integer maxNumberOfTicks = null;
    private Long SecondsToTerminate = null;
    private final Map<String, EntityDefinition> name2EntitiesDef = new HashMap<>();
    private final List<Rule> rules = new ArrayList<>();
    final EnvironmentVariablesDefManager environmentVariablesDefManager = new EnvironmentVariablesDefManagerImpl();

    public void addEnvironmentVariableDef(PropertyDefinition envVarDefinitionToAdd)    {
        this.environmentVariablesDefManager.addEnvironmentVariable(envVarDefinitionToAdd);
    }
    public void addEntityDefinition(EntityDefinition entityDefinitionToAdd){
        name2EntitiesDef.put(entityDefinitionToAdd.getName(), entityDefinitionToAdd);
    }
    public void setMaxNumberOfTicks(int maxNumberOfTicks) {
        this.maxNumberOfTicks = maxNumberOfTicks;
    }

    public void setSecondsToTerminate(long secondsToTerminate) {
        SecondsToTerminate = secondsToTerminate;
    }


    public EntityDefinition getEntityDefinitionByName(String entityName){
        return name2EntitiesDef.get(entityName);
    }

    public void addRule(Rule ruleToAdd)
    {
        rules.add(ruleToAdd);
    }

    public PropertyDefinition getEnvironmentVariableDefByName(String name)    {
        try {
            return this.environmentVariablesDefManager.getEnvironmentVariableByName(name);
        }
        catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    public Collection<EntityDefinition> getEntitiesDefinitions()
    {
        return name2EntitiesDef.values();
    }

    public List<Rule> getRules()
    {
        return rules;
    }

    public Collection<PropertyDefinition> getEnvironmentVariablesDefinitions(){
        return this.environmentVariablesDefManager.getEnvironmentVariables();
    }

    public Integer getMaxNumberOfTicks() {
        return maxNumberOfTicks;
    }

    public Long getSecondsToTerminate() {
        return SecondsToTerminate;
    }

}