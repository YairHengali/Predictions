package engine;

public class Range {
    private Number from;
    private Number to;

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
