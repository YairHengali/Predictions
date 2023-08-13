package engine.property;

import engine.range.Range;

public class PropertyDefinition {
    private final String name;
    private final PropertyType type;
    private final Range valueRange;
    private boolean isInitializedRandomly;
    private String initValue;




    public PropertyDefinition(String name, PropertyType type, Range valueRange, boolean isInitializedRandomly, String initValue) {
        this.name = name;
        this.type = type;
        this.valueRange = valueRange;
        this.isInitializedRandomly = isInitializedRandomly;
        this.initValue = initValue;
    }

    public String getName() {
        return name;
    }

    public PropertyType getType() {
        return type;
    }

    public Range getValueRange() {
        return valueRange;
    }

    public boolean isInitializedRandomly() {
        return isInitializedRandomly;
    }

    public String getInitValue() {
        return initValue;
    }

    public void setInitializedRandomly(boolean initializedRandomly) {
        isInitializedRandomly = initializedRandomly;
    }

    public void setInitValue(String initValue) {

        checkValidValueType(initValue);
        checkValueInRange(initValue);
        this.initValue = initValue;
    }

    private void checkValidValueType(String newValue){
        switch(this.type){
            case BOOLEAN:
                if(!(newValue.equals("true") || newValue.equals("false"))){
                    throw new ClassCastException("Type is not matching, expecting boolean!");
                }
                break;
            case DECIMAL:
                try {
                    Integer.parseInt(newValue);
                }
                catch (NumberFormatException e){
                    throw new NumberFormatException("Type is not matching, expecting decimal!");
                }
                break;
            case FLOAT:
                try {
                    Float.parseFloat(newValue);
                }
                catch (NumberFormatException e){
                    throw new NumberFormatException("Type is not matching, expecting decimal!");
                }
                break;
        }
    }

    private void checkValueInRange(String newValue){
        if(this.valueRange != null){
            if(this.type == PropertyType.DECIMAL){
                int decimalValue = Integer.parseInt(newValue);
                if(  !(decimalValue >= valueRange.getFrom().intValue() && decimalValue <= valueRange.getTo().intValue()) ){
                    throw new RuntimeException("Value not in range ("+ valueRange.getFrom() + " - " + valueRange.getTo() + ")");
                }
            }
            else if (this.type == PropertyType.FLOAT) {
                float floatValue = Float.parseFloat(newValue);
                if(  !(floatValue >= valueRange.getFrom().floatValue() && floatValue <= valueRange.getTo().floatValue()) ){
                    throw new RuntimeException("Value not in range ("+ valueRange.getFrom() + " - " + valueRange.getTo() + ")");
                }
            }
        }
    }
}
