package engine.expression;

import engine.environment.active.ActiveEnvironmentVariables;
import engine.property.PropertyType;

import java.lang.reflect.Type;

public class Expression implements Serializable {
    protected String rawExpression;

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
