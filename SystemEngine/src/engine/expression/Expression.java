package engine.expression;

import engine.entity.EntityInstance;
import engine.environment.active.ActiveEnvironmentVariables;
import engine.property.PropertyType;

import java.io.Serializable;
import java.util.Random;

public class Expression implements Serializable {
    protected final String rawExpression;
    private final ActiveEnvironmentVariables environmentVariables;
    private final EntityInstance mainEntity;

    public Expression(String rawExpression, ActiveEnvironmentVariables environmentVariables, EntityInstance mainEntity) {
        this.rawExpression = rawExpression;
        this.environmentVariables = environmentVariables;
        this.mainEntity = mainEntity;
    }

    public String praseExpressionToValueString(PropertyType typeOfExpectedValue)
    {
        if (rawExpression.startsWith("environment") || (rawExpression.startsWith("random"))) {
            return convertHelpFunctionsToStr(typeOfExpectedValue);
        }

        else if(mainEntity.getPropertyByName(rawExpression) != null){
            return mainEntity.getPropertyByName(rawExpression).getValue();
        }

        else {
            return rawExpression;
        }
    }
    private String convertHelpFunctionsToStr(PropertyType typeOfExpectedValue)
    {
        if (rawExpression.startsWith("environment")){
            String envVarName = rawExpression.substring(rawExpression.indexOf('(') + 1, rawExpression.indexOf(')'));
            if(environmentVariables.getEvnVariable(envVarName).getType() == typeOfExpectedValue){
                return environmentVariables.getEvnVariable(envVarName).getValue();
            }
            else{
                throw new IllegalArgumentException("Expected environment variable of type: " + typeOfExpectedValue + " and received environment variable of type: " + environmentVariables.getEvnVariable(envVarName).getType() + " in function environment");
            }

        }

        else{ // if(rawExpression.startsWith("random")){
            String value = rawExpression.substring(rawExpression.indexOf('(') + 1, rawExpression.indexOf(')'));
            try {
                int val = Integer.parseInt(value);
                Random random = new Random();
                if(typeOfExpectedValue == PropertyType.DECIMAL || typeOfExpectedValue == PropertyType.FLOAT)
                    return String.valueOf(random.nextInt(val) + 1);
                else{
                    throw new IllegalArgumentException("Error, trying to insert a decimal value (from random function) to a property of type: " + typeOfExpectedValue);
                }
            }
            catch (NumberFormatException e){
                throw new RuntimeException(e + "\n random function must get a decimal number as argument");
            }
        }
    }
}
