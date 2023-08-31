package desktop.header;

import desktop.AppController;
import engineAnswers.EntityDTO;
import engineAnswers.PropertyDTO;
import engineAnswers.RuleDTO;
import engineAnswers.SimulationDetailsDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

public class HeaderController {


    @FXML private Button loadFileBTN;
    @FXML private TextField loadFileTF;

    private AppController mainController;
    private String lastAccessedFolderPath = "";
    private SimpleStringProperty selectedFileProperty;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void initialize(){
        loadFileTF.textProperty().bind(selectedFileProperty);

    }

    public HeaderController(){
        selectedFileProperty = new SimpleStringProperty("No file loaded");
    }
    @FXML
    void loadFileButtonActionListener(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Simulation File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));

            // Set the initial directory to the last accessed folder
            if (!lastAccessedFolderPath.isEmpty()) {
                fileChooser.setInitialDirectory(new File(lastAccessedFolderPath));
            }

            // Show the file dialog and get the selected file
            File selectedFile = fileChooser.showOpenDialog(loadFileBTN.getScene().getWindow());

            if (selectedFile == null) {
                return;
            }

            // Update the last accessed folder's path
            lastAccessedFolderPath = selectedFile.getParent();


            // Load the selected file using the systemEngine
            mainController.getSystemEngine().loadSimulation(selectedFile.getAbsolutePath());
            mainController.setIsFileLoaded(true);

            // Update the text field with the selected file path
            selectedFileProperty.set(selectedFile.getAbsolutePath());


            System.out.println("The xml file has loaded successfully!" + System.lineSeparator());
            mainController.getSystemEngine().clearPastSimulations();
            mainController.addDataToSimulationTreeView();
            mainController.addDataToEntitiesTable();
            mainController.addDataToEnvVarsTable();

        }
        catch (Exception e) {
            System.out.println(e.getMessage() + System.lineSeparator());
        }
    }


}
