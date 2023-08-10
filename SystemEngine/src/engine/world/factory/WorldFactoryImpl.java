package engine.world.factory;

import engine.range.Range;
import engine.world.World;
import engine.action.api.Action;
import engine.action.api.ClacType;
import engine.action.impl.*;
import engine.action.impl.condition.ConditionOp;
import engine.action.impl.condition.LogicalOperator;
import engine.action.impl.condition.impl.ConditionImpl;
import engine.action.impl.condition.impl.MultipleCondition;
import engine.action.impl.condition.impl.SingleCondition;
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
            addTerminationSettings();

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

            PropertyDefinition newEnvVarDef;

            if (prdEnvProperty.getPRDRange() != null) {
                newEnvVarDef = new PropertyDefinition(prdEnvProperty.getPRDName(), PropertyType.valueOf(prdEnvProperty.getType().toUpperCase()),
                        new Range(prdEnvProperty.getPRDRange().getFrom(), prdEnvProperty.getPRDRange().getTo()),
                        false, "");
            }
            else{
                newEnvVarDef = new PropertyDefinition(prdEnvProperty.getPRDName(), PropertyType.valueOf(prdEnvProperty.getType().toUpperCase()),
                        null, false, "");
            }

            currWorkingWorld.addEnvironmentVariableDef(newEnvVarDef);

        }
    } //TODO: CURRENTLY ADDED DIRECTLY INTO PROPERTY INSTANCES
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

                PropertyDefinition newPropertyDef = createPropertyDefinitionFromPrdProperty(prdProperty);
                newEntityDef.addPropertyDefinition(newPropertyDef);
            }
            currWorkingWorld.addEntityDefinition(newEntityDef);
        }
    }

    private PropertyDefinition createPropertyDefinitionFromPrdProperty(PRDProperty prdProperty)
    {
        PropertyDefinition resPropertyDef;

        if (prdProperty.getPRDRange() != null) {
            resPropertyDef = new PropertyDefinition(prdProperty.getPRDName(), PropertyType.valueOf(prdProperty.getType().toUpperCase()),
                    new Range(prdProperty.getPRDRange().getFrom(), prdProperty.getPRDRange().getTo()),
                    prdProperty.getPRDValue().isRandomInitialize(), prdProperty.getPRDValue().getInit());
        }
        else{
            resPropertyDef = new PropertyDefinition(prdProperty.getPRDName(), PropertyType.valueOf(prdProperty.getType().toUpperCase()),
                    null, prdProperty.getPRDValue().isRandomInitialize(), prdProperty.getPRDValue().getInit());
        }

        return resPropertyDef;
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
                Action newAction = createActionFromPrd(prdAction);
                currentRule.addAction(newAction);
            }
            currWorkingWorld.addRule(currentRule);
        }
    } //TODO: DEAL WITH EXCEPTIONS
//////////
    // TODO: REARRANGE THE METHODS BELLOW
    //
    private void addTerminationSettings()
    {
       //TODO: how to get them???
    }
    private ConditionImpl createConditionAction(String actionEntityName, PRDCondition prdCondition) //ASSUMING ALL CONDITIONS CONTAIN THE SAME THEN AND ELSE
    {
        ConditionImpl resCondition;
        if (prdCondition.getSingularity().equals("single")) {
            if (!isExistingPropertyInEntity(prdCondition.getEntity(), prdCondition.getProperty())) {
                throw new IllegalArgumentException("the property: " + prdCondition.getProperty() + " that referenced in a condition does not exist in the entity: " + prdCondition.getEntity());
            }

            ConditionOp conditionOp = null;
            switch (prdCondition.getOperator()) {
                case "=":
                    conditionOp = ConditionOp.EQUALS;
                    break;
                case "!=":
                    conditionOp = ConditionOp.NOTEQUALS;
                    break;
                case "bt":
                    conditionOp = ConditionOp.BT;
                    break;
                case "lt":
                    conditionOp = ConditionOp.LT;
                    break;
            }

            resCondition = new SingleCondition(prdCondition.getEntity(), prdCondition.getProperty(), conditionOp, prdCondition.getValue());//TODO: IN the next version there might be entity of action and entity of rule
        }
        else //singularity: multiple
        {
            MultipleCondition multipleCondition = new MultipleCondition(actionEntityName, LogicalOperator.valueOf(prdCondition.getLogical().toUpperCase()));
            for (PRDCondition prdSubCondition : prdCondition.getPRDCondition()) {
                multipleCondition.addCondition(createConditionAction(actionEntityName, prdSubCondition));
            }

            resCondition = multipleCondition;
        }

        return resCondition;
    }

    private Action createActionFromPrd(PRDAction prdAction)
    {
        Action resAction = null;
        if (currWorkingWorld.getEntityDefinitionByName(prdAction.getEntity()) == null) {
            throw new IllegalArgumentException("the entity: " + prdAction.getEntity() + " that referenced in action: " + prdAction.getType() + "does not exist!");
        }

        switch (prdAction.getType()) {
            case "increase":
            case "decrease":
//TODO: case sensitive???

                checkForValidIncreaseDecreaseArguments(prdAction);

                if (prdAction.getType().equals("increase")) {
                    resAction = new Increase(prdAction.getEntity(), prdAction.getProperty(), prdAction.getBy());
                } else {
                    resAction = new Decrease(prdAction.getEntity(), prdAction.getProperty(), prdAction.getBy());
                }
                break;

            case "calculation":

                if (!isExistingPropertyInEntity(prdAction.getEntity(), prdAction.getResultProp())) {
                    throw new IllegalArgumentException("the property: " + prdAction.getResultProp() + " that referenced in the action: " + prdAction.getType() + " does not exist in the entity: " + prdAction.getEntity());
                }
                else if (!isNumericPropertyInEntity(prdAction.getEntity(), prdAction.getResultProp()))
                {
                    throw new IllegalArgumentException("Invalid xml file! property in" + prdAction.getResultProp() + "must be of numeric type.");
                }

                if (prdAction.getPRDMultiply() != null) {
                    if (!(isNumericArg(prdAction.getEntity(), prdAction.getPRDMultiply().getArg1()) && isNumericArg(prdAction.getEntity(), prdAction.getPRDMultiply().getArg2()))) {
                        throw new IllegalArgumentException("Invalid xml file! arguments to" + prdAction.getType() + "action must be numeric.");
                    } else {
                        resAction = new Calculation(prdAction.getEntity(), prdAction.getProperty(), prdAction.getPRDMultiply().getArg1(), prdAction.getPRDMultiply().getArg2(), ClacType.MULTIPLY);
                    }
                } else if (prdAction.getPRDDivide() != null) {
                    if (!(isNumericArg(prdAction.getEntity(), prdAction.getPRDDivide().getArg1()) && isNumericArg(prdAction.getEntity(), prdAction.getPRDDivide().getArg2()))) {
                        throw new IllegalArgumentException("Invalid xml file! arguments to" + prdAction.getType() + "action must be numeric.");
                    } else {
                        resAction = new Calculation(prdAction.getEntity(), prdAction.getProperty(), prdAction.getPRDDivide().getArg1(), prdAction.getPRDDivide().getArg2(), ClacType.DIVIDE);
                    }
                } else {
                    throw new IllegalArgumentException("Calculation action supports MULTIPLY or DIVIDE only!");
                }
                break;

            case "condition": //TODO: add an checks for condition action
                ConditionImpl resCondition = createConditionAction(prdAction.getEntity(), prdAction.getPRDCondition()); //TODO make abstract father

                for (PRDAction prdActionInThen : prdAction.getPRDThen().getPRDAction()) {
                    resCondition.addActionToThen(createActionFromPrd(prdActionInThen));
                }

                if (prdAction.getPRDElse() != null) {
                    for (PRDAction prdActionInElse : prdAction.getPRDElse().getPRDAction()) {
                        resCondition.addActionToElse(createActionFromPrd(prdActionInElse));
                    }
                }

                resAction = resCondition;
                break;

            case "set":
                if (!isExistingPropertyInEntity(prdAction.getEntity(), prdAction.getProperty())) {
                    throw new IllegalArgumentException("the property: " + prdAction.getProperty() + " that referenced in action: " + prdAction.getType() + " does not exist in the entity: " + prdAction.getEntity());
                }
                resAction = new SetAction(prdAction.getEntity(), prdAction.getProperty(), prdAction.getValue());
                break;

            case "kill":
                resAction = new Kill(prdAction.getEntity());
                break;
        }

        return resAction;
    }

    private void checkForValidIncreaseDecreaseArguments(PRDAction prdAction)
    {
        if (!isNumericArg(prdAction.getEntity(), prdAction.getBy())) {
            throw new IllegalArgumentException("Invalid xml file! arguments to" + prdAction.getType() + "action must be numeric.");
        } else if (!isExistingPropertyInEntity(prdAction.getEntity(), prdAction.getProperty())) {
            throw new IllegalArgumentException("the property: " + prdAction.getProperty() + " that referenced in action: " + prdAction.getType() + ", does not exist in the entity: " + prdAction.getEntity());
        }
        else if (!isNumericPropertyInEntity(prdAction.getEntity(), prdAction.getProperty()))
        {
            throw new IllegalArgumentException("Invalid xml file! property in" + prdAction.getType() + "must be of numeric type.");
        }
    }

    private boolean isNumericPropertyInEntity(String entityName, String propertyName) {
        PropertyType inputPropertyType = this.currWorkingWorld.getEntityDefinitionByName(entityName).getPropertyDefinitionByName(propertyName).getType();
        return (inputPropertyType == PropertyType.DECIMAL || inputPropertyType == PropertyType.FLOAT);
    }

    private boolean isExistingPropertyInEntity(String entityName, String propertyName) {
        return this.currWorkingWorld.getEntityDefinitionByName(entityName).getPropertyDefinitionByName(propertyName) != null;
    }
    private boolean isNumericArg(String mainEntityName, String arg) {
        if (arg.startsWith("environment")){
            String envVarName = arg.substring(arg.indexOf('(') + 1, arg.indexOf(')'));
            return (this.currWorkingWorld.getEnvironmentVariableDefByName(envVarName).getType() == PropertyType.DECIMAL
                    || this.currWorkingWorld.getEnvironmentVariableDefByName(envVarName).getType() == PropertyType.FLOAT);
        }
        else if(arg.startsWith("random")){
            String value = arg.substring(arg.indexOf('(') + 1, arg.indexOf(')'));
            return isNumericStr(value);
        }
        else if(isExistingPropertyInEntity(mainEntityName, arg) && isNumericPropertyInEntity(mainEntityName, arg))
        {
            return true;
        }
        else {
            return isNumericStr(arg);
        }
    }

    private boolean isNumericStr(String str)
    {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) { //TODO: MAYBE THROW THE EXCEPTION OUTSIDE
            return false;
        }
    }


}
