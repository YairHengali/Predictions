package engine.world.factory;

import engine.Range;
import engine.World;
import engine.action.api.Action;
import engine.action.api.ClacType;
import engine.action.impl.Calculation;
import engine.action.impl.Increase;
import engine.entity.EntityDefinition;
import engine.property.PropertyDefinition;
import engine.property.PropertyType;
import engine.rule.Rule;
import engine.rule.RuleImpl;
import jaxb.generated.*;

import java.util.HashSet;
import java.util.Set;

public class WorldFactoryImpl implements WorldFactory{
    private PRDWorld generatedWorld;
    private World currWorkingWorld;

    @Override
    public World createWorld() {
        return new World();
    }
    @Override
    public void setGeneratedWorld(PRDWorld worldToSet) {
        this.generatedWorld = worldToSet;
    }
    @Override
    public void insertDataToWorld(World simulation) {
        this.currWorkingWorld = simulation;

        try {
            addEnvironmentVariables();
            addEntitiesDefinitions();
            addRules();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }



    private void addEnvironmentVariables() throws Exception {
        int environmentVariablesCount = generatedWorld.getPRDEvironment().getPRDEnvProperty().size();
        Set<String> EnvVarsNames = new HashSet<>();

        for (int i = 0; i < environmentVariablesCount; i++) {
            PRDEnvProperty prdEnvProperty = generatedWorld.getPRDEvironment().getPRDEnvProperty().get(i);
            if (EnvVarsNames.contains(prdEnvProperty.getPRDName())) {
                throw new Exception(String.format("an environment variable with the name: %s is already exists. each environment variable needs to have a unique name!", prdEnvProperty.getPRDName()));
            }
            else
            {
                EnvVarsNames.add(prdEnvProperty.getPRDName());
            }

            if (prdEnvProperty.getPRDRange() != null)
            {
                currWorkingWorld.addEnvironmentVariable(prdEnvProperty.getPRDName(), PropertyType.valueOf(prdEnvProperty.getType().toUpperCase()),new Range(prdEnvProperty.getPRDRange().getFrom(), prdEnvProperty.getPRDRange().getTo()));
            }
            else
            {
                currWorkingWorld.addEnvironmentVariable(prdEnvProperty.getPRDName(), PropertyType.valueOf(prdEnvProperty.getType().toUpperCase()),null);
            }
        }
    }
    private void addEntitiesDefinitions() throws Exception {
        int entitiesCount = generatedWorld.getPRDEntities().getPRDEntity().size();
        for (int i = 0; i < entitiesCount; i++) {
            PRDEntity prdEntity = generatedWorld.getPRDEntities().getPRDEntity().get(i);

            EntityDefinition newEntityDef = new EntityDefinition(prdEntity.getName(), prdEntity.getPRDPopulation());

            int entityPropertiesCount = prdEntity.getPRDProperties().getPRDProperty().size();
            Set<String> entityPropertiesNames = new HashSet<>();
            for (int j = 0; j < entityPropertiesCount; j++) {
                PRDProperty prdProperty = prdEntity.getPRDProperties().getPRDProperty().get(j);

                if (entityPropertiesNames.contains(prdProperty.getPRDName())) {
                    throw new Exception(String.format("an property with the name: %s is already exists in the entity: %s. each property in a entity needs to have a unique name!", prdProperty.getPRDName(), prdEntity.getName()));
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
            currWorkingWorld.addEntityDefinition(newEntityDef);
        }
    }
    private void addRules() throws Exception {
        for (PRDRule prdRule : generatedWorld.getPRDRules().getPRDRule()) {

            Rule currentRule;
            if (prdRule.getPRDActivation() != null) {
                currentRule = new RuleImpl(prdRule.getName(), prdRule.getPRDActivation().getTicks(), prdRule.getPRDActivation().getProbability());
            }
            else {
                currentRule = new RuleImpl(prdRule.getName(), null, null);
            }


            // Adding Rule Actions
            for (PRDAction prdAction : prdRule.getPRDActions().getPRDAction()) { //UNFINISHED! TODO: ALL THE EDGE CASES OF PROPERTIES Existing check. (5, 6 in the XML Checks)
                Action currentAction = null;

                if (currWorkingWorld.getEntityDefinitionByName(prdAction.getEntity()) == null) {
                    throw new Exception(String.format("the entity: %s that referenced in the action: %s in the rule: %s, does not exist!", prdAction.getEntity(), prdAction.getType(), prdRule.getName()));
                }

                /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
                if (prdAction.getType().equals("increase") || prdAction.getType().equals("decrease")) //TODO: case sensitive???
                {
                    checkForValidIncreaseDecreaseArguments(prdRule, prdAction);

                    if (prdAction.getType().equals("increase")){
                        currentAction = new Increase(prdAction.getEntity(), prdAction.getProperty(), prdAction.getBy());
                    }
                    else{
                        //currentAction = new Decrease(prdAction.getEntity(), prdAction.getProperty(), prdAction.getBy());
                    }
                }

                /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
                else if (prdAction.getType().equals("calculation")) {

                    if (!isExistingPropertyInEntity(prdAction.getEntity(), prdAction.getResultProp())) {
                        throw new IllegalArgumentException("the property: " + prdAction.getProperty() + " that referenced in the action: " + prdAction.getType() + " in the rule: " + prdRule.getName() + ", does not exist in the entity: " + prdAction.getEntity());
                    }

                    if (prdAction.getPRDMultiply() != null) {
                        if (!(isNumericArg(prdAction.getPRDMultiply().getArg1()) && isNumericArg(prdAction.getPRDMultiply().getArg2()))) {
                            throw new IllegalArgumentException("Invalid xml file! arguments to" + prdAction.getType() + "action must be numeric.");
                        } else {
                            currentAction = new Calculation(prdAction.getEntity(), prdAction.getProperty(), prdAction.getPRDMultiply().getArg1(), prdAction.getPRDMultiply().getArg2(), ClacType.MULTIPLY);
                        }
                    } else if (prdAction.getPRDDivide() != null) {
                        if (!(isNumericArg(prdAction.getPRDDivide().getArg1()) && isNumericArg(prdAction.getPRDDivide().getArg2()))) {
                            throw new IllegalArgumentException("Invalid xml file! arguments to" + prdAction.getType() + "action must be numeric.");
                        } else {
                            currentAction = new Calculation(prdAction.getEntity(), prdAction.getProperty(), prdAction.getPRDMultiply().getArg1(), prdAction.getPRDMultiply().getArg2(), ClacType.DIVIDE);
                        }
                    } else {
                        throw new IllegalArgumentException("Calculation action supports MULTIPLY or DIVIDE only!");
                    }
                }
                /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
                else if (prdAction.getType().equals("condition")) {

                }

                else if (prdAction.getType().equals("set")) {
                    if (!isExistingPropertyInEntity(prdAction.getEntity(), prdAction.getResultProp())) {
                        throw new IllegalArgumentException("the property: " + prdAction.getProperty() + " that referenced in the action: " + prdAction.getType() + " in the rule: " + prdRule.getName() + ", does not exist in the entity: " + prdAction.getEntity());
                    }
                    //currentAction = new SetAction() TODO: SET ACTION
                }
                else if (prdAction.getType().equals("kill")) {
                    //currentAction = new Kill() TODO: kill ACTION
                }

                currentRule.addAction(currentAction);
            }
        }
    }

    private void checkForValidIncreaseDecreaseArguments(PRDRule prdRule ,PRDAction prdAction)
    {
        if (!isNumericArg(prdAction.getBy())) {
            throw new IllegalArgumentException("Invalid xml file! arguments to" + prdAction.getType() + "action must be numeric.");
        } else if (!isExistingPropertyInEntity(prdAction.getEntity(), prdAction.getProperty())) {
            throw new IllegalArgumentException("the property: " + prdAction.getProperty() + " that referenced in the action: " + prdAction.getType() + " in the rule: " + prdRule.getName() + ", does not exist in the entity: " + prdAction.getEntity());
        }
    }

    private void checkForValidCaclulationArguments(PRDAction prdAction)
    {

    }
    private boolean isExistingPropertyInEntity(String entityName, String propertyName) {
        return this.currWorkingWorld.getEntityDefinitionByName(entityName).getPropertyDefinitionByName(propertyName) != null;
    }
    private boolean isNumericArg(String arg) {
        if (arg.startsWith("environment")){
            //TODO: extract arg from expression, retrieve envVar and validate numeric
        }
        else if(arg.startsWith("random")){
            //TODO: extract arg from expression, try pars and return random
        }
        else {
            //TODO: try pars
        }
        return true;
    }



}
