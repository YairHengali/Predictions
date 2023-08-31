package desktop;

import desktop.details.DetailsController;
import desktop.execution.ExecutionController;
import desktop.header.HeaderController;
import desktop.results.ResultsController;
import engine.system.SystemEngine;
import engine.system.SystemEngineImpl;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class AppController {
    private SystemEngine systemEngine = new SystemEngineImpl();

    @FXML
    private VBox headerComponent;
    @FXML
    private HeaderController headerComponentController;
    @FXML
    private GridPane detailsComponent;
    @FXML
    private DetailsController detailsComponentController;
    @FXML
    private GridPane executionComponent;
    @FXML
    private ExecutionController executionComponentController;
    @FXML
    private GridPane resultsComponent;
    @FXML
    private ResultsController resultsComponentController;
    @FXML
    private TabPane tabPane;

    @FXML
    private Tab detailsTab;

    @FXML
    private Tab newExecutionTab;

    @FXML
    private Tab resultsTab;

    private SimpleBooleanProperty isFileLoaded;

    public boolean getIsFileLoaded() {
        return isFileLoaded.get();
    }

    public SimpleBooleanProperty isFileLoadedProperty() {
        return isFileLoaded;
    }

    public void setIsFileLoaded(boolean isFileLoaded) {
        this.isFileLoaded.set(isFileLoaded);
    }

    @FXML
    public void initialize() {
        if (headerComponentController != null && detailsComponentController != null && executionComponentController != null && resultsComponentController != null) {
            headerComponentController.setMainController(this);
            detailsComponentController.setMainController(this);
            executionComponentController.setMainController(this);
            resultsComponentController.setMainController(this);
        }
    }

    public AppController(){
        isFileLoaded = new SimpleBooleanProperty(false);
    }

    public void addDataToSimulationTreeView()
    {
        detailsComponentController.addDataToSimulationTreeView();
    }

    public SystemEngine getSystemEngine() {
        return systemEngine;
    }

    public void moveToResultsTab(){
        tabPane.getSelectionModel().select(resultsTab);
    }

    public void addDataToEntitiesTable()
    {
        executionComponentController.addDataToEntitiesTable();
    }

    public void addDataToEnvVarsTable() {
        executionComponentController.addDataToEnvVarsListView();
    }
}