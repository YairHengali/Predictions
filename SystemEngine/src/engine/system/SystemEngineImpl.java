package engine.system;

import engine.action.api.Action;
import engine.entity.EntityDefinition;
import engine.entity.EntityInstance;
import engine.property.PropertyDefinition;
import engine.property.api.PropertyInstance;
import engine.rule.Rule;
import engine.world.TerminationReason;
import engine.world.WorldDefinition;
import engine.world.WorldInstance;
import engine.world.factory.WorldDefFactory;
import engine.world.factory.WorldDefFactoryImpl;
import engineAnswers.*;
import jaxb.generated2.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SystemEngineImpl implements SystemEngine, Serializable {
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "jaxb.generated2";
    private WorldInstance simulation = null;
    private WorldDefinition simulationDef = null;
    private final WorldDefFactory worldDefFactory = new WorldDefFactoryImpl();
    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy | HH.mm.ss");
    int currentSimulationID = 0;
    private final Map<Integer, WorldInstance> id2pastSimulation = new HashMap<>();
    private boolean isThereLoadedSimulation = false;

    private int numOfThreads = 1;
    ExecutorService threadExecutor;



    @Override
    public void loadSimulation(String filePath) throws Exception {
        final WorldDefinition currentlyWorkingWorldDef = this.simulationDef;

        if (!filePath.endsWith(".xml")){
            throw new Exception("Invalid file format! must be a .xml file");
        }
        try {
            InputStream inputStream = new FileInputStream(new File(filePath));
            PRDWorld generatedWorld = deserializeFrom(inputStream);

            simulationDef = new WorldDefinition();
            worldDefFactory.insertDataToWorldDefinition(this.simulationDef, generatedWorld);
            this.numOfThreads = generatedWorld.getPRDThreadCount();
            threadExecutor = Executors.newFixedThreadPool(this.numOfThreads);


            isThereLoadedSimulation = true;
        } catch (Exception e) {
            if (currentlyWorkingWorldDef != null)
            {
                this.simulationDef = currentlyWorkingWorldDef;
            }
            throw new RuntimeException(e.getMessage());
        }
    }
    @Override
    public SimulationDetailsDTO getSimulationDetails() {
        SimulationDetailsDTO simulationDetails;
        List<EntityDTO> entitiesDetails = new ArrayList<>();
        List<RuleDTO> rulesDetails = new ArrayList<>();

        for (EntityDefinition entityDefinition: simulationDef.getEntitiesDefinitions()) {
            List<PropertyDTO> propertiesDetails = new ArrayList<>();
            for (PropertyDefinition propertyDefinition: entityDefinition.getName2propertyDef().values()) {
                addPropertyToDtoList(propertiesDetails, propertyDefinition);
            }
            entitiesDetails.add(new EntityDTO(entityDefinition.getName(), entityDefinition.getPopulation(), propertiesDetails));
        }

        List<PropertyDTO> envVarsDetails = getEnvVarsDefinitionDto();

        for (Rule rule: simulationDef.getRules()) {
            List<ActionDTO> actionsDetails = new ArrayList<>();
            for (Action action: rule.getActions()){
                actionsDetails.add(new ActionDTO(action.getActionType().toString()));
            }
            rulesDetails.add(new RuleDTO(rule.getName(), rule.getTicksForActivations(), rule.getProbForActivations(), actionsDetails));
        }

        simulationDetails = new SimulationDetailsDTO(entitiesDetails, envVarsDetails, rulesDetails, simulationDef.getMaxNumberOfTicks(), simulationDef.getSecondsToTerminate(), simulationDef.isTerminationByUser(), simulationDef.getNumOfRowsInGrid(), simulationDef.getNumOfColsInGrid());
        return simulationDetails;
    }
    private void addPropertyToDtoList(List<PropertyDTO> propertiesDtoList, PropertyDefinition propertyDefinition) {
        if (propertyDefinition.getValueRange() != null) {
            propertiesDtoList.add( new PropertyDTO(propertyDefinition.getName(), propertyDefinition.getType().toString(), propertyDefinition.getValueRange().getFrom(),propertyDefinition.getValueRange().getTo(), propertyDefinition.isInitializedRandomly(), propertyDefinition.getInitValue()));
        }
        else{
            propertiesDtoList.add(new PropertyDTO(propertyDefinition.getName(), propertyDefinition.getType().toString(), null,null, propertyDefinition.isInitializedRandomly(), propertyDefinition.getInitValue()));
        }
    }
    private static PRDWorld deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (PRDWorld) u.unmarshal(in);
    }

    private WorldInstance createNewSimulation()
    {
        WorldInstance resSimulation = new WorldInstance(this.simulationDef);
        resSimulation.runInitIteration(this.simulationDef);
        String dateOfRun = simpleDateFormat.format(new Date());
        resSimulation.setDateOfRun(dateOfRun);

        return resSimulation;
    }
    @Override
    public void updateEntityDefPopulation(EntityDTO newEntityDTO) {
        this.simulationDef.getEntityDefinitionByName(newEntityDTO.getName()).setPopulation(newEntityDTO.getPopulation());
    }
    @Override
    public void setAllPopulationToZero() {
        simulationDef.getEntitiesDefinitions().forEach(entity -> entity.setPopulation(0));
    }
    @Override
    public Collection<EntityDTO> getEntitiesListDTO() {
        List<EntityDTO> entitiesDetails = new ArrayList<>();

        for (EntityDefinition entityDefinition: simulationDef.getEntitiesDefinitions()) {
            List<PropertyDTO> propertiesDetails = new ArrayList<>();
            for (PropertyDefinition propertyDefinition: entityDefinition.getName2propertyDef().values()) {
                addPropertyToDtoList(propertiesDetails, propertyDefinition);
            }
            entitiesDetails.add(new EntityDTO(entityDefinition.getName(), entityDefinition.getPopulation(), propertiesDetails));
        }

        return entitiesDetails;
    }

    @Override
    public void clearPastSimulations()
    {
        id2pastSimulation.clear();
        currentSimulationID = 0;
    }


    @Override
    public EndOfSimulationDTO runSimulation() {
        WorldInstance currSimulation = this.createNewSimulation();
        id2pastSimulation.put(currentSimulationID++, currSimulation);
        threadExecutor.execute(currSimulation);

//        TerminationReason terminationReason = simulation.runMainLoop();
//        currentSimulationID++;

//        return new EndOfSimulationDTO(currentSimulationID, terminationReason.toString());
        return null;
    }

    @Override
    public List<ActiveEnvVarDTO> getActiveEnvVarsDto() {
        List<ActiveEnvVarDTO> activeEnvVarDtos = new ArrayList<>();

        for (PropertyInstance activeEnvVar : simulation.getActiveEnvironmentVariables()) {
            activeEnvVarDtos.add(new ActiveEnvVarDTO(activeEnvVar.getName(), activeEnvVar.getValue()));
        }
        return activeEnvVarDtos;
    }

    @Override
    public List<PropertyDTO> getEnvVarsDefinitionDto() {
        List<PropertyDTO> environmentVariablesDetails = new ArrayList<>();
        for (PropertyDefinition environmentVariableDefinition: simulationDef.getEnvironmentVariablesDefinitions()) {
            addPropertyToDtoList(environmentVariablesDetails, environmentVariableDefinition);
        }
        return environmentVariablesDetails;
    }

    @Override
    public void setEnvVarDefFromDto(PropertyDTO envVarDto) {
        simulationDef.getEnvironmentVariableDefByName(envVarDto.getName()).setInitializedRandomly(envVarDto.isInitialisedRandomly());

        if(!(envVarDto.isInitialisedRandomly()))
        {
            simulationDef.getEnvironmentVariableDefByName(envVarDto.getName()).setInitValue(envVarDto.getInitValue());
        }
    }

    @Override
    public List<pastSimulationDTO> getPastSimulationsDetails(){
        List <pastSimulationDTO> pastSimulations = new ArrayList<>();
        for(Map.Entry<Integer, WorldInstance> entry : id2pastSimulation.entrySet()){
            pastSimulations.add(new pastSimulationDTO(entry.getValue().getDateOfRun(), entry.getKey().intValue()));
        }

        pastSimulations.sort(Comparator.comparing(pastSimulationDTO::getDateOfRun));
        return pastSimulations;
    }

    @Override
    public Boolean isThereLoadedSimulation() {
        return this.isThereLoadedSimulation;
    }

    @Override
    public HistogramDTO getHistogram(int simulationID, String entityName, String propertyName){

        if(!this.id2pastSimulation.containsKey(simulationID))
            throw new RuntimeException("Trying to view simulation with ID:" + simulationID + ", this ID does not exist in the system!");
        long valueCounter = 0;
        WorldInstance desiredSimulation = this.id2pastSimulation.get(simulationID);
        List<EntityInstance> pastSimulationEntities = desiredSimulation.getEntityInstanceManager().getInstancesListByName(entityName);
        Map<String, Long> histogram = new TreeMap<>();

        for(EntityInstance entity : pastSimulationEntities) {
            valueCounter = 0L;
            if (histogram.containsKey(entity.getPropertyByName(propertyName).getValue())) {
                valueCounter = histogram.get(entity.getPropertyByName(propertyName).getValue());
            }
            histogram.put(entity.getPropertyByName(propertyName).getValue(), valueCounter + 1   );
        }
        return new HistogramDTO(simulationID, entityName, propertyName, histogram);
    }

    @Override
    public List<EntityCountDTO> getPastSimulationEntityCount(pastSimulationDTO desiredPastSimulation) {

        List<EntityCountDTO> entityCountDtos = new ArrayList<>();
        WorldInstance pastSimulation = this.id2pastSimulation.get(desiredPastSimulation.getId());

        for (EntityDefinition entityDefinition : simulationDef.getEntitiesDefinitions()) {

            int startCount = pastSimulation.getEntityInstanceManager().getEntityDefByName(entityDefinition.getName()).getPopulation(); //TODO: CHANGE! DEF CHANGES EVERY TIME - maybe each instance will hold its def?
            int endCount = pastSimulation.getEntityInstanceManager().getInstancesListByName(entityDefinition.getName()).size();
            entityCountDtos.add(new EntityCountDTO(entityDefinition.getName(),startCount, endCount));

        }
        return entityCountDtos;
    }

    @Override
    public List<EntityDTO> getPastSimulationEntitiesDTO(pastSimulationDTO desiredPastSimulation) {
        List<EntityDTO> entitiesDetails = new ArrayList<>();

        for (EntityDefinition entityDefinition: simulationDef.getEntitiesDefinitions()) {
            List<PropertyDTO> propertiesDetails = new ArrayList<>();
            for (PropertyDefinition propertyDefinition: entityDefinition.getName2propertyDef().values()) {
                addPropertyToDtoList(propertiesDetails, propertyDefinition);
            }
            entitiesDetails.add(new EntityDTO(entityDefinition.getName(), entityDefinition.getPopulation(), propertiesDetails));
        }

        return entitiesDetails;
    }
//
//    public void pullData(int simulationID){
//        WorldInstance wantedSimulation = id2pastSimulation.get(simulationID);
//        wantedSimulation.getCurrentTick();
//        wantedSimulation.getCurrentTime();
//        wantedSimulation.getEntitiesCount();
//    }
//
//    public void pauseSimulation(int simulationID){
//        WorldInstance wantedSimulation = id2pastSimulation.get(simulationID);
//        wantedSimulation.setState(PAUSED);
//    }
//
//    public void resumeSimulation(int simulationID){
//        WorldInstance wantedSimulation = id2pastSimulation.get(simulationID);
//        wantedSimulation.setState(RUNNING);
//    }
//
//    public void stopSimulation(int simulationID){
//        WorldInstance wantedSimulation = id2pastSimulation.get(simulationID);
//        wantedSimulation.setState(ENDED);
//    }

}
