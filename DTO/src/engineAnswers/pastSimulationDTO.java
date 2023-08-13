package engineAnswers;

import java.util.List;

public class pastSimulationDTO {
    private final String dateOfRun;
    private final int id;
    private final List<EntityCountDTO> entityCountDtos;

    public pastSimulationDTO(String dateOfRun, int id, List<EntityCountDTO> entityCountDtos) {//TODO: add properties histogram and entities counts
        this.dateOfRun = dateOfRun;
        this.id = id;
        this.entityCountDtos = entityCountDtos;
    }

    public List<EntityCountDTO> getEntityCountDtos() {
        return entityCountDtos;
    }

    public String getDateOfRun() {
        return dateOfRun;
    }

    public int getId() {
        return id;
    }
}
