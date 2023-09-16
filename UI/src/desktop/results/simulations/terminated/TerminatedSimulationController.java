package desktop.results.simulations.terminated;

import desktop.AppController;
import engineAnswers.EntityCountDTO;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import desktop.results.simulations.simulationControllerAPI;
import ex2.runningSimulationDTO;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

import java.util.Collection;

public class TerminatedSimulationController implements simulationControllerAPI
{
    private int currentChosenSimulationID;

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
    private Label reasonLBL;

    @FXML
    private Label errorLBL;

    @FXML
    private NumberAxis lineChart;

    @FXML
    private Button histogramBTN;

    @FXML
    private Button consistencyBTN;

    @FXML
    private Button avgBTN;

    private AppController mainController;


    private SimpleIntegerProperty currTick = new SimpleIntegerProperty(0);
    private SimpleLongProperty runTime = new SimpleLongProperty(0);
    private SimpleStringProperty status = new SimpleStringProperty();
    private SimpleStringProperty reason = new SimpleStringProperty();

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
        this.reasonLBL.textProperty().bind(reason);
    }
    @Override
    public void setDataFromDTO(runningSimulationDTO simulationDTO) {

        currTick.set(simulationDTO.getCurrentTick());
        runTime.set(simulationDTO.getCurrentSeconds());
        status.set(simulationDTO.getStatus());

        bindDataToEntityTableView(simulationDTO);
    }

    @Override
    public void setCurrentChosenSimulationID(int ID) {
        this.currentChosenSimulationID = ID;
    }

    @Override
    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    private void bindDataToEntityTableView(runningSimulationDTO simulationDTO){
        Collection<EntityCountDTO> entityCountDTOCollection = simulationDTO.getEntityCountDTOS();
        ObservableList<EntityCountDTO> data = FXCollections.observableArrayList();
        data.clear();
        data.addAll(entityCountDTOCollection);
        entityTableView.setItems(data);
    }
}
