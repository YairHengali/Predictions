package engine.world;

import engine.action.api.Action;
import engine.context.ContextImpl;
import engine.entity.EntityInstance;
import engine.entity.manager.api.EntityInstanceManager;
import engine.entity.manager.impl.EntityInstanceManagerImpl;
import engine.environment.active.ActiveEnvironmentVariables;
import engine.environment.active.ActiveEnvironmentVariablesImpl;
//import engine.environment.manager.EnvironmentVariablesDefManager;
import engine.property.PropertyDefinition;
import engine.property.api.PropertyInstance;
import engine.rule.Rule;
import engine.world.factory.SecondaryEntityDetails;
import javafx.util.Pair;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WorldInstance implements Serializable, Runnable {
    private String dateOfRun = "";
    private int currentNumberOfTicks = 0;
    private Instant startTime;
    private long runningTime = 0;
    private final List<Rule> rules = new ArrayList<>();
    EntityInstanceManager entityInstanceManager;
    ActiveEnvironmentVariables activeEnvironmentVariables;
    private Integer maxNumberOfTicks = null;
    private Long secondsToTerminate = null;
    private boolean isTerminationByUser = false;
    private Instant endTime;
    private SimulationStatus status;
    private final Object statusLock = new Object();

    private int simulationID;



    public WorldInstance(WorldDefinition worldDef, int id) {
        this.secondsToTerminate = worldDef.getSecondsToTerminate();
        this.maxNumberOfTicks = worldDef.getMaxNumberOfTicks();
        this.rules.addAll(worldDef.getRules());
        this.isTerminationByUser = worldDef.isTerminationByUser();
        this.simulationID = id;
        this.status = SimulationStatus.CREATED;
    }
    public String getStatusString(){
        return this.status.toString();
    }

    public boolean isTerminateByUser(){
        return isTerminationByUser;
    }
    public EntityInstanceManager getEntityInstanceManager() {
        return entityInstanceManager;
    }

    public Collection<PropertyInstance> getActiveEnvironmentVariables() {
        return activeEnvironmentVariables.getEvnVariables();
    }

    public Instant getStartTime() {
        return startTime;
    }

    public int getCurrentNumberOfTicks() {
        return currentNumberOfTicks;
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

    public void pauseSimulation(){
        synchronized (statusLock) {
            if (this.status == SimulationStatus.RUNNING) {
                this.status = SimulationStatus.PAUSED;
                runningTime += Duration.between(startTime , Instant.now()).getSeconds();
            }
        }

    }

    public void resumeSimulation(){
        synchronized (statusLock) {
            if (this.status == SimulationStatus.PAUSED) {
                this.status = SimulationStatus.RUNNING;
                startTime = Instant.now();
            }
        }
    }

//    public TerminationReason runMainLoop(){ //TICK 1 and up...;
//        this.startTime = System.currentTimeMillis();
//        currentNumberOfTicks = 1;
//
//        while (!isTermination())
//        {
//            for (Rule rule: rules) {
//                if (rule.isActive(this.currentNumberOfTicks))
//                {
//                    rule.runRule(this.entityInstanceManager, this.activeEnvironmentVariables, this.currentNumberOfTicks);
//                }
//            }
//            currentNumberOfTicks++;
//            entityInstanceManager.makeMoveToAllEntities();
//        }
//        if(currentNumberOfTicks >= maxNumberOfTicks)
//        {
//            System.out.println("Simulation ended by thread: " + Thread.currentThread().getId());
//            return TerminationReason.MAXTICKSREACHED;
//        }
//        else
//        {
//            System.out.println("Simulation ended by thread: " + Thread.currentThread().getId());
//            return TerminationReason.SECONDSREACHED;
//        }
//    }

    public TerminationReason runMainLoopEx2(){ //TICK 1 and up...;
//        this.startTime = System.currentTimeMillis(); replaced >
        boolean isTerminated = false;
        boolean isPaused = false;


        synchronized (this){
            this.status = SimulationStatus.RUNNING;
        }
        this.startTime = Instant.now();
        currentNumberOfTicks = 1;


        while (!isTerminated)
        {
            isPaused = checkIfPaused();
            isTerminated = isTermination();
            if(!isPaused && !isTerminated){
//            System.out.println("Thread: " + Thread.currentThread().getId() + ": I am running in tick number: " + this.currentNumberOfTicks + " | Sick count: " + entityInstanceManager.getInstancesListByName("Sick").size() + " | Healthy count: " + entityInstanceManager.getInstancesListByName("Healthy").size());

                entityInstanceManager.makeMoveToAllEntities();

                List<Action> actionList = rules.stream()
                        .filter(rule -> rule.isActive(this.currentNumberOfTicks))
                        .flatMap(rule -> rule.getActions().stream())
                        .collect(Collectors.toList());


                Stream<EntityInstance> allEntitiesInstances = entityInstanceManager.getAllEntitiesInstances();

                allEntitiesInstances.forEach(entityInstance -> {
                    String entityName = entityInstance.getName();
                    actionList.stream()
                            .filter(action -> action.getMainEntityName().equals(entityName))
                            .forEach(action -> {
                                try {
                                    SecondaryEntityDetails secondaryEntityDetails = action.getSecondaryEntityDetails();
                                    if (secondaryEntityDetails == null) { //no secondary entity
                                        action.Run(new ContextImpl(entityInstance, null, this.entityInstanceManager, this.activeEnvironmentVariables, this.currentNumberOfTicks));
                                    } else {
                                        List<EntityInstance> secondaryEntities = entityInstanceManager.getInstancesListByName(secondaryEntityDetails.getName());
//                                        .stream();

                                        if (secondaryEntityDetails.getMaxCount() == null) { // count = "all"
                                            if (secondaryEntityDetails.getCondition() == null) { // no condition
                                                //use all secondary list
                                                secondaryEntities.forEach(secondaryEntityInstance -> action.Run(new ContextImpl(entityInstance, secondaryEntityInstance, this.entityInstanceManager, this.activeEnvironmentVariables, this.currentNumberOfTicks)));
                                            } else { // there is condition
                                                //use all secondary list after filtered by condition
                                                secondaryEntities
                                                        .stream()
                                                        .filter(secondaryEntityInstance -> secondaryEntityDetails.getCondition().evaluateCondition(new ContextImpl(secondaryEntityInstance, null, this.entityInstanceManager, this.activeEnvironmentVariables, this.currentNumberOfTicks))) //SENT THE CURRENT ENTITY(SECONDARY) AS MAIN, AND NULL AS SECONDARY
                                                        .forEach(secondaryEntityInstance -> action.Run(new ContextImpl(entityInstance, secondaryEntityInstance, this.entityInstanceManager, this.activeEnvironmentVariables, this.currentNumberOfTicks)));
                                            }
                                        } else { // count is a number
                                            if (secondaryEntityDetails.getCondition() == null) { // no condition
                                                //use random up to MaxCount secondary entities
                                                Random random = new Random();
                                                int maxCount = secondaryEntityDetails.getMaxCount();
                                                int secEntSize = secondaryEntities.size();

                                                for (int i = 0; i < maxCount && i < secEntSize; i++) {
                                                    EntityInstance randChosenSecEnt = secondaryEntities.get(random.nextInt(secEntSize));
                                                    action.Run(new ContextImpl(entityInstance, randChosenSecEnt, this.entityInstanceManager, this.activeEnvironmentVariables, this.currentNumberOfTicks));
                                                }

                                            } else {// there is condition
                                                //use random up to MaxCount secondary entities filtered by condition
                                                List<EntityInstance> secondaryEntitiesAfterCondition = secondaryEntities
                                                        .stream()
                                                        .filter(secondaryEntity -> secondaryEntityDetails.getCondition().evaluateCondition(new ContextImpl(secondaryEntity, null, this.entityInstanceManager, this.activeEnvironmentVariables, this.currentNumberOfTicks))) //SENT THE CURRENT ENTITY(SECONDARY) AS MAIN, AND NULL AS SECONDARY
                                                        .collect(Collectors.toList());

                                                Random random = new Random();
                                                int maxCount = secondaryEntityDetails.getMaxCount();
                                                int secEntSize = secondaryEntitiesAfterCondition.size(); //NEED TO CHOOSE MINIMUM FROM THEM OF FROM ALL?

                                                for (int i = 0; i < maxCount && i < secEntSize; i++) {
                                                    EntityInstance randChosenSecEnt = secondaryEntitiesAfterCondition.get(random.nextInt(secEntSize));
                                                    action.Run(new ContextImpl(entityInstance, randChosenSecEnt, this.entityInstanceManager, this.activeEnvironmentVariables, this.currentNumberOfTicks));
                                                }
                                            }
                                        }

                                    }
                                } catch (Exception e) {
                                    throw new RuntimeException(e.getMessage() + "\n" + "Error occurred with main entity: " +
                                            entityName); //TODO: might not needed because cant get the rule name
                                }
                            });
                });


                this.entityInstanceManager.killEntities();
                this.entityInstanceManager.createScratchEntities();
                this.entityInstanceManager.createDerivedEntities();
                currentNumberOfTicks++;

            } // if not paused
        }

        synchronized (this){
            if(this.status == SimulationStatus.RUNNING)
                this.runningTime += Duration.between(startTime , Instant.now()).getSeconds();
            this.status = SimulationStatus.TERMINATED;
        }


        if(this.maxNumberOfTicks != null && currentNumberOfTicks >= this.maxNumberOfTicks)
        {
            System.out.println("Simulation ended by thread: " + Thread.currentThread().getId());
            return TerminationReason.MAXTICKSREACHED;
        }
        else if(this.secondsToTerminate != null && Duration.between(startTime, Instant.now()).getSeconds() >= secondsToTerminate)
        {
            System.out.println("Simulation ended by thread: " + Thread.currentThread().getId());
            this.endTime = Instant.now(); //TESTT.. WILL BE BY SIMULATION STATE
            return TerminationReason.SECONDSREACHED;
        }
        else
        {
            System.out.println("Simulation ended by thread: " + Thread.currentThread().getId());
            return TerminationReason.ENDEDBYUSER;
        }
    }

    private boolean checkIfPaused(){
        synchronized (statusLock) {
            return this.status == SimulationStatus.PAUSED;
        }
    }
    private boolean isTermination(){
        long timeSimulationRunning = Duration.between(startTime, Instant.now()).getSeconds();

        synchronized (this) {
            if (this.status == SimulationStatus.TERMINATED)
                return true;
        }

        if(this.isTerminationByUser)
            return false;

        if (this.secondsToTerminate != null && this.maxNumberOfTicks != null)
        {
//            return ((System.currentTimeMillis()-this.startTime)/1000 >= this.secondsToTerminate) ||
//                    (this.currentNumberOfTicks >= this.maxNumberOfTicks);
            return (timeSimulationRunning >= this.secondsToTerminate) ||
                    (this.currentNumberOfTicks >= this.maxNumberOfTicks);

        }
        else if (this.secondsToTerminate != null)
        {
//            return ((System.currentTimeMillis()-this.startTime)/1000 >= this.secondsToTerminate);
            return (timeSimulationRunning >= this.secondsToTerminate);
        }
        else // this.maxNumberOfTicks != null
        {
            return (this.currentNumberOfTicks >= this.maxNumberOfTicks);
        }

    }

    public long getRunningTime() { //TESTT WILL BE WITH SIMULATION STATE
//        if (startTime == null) //PROBABLY NEED TO SYNCHRONIZE
//            return 0;
//        else if(endTime == null) //PROBABLY NEED TO SYNCHRONIZE
//            return Duration.between(startTime , Instant.now()).getSeconds();
//        else //endTime != null
//            return Duration.between(startTime , endTime).getSeconds();


        // running time is being updated whenever simulation is paused in pauseSimulation(),
        // startTime is being updated whenever simulation is resumed in resumeSimulation(),
        // if currently running - summing old value of runningTime and new duration.
        synchronized (statusLock) {
            if (this.status == SimulationStatus.RUNNING)
                return runningTime + Duration.between(startTime, Instant.now()).getSeconds();
            else
                return runningTime;
        }
    }

    public String getDateOfRun() {
        return dateOfRun;
    }

    public void setDateOfRun(String dateOfRun) {
        this.dateOfRun = dateOfRun;
    }

    public void terminateByUser(){
        synchronized (statusLock) {
            if(this.status != SimulationStatus.TERMINATED) {
                if(this.status == SimulationStatus.RUNNING){
                    this.runningTime += Duration.between(startTime, Instant.now()).getSeconds();
                }
                this.status = SimulationStatus.TERMINATED;
            }
        }
    }

    public Integer getMaxNumberOfTicks() {
        return maxNumberOfTicks;
    }

    public Long getSecondsToTerminate() {
        return secondsToTerminate;
    }

    @Override
    public void run() {
//        runMainLoop();
        runMainLoopEx2();
    }

}
