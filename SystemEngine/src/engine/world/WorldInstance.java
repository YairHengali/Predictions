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

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

//    public TerminationReason runMainLoop(){ //TICK 1 and up...;
//        this.startTime = System.currentTimeMillis();
//        currentNumberOfTicks = 1;
//
//        while (!isTermination())
//        {
//            //TODO: NEED TO BE DONE WITH STREAMS NOW TO MAKE IT EASIER (PAGE 23)
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
        this.startTime = System.currentTimeMillis();
        currentNumberOfTicks = 1;


        while (!isTermination())
        {
            entityInstanceManager.makeMoveToAllEntities();
            //TODO: NEED TO BE DONE WITH STREAMS NOW TO MAKE IT EASIER (PAGE 23)

            List<Action> actionList = rules.stream()
                                            .filter(rule -> rule.isActive(this.currentNumberOfTicks))
                                            .flatMap(rule -> rule.getActions().stream())
                                            .collect(Collectors.toList());


            Stream<EntityInstance> allEntitiesInstances = entityInstanceManager.getAllEntitiesInstances(); //TODO: NOW WHEN HAVE THIS TEMPLATE, THING ABOUT SOURCE TARGET
            //TODO: MAYBE ASK AVIAD, DOES TARGET IS: SECONDERY WITH: "ALL" AND WITHOUT CONDITION, OR THAT IT ALSO MAY HAVE E SECONDARY ENTITY

            allEntitiesInstances.forEach(entityInstance -> {
                String entityName = entityInstance.getName();
                actionList.stream()
                        .filter(action -> action.getMainEntityName().equals(entityName))
                        .forEach(action -> {
                            SecondaryEntityDetails secondaryEntityDetails = action.getSecondaryEntityDetails();
                            if (secondaryEntityDetails == null) { //no secondary entity
                                action.Run(new ContextImpl(entityInstance, null, this.entityInstanceManager, this.activeEnvironmentVariables, this.currentNumberOfTicks));
                            }
                            else {
                                List<EntityInstance> secondaryEntities = entityInstanceManager.getInstancesListByName(secondaryEntityDetails.getName());
//                                        .stream();

                                if (secondaryEntityDetails.getMaxCount() == null) { // count = "all"
                                    //use all secondary list
                                    secondaryEntities.forEach(secondaryEntityInstance -> action.Run(new ContextImpl(entityInstance, secondaryEntityInstance, this.entityInstanceManager, this.activeEnvironmentVariables, this.currentNumberOfTicks )));

                                }else {
                                    if(secondaryEntityDetails.getCondition() == null){ // no condition
                                        Random random = new Random();
                                        int maxCount = secondaryEntityDetails.getMaxCount();
                                        int secEntSize = secondaryEntities.size();

                                        for (int i = 0; i < maxCount && i < secEntSize; i++) {
                                            EntityInstance randChosenSecEnt = secondaryEntities.get(random.nextInt(secEntSize));
                                            action.Run(new ContextImpl(entityInstance, randChosenSecEnt, this.entityInstanceManager, this.activeEnvironmentVariables, this.currentNumberOfTicks ));
                                        }

                                        //use random up to MaxCount secondary stream
//                                        List<EntityInstance> shuffledList = new ArrayList<>(secondaryEntities);
//                                        Collections.shuffle(secondaryEntities); //MAKE SURE THE SHUFFLE DONT MAKE TRUBLE TO ORIGINAL


                                    } else{// there is condition //TODO: is there option for all and condition?
                                        //use random up to MaxCount ON secondary stream that filtered by their condition
                                        List<EntityInstance> secondaryEntitiesAfterCondition = secondaryEntities
                                                .stream()
                                                .filter(secondaryEntity -> secondaryEntityDetails.getCondition().evaluateCondition(new ContextImpl(secondaryEntity, null, this.entityInstanceManager, this.activeEnvironmentVariables, this.currentNumberOfTicks))) //SENT THE CURRENT ENTITY(SECONDARY) AS MAIN, AND NULL AS SECONDARY
                                                .collect(Collectors.toList());

                                        Random random = new Random();
                                        int maxCount = secondaryEntityDetails.getMaxCount();
                                        int secEntSize = secondaryEntitiesAfterCondition.size(); //NEED TO CHOOSE MINIMUM FROM THEM OF FROM ALL?

                                        for (int i = 0; i < maxCount && i < secEntSize; i++) {
                                            EntityInstance randChosenSecEnt = secondaryEntitiesAfterCondition.get(random.nextInt(secEntSize));
                                            action.Run(new ContextImpl(entityInstance, randChosenSecEnt, this.entityInstanceManager, this.activeEnvironmentVariables, this.currentNumberOfTicks ));
                                        }
                                    }
                                }

                            }
                        });
                });


            this.entityInstanceManager.killEntities();
            this.entityInstanceManager.createScratchEntities();
            this.entityInstanceManager.createDerivedEntities();
            currentNumberOfTicks++;

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
//        runMainLoop();
        runMainLoopEx2();
    }

}
