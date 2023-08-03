package engine;

import jaxb.generated.PRDWorld;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class SystemEngine {
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "jaxb.generated";
    private World simulation;

    public World getSimulation() {
        return simulation;
    }

    public void loadSimulation(String filePath) {
        try {
            InputStream inputStream = new FileInputStream(new File(filePath));
            PRDWorld generatedWorld = deserializeFrom(inputStream);
        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        }


    }
    private static PRDWorld deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (PRDWorld) u.unmarshal(in);
    }
    public String showSimulation() {
        return simulation.toString();
    }

    public void runSimulation() {
    }

    public String showPastSimulation(){return "";}
}
