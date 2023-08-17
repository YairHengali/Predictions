package engine.world.factory;

import engine.range.Range;
import engine.world.WorldDefinition;
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
import exceptions.xml.NotExistingEntityException;
import exceptions.xml.NotExistingPropertyException;
import exceptions.xml.NotUniqueEnvVarException;
import exceptions.xml.NotUniquePropertyException;
import jaxb.generated.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class WorldDefFactoryImpl implements WorldDefFactory, Serializable {

    private WorldDefinition currWorkingWorld;

    @Override
    public void insertDataToWorldDefinition(WorldDefinition simulationDef, PRDWorld generatedWorld) {
        this.currWorkingWorld = simulationDef;

        try{
            addEnvironmentVariables(generatedWorld);
            addEntitiesDefinitions(generatedWorld);
            addRules(generatedWorld);
            addTerminationSettings(generatedWorld);
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage()); //TODO: Changed to e.getMessage, better to be: e ??
        }
    }

    private void addEnvironmentVariables(PRDWorld generatedWorld) {
        int environmentVariablesCount = generatedWorld.getPRDEvironment().getPRDEnvProperty().size();
        Set<String> EnvVarsNames = new HashSet<>();

        for (int i = 0; i < environmentVariablesCount; i++) {
            PRDEnvProperty prdEnvProperty = generatedWorld.getPRDEvironment().getPRDEnvProperty().get(i);
            if (EnvVarsNames.contains(prdEnvProperty.getPRDName())) {
                throw new NotUniqueEnvVarException(prdEnvProperty.getPRDName());
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
    }

    private void addEntitiesDefinitions(PRDWorld generatedWorld) {
        int entitiesCount = generatedWorld.getPRDEntities().getPRDEntity().size();
        for (int i = 0; i < entitiesCount; i++) {
            PRDEntity prdEntity = generatedWorld.getPRDEntities().getPRDEntity().get(i);

            EntityDefinition newEntityDef = new EntityDefinition(prdEntity.getName(), prdEntity.getPRDPopulation());

            int entityPropertiesCount = prdEntity.getPRDProperties().getPRDProperty().size();
            Set<String> entityPropertiesNames = new HashSet<>();
            for (int j = 0; j < entityPropertiesCount; j++) {
                PRDProperty prdProperty = prdEntity.getPRDProperties().getPRDProperty().get(j);

                if (entityPropertiesNames.contains(prdProperty.getPRDName())) {
                    throw new NotUniquePropertyException(prdProperty.getPRDName(), prdEntity.getName());
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

    private void addTerminationSettings(PRDWorld generatedWorld)
    {
        for (Object obj: generatedWorld.getPRDTermination().getPRDByTicksOrPRDBySecond()) {
            if (obj instanceof PRDByTicks) {
                PRDByTicks ticks = (PRDByTicks) obj;
                currWorkingWorld.setMaxNumberOfTicks(ticks.getCount());
            } else if (obj instanceof PRDBySecond) {
                PRDBySecond seconds = (PRDBySecond) obj;
                currWorkingWorld.setSecondsToTerminate(seconds.getCount());
            }
        }
    }

    private void addRules(PRDWorld generatedWorld) {
        for (PRDRule prdRule : generatedWorld.getPRDRules().getPRDRule()) {

            Rule currentRule;
            if (prdRule.getPRDActivation() != null) {
                currentRule = new RuleImpl(prdRule.getName(), prdRule.getPRDActivation().getTicks(), prdRule.getPRDActivation().getProbability());
            }
            else {
                currentRule = new RuleImpl(prdRule.getName(), null, null);
            }

            // Adding Rule Actions
            for (PRDAction prdAction : prdRule.getPRDActions().getPRDAction()) {
                Action newAction = createActionFromPrd(prdAction);
                currentRule.addAction(newAction);
            }
            currWorkingWorld.addRule(currentRule);
        }
    }

    private ConditionImpl createConditionAction(String actionEntityName, PRDCondition prdCondition) //ASSUMING ALL CONDITIONS CONTAIN THE SAME THEN AND ELSE
    {
        ConditionImpl resCondition;
        if (prdCondition.getSingularity().equals("single")) {
            if (!isExistingPropertyInEntity(prdCondition.getEntity(), prdCondition.getProperty())) {
                throw new NotExistingPropertyException(prdCondition.getProperty(),"Condition", prdCondition.getEntity());
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

            resCondition = new SingleCondition(prdCondition.getEntity(), prdCondition.getProperty(), conditionOp, prdCondition.getValue());
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
            throw new NotExistingEntityException(prdAction.getEntity(), prdAction.getType());
        }

        switch (prdAction.getType()) {
            case "increase":
            case "decrease":

                checkForValidIncreaseDecreaseArguments(prdAction);

                if (prdAction.getType().equals("increase")) {
                    resAction = new Increase(prdAction.getEntity(), prdAction.getProperty(), prdAction.getBy());
                } else {
                    resAction = new Decrease(prdAction.getEntity(), prdAction.getProperty(), prdAction.getBy());
                }
                break;

            case "calculation":

                if (!isExistingPropertyInEntity(prdAction.getEntity(), prdAction.getResultProp())) {
                    throw new NotExistingPropertyException(prdAction.getResultProp(), prdAction.getType(), prdAction.getEntity());
                }
                else if (!isNumericPropertyInEntity(prdAction.getEntity(), prdAction.getResultProp()))
                {
                    throw new IllegalArgumentException("Invalid xml file! property in " + prdAction.getType() + " must be of a numeric type.");
                }

                if (prdAction.getPRDMultiply() != null) {
                    if (!(isNumericArg(prdAction.getEntity(), prdAction.getPRDMultiply().getArg1()) && isNumericArg(prdAction.getEntity(), prdAction.getPRDMultiply().getArg2()))) {
                        throw new IllegalArgumentException("Invalid xml file! arguments to " + prdAction.getType() + " action must be numeric.");
                    } else {
                        resAction = new Calculation(prdAction.getEntity(), prdAction.getResultProp(), prdAction.getPRDMultiply().getArg1(), prdAction.getPRDMultiply().getArg2(), ClacType.MULTIPLY);
                    }
                } else if (prdAction.getPRDDivide() != null) {
                    if (!(isNumericArg(prdAction.getEntity(), prdAction.getPRDDivide().getArg1()) && isNumericArg(prdAction.getEntity(), prdAction.getPRDDivide().getArg2()))) {
                        throw new IllegalArgumentException("Invalid xml file! arguments to " + prdAction.getType() + " action must be numeric.");
                    } else {
                        resAction = new Calculation(prdAction.getEntity(), prdAction.getResultProp(), prdAction.getPRDDivide().getArg1(), prdAction.getPRDDivide().getArg2(), ClacType.DIVIDE);
                    }
                } else {
                    throw new IllegalArgumentException("Invalid xml file! Calculation action supports MULTIPLY or DIVIDE only!");
                }
                break;

            case "condition":
                ConditionImpl resCondition = createConditionAction(prdAction.getEntity(), prdAction.getPRDCondition());

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
                    throw new NotExistingPropertyException(prdAction.getProperty(), prdAction.getType(), prdAction.getEntity());
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

        if (!isExistingPropertyInEntity(prdAction.getEntity(), prdAction.getProperty())) {
            throw new NotExistingPropertyException(prdAction.getProperty(), prdAction.getType(), prdAction.getEntity());
        }
        else if (!isNumericArg(prdAction.getEntity(), prdAction.getBy())) {
            throw new IllegalArgumentException("Invalid xml file! argument: " + prdAction.getBy() + ", to action " + prdAction.getType() + " - expected to be numerical!");
        }
        else if (!isNumericPropertyInEntity(prdAction.getEntity(), prdAction.getProperty()))
        {
            throw new IllegalArgumentException("Invalid xml file! property: " + prdAction.getProperty() + ", in action " + prdAction.getType() + " - expected to be numerical!");
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
            return true;
//            String value = arg.substring(arg.indexOf('(') + 1, arg.indexOf(')'));
//            try {
//                Integer.parseInt(value);
//                return true;
//            } catch (NumberFormatException e) {
//                return false;
//            }
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
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
