package desktop.header;

import desktop.AppController;
import engineAnswers.EntityDTO;
import engineAnswers.PropertyDTO;
import engineAnswers.RuleDTO;
import engineAnswers.SimulationDetailsDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.stage.FileChooser;

import java.io.File;

public class HeaderController {

    @FXML
    private Button loadFileBTN;

    @FXML
    private TextField loadFileTF;

    private AppController mainController;
    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    @FXML
    void loadFileButtonActionListener(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Simulation File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));

            // Show the file dialog and get the selected file
            File selectedFile = fileChooser.showOpenDialog(loadFileBTN.getScene().getWindow());

            if (selectedFile != null) {
                // Load the selected file using the systemEngine
                mainController.getSystemEngine().loadSimulation(selectedFile.getAbsolutePath());

                // Update the text field with the selected file path
                loadFileTF.setText(selectedFile.getAbsolutePath());


                System.out.println("The xml file has loaded successfully!" + System.lineSeparator());
                mainController.getSystemEngine().clearPastSimulations();
                mainController.addDataToSimulationTreeView();
                mainController.addDataToEntitiesTable();
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage() + System.lineSeparator());
        }
    }
}
