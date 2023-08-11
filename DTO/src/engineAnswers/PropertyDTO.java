package engineAnswers;

public class PropertyDTO {
    private final String name;
    private final String type;
    private final Number from;
    private final Number to;
    private final boolean isInitialisedRandomly;

    public PropertyDTO(String name, String type, Number from, Number to, boolean isInitialisedRandomly) {
        this.name = name;
        this.type = type;
        this.from = from;
        this.to = to;
        this.isInitialisedRandomly = isInitialisedRandomly;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Number getFrom() {
        return from;
    }

    public Number getTo() {
        return to;
    }

    public boolean isInitialisedRandomly() {
        return isInitialisedRandomly;
    }
}
