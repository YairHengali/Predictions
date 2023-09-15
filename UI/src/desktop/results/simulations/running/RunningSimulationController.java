package desktop.results.simulations.running;

import desktop.results.simulations.simulationControllerAPI;
import engineAnswers.EntityCountDTO;
import ex2.runningSimulationDTO;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
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

    @FXML
    private HBox timeProgressHBox;

    @FXML
    private HBox ticksProgressHBox;

    private SimpleIntegerProperty currTick = new SimpleIntegerProperty(0);
    private SimpleLongProperty runTime = new SimpleLongProperty(0);
    private SimpleStringProperty status = new SimpleStringProperty();

    private SimpleBooleanProperty showingTimeProgress = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty showingTicksProgress = new SimpleBooleanProperty(false);
    private SimpleDoubleProperty ticksProgress = new SimpleDoubleProperty(0);
    private SimpleDoubleProperty timeProgress = new SimpleDoubleProperty(0);

    @Override
    public void setDataFromDTO(runningSimulationDTO simulationDTO) {
        currTick.set(simulationDTO.getCurrentTick());
        runTime.set(simulationDTO.getCurrentSeconds());
        status.set(simulationDTO.getStatus());

        bindDataToEntityTableView(simulationDTO);

        boolean isTicksProgress = simulationDTO.getTotalTicks() != null;
        boolean isTimeProgress = simulationDTO.getTotalSeconds() != null;

        if(isTimeProgress) {
            this.showingTimeProgress.set(true);
            setTimeProgressBar(simulationDTO.getCurrentSeconds(), 100);
        }
        else
            this.showingTimeProgress.set(false);

        if(isTicksProgress) {
            this.showingTicksProgress.set(true);
            setTicksProgressBar(simulationDTO.getCurrentTick(), 100);
        }
        else
            this.showingTicksProgress.set(false);


    }

    private void setTicksProgressBar(int currTick, int totalTicks){
        double progress = (double) currTick /totalTicks;
        ticksProgressBar.setProgress(progress);
        ticksProgress.set(progress  * 100);
    }

    private void setTimeProgressBar(long currTime, long totalTime){
        double progress = (double) currTime /totalTime;
        timeProgressBar.setProgress(progress);
        timeProgress.set(progress * 77);
    }

    private void bindDataToEntityTableView(runningSimulationDTO simulationDTO){
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
        this.ticksPrecentLBL.textProperty().bind(ticksProgress.asString());
        this.timePrecentLBL.textProperty().bind(timeProgress.asString());
        this.timeProgressHBox.visibleProperty().bind(showingTimeProgress);
        this.ticksProgressHBox.visibleProperty().bind(showingTicksProgress);




    }



}
