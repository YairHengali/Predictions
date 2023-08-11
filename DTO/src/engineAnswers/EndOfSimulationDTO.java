package engineAnswers;

public class EndOfSimulationDTO {
    int simulationID;
    String reasonOfTermination;

    public EndOfSimulationDTO(int simulationID, String reasonOfTermination) {
        this.simulationID = simulationID;
        this.reasonOfTermination = reasonOfTermination;
    }

    public int getSimulationID() {
        return simulationID;
    }

    public String getReasonOfTermination() {
        return reasonOfTermination;
    }
}
