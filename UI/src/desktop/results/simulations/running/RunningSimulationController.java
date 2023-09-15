package desktop.results.simulations.running;

import desktop.results.simulations.simulationControllerAPI;
import engineAnswers.EntityCountDTO;
import engineAnswers.EntityDTO;
import ex2.runningSimulationDTO;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

import java.util.Collection;

public class RunningSimulationController implements simulationControllerAPI {

    // ~~~~~~~~ Table view ~~~~~~~~
    @FXML
    private TableView<EntityCountDTO> entityTableView;

    @FXML
    private TableColumn<EntityCountDTO, String> entityColumn;

    @FXML
    private TableColumn<EntityCountDTO, Integer> entityCountAtBegColumn;

    @FXML
    private TableColumn<EntityCountDTO, Integer> entityCountAtEndColumn;

    // ~~~~~~~~ ^ Table view ^ ~~~~~~~~

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

        Collection<EntityCountDTO> entityCountDTOCollection = simulationDTO.getEntityCountDTOS();
        ObservableList<EntityCountDTO> data = FXCollections.observableArrayList();

        data.clear();
        data.addAll(entityCountDTOCollection);

        entityTableView.setItems(data);


    }
    @FXML
    public void initialize() {
        entityColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        entityCountAtBegColumn.setCellValueFactory(new PropertyValueFactory<>("populationAtStart"));
        entityCountAtEndColumn.setCellValueFactory(new PropertyValueFactory<>("populationAtEnd"));
        entityCountAtBegColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        entityCountAtEndColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        this.tickLBL.textProperty().bind(currTick.asString());
        this.timeLBL.textProperty().bind(runTime.asString());
        this.statusLBL.textProperty().bind(status);
    }



}
