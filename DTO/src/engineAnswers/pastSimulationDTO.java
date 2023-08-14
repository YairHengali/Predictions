package engineAnswers;

import java.util.List;

public class pastSimulationDTO {
    private final String dateOfRun;
    private final int id;

    public pastSimulationDTO(String dateOfRun, int id) {//TODO: add properties histogram and entities counts
        this.dateOfRun = dateOfRun;
        this.id = id;
    }

    public String getDateOfRun() {
        return dateOfRun;
    }

    public int getId() {
        return id;
    }
}
