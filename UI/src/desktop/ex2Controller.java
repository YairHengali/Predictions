package desktop;

import engine.system.SystemEngine;
import engine.system.SystemEngineImpl;
import engineAnswers.EntityDTO;
import engineAnswers.PropertyDTO;
import engineAnswers.RuleDTO;
import engineAnswers.SimulationDetailsDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;

public class ex2Controller {
    private SystemEngine systemEngine = new SystemEngineImpl();
    @FXML
    private Button loadFileBTN;

    @FXML
    private TextField loadFileTF;

    @FXML
    private Tab detailsTab;

    @FXML
    private TreeView<String> simulationTV;

    @FXML
    private Tab newExecutionTab;

    @FXML
    private Tab resultsTab;

    @FXML
    void loadFileButtonActionListener(ActionEvent event) {
        try {
//            systemEngine.loadSimulation(loadFileTF.getText()); //TODO FILE BROWSER
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Simulation File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));

            // Show the file dialog and get the selected file
            File selectedFile = fileChooser.showOpenDialog(loadFileBTN.getScene().getWindow());

            if (selectedFile != null) {
                // Load the selected file using the systemEngine
                systemEngine.loadSimulation(selectedFile.getAbsolutePath());

                // Update the text field with the selected file path
                loadFileTF.setText(selectedFile.getAbsolutePath());


                System.out.println("The xml file has loaded successfully!" + System.lineSeparator());
                systemEngine.clearPastSimulations();
                addDataToSimulationTreeView();
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage() + System.lineSeparator());
        }


    }

    private void addDataToSimulationTreeView() {

        SimulationDetailsDTO simulationDetailsDTO = systemEngine.showSimulationDetails();
        TreeItem<String> rootItem = new TreeItem<>("Simulation Data");

        TreeItem<String> entitiesItem = new TreeItem<>("Entities");
        for (EntityDTO entityDTO : simulationDetailsDTO.getEntities()) {
            TreeItem<String> entityItem = new TreeItem<>(entityDTO.getName());

            TreeItem<String> propertiesItem = new TreeItem<>("Properties");
            for (PropertyDTO propertyDTO : entityDTO.getProperties()) {
                propertiesItem.getChildren().add(new TreeItem<>(propertyDTO.getName()));
            }

            entityItem.getChildren().add(propertiesItem);
            entitiesItem.getChildren().add(entityItem);
        }

        TreeItem<String> rulesItem = new TreeItem<>("Rules");
        for (RuleDTO ruleDTO : simulationDetailsDTO.getRules()) {
            rulesItem.getChildren().add(new TreeItem<>(ruleDTO.getName()));
        }

        TreeItem<String> terminationItem = new TreeItem<>("Termination Conditions");

        rootItem.getChildren().add(entitiesItem);
        rootItem.getChildren().add(rulesItem);
        rootItem.getChildren().add(terminationItem);
        simulationTV.setRoot(rootItem);
        simulationTV.refresh();
    }
}
