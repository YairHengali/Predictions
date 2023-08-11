package engine.system;

import engine.world.World;
import engineAnswers.SimulationDetailsDTO;

public interface SystemEngine {
    public World getSimulation();
    public void loadSimulation(String filePath) throws Exception;
    public SimulationDetailsDTO showSimulationDetails();
    public int runSimulation();
    public String showPastSimulationDetails();
}
