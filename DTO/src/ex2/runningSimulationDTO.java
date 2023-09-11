package ex2;

import engineAnswers.EntityCountDTO;

import java.util.List;

public class runningSimulationDTO {
    int currentTick;
    long currentSeconds;
    String status;
    List<EntityCountDTO> entityCountDTOS;

    public runningSimulationDTO(int currentTick, long currentSeconds, List<EntityCountDTO> entityCountDTOS, String status) {
        this.currentTick = currentTick;
        this.currentSeconds = currentSeconds;
        this.entityCountDTOS = entityCountDTOS;
        this.status = status;
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

    public String getStatus() {
        return status;
    }
}
