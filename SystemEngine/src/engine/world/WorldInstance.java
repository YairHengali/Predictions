package engine.world;

import engine.entity.manager.api.EntityInstanceManager;
import engine.entity.manager.impl.EntityInstanceManagerImpl;
import engine.environment.active.ActiveEnvironmentVariables;
import engine.environment.active.ActiveEnvironmentVariablesImpl;
//import engine.environment.manager.EnvironmentVariablesDefManager;
import engine.property.PropertyDefinition;
import engine.property.api.PropertyInstance;
import engine.rule.Rule;

import java.io.Serializable;
import java.util.*;

public class WorldInstance implements Serializable, Runnable {
    private String dateOfRun = "";
    private int currentNumberOfTicks = 0;
    private long startTime;
    private final List<Rule> rules = new ArrayList<>();
    EntityInstanceManager entityInstanceManager;
    ActiveEnvironmentVariables activeEnvironmentVariables;
    private Integer maxNumberOfTicks = null;
    private Long secondsToTerminate = null;
    private boolean isTerminationByUser = false;



    public WorldInstance(WorldDefinition worldDef) {
        this.secondsToTerminate = worldDef.getSecondsToTerminate();
        this.maxNumberOfTicks = worldDef.getMaxNumberOfTicks();
        this.rules.addAll(worldDef.getRules());
        this.isTerminationByUser = worldDef.isTerminationByUser();

    }
    public EntityInstanceManager getEntityInstanceManager() {
        return entityInstanceManager;
    }

    public Collection<PropertyInstance> getActiveEnvironmentVariables() {
        return activeEnvironmentVariables.getEvnVariables();
    }



    public void runInitIteration(WorldDefinition simulationDef){//Tick0
        currentNumberOfTicks = 0;

        entityInstanceManager = new EntityInstanceManagerImpl(simulationDef.getEntitiesDefinitions());
        entityInstanceManager.createEntitiesInstancesAndLocate(simulationDef.getNumOfRowsInGrid(), simulationDef.getNumOfColsInGrid());




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
            //TODO: NEED TO BE DONE WITH STREAMS NOW TO MAKE IT EASIER (PAGE 23)
            for (Rule rule: rules) {
                if (rule.isActive(this.currentNumberOfTicks))
                {
                    rule.runRule(this.entityInstanceManager, this.activeEnvironmentVariables, this.currentNumberOfTicks);
                }
            }
            currentNumberOfTicks++;
            entityInstanceManager.makeMoveToAllEntities();
        }
        if(currentNumberOfTicks >= maxNumberOfTicks)
        {
            System.out.println("Simulation ended by thread: " + Thread.currentThread().getId());
            return TerminationReason.MAXTICKSREACHED;
        }
        else
        {
            System.out.println("Simulation ended by thread: " + Thread.currentThread().getId());
            return TerminationReason.SECONDSREACHED;
        }
    }

    public boolean isTermination(){
        if(this.isTerminationByUser)
            return false;

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


    @Override
    public void run() {
        runMainLoop();
    }
}
