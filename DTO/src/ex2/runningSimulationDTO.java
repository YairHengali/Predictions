package ex2;

import engineAnswers.EntityCountDTO;

import java.util.List;

public class runningSimulationDTO {
    int currentTick;
    long currentSeconds;
    List<EntityCountDTO> entityCountDTOS;

    public runningSimulationDTO(int currentTick, long currentSeconds, List<EntityCountDTO> entityCountDTOS) {
        this.currentTick = currentTick;
        this.currentSeconds = currentSeconds;
        this.entityCountDTOS = entityCountDTOS;
    }

    public int getCurrentTick() {
        return currentTick;
    }

    public long getCurrentSeconds() {
        return currentSeconds;
    }

    public List<EntityCountDTO> getEntityCountDTOS() {
        return entityCountDTOS;
    }
}
