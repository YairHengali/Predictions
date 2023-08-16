package engine.expression;

import engine.entity.EntityInstance;
import engine.environment.active.ActiveEnvironmentVariables;

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
                throw new RuntimeException(e + "\n random function must get a decimal number as argument");
            }
        }
    }
}
