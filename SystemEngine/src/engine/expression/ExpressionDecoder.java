package engine.expression;
import java.lang.reflect.*;
import java.util.Arrays;

import engine.Entity;
import engine.World;

public class ExpressionDecoder {
    private String expression;


    public ExpressionDecoder(String expression, String expectedType){
        this.expression = expression;
    }

    public String decode(Entity mainEntity) throws Exception {
        
        String[] words = this.expression.split("\\s+");
        if (words.length > 0) {
            String firstWord = words[0];
            return "";
        }
        else {
            throw new Exception("Empty Expression!");
        }
    }
    
    private boolean isWorldMethod(String firstWord)
    {
        int openingParenIndex = firstWord.indexOf("(");
        if (openingParenIndex != -1) {
            String methodName = firstWord.substring(0, openingParenIndex);
            try {
                Method[] worldMethods = Class.forName("engine.World").getDeclaredMethods();
                for (Method method : worldMethods) {
                    if (method.getName().substring(0,method.getName().indexOf("(")).contains(methodName))
                        return true;
                }
            } catch (ClassNotFoundException e) {
                return false;
            }
        }
        return false;
    }
}
