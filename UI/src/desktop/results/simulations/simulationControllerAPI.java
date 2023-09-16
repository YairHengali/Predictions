package desktop.results.simulations;

import desktop.AppController;
import ex2.runningSimulationDTO;

public interface simulationControllerAPI {
    void setDataFromDTO(runningSimulationDTO simulationDTO);

    void setCurrentChosenSimulationID(int ID);

    void setMainController(AppController mainController);
}
