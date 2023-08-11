package engine.system;

import engine.action.api.Action;
import engine.entity.EntityDefinition;
import engine.property.PropertyDefinition;
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
                if (propertyDefinition.getValueRange() != null) {
                    propertiesDetails.add( new PropertyDTO(propertyDefinition.getName(), propertyDefinition.getType().toString(), propertyDefinition.getValueRange().getFrom(),propertyDefinition.getValueRange().getTo(), propertyDefinition.isInitializedRandomly()));
                }
                else{
                    propertiesDetails.add(new PropertyDTO(propertyDefinition.getName(), propertyDefinition.getType().toString(), null,null, propertyDefinition.isInitializedRandomly()));
                }
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

    private static PRDWorld deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (PRDWorld) u.unmarshal(in);
    }


    @Override

    public int runSimulation() {
        String datOfRun = simpleDateFormat.format(new Date());

//        initializeEnvVars();
//        showEnvVarValues();
//        runTheSimulation();

        TerminationReason terminationReason = simulation.runMainLoop();



//return ID of simulation
        return 1;
    }


    private void initializeEnvVars() {

    }

    @Override
    public String showPastSimulationDetails(){return "";}

}
