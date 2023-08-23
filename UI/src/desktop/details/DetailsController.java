package desktop.details;

import desktop.AppController;
import engineAnswers.EntityDTO;
import engineAnswers.PropertyDTO;
import engineAnswers.RuleDTO;
import engineAnswers.SimulationDetailsDTO;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class DetailsController {

    @FXML
    private TreeView<String> simulationTV;

    private AppController mainController;
    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void addDataToSimulationTreeView() {

        SimulationDetailsDTO simulationDetailsDTO = mainController.getSystemEngine().showSimulationDetails();
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
        this.simulationTV.setRoot(rootItem);
        this.simulationTV.refresh();
    }
}
