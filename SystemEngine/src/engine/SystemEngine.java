package engine;

import engine.entity.EntityDefinition;
import engine.property.PropertyDefinition;
import engine.property.PropertyType;
import engine.property.impl.DecimalProperty;
import engine.property.impl.FloatProperty;
import jaxb.generated.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

public class SystemEngine {
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "jaxb.generated";
    private World simulation = new World();
    private PRDWorld m_generatedWorld;


    public World getSimulation() {
        return simulation;
    }

    public void loadSimulation(String filePath) throws Exception { //TODO: Add Exceptions fo invalid data
        if (!filePath.endsWith(".xml")){
            throw new Exception("Invalid file format! needs to be a .xml file");
        }
        try {
            InputStream inputStream = new FileInputStream(new File(filePath));
            m_generatedWorld = deserializeFrom(inputStream);
        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            addEnvironmentVariables();
            addEntitiesDefinitions();
            addRules();
        }
        catch (Exception e){
            e.getMessage();
        }
    }

    private static PRDWorld deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (PRDWorld) u.unmarshal(in);
    }

    private void addEnvironmentVariables() throws Exception {
        int environmentVariablesCount = m_generatedWorld.getPRDEvironment().getPRDEnvProperty().size();
        Set<String> EnvVarsNames = new HashSet<>();

        for (int i = 0; i < environmentVariablesCount; i++) {
            PRDEnvProperty prdEnvProperty = m_generatedWorld.getPRDEvironment().getPRDEnvProperty().get(i);
            if (EnvVarsNames.contains(prdEnvProperty.getPRDName())) {
                throw new Exception(String.format("an environment variable with the name: %s is already exists. each environment variable needs to have a unique name!", prdEnvProperty.getPRDName()));
            }
            else
            {
                EnvVarsNames.add(prdEnvProperty.getPRDName());
            }

            if (prdEnvProperty.getPRDRange() != null)
            {
                simulation.addEnvironmentVariable(prdEnvProperty.getPRDName(), PropertyType.valueOf(prdEnvProperty.getType().toUpperCase()),new Range(prdEnvProperty.getPRDRange().getFrom(), prdEnvProperty.getPRDRange().getTo()));
            }
            else
            {
                simulation.addEnvironmentVariable(prdEnvProperty.getPRDName(), PropertyType.valueOf(prdEnvProperty.getType().toUpperCase()),null);
            }
        }
    }
    private void addEntitiesDefinitions() {
        int entitiesCount = m_generatedWorld.getPRDEntities().getPRDEntity().size();
        for (int i = 0; i < entitiesCount; i++) {
            PRDEntity prdEntity = m_generatedWorld.getPRDEntities().getPRDEntity().get(i);

            EntityDefinition newEntityDef = new EntityDefinition(prdEntity.getName(), prdEntity.getPRDPopulation());

            int entityPropertiesCount = prdEntity.getPRDProperties().getPRDProperty().size();
            Set<String> entityPropertiesNames = new HashSet<>();
            for (int j = 0; j < entityPropertiesCount; j++) {
                PRDProperty prdProperty = prdEntity.getPRDProperties().getPRDProperty().get(j);

                if (entityPropertiesNames.contains(prdProperty.getPRDName())) {
                    throw new Exception(String.format("an property with the name: %s is already exists in the entity: %s. each property in a entity needs to have a unique name!", prdProperty.getPRDName(), prdEntity.getName());
                }
                else
                {
                    entityPropertiesNames.add(prdProperty.getPRDName());
                }

                PropertyDefinition newPropertyDef;
                if (prdProperty.getPRDRange() != null) {
                    newPropertyDef = new PropertyDefinition(prdProperty.getPRDName(), PropertyType.valueOf(prdProperty.getType().toUpperCase()),
                            new Range(prdProperty.getPRDRange().getFrom(), prdProperty.getPRDRange().getTo()),
                            prdProperty.getPRDValue().isRandomInitialize(), prdProperty.getPRDValue().getInit());
                }
                else{
                    newPropertyDef = new PropertyDefinition(prdProperty.getPRDName(), PropertyType.valueOf(prdProperty.getType().toUpperCase()),
                            null, prdProperty.getPRDValue().isRandomInitialize(), prdProperty.getPRDValue().getInit());
                }
                newEntityDef.addPropertyDefinition(newPropertyDef);
            }
            simulation.addEntityDefinition(newEntityDef);
        }
    }

    private void addRules() throws Exception {

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

            for (PRDAction prdAction : prdRule.getPRDActions().getPRDAction()) { //UNFINISHED! TODO: ALL THE EDGE CASES OF PROPERTIES Existing check. (5, 6 in the XML Checks)
                if (prdAction.getType().equals("increase") || prdAction.getType().equals("decrease")) //TODO: case sensitive???
                {
                    if(prdAction.getBy().startsWith("environment") || prdAction.getBy().startsWith("random"))
                    {
                    //TODO: check that inside is a numeric
                    }
                    else{
                        try {
                            Float.parseFloat(prdAction.getBy());
                        } catch (NumberFormatException e) {
                            throw new Exception("Invalid xml file! arguments to" + prdAction.getType() + "action must be numeric.");
                        }
                    }
                }
                else if (prdAction.getType().equals("calculation"))
                {
                    if(prdAction.getPRDMultiply().getArg1().startsWith("environment"))//TODO: Continue || prdAction.getPRDMultiply().getArg2().startsWith("environment"))
                    {
                        //TODO: check that inside is a numeric
                    }
                    else{
                        try {
                            Float.parseFloat(prdAction.getBy());
                        } catch (NumberFormatException e) {
                            throw new Exception("Invalid xml file! arguments to" + prdAction.getType() + "action must be numeric.");
                        }
                    }
                }
                if (simulation.getEntityDefinitionByName(prdAction.getEntity()) == null)
                {
                    throw new Exception(String.format("the entity: %s that referenced in the action: %s in the rule: %s, does not exist!",prdAction.getEntity(), prdAction.getType(), prdRule.getName()));
                }
                if((simulation.getEntityDefinitionByName(prdAction.getEntity()).getPropertyDefinitionByName(prdAction.getProperty()) == null) ||
                        (simulation.getEntityDefinitionByName(prdAction.getEntity()).getPropertyDefinitionByName(prdAction.getResultProp()) == null))
                {
                    throw new Exception(String.format("the property: %s that referenced in the action: %s in the rule: %s, does not exist in the entity: %s!",prdAction.getProperty(), prdAction.getType(), prdRule.getName(), prdAction.getEntity()));
                }

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
