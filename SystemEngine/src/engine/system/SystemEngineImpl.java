package engine.system;

import engine.action.api.Action;
import engine.entity.EntityDefinition;
import engine.entity.EntityInstance;
import engine.property.PropertyDefinition;
import engine.property.api.PropertyInstance;
import engine.rule.Rule;
import engine.world.TerminationReason;
import engine.world.World;
import engine.world.factory.WorldFactory;
import engine.world.factory.WorldFactoryImpl;
import engineAnswers.*;
import jaxb.generated.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class SystemEngineImpl implements SystemEngine, Serializable {
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "jaxb.generated";
    private World simulation = null;
    private final WorldFactory worldFactory = new WorldFactoryImpl();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy | hh.mm.ss");
    int currentSimulationID = 0;
    boolean isThereLoadedSimulation = false;
    private Map<Integer,World> id2pastSimulation = new HashMap<>();
    PRDWorld currentlyWorkingGeneratedWorld = null;


    @Override
    public void loadSimulation(String filePath) throws Exception { //TODO: Add Exceptions fo invalid data
        if (!filePath.endsWith(".xml")){
            throw new Exception("Invalid file format! must be a .xml file");
        }
        try {
            InputStream inputStream = new FileInputStream(new File(filePath));
            PRDWorld generatedWorld = deserializeFrom(inputStream);
            worldFactory.setGeneratedWorld(generatedWorld);
            createNewSimulation();
            currentlyWorkingGeneratedWorld = generatedWorld;
            //TODO: HERE RESET ALL INFORMATION DONT NEEDED (BECAUSE NEW ONE HAS LOADED - EVEN IF OF SAME KIND(E.G 2 cigaretes in a row?) )
            //CLEAR PAST THAT NOT NEDDED
            //CLEAR PAST THAT NOT NEDDED
            isThereLoadedSimulation = true;
        } catch (Exception e) {
            if (currentlyWorkingGeneratedWorld != null)
            {
                worldFactory.setGeneratedWorld(currentlyWorkingGeneratedWorld);
                createNewSimulation();
            }
            throw new RuntimeException(e.getMessage());
        }
//        } catch (JAXBException | FileNotFoundException e) {
//            throw new RuntimeException(e.getMessage());
//        }
    }

    @Override
    public SimulationDetailsDTO showSimulationDetails() {
        SimulationDetailsDTO simulationDetails;
        List<EntityDTO> entitiesDetails = new ArrayList<>();
        List<RuleDTO> rulesDetails = new ArrayList<>();

        for (EntityDefinition entityDefinition: simulation.getEntitiesDefinitions()) {

            List<PropertyDTO> propertiesDetails = new ArrayList<>();
            for (PropertyDefinition propertyDefinition: entityDefinition.getName2propertyDef().values()) {
                addPropertyToDtoList(propertiesDetails, propertyDefinition);
            }
            entitiesDetails.add(new EntityDTO(entityDefinition.getName(), entityDefinition.getPopulation(), propertiesDetails));
        }

        for (Rule rule: simulation.getRules()) {
            List<ActionDTO> actionsDetails = new ArrayList<>();
            for (Action action: rule.getActions()){
                actionsDetails.add(new ActionDTO(action.getActionType().toString()));
            }
            rulesDetails.add(new RuleDTO(rule.getName(), rule.getTicksForActivations(), rule.getProbForActivations(), actionsDetails));
        }

        simulationDetails = new SimulationDetailsDTO(entitiesDetails, rulesDetails, simulation.getMaxNumberOfTicks(), simulation.getSecondsToTerminate());
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


    private void createNewSimulation()
    {
        simulation = worldFactory.createWorld();
        worldFactory.insertDataToWorld(this.simulation);
    }

    @Override
    public EndOfSimulationDTO runSimulation() {
        String dateOfRun = simpleDateFormat.format(new Date());
        simulation.setDateOfRun(dateOfRun);

        TerminationReason terminationReason = simulation.runMainLoop();
        currentSimulationID++;

        id2pastSimulation.put(currentSimulationID,this.simulation);
        createNewSimulation();
        return new EndOfSimulationDTO(currentSimulationID, terminationReason.toString());
    }

    @Override
    public List<ActiveEnvVarDTO> getActiveEnvVarsDto() { //TODO: getActiveEnvironmentVariables return COLLECTION, so the order might change! if relevant - we can change by get by name for each one by the original list
        List<ActiveEnvVarDTO> activeEnvVarDtos = new ArrayList<>();
        simulation.runInitIteration();

        for (PropertyInstance activeEnvVar : simulation.getActiveEnvironmentVariables()) {
            activeEnvVarDtos.add(new ActiveEnvVarDTO(activeEnvVar.getName(), activeEnvVar.getValue()));
        }
        return activeEnvVarDtos;
    }

    @Override
    public List<PropertyDTO> getEnvVarsDefinitionDto() {
        List<PropertyDTO> environmentVariablesDetails = new ArrayList<>();
        for (PropertyDefinition environmentVariableDefinition: simulation.getEnvironmentVariablesDefinitions()) {
            addPropertyToDtoList(environmentVariablesDetails, environmentVariableDefinition);
        }
        return environmentVariablesDetails;
    }

//    @Override
//    public void setEnvVarsFromDto(List<PropertyDTO> envVarsDto)
//    {
//        for (PropertyDTO envVarDto :envVarsDto) {
//            simulation.getEnvironmentVariableDefByName(envVarDto.getName()).setInitializedRandomly(envVarDto.isInitialisedRandomly());
//            simulation.getEnvironmentVariableDefByName(envVarDto.getName()).setInitValue(envVarDto.getInitValue());
//        }
//
//        //After:
//            //runInitIteration();
//            //return dto with value, so the manu could print it
//    }

    @Override
    public void setEnvVarFromDto(PropertyDTO envVarDto) {
        simulation.getEnvironmentVariableDefByName(envVarDto.getName()).setInitializedRandomly(envVarDto.isInitialisedRandomly());

        if(!(envVarDto.isInitialisedRandomly()))
        {
            simulation.getEnvironmentVariableDefByName(envVarDto.getName()).setInitValue(envVarDto.getInitValue());
        }
    }

    @Override
    public List<pastSimulationDTO> getPastSimulationsDetails(){
        List <pastSimulationDTO> pastSimulations = new ArrayList<>(); //TODO: maybe need to order by date somehow, in case changing clock
        for(Map.Entry<Integer, World> entry : id2pastSimulation.entrySet()){
            pastSimulations.add(new pastSimulationDTO(entry.getValue().getDateOfRun(), entry.getKey().intValue()));
        }

        return pastSimulations;
    }

    @Override
    public Boolean isThereLoadedSimulation() {
        return isThereLoadedSimulation; //TODO: maybe more complex tesing
    }

    @Override
    public HistogramDTO getHistogram(int simulationID, String entityName, String propertyName){

        if(!this.id2pastSimulation.containsKey(simulationID))
            throw new RuntimeException("Trying to view simulation with ID:" + simulationID + ", this ID does not exist in the system!");

        World desiredSimulation = this.id2pastSimulation.get(simulationID);
        List<EntityInstance> pastSimulationEntities = desiredSimulation.getEntityInstanceManager().getInstancesListByName(entityName);
        Map<String, Long> histogram = pastSimulationEntities
                .stream()
                .map(entityInstance -> entityInstance.getPropertyByName(propertyName))
                .collect(Collectors.groupingBy(property -> property.getValue(),Collectors.counting()));

        return new HistogramDTO(simulationID, entityName, propertyName, histogram);

    }

    @Override
    public List<EntityCountDTO> getPastSimulationEntityCount(pastSimulationDTO desiredPastSimulation) {

        List<EntityCountDTO> entityCountDtos = new ArrayList<>();
        World pastSimulation = this.id2pastSimulation.get(desiredPastSimulation.getId());
        for (EntityDefinition entityDefinition :pastSimulation.getEntitiesDefinitions()) {
            int startCount = entityDefinition.getPopulation();
            int endCount = pastSimulation.getEntityInstanceManager().getInstancesListByName(entityDefinition.getName()).size();
            entityCountDtos.add(new EntityCountDTO(entityDefinition.getName(),startCount, endCount));
        }
        return entityCountDtos;
    }

    @Override
    public List<EntityDTO> getPastSimulationEntitiesDTO(pastSimulationDTO desiredPastSimulation) {
        List<EntityDTO> entitiesDetails = new ArrayList<>();

        for (EntityDefinition entityDefinition: simulation.getEntitiesDefinitions()) {
            List<PropertyDTO> propertiesDetails = new ArrayList<>();
            for (PropertyDefinition propertyDefinition: entityDefinition.getName2propertyDef().values()) {
                addPropertyToDtoList(propertiesDetails, propertyDefinition);
            }
            entitiesDetails.add(new EntityDTO(entityDefinition.getName(), entityDefinition.getPopulation(), propertiesDetails));
        }

        return entitiesDetails;
    }
}
