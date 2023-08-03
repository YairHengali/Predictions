package engine;

public class Range {
    private double from;
    private double to;

    public Range(double from, double to)
    {
        this.from = from;
        this.to = to;
    }

    public double getFrom() {
        return from;
    }

    public double getTo() {
        return to;
    }

    @Override
    public String toString() {
        return "(" + from + " to " + to + ")";
    }
}
