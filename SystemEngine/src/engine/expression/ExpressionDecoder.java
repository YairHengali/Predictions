package engine.expression;
import java.lang.reflect.*;

public class ExpressionDecoder {
    private String expression;


    public ExpressionDecoder(String expression, String expectedType){
        this.expression = expression;
    }

    public String decode(EntityDef mainEntity) throws Exception {
        
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
                Method[] worldMethods = Class.forName("engine.world.World").getDeclaredMethods();
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
