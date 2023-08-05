package engine.expression;

import java.lang.reflect.Type;

public class Expression {
    protected String rawExpression; //TODO: כל הטעינה והבדיקה קורית בטעינת הקובץ, SO MAYBE EXPRESSION CAN BE WITH THE REAL VALUE

    public Expression(String rawExpression) {
        this.rawExpression = rawExpression;
    }

    @Override
    public String toString(){
        return "Expression: " + rawExpression;
    }
    public Object evaluate(){
        return null;
    }

}
