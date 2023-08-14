package engine.system;

import engine.world.World;
import engineAnswers.*;

import java.util.List;

public interface SystemEngine {
    public World getSimulation();
    public void loadSimulation(String filePath) throws Exception;
    public SimulationDetailsDTO showSimulationDetails();
    public EndOfSimulationDTO runSimulation();
    public List<pastSimulationDTO> getPastSimulationsDetails();
    public Boolean isThereLoadedSimulation();
    List<PropertyDTO> getEnvVarsDefinitionDto();
    List<ActiveEnvVarDTO> getActiveEnvVarsDto();
    //void setEnvVarsFromDto(List<PropertyDTO> envVarsDto);
    void setEnvVarFromDto(PropertyDTO envVarDto);
}
