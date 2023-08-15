package engine.range;

public class Range {
    private final Number from;
    private final Number to;

    public Range(double from, double to)
    {
        this.from = from;
        this.to = to;
    }

    public Number getFrom() {
        return from;
    }

    public Number getTo() {
        return to;
    }

    @Override
    public String toString() {
        return "(" + from + " to " + to + ")";
    }
}
