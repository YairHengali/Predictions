package engine.system;

import engine.world.World;

public interface SystemEngine {
    public World getSimulation();
    public void loadSimulation(String filePath) throws Exception;
    public String showSimulationDetails();
    public int runSimulation();
    public String showPastSimulation();
}
