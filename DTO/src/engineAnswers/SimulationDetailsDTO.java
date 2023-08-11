package engineAnswers;

import java.util.List;

public class SimulationDetailsDTO {
    private final List <EntityDTO> entities;
    private final List <RuleDTO> rules;
    private final int maxNumberOfTicks;
    private final long SecondsToTerminate;

    public SimulationDetailsDTO(List<EntityDTO> entities, List<RuleDTO> rules, int maxNumberOfTicks, long secondsToTerminate) {
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

    public int getMaxNumberOfTicks() {
        return maxNumberOfTicks;
    }

    public long getSecondsToTerminate() {
        return SecondsToTerminate;
    }
}
