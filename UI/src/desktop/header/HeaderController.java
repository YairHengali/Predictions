package desktop.header;

import desktop.AppController;
import engineAnswers.EntityDTO;
import engineAnswers.PropertyDTO;
import engineAnswers.RuleDTO;
import engineAnswers.SimulationDetailsDTO;
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
                mainController.addDataToEnvVarsTable();

                loadEnvVarComponents();
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage() + System.lineSeparator());
        }
    }

    void loadEnvVarComponents(){
        FXMLLoader loaderComponent1 = new FXMLLoader(getClass().getResource("BooleanEnvVar.fxml"));
//        FXMLLoader loaderComponent2 = new FXMLLoader(getClass().getResource("component2.fxml"));
        try {
            Node component1 = loaderComponent1.load();
            component1.setVisible(false);
            mainController.addEnvVarComponentToExecutionGrid(component1);

//            Node component2 = loaderComponent2.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }




    }
}
