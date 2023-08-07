package engine.system;

import engine.World;

public interface SystemEngine {
    public World getSimulation();
    public void loadSimulation(String filePath) throws Exception;
    public String showSimulationDetails();
    public void runSimulation();
    public String showPastSimulation();
}
