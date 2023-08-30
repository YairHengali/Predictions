package desktop.execution.envvar.api;

import engineAnswers.PropertyDTO;

public interface EnvVarControllerAPI {
    void setDataFromDTO(PropertyDTO envVarDTO);
    PropertyDTO createEnvVarDTO();
}
