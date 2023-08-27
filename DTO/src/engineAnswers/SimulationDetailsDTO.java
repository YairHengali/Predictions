package engineAnswers;

import java.util.List;
import java.util.Map;

public class SimulationDetailsDTO {
    private final List<EntityDTO> entities;
    private final List<PropertyDTO> environmentVariables;
    private final List<RuleDTO> rules;
    private final Integer maxNumberOfTicks;
    private final Long SecondsToTerminate;

    public SimulationDetailsDTO(List<EntityDTO> entities,  List<PropertyDTO> environmentVariables, List<RuleDTO> rules, Integer maxNumberOfTicks, Long secondsToTerminate) {
        this.entities = entities;
        this.environmentVariables = environmentVariables;
        this.rules = rules;
        this.maxNumberOfTicks = maxNumberOfTicks;
        this.SecondsToTerminate = secondsToTerminate;
    }

    public List<EntityDTO> getEntities() {
        return entities;
    }

    public List<PropertyDTO> getEnvironmentVariables() {
        return environmentVariables;
    }

    public List<RuleDTO> getRules() {
        return rules;
    }

    public Integer getMaxNumberOfTicks() {
        return maxNumberOfTicks;
    }

    public Long getSecondsToTerminate() {
        return SecondsToTerminate;
    }
}
