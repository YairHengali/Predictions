package desktop;

import desktop.details.DetailsController;
import desktop.execution.ExecutionController;
import desktop.header.HeaderController;
import desktop.results.ResultsController;
import engine.system.SystemEngine;
import engine.system.SystemEngineImpl;
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
    private Tab detailsTab;

    @FXML
    private Tab newExecutionTab;

    @FXML
    private Tab resultsTab;

    @FXML
    public void initialize() {
        if (headerComponentController != null && detailsComponentController != null && executionComponentController != null && resultsComponentController != null) {
            headerComponentController.setMainController(this);
            detailsComponentController.setMainController(this);
            executionComponentController.setMainController(this);
            resultsComponentController.setMainController(this);
        }
    }

    public void addDataToSimulationTreeView()
    {
        detailsComponentController.addDataToSimulationTreeView();
    }

    public SystemEngine getSystemEngine() {
        return systemEngine;
    }
}
