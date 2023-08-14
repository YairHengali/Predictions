package engine.system;

import engine.action.api.Action;
import engine.entity.EntityDefinition;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SystemEngineImpl implements SystemEngine{
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "jaxb.generated";
    private World simulation = null;
    private final WorldFactory worldFactory = new WorldFactoryImpl();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy | hh.mm.ss");
    int currentSimulationID = 0;
    boolean isThereLoadedSimulation = false;
    private List <pastSimulationDTO> pastSimulations = new ArrayList<>(); //TODO: maybe need to order by date somehow, in case changing clock


    public SystemEngineImpl(){
        this.simulation = worldFactory.createWorld();
    }

    @Override
    public World getSimulation() {
        return simulation;
    }

    @Override
    public void loadSimulation(String filePath) throws Exception { //TODO: Add Exceptions fo invalid data
        if (!filePath.endsWith(".xml")){
            throw new Exception("Invalid file format! needs to be a .xml file");
        }
        try {
            InputStream inputStream = new FileInputStream(new File(filePath));
            worldFactory.setGeneratedWorld(deserializeFrom(inputStream));
        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            worldFactory.insertDataToWorld(this.simulation);
            isThereLoadedSimulation = true;
        }
        catch (Exception e){
            e.getMessage();
        }
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


    @Override

    public EndOfSimulationDTO runSimulation() {
        String dateOfRun = simpleDateFormat.format(new Date());
        TerminationReason terminationReason = simulation.runMainLoop();



//return ID of simulation and add it to Past Simulations with its date
        currentSimulationID++;

        //TODO: CRREATE PAST SIMULATIONS NOT AS DTO
        List<EntityCountDTO> entityCountDtos = new ArrayList<>();
        for (EntityDefinition entityDefinition :simulation.getEntitiesDefinitions()) {
            int startCount = entityDefinition.getPopulation();
            int endCount = simulation.getEntityInstanceManager().getInstancesListByName(entityDefinition.getName()).size();
            entityCountDtos.add(new EntityCountDTO(entityDefinition.getName(),startCount, endCount));
        }

        pastSimulations.add(new pastSimulationDTO(dateOfRun, currentSimulationID, entityCountDtos)); //TODO: add properties histogram to DTO
        //TODO:^^

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
    public List<pastSimulationDTO> getPastSimulationsDetails(){return pastSimulations;} //TODO - create the DTO Here

    @Override
    public Boolean isThereLoadedSimulation() {
        return isThereLoadedSimulation; //TODO: maybe more complex tesing
    }
}
