package desktop.details;

import desktop.AppController;
import engineAnswers.EntityDTO;
import engineAnswers.PropertyDTO;
import engineAnswers.RuleDTO;
import engineAnswers.SimulationDetailsDTO;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.util.HashMap;
import java.util.Map;

public class DetailsController {

    @FXML
    private TreeView<String> simulationTV;
    @FXML
    private TextArea detailsTextArea;

    private AppController mainController;
    private SimulationDetailsDTO simulationDetailsDTO = null;
    Map<TreeItem<String>, String> treeItem2Details = new HashMap<>(); //TODO: HOW TO IMPLEMENT THE MASTER DETAILS??

    public void initialize() {
        simulationTV.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showDetails(newValue)
        );
    }

    private void showDetails(TreeItem<String> selectedNode) {
        if (selectedNode != null && selectedNode.isLeaf()) {
            String nodeName = selectedNode.getValue();
            String details = "Details for " + nodeName; // Replace with actual details retrieval

            detailsTextArea.setText(details);
        } else {

            detailsTextArea.setText("");
        }
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void addDataToSimulationTreeView() {

        this.simulationDetailsDTO = mainController.getSystemEngine().showSimulationDetails();
        TreeItem<String> rootItem = new TreeItem<>("Loaded Simulation Data");

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

        TreeItem<String> envVarsItem = new TreeItem<>("Environment Variables:");
        for (PropertyDTO envVarDTO : simulationDetailsDTO.getEnvironmentVariables()) {
            envVarsItem.getChildren().add(new TreeItem<>(envVarDTO.getName()));
        }

        TreeItem<String> rulesItem = new TreeItem<>("Rules");
        for (RuleDTO ruleDTO : simulationDetailsDTO.getRules()) {
            rulesItem.getChildren().add(new TreeItem<>(ruleDTO.getName()));
        }

        TreeItem<String> terminationItem = new TreeItem<>("Termination Conditions");

        rootItem.getChildren().add(entitiesItem);
        rootItem.getChildren().add(envVarsItem);
        rootItem.getChildren().add(rulesItem);
        rootItem.getChildren().add(terminationItem);
        this.simulationTV.setRoot(rootItem);
        this.simulationTV.refresh();
    }
}
