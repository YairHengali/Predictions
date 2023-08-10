package engine;

import engine.entity.EntityInstance;
import engine.environment.active.ActiveEnvironmentVariables;
import engine.property.PropertyType;

public class YairExpression {
    protected String rawExpression;
    private ActiveEnvironmentVariables environmentVariables;
    private EntityInstance mainEntity;

    public YairExpression(String rawExpression, ActiveEnvironmentVariables environmentVariables, EntityInstance mainEntity) {
        this.rawExpression = rawExpression;
        this.environmentVariables = environmentVariables;
        this.mainEntity = mainEntity;
    }


    private boolean isExistingPropertyInEntity(String entityName, String propertyName) {
        return this.currWorkingWorld.getEntityDefinitionByName(entityName).getPropertyDefinitionByName(propertyName) != null;
    }

    public String praseExpressionToValueString()
    {
        if (rawExpression.startsWith("environment")) || (rawExpression.startsWith("random")) {
            return func;
        }

        else if(mainEntity.getPropertyByName(rawExpression) != null){
            return mainEntity.getPropertyByName(rawExpression).;
        }

        else {
            return rawExpression;
        }
    }
    private String func()
    {
        if (rawExpression.startsWith("environment")){
            String envVarName = rawExpression.substring(rawExpression.indexOf('(') + 1, rawExpression.indexOf(')'));
            return environmentVariables.getEvnVariable(envVarName);
        }

        else if(rawExpression.startsWith("random")){
            String value = rawExpression.substring(rawExpression.indexOf('(') + 1, rawExpression.indexOf(')'));
            return Float.parseFloat(value);
        }
        else if(isExistingPropertyInEntity(mainEntityName, rawExpression) && isNumericPropertyInEntity(mainEntityName, arg))
        {
            return true;
        }
        else {
            return isNumericStr(rawExpression);
        }
    }
}
