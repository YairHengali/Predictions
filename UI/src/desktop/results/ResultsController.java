package desktop.results;

import desktop.AppController;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class ResultsController {
    @FXML
    private ListView<String> executionList;

    private AppController mainController;
    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

}
