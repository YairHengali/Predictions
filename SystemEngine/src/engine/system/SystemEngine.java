package engine.system;

import engine.world.World;
import engineAnswers.EndOfSimulationDTO;
import engineAnswers.PropertyDTO;
import engineAnswers.SimulationDetailsDTO;
import engineAnswers.pastSimulationDTO;

import java.util.List;

public interface SystemEngine {
    public World getSimulation();
    public void loadSimulation(String filePath) throws Exception;
    public SimulationDetailsDTO showSimulationDetails();
    public EndOfSimulationDTO runSimulation();
    public List<pastSimulationDTO> getPastSimulationsDetails();
    public Boolean isThereLoadedSimulation();
    List<PropertyDTO> getEnvVarsDto();
    //void setEnvVarsFromDto(List<PropertyDTO> envVarsDto);
    void setEnvVarFromDto(PropertyDTO envVarDto);
}
