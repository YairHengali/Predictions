package engine;

import engine.entity.EntityDefinition;
import engine.property.PropertyDefinition;
import engine.property.PropertyType;
import jaxb.generated.PRDEntity;
import jaxb.generated.PRDProperty;
import jaxb.generated.PRDRule;
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
    private World simulation = new World();
    private PRDWorld m_generatedWorld;


    public World getSimulation() {
        return simulation;
    }

    public void loadSimulation(String filePath) { //TODO: Add Exceptions fo invalid data
        try {
            InputStream inputStream = new FileInputStream(new File(filePath));
            m_generatedWorld = deserializeFrom(inputStream);
        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        }

        addEntitiesDefinitions();
        addRules();
    }

    private static PRDWorld deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (PRDWorld) u.unmarshal(in);
    }

    private void addEntitiesDefinitions() {
        int entitiesCount = m_generatedWorld.getPRDEntities().getPRDEntity().size();
        for (int i = 0; i < entitiesCount; i++) {
            PRDEntity PRDEntity = m_generatedWorld.getPRDEntities().getPRDEntity().get(i);

            EntityDefinition newEntityDef = new EntityDefinition(PRDEntity.getName(), PRDEntity.getPRDPopulation());

            int entityPropertiesCount = PRDEntity.getPRDProperties().getPRDProperty().size();
            for (int j = 0; j < entityPropertiesCount; j++) {
                PRDProperty PRDProperty = PRDEntity.getPRDProperties().getPRDProperty().get(j);

                PropertyDefinition newPropertyDef = new PropertyDefinition(PRDProperty.getPRDName(), PropertyType.valueOf(PRDProperty.getType().toUpperCase()),
                        new Range(PRDProperty.getPRDRange().getFrom(), PRDProperty.getPRDRange().getTo()),
                        PRDProperty.getPRDValue().isRandomInitialize(), PRDProperty.getPRDValue().getInit());
                newEntityDef.addPropertyDefinition(newPropertyDef);
            }
            simulation.addEntityDefinition(newEntityDef);
        }
    }

    private void addRules() {

        int rulesCount = m_generatedWorld.getPRDRules().getPRDRule().size();
        for (int i = 0; i < rulesCount; i++)
        {
            PRDRule prdRule = m_generatedWorld.getPRDRules().getPRDRule().get(i);
            if (prdRule.getPRDActivation() != null)
            {
                simulation.addRule(prdRule.getName(), prdRule.getPRDActivation().getTicks(), prdRule.getPRDActivation().getProbability());
            }
            else
            {
                simulation.addRule(prdRule.getName(), null, null);
            }


        }
    }
    public String showSimulation() {
        return simulation.toString();
    }

    public void runSimulation() {
    }

    public String showPastSimulation(){return "";}
}
