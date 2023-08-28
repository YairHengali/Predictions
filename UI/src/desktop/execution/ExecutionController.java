package desktop.execution;

import engineAnswers.EntityDTO;
import engineAnswers.PropertyDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import desktop.AppController;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.util.converter.IntegerStringConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ExecutionController {

    @FXML
    private GridPane executionGrid;
    List<Node> envVarsCopmponents = new ArrayList<>();
    @FXML
    private Button startBTN;
    @FXML
    private Button stamButton;
    @FXML
    private Button clearBTN;

    ////////////////////////////////////////////////////////
    @FXML
    private TableView<EntityDTO> entityPopulationTable;
    @FXML
    private TableColumn<EntityDTO, String> entityCol;
    @FXML
    private TableColumn<EntityDTO, Integer> populationCol;
    ////////////////////////////////////////////////////////
    @FXML
    private ListView<String> envListView;

//    @FXML
//    private TableView<PropertyDTO> envVarsTable;
//    @FXML
//    private TableColumn<PropertyDTO, String> envNameCol;
//    @FXML
//    private TableColumn<PropertyDTO, Float> envFromCol;
//    @FXML
//    private TableColumn<PropertyDTO, Float> envToCol;
//    @FXML
//    private TableColumn<?, ?> envTypeCol;
    ////////////////////////////////////////////////////////



    private AppController mainController;
    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    @FXML
    void startButtonActionListener(ActionEvent event) {
        mainController.moveToResultsTab();
        mainController.getSystemEngine().createNewSimulation();
    }
    @FXML
    void clrButtonActionListener(ActionEvent event) {
        mainController.getSystemEngine().setAllPopulationToZero();
        addDataToEntitiesTable();
//        entityPopulationTable.refresh();
    }


    @FXML
    void stamButtonClicked(ActionEvent event) {
        envVarsCopmponents.get(0).setVisible(true);
    }
    public void addDataToEntitiesTable()
    {
        Collection<EntityDTO> entityDTOCollection = mainController.getSystemEngine().getEntitiesListDTO();

        ObservableList<EntityDTO> data = FXCollections.observableArrayList();
        data.addAll(entityDTOCollection);

        entityPopulationTable.setItems(data);
    }
    public void addDataToEnvVarsTable()
    {


        List<PropertyDTO> envVarsDefinitionDto = mainController.getSystemEngine().getEnvVarsDefinitionDto();
        envVarsDefinitionDto.forEach((envVar) -> envListView.getItems().add(envVar.getName()));

//        getNameObservableList<PropertyDTO> data = FXCollections.observableArrayList();
//        data.addAll(simulationDetailsDTO.getEnvironmentVariables());
//
//        envVarsTable.setItems(data);
    }
    public void initialize()
    {
        entityCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        populationCol.setCellValueFactory(new PropertyValueFactory<>("population"));
        populationCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        populationCol.setOnEditCommit(event -> {

            //TODO : checkForMaxNumOfEntities();

            // Get the edited item
            EntityDTO oldEntityDTO = event.getRowValue();

            // Update the population property of the City
            EntityDTO newEntityDTO = new EntityDTO(oldEntityDTO.getName(), event.getNewValue(), oldEntityDTO.getProperties() );

            // Call your function in the engine with the updated value
            mainController.getSystemEngine().updateEntityDefPopulation(newEntityDTO);
        });


//        envNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
//        envToCol.setCellValueFactory(new PropertyValueFactory<>("to"));
//        envFromCol.setCellValueFactory(new PropertyValueFactory<>("from"));
//        envTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));


    }


    void setEnvVarComponentVisible(Node component){

    }
    public void addEnvVarComponentToGrid(Node component1) {
        executionGrid.add(component1, 3, 1);
        envVarsCopmponents.add(component1);
    }
}
