package desktop.results.simulations.terminated;

import desktop.AppController;
import desktop.results.simulations.terminated.statistics.HistogramController;
import engineAnswers.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import desktop.results.simulations.simulationControllerAPI;
import ex2.runningSimulationDTO;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javafx.stage.Stage;

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
    private Label resultLBL;

    @FXML
    private NumberAxis lineChart;

    @FXML
    private ComboBox<String> entityComboBox = new ComboBox<>();
    private String selectedEntity = null;

    @FXML
    private ComboBox<String> propertyComboBox = new ComboBox<>();
    private String selectedProperty = null;

    @FXML
    private ComboBox<String> functionComboBox;

    @FXML
    private VBox resultVBox;

    private AppController mainController;

    Map<String, Node> name2ExtraDetailsComponent = new HashMap<>();



    private SimpleIntegerProperty currTick = new SimpleIntegerProperty(0);
    private SimpleLongProperty runTime = new SimpleLongProperty(0);
    private SimpleStringProperty status = new SimpleStringProperty();
    private SimpleStringProperty reason = new SimpleStringProperty();

    private Stage HistogramStage;
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

        propertyComboBox.setDisable(true);
        functionComboBox.setDisable(true);
        functionComboBox.getItems().add("1. Histogram");
        functionComboBox.getItems().add("2. Consistency");
        functionComboBox.getItems().add("3. Average");

        entityComboBox.setOnAction(event -> {
            selectedEntity = entityComboBox.getSelectionModel().getSelectedItem();
            if (selectedEntity != null) {
                propertyComboBox.getItems().clear();
                propertyComboBox.setDisable(false);
                propertyComboBox.setPromptText("Property");
                functionComboBox.setDisable(true);
                functionComboBox.setPromptText("Extra Details");
                resultLBL.setVisible(false);



                for(EntityDTO entity : mainController.getSystemEngine().getEntitiesListDTO() ){
                    if(entity.getName().equals(selectedEntity))
                        entity.getProperties().forEach(property -> propertyComboBox.getItems().add(property.getName()));
                }
            }
        });

        propertyComboBox.setOnAction(event -> {
            selectedProperty = propertyComboBox.getSelectionModel().getSelectedItem();
            functionComboBox.setPromptText("Extra Details");
            functionComboBox.setDisable(false);
            resultLBL.setVisible(false);


        });

    }

    private void showAverage(){
        try {
            AveragePropertyValueDTO avgDTO = mainController.getSystemEngine().getAverageOfPropertyInTerminatedSimulation(currentChosenSimulationID, selectedEntity, selectedProperty);
            resultLBL.setVisible(true);
            resultLBL.setText("Average: " + avgDTO.getValue());
        }
        catch (Exception e){
            resultLBL.setVisible(false);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.initOwner(this.entityTableView.getScene().getWindow());
            alert.showAndWait();
        }

    }

    private void showConsistency(){
        try {
            PropertyConsistencyDTO consistencyDTO = mainController.getSystemEngine().getConsistencyOfPropertyInTerminatedSimulation(currentChosenSimulationID, selectedEntity, selectedProperty);
            resultLBL.setVisible(true);
            resultLBL.setText("Consistency: " + consistencyDTO.getValue());
        }
        catch (Exception e){
            resultLBL.setVisible(false);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.initOwner(this.entityTableView.getScene().getWindow());
            alert.showAndWait();
        }
    }

    private void showHistogram() {
        HistogramDTO histogramDTO = mainController.getSystemEngine().getHistogramOfPropertyInTerminatedSimulation(currentChosenSimulationID,selectedEntity,selectedProperty);
            createHistogramComponent(histogramDTO);

        // Create a new window for the histogram
        if (HistogramStage != null) {
            HistogramStage.close();
        }

        HistogramStage = new Stage();
        HistogramStage.setTitle("Histogram Preview");
        HistogramStage.setScene(new Scene((Parent) name2ExtraDetailsComponent.get("Histogram"),600,600));
        HistogramStage.show();
        //setExtraDetailsComponent(name2ExtraDetailsComponent.get("Histogram"));

        resultLBL.setVisible(false);
    }

    private void setExtraDetailsComponent(Node component) {
        if(!resultVBox.getChildren().isEmpty()) {
            resultVBox.getChildren().clear();
        }

        resultVBox.getChildren().add(component);
    }

    private void createHistogramComponent(HistogramDTO histogramDTO) {
        FXMLLoader loaderComponent = new FXMLLoader();
        Node component = null;
        HistogramController simulationController = null;
        loaderComponent.setLocation(getClass().getResource("/desktop/results/simulations/terminated/statistics/Histogram.fxml"));


        try {
            component = loaderComponent.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        simulationController = loaderComponent.getController();

        name2ExtraDetailsComponent.put("Histogram",component);
        simulationController.setDataFromDTO(histogramDTO);
    }

    @Override
    public void setDataFromDTO(runningSimulationDTO simulationDTO) {

        currTick.set(simulationDTO.getCurrentTick());
        runTime.set(simulationDTO.getCurrentSeconds());
        status.set(simulationDTO.getStatus());

        bindDataToEntityTableView(simulationDTO);

        entityComboBox.getItems().clear();
        mainController.getSystemEngine().getEntitiesListDTO().forEach(entityDTO -> entityComboBox.getItems().add(entityDTO.getName()));
    }

    @Override
    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void setSimulationID(int simulationID ){
        this.currentChosenSimulationID = simulationID;
    }

    @FXML
    public void showExtraDetailsSelected(ActionEvent event) {
        String selectedFunction = functionComboBox.getSelectionModel().getSelectedItem();
        if(selectedFunction.startsWith("1")){
            showHistogram();
        }
        else if (selectedFunction.startsWith("2")) {
            showConsistency();
        }
        else if (selectedFunction.startsWith("3")) {
            showAverage();
        }
    }
    private void bindDataToEntityTableView(runningSimulationDTO simulationDTO){
        Collection<EntityCountDTO> entityCountDTOCollection = simulationDTO.getEntityCountDTOS();
        ObservableList<EntityCountDTO> data = FXCollections.observableArrayList();
        data.clear();
        data.addAll(entityCountDTOCollection);
        entityTableView.setItems(data);
    }

}
