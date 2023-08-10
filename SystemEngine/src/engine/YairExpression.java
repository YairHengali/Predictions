package engine;

import engine.entity.EntityInstance;
import engine.environment.active.ActiveEnvironmentVariables;
import engine.property.PropertyType;

import java.util.Random;

public class YairExpression {
    protected String rawExpression;
    private ActiveEnvironmentVariables environmentVariables;
    private EntityInstance mainEntity;

    public YairExpression(String rawExpression, ActiveEnvironmentVariables environmentVariables, EntityInstance mainEntity) {
        this.rawExpression = rawExpression;
        this.environmentVariables = environmentVariables;
        this.mainEntity = mainEntity;
    }

    public String praseExpressionToValueString()
    {
        if (rawExpression.startsWith("environment") || (rawExpression.startsWith("random"))) {
            return convertHelpFunctionsToStr();
        }

        else if(mainEntity.getPropertyByName(rawExpression) != null){
            return mainEntity.getPropertyByName(rawExpression).getValue();
        }

        else {
            return rawExpression;
        }
    }
    private String convertHelpFunctionsToStr()
    {
        if (rawExpression.startsWith("environment")){
            String envVarName = rawExpression.substring(rawExpression.indexOf('(') + 1, rawExpression.indexOf(')'));
            return environmentVariables.getEvnVariable(envVarName).getValue();
        }

        else{ // if(rawExpression.startsWith("random")){
            String value = rawExpression.substring(rawExpression.indexOf('(') + 1, rawExpression.indexOf(')'));
            try {
                int val = Integer.parseInt(value);
                Random random = new Random();
                return String.valueOf(random.nextInt(val) + 1);
            }
            catch (NumberFormatException e){
                throw e;
            }
        }
    }
}
