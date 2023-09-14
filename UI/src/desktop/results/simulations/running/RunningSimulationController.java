package desktop.results.simulations.running;

import desktop.results.simulations.simulationControllerAPI;
import ex2.runningSimulationDTO;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class RunningSimulationController implements simulationControllerAPI {

    @FXML
    private TableView<?> entityTableView;

    @FXML
    private TableColumn<?, ?> entityColumn;

    @FXML
    private TableColumn<?, ?> entityCountAtBegColumn;

    @FXML
    private TableColumn<?, ?> entityCountAtEndColumn;

    @FXML
    private Label statusLBL;

    @FXML
    private Label tickLBL;

    @FXML
    private Label timeLBL;

    @FXML
    private ProgressBar ticksProgressBar;

    @FXML
    private Label ticksPrecentLBL;

    @FXML
    private ProgressBar timeProgressBar;

    @FXML
    private Label timePrecentLBL;

    @FXML
    private Label errorLBL;

    private SimpleIntegerProperty currTick = new SimpleIntegerProperty(0);
    private SimpleLongProperty runTime = new SimpleLongProperty(0);
    private SimpleStringProperty status = new SimpleStringProperty();

    @Override
    public void setDataFromDTO(runningSimulationDTO simulationDTO) {
        currTick.set(simulationDTO.getCurrentTick());
        runTime.set(simulationDTO.getCurrentSeconds());
        status.set(simulationDTO.getStatus());


    }
    @FXML
    public void initialize() {
        this.tickLBL.textProperty().bind(currTick.asString());
        this.timeLBL.textProperty().bind(runTime.asString());
        this.statusLBL.textProperty().bind(status);
    }



}
