package desktop.execution;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import desktop.AppController;

public class ExecutionController {

    @FXML
    private Button startBTN;

    @FXML
    private Button clearBTN;
    private AppController mainController;
    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    @FXML
    void startButtonActionListener(ActionEvent event) {
        mainController.moveToResultsTab();
    }
}
