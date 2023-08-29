package desktop.execution;

import engine.property.PropertyType;
import engineAnswers.EndOfSimulationDTO;
import engineAnswers.EntityDTO;
import engineAnswers.PropertyDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import desktop.AppController;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.util.*;

public class ExecutionController {

    @FXML
    private GridPane executionGrid;
    Map<String, Node> name2envVarComponent = new HashMap<>();
    Map<String, EnvVarControllerAPI> name2envVarController = new HashMap<>();

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


    @FXML private HBox currentEnvVarDetails;

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
        clearBTN.disableProperty().bind(mainController.isFileLoadedProperty().not());
    }



    @FXML
    void startButtonActionListener(ActionEvent event) {
        try {
            for (EnvVarControllerAPI controller : name2envVarController.values()) {
                PropertyDTO envVarDTO = controller.createEnvVarDTO();
                mainController.getSystemEngine().setEnvVarDefFromDto(envVarDTO);
            }

        mainController.moveToResultsTab();
        //mainController.getSystemEngine().createNewSimulation();
        mainController.getSystemEngine().runSimulation(); //TODO: stopped here!


        }catch (Exception e) {
            System.out.println("Error: " + e.getMessage() + " try again!");
        }
    }

    @FXML
    void clrButtonActionListener(ActionEvent event) {
        mainController.getSystemEngine().setAllPopulationToZero();
        addDataToEntitiesTable();
    }


    @FXML
    void stamButtonClicked(ActionEvent event) {
        return;
    }
    public void addDataToEntitiesTable()
    {
        Collection<EntityDTO> entityDTOCollection = mainController.getSystemEngine().getEntitiesListDTO();

        ObservableList<EntityDTO> data = FXCollections.observableArrayList();
        data.addAll(entityDTOCollection);

        entityPopulationTable.setItems(data);
    }

    public void addDataToEnvVarsListView()
    {
        envListView.getItems().clear();
        List<PropertyDTO> envVarsDefinitionDto = mainController.getSystemEngine().getEnvVarsDefinitionDto();
        envVarsDefinitionDto.forEach((envVar) -> envListView.getItems().add(envVar.getName()));

//        getNameObservableList<PropertyDTO> data = FXCollections.observableArrayList();
//        data.addAll(simulationDetailsDTO.getEnvironmentVariables());
//
//        envVarsTable.setItems(data);
    }
    public ExecutionController(){

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

        envListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            showEnvVarDetails(newValue);
        });

    }

    private void showEnvVarDetails(String newValue) {
        for (PropertyDTO envVarDTO : mainController.getSystemEngine().getEnvVarsDefinitionDto()) {
            if (envVarDTO.getName().equals(newValue)) {
                if (!name2envVarComponent.containsKey(newValue)) {
                    createEnvVarComponent(envVarDTO);
                }

                if (currentEnvVarDetails.getChildren().size() != 0) {
                    currentEnvVarDetails.getChildren().clear();
                }
                currentEnvVarDetails.getChildren().add(name2envVarComponent.get(newValue));
            }
        }
    }

    private void createEnvVarComponent(PropertyDTO envVarDTO)
    {
        try {
        FXMLLoader loaderComponent = new FXMLLoader();
        Node component = null;
        EnvVarControllerAPI envVarController = null;

        switch (PropertyType.valueOf(envVarDTO.getType().toUpperCase())){
            case BOOLEAN:
                loaderComponent.setLocation(getClass().getResource("BooleanEnvVar.fxml"));
                component = loaderComponent.load();
                envVarController = loaderComponent.getController();
                break;
            case FLOAT:
                break;
            case STRING:
                break;
        }

        envVarController.setDataFromDTO(envVarDTO);
        name2envVarController.put(envVarDTO.getName(), envVarController);
        name2envVarComponent.put(envVarDTO.getName(), component);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    void loadEnvVarComponents(){
//        FXMLLoader loaderComponent1 = new FXMLLoader(getClass().getResource("BooleanEnvVar.fxml"));
////        FXMLLoader loaderComponent2 = new FXMLLoader(getClass().getResource("component2.fxml"));
//        try {
//            Node component1 = loaderComponent1.load();
//            component1.setVisible(false);
//            addEnvVarComponentToGrid(component1);
//
////            Node component2 = loaderComponent2.load();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    void setEnvVarComponentVisible(Node component){

    }
    public void addEnvVarComponentToGrid(Node component1) {
        currentEnvVarDetails.getChildren().add(component1);
//        executionGrid.add(component1, 3, 1);
//        envVarsCopmponents.add(component1);
    }
}
