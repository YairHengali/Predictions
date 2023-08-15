package engineAnswers;

import java.util.List;
import java.util.Locale;

public class SimulationDetailsDTO {
    private final List <EntityDTO> entities;
    private final List <RuleDTO> rules;
    private final Integer maxNumberOfTicks;
    private final Long SecondsToTerminate;

    public SimulationDetailsDTO(List<EntityDTO> entities, List<RuleDTO> rules, Integer maxNumberOfTicks, Long secondsToTerminate) {
        this.entities = entities;
        this.rules = rules;
        this.maxNumberOfTicks = maxNumberOfTicks;
        this.SecondsToTerminate = secondsToTerminate;
    }

    public List<EntityDTO> getEntities() {
        return entities;
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
