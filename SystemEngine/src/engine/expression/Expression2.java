package engine.expression;

import engine.context.Context;
import engine.entity.EntityInstance;
import engine.environment.active.ActiveEnvironmentVariables;
import engine.property.PropertyType;

import java.util.Random;

public class Expression2 {
    protected final String rawExpression;
    private final Context context;

    public Expression2(String rawExpression, Context context) {
        this.rawExpression = rawExpression;
        this.context = context;
    }

    public String praseExpressionToValueString(PropertyType typeOfExpectedValue)
    {
        String mainEntityName = context.getPrimaryEntityInstance().getName();
        String secondaryEntityName = context.getSecondaryEntityInstance().getName();

        if ((rawExpression.startsWith("environment")) || (rawExpression.startsWith("random")) || (rawExpression.startsWith("percent")) || (rawExpression.startsWith(mainEntityName + ".")) || (rawExpression.startsWith(secondaryEntityName + "."))) {
            return convertHelpFunctionsToStr(typeOfExpectedValue);
        }

        else if(context.getPrimaryEntityInstance().getPropertyByName(rawExpression) != null){
            return context.getPrimaryEntityInstance().getPropertyByName(rawExpression).getValue();
        }

        else {
            return rawExpression;
        }
    }
    private String convertHelpFunctionsToStr(PropertyType typeOfExpectedValue)
    {
        ActiveEnvironmentVariables environmentVariables = context.getActiveEnvironmentVariables();
        String mainEntityName = context.getPrimaryEntityInstance().getName();
        String secondaryEntityName = context.getSecondaryEntityInstance().getName();


        if (rawExpression.startsWith("environment")){
            String envVarName = rawExpression.substring(rawExpression.indexOf('(') + 1, rawExpression.indexOf(')'));
            if(environmentVariables.getEvnVariable(envVarName).getType() == typeOfExpectedValue){
                return environmentVariables.getEvnVariable(envVarName).getValue();
            }
            else{
                throw new IllegalArgumentException("Expected environment variable of type: " + typeOfExpectedValue + " and received environment variable of type: " + environmentVariables.getEvnVariable(envVarName).getType() + " in function environment");
            }

        }

        else if(rawExpression.startsWith("random")){
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

        else if(rawExpression.startsWith("percent")){ // TODO: EXCEPTIONS?
            Expression2 innerExp1 = new Expression2 (rawExpression.substring(rawExpression.indexOf('(') + 1, rawExpression.indexOf(',')), context);
            Expression2 innerExp2 = new Expression2(rawExpression.substring(rawExpression.indexOf(',') + 1, rawExpression.indexOf(')')), context);

            Float numericExp1 = Float.parseFloat(innerExp1.praseExpressionToValueString(PropertyType.FLOAT));
            Float numericExp2 = Float.parseFloat(innerExp2.praseExpressionToValueString(PropertyType.FLOAT));

            return String.valueOf(numericExp1 * (numericExp2 / 100));
        }

        else if(rawExpression.startsWith("ticks")){ // TODO: Implement that(need to change wntity instacne according to this and show results tab
            return"TODOOOOOOOOOOOOOO";
        }

        else if (rawExpression.startsWith(mainEntityName + ".")){ //TODO: EXCEPTIONS?
            String propertyName = rawExpression.substring(rawExpression.indexOf('.') + 1);
            return context.getPrimaryEntityInstance().getPropertyByName(propertyName).getValue();
        }

        else{ //rawExpression.startsWith(secondaryEntityName + ".") //TODO: EXCEPTIONS?
            String propertyName = rawExpression.substring(rawExpression.indexOf('.') + 1);
            return context.getSecondaryEntityInstance().getPropertyByName(propertyName).getValue();
        }
    }
}
