package desktop.execution;

import engineAnswers.EntityDTO;
import engineAnswers.SimulationDetailsDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import desktop.AppController;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

public class ExecutionController {

    @FXML
    private Button startBTN;

    @FXML
    private TableView<EntityDTO> entityPopulationTable;

    @FXML
    private TableColumn<EntityDTO, String> entityCol;

    @FXML
    private TableColumn<EntityDTO, Integer> populationCol;

    @FXML
    private Button clearBTN;
    private AppController mainController;
    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    @FXML
    void startButtonActionListener(ActionEvent event) {
        mainController.moveToResultsTab();
        mainController.getSystemEngine().createNewSimulation();

    }

    public void addDataToEntitiesTable()
    {
        SimulationDetailsDTO simulationDetailsDTO = mainController.getSimulationDetailsDTO();

        ObservableList<EntityDTO> data = FXCollections.observableArrayList();
        data.addAll(simulationDetailsDTO.getEntities());

        entityPopulationTable.setItems(data);
    }
    public void initialize()
    {
        entityCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        populationCol.setCellValueFactory(new PropertyValueFactory<>("population"));
        populationCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    }
}
