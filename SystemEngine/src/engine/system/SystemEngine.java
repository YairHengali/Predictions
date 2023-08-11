package engine.system;

import engine.world.World;
import engineAnswers.EndOfSimulationDTO;
import engineAnswers.SimulationDetailsDTO;

public interface SystemEngine {
    public World getSimulation();
    public void loadSimulation(String filePath) throws Exception;
    public SimulationDetailsDTO showSimulationDetails();
    public EndOfSimulationDTO runSimulation();
    public String showPastSimulationDetails();
}
