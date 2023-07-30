package engine;

public class SystemEngine {
    private World simulation = new World();

    public World getSimulation() {
        return simulation;
    }

    public void loadSimulation() {
    }

    public String showSimulation() {
        return simulation.toString();
    }

    public void runSimulation() {
    }

    public String showPastSimulation(){return "";}
}
