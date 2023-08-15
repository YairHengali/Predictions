package engine.world;

import engine.action.api.Action;
import engine.entity.EntityDefinition;
import engine.entity.manager.EntityInstanceManager;
import engine.entity.manager.EntityInstanceManagerImpl;
import engine.environment.active.ActiveEnvironmentVariables;
import engine.environment.active.ActiveEnvironmentVariablesImpl;
import engine.environment.manager.EnvironmentVariablesManager;
//import engine.environment.manager.EnvironmentVariablesManager;
import engine.environment.manager.EnvironmentVariablesManagerImpl;
import engine.property.PropertyDefinition;
import engine.property.api.PropertyInstance;
import engine.rule.Rule;
import engine.rule.RuleImpl;

import java.io.Serializable;
import java.util.*;

public class WorldInstance implements Serializable {
    private String dateOfRun = "";
    private int currentNumberOfTicks = 0;
    private long startTime;
    private final List<Rule> rules = new ArrayList<>();
    EntityInstanceManager entityInstanceManager;
    ActiveEnvironmentVariables activeEnvironmentVariables;
    private Integer maxNumberOfTicks = null;
    private Long secondsToTerminate = null;


    public EntityInstanceManager getEntityInstanceManager() {
        return entityInstanceManager;
    }

    public Collection<PropertyInstance> getActiveEnvironmentVariables() {
        return activeEnvironmentVariables.getEvnVariables();
    }

    ///////// Termination conditions:

    public WorldInstance(WorldDefinition worldDef) {
        this.secondsToTerminate = worldDef.getSecondsToTerminate();
        this.maxNumberOfTicks = worldDef.getMaxNumberOfTicks();
        this.rules.addAll(worldDef.getRules());
    }


    public void runInitIteration(WorldDefinition simulationDef){//Tick0
        currentNumberOfTicks = 0;

        entityInstanceManager = new EntityInstanceManagerImpl();

        for(EntityDefinition entityDefinition : simulationDef.getEntitiesDefinitions())
        {
            entityInstanceManager.createEntityInstances(entityDefinition);
        }

        activeEnvironmentVariables = new ActiveEnvironmentVariablesImpl();
        for(PropertyDefinition envVarDef : simulationDef.getEnvironmentVariablesDefinitions())
        {
            activeEnvironmentVariables.createEvnVariableFromDef(envVarDef);
        }
    }

    public TerminationReason runMainLoop(){ //TICK 1 and up...;
        this.startTime = System.currentTimeMillis();
        currentNumberOfTicks = 1;

        while (!isTermination())
        {
            for (Rule rule: rules) {
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

    public boolean isTermination(){
        if (this.secondsToTerminate != null && this.maxNumberOfTicks != null)
        {
            return ((System.currentTimeMillis()-this.startTime)/1000 >= this.secondsToTerminate) ||
                    (this.currentNumberOfTicks >= this.maxNumberOfTicks);
        }
        else if (this.secondsToTerminate != null)
        {
            return ((System.currentTimeMillis()-this.startTime)/1000 >= this.secondsToTerminate);
        }
        else // this.maxNumberOfTicks != null
        {
            return (this.currentNumberOfTicks >= this.maxNumberOfTicks);
        }

    }


    public String getDateOfRun() {
        return dateOfRun;
    }

    public void setDateOfRun(String dateOfRun) {
        this.dateOfRun = dateOfRun;
    }


}
