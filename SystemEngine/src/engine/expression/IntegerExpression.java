package engine.expression;

public class IntegerExpression extends Expression{
    private int value;

    public IntegerExpression(String rawExpression) {
        super(rawExpression);
        try {
            this.value = Integer.parseInt(rawExpression);
        }
        catch (NumberFormatException e){
            throw e;
        }
    }
    int getValue()
    {
        return this.value;
    }
}
