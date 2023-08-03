package engine.expression;

public class BoolExpression extends Expression{
    private boolean value;

    public BoolExpression(String rawExpression) throws Exception{
        super(rawExpression);
        switch (rawExpression) {
            case "true":
                this.value = true;
                break;
            case "false":
                this.value = false;
                break;
            default:
                throw new Exception("Invalid Arguments, Expected 'true' / ' false");
        }
    }

    public boolean getValue(){
        return this.value;
    }
}
