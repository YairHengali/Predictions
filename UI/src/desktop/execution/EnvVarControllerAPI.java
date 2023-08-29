package desktop.execution;

import engineAnswers.PropertyDTO;

public interface EnvVarControllerAPI {
    void setDataFromDTO(PropertyDTO envVarDTO);
    PropertyDTO createEnvVarDTO();
}
