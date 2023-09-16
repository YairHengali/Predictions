package engine.system;

import engineAnswers.*;
import ex2.ThreadpoolDTO;
import ex2.runningSimulationDTO;

import java.util.Collection;
import java.util.List;

public interface SystemEngine {
    void loadSimulation(String filePath) throws Exception;
    SimulationDetailsDTO getSimulationDetails();
    pastSimulationDTO runSimulation();
    List<pastSimulationDTO> getPastSimulationsDetails();
    Boolean isThereLoadedSimulation();
    List<PropertyDTO> getEnvVarsDefinitionDto();
    List<ActiveEnvVarDTO> getActiveEnvVarsDto();
    //void setEnvVarsFromDto(List<PropertyDTO> envVarsDto);
    void setEnvVarDefFromDto(PropertyDTO envVarDto);
    HistogramDTO getHistogram(int simulationID, String entityName, String propertyName);

    List<EntityCountDTO> getPastSimulationEntityCount(pastSimulationDTO desiredPastSimulation);

    List<EntityDTO> getPastSimulationEntitiesDTO(pastSimulationDTO desiredPastSimulation);

    void clearPastSimulations();

//    void createNewSimulation();

    void updateEntityDefPopulation(EntityDTO newEntityDTO);

    void setAllPopulationToZero();

    Collection<EntityDTO> getEntitiesListDTO();

    //////////////////////////////////////TRYING PULLING DATA:
    runningSimulationDTO pullData(int simulationID);

    void pauseSimulation(int simulationID);

    void resumeSimulation(int simulationID);
    void stopSimulation(int simulationID);

    ThreadpoolDTO getThreadpoolData();

}
