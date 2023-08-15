package engine.system;

import engineAnswers.*;

import java.io.Serializable;
import java.util.List;

public interface SystemEngine {
    void loadSimulation(String filePath) throws Exception;
    SimulationDetailsDTO showSimulationDetails();
    EndOfSimulationDTO runSimulation();
    List<pastSimulationDTO> getPastSimulationsDetails();
    Boolean isThereLoadedSimulation();
    List<PropertyDTO> getEnvVarsDefinitionDto();
    List<ActiveEnvVarDTO> getActiveEnvVarsDto();
    //void setEnvVarsFromDto(List<PropertyDTO> envVarsDto);
    void setEnvVarFromDto(PropertyDTO envVarDto);
    HistogramDTO getHistogram(int simulationID, String entityName, String propertyName);

    List<EntityCountDTO> getPastSimulationEntityCount(pastSimulationDTO desiredPastSimulation);

    List<EntityDTO> getPastSimulationEntitiesDTO(pastSimulationDTO desiredPastSimulation);

}
