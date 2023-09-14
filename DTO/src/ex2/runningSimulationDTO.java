package ex2;

import engineAnswers.EntityCountDTO;

import java.util.List;

public class runningSimulationDTO {
    int currentTick;
    int totalTicks;
    long currentSeconds;
    long totalSeconds;
    String status;
    boolean isTerminateByUser;
    List<EntityCountDTO> entityCountDTOS;

    public runningSimulationDTO(int currentTick, int totalTicks, long currentSeconds, long totalSeconds, String status, boolean isTerminateByUser, List<EntityCountDTO> entityCountDTOS) {
        this.currentTick = currentTick;
        this.totalTicks = totalTicks;
        this.currentSeconds = currentSeconds;
        this.totalSeconds = totalSeconds;
        this.status = status;
        this.isTerminateByUser = isTerminateByUser;
        this.entityCountDTOS = entityCountDTOS;
    }

    public boolean isTerminateByUser() {
        return isTerminateByUser;
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

    public int getTotalTicks() {
        return totalTicks;
    }

    public long getTotalSeconds() {
        return totalSeconds;
    }
}
