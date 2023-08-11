package engine.system;

import engine.world.World;
import engine.world.factory.WorldFactory;
import engine.world.factory.WorldFactoryImpl;
import jaxb.generated.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class SystemEngineImpl implements SystemEngine{
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "jaxb.generated";

    private World simulation = null;
    private WorldFactory worldFactory = new WorldFactoryImpl();

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
    public String showSimulationDetails() {
        return null;
    }

    private static PRDWorld deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (PRDWorld) u.unmarshal(in);
    }


    @Override
    public int runSimulation() {
//        initializeEnvVars();
//        showEnvVarValues();
//        runTheSimulation();

        simulation.runMainLoop();

        return 1;
    }
    @Override
    public String showPastSimulation(){return "";}
}
