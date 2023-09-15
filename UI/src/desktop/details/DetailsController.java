package desktop.details;

import desktop.AppController;
import desktop.details.action.api.ActionControllerAPI;
import engine.action.api.ActionType;
import engineAnswers.*;
import ex2.actions.SingleConditionDTO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DetailsController {

    @FXML
    private TreeView<String> simulationTV;
    @FXML
    private TextArea detailsTextArea;
    @FXML
    private FlowPane detailsFlowPane;
    private AppController mainController;

    Map<TreeItem<String>, String> treeItem2Details = new HashMap<>(); //TODO: HOW TO IMPLEMENT THE MASTER DETAILS??

    public void initialize() {
        simulationTV.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showDetails(newValue)
        );
    }

    private void showDetails(TreeItem<String> selectedNode) { //TODO: can change in simulationDetailsDTO to maps but for now tried like that
        SimulationDetailsDTO simulationDetailsDTO = mainController.getSystemEngine().getSimulationDetails();
        detailsFlowPane.getChildren().clear();

        if (selectedNode != null && selectedNode.isLeaf()) {
            String selectedNodeValue = selectedNode.getValue();
            String details = "";
            if (selectedNodeValue.equals("Termination Conditions"))
            {
                if (simulationDetailsDTO.getMaxNumberOfTicks() != null) {
                    details += "Number Of Ticks: " + simulationDetailsDTO.getMaxNumberOfTicks() + System.lineSeparator();
                }
                if (simulationDetailsDTO.getSecondsToTerminate() != null){
                    details += "Number Of Seconds: " + simulationDetailsDTO.getSecondsToTerminate() + System.lineSeparator();
                }
                if(simulationDetailsDTO.isTerminatedByUser()){
                    details += "By User";
                }
            }
            else if (selectedNodeValue.equals("Activation")) {
                String selectedNodeRuleName = selectedNode.getParent().getValue();
                for (RuleDTO ruleDTO :simulationDetailsDTO.getRules()) {
                    if (ruleDTO.getName().equals(selectedNodeRuleName)) {
                        //TODO: make it a component
                        details = ("Ticks for activation: " + ruleDTO.getTicksForActivation() + System.lineSeparator() +
                                "Probability for activation: " + ruleDTO.getProbabilityForActivation() + System.lineSeparator());
                    }
                }
            }

            else if (selectedNodeValue.equals("Actions")) {
                String selectedNodeRuleName = selectedNode.getParent().getValue();
                for (RuleDTO ruleDTO : simulationDetailsDTO.getRules()) {
                    if (ruleDTO.getName().equals(selectedNodeRuleName)) {
                        for (ActionDTO actionDTO : ruleDTO.getActions()) {
                            detailsFlowPane.getChildren().add(createActionComponent(actionDTO));
                        }
                    }
                }
            }


            else{
                switch (selectedNode.getParent().getValue()) {
                    case "Properties":
                        String selectedNodeEntityName = selectedNode.getParent().getParent().getValue();
                        for (EntityDTO entityDTO : simulationDetailsDTO.getEntities()) {
                            if (entityDTO.getName().equals(selectedNodeEntityName)) {
                                for (PropertyDTO propertyDTO : entityDTO.getProperties())//TODO: better change in entityDTO to maps but for now tried like that
                                {
                                    if (propertyDTO.getName().equals(selectedNodeValue)) {
                                        //TODO: make it a component
                                        details = ("Name: " + propertyDTO.getName() + System.lineSeparator() +
                                                "Type: " + propertyDTO.getType() + System.lineSeparator());
                                        if (propertyDTO.getFrom() != null) {
                                            if (propertyDTO.getType().equals("DECIMAL")) {
                                                details += ("Range: " + propertyDTO.getFrom().intValue() + " to " + propertyDTO.getTo().intValue() + System.lineSeparator());
                                            } else {
                                                details += ("Range: " + propertyDTO.getFrom() + " to " + propertyDTO.getTo() + System.lineSeparator());
                                            }
                                        }
                                        details += ("Is initialized randomly: " + propertyDTO.isInitialisedRandomly());
                                    }
                                }
                            }
                        }
                        break;
                    case "Environment Variables":
                        for (PropertyDTO envVarDTO : simulationDetailsDTO.getEnvironmentVariables()) {
                            if (envVarDTO.getName().equals(selectedNodeValue)) {
                                //TODO: make it a component
                                details = ("Name: " + envVarDTO.getName() + System.lineSeparator() +
                                        "Type: " + envVarDTO.getType() + System.lineSeparator());
                                if (envVarDTO.getFrom() != null) {
                                    if (envVarDTO.getType().equals("DECIMAL")) {
                                        details += ("Range: " + envVarDTO.getFrom().intValue() + " to " + envVarDTO.getTo().intValue());
                                    } else {
                                        details += ("Range: " + envVarDTO.getFrom() + " to " + envVarDTO.getTo());
                                    }
                                }
                            }
                        }
                        break;
//                    case "Rules": //TODO: Deal with actions details inside
//                        for (RuleDTO ruleDTO :simulationDetailsDTO.getRules()) {
//                            if (ruleDTO.getName().equals(selectedNodeValue))
//                            {
//                                //TODO: make it a component
//                                details = ("Name: " + ruleDTO.getName() + System.lineSeparator() +
//                                        "Ticks for activation: " + ruleDTO.getTicksForActivation() + System.lineSeparator() +
//                                        "Probability for activation: " + ruleDTO.getProbabilityForActivation()+ System.lineSeparator() +
//                                        "Number of actions: " + ruleDTO.getActions().size());
//                            }
//                        }
//                        break;
                    case "Actions":
//                        String selectedNodeRuleName = selectedNode.getParent().getValue();
//                        for (RuleDTO ruleDTO :simulationDetailsDTO.getRules()) {
//                            if (ruleDTO.getName().equals(selectedNodeRuleName)) {
//                                for (ActionDTO actionDTO : ruleDTO.getActions()) {
////                        TODO: CREATE ACTIONS COMPONENTS AND ADD THEM TO THE RIGHT SIDE!!
//
//                                }
//                            }
//                        }
//
                        //FOR EACH ACTION WHEN SELECTED: CANNOT BE BY NAME! BECAUSE NOT UNIQUE, NEED TO CONSIDER OTHER APPROACH
//                                  if(actionDTO.getName().equals(selectedNodeValue))


                        break;
                    default:
                        details = "";
                        break;
                }
            }


//            if(!detailsFlowPane.getChildren().isEmpty())
//                detailsFlowPane.getChildren().clear();

            detailsTextArea.setText(details);
//            detailsTextArea.setVisible(true);
            detailsFlowPane.getChildren().add(detailsTextArea);
        }
//        else { //FOR CLEANUP WHEN NOT LEAF
//
//            detailsTextArea.setText("");
//        }
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void addDataToSimulationTreeView() {

        SimulationDetailsDTO simulationDetailsDTO = mainController.getSystemEngine().getSimulationDetails();
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

        TreeItem<String> envVarsItem = new TreeItem<>("Environment Variables");
        for (PropertyDTO envVarDTO : simulationDetailsDTO.getEnvironmentVariables()) {
            envVarsItem.getChildren().add(new TreeItem<>(envVarDTO.getName()));
        }

        TreeItem<String> rulesItem = new TreeItem<>("Rules");

        for (RuleDTO ruleDTO : simulationDetailsDTO.getRules()) {
            TreeItem<String> ruleItem = new TreeItem<>(ruleDTO.getName());

            TreeItem<String> actionsItem = new TreeItem<>("Actions");
//            for (ActionDTO actionDTO: ruleDTO.getActions()) { //ADD INNER ACTIONS TO TREE:
//                actionsItem.getChildren().add(new TreeItem<>(actionDTO.getType()));
//            };

            ruleItem.getChildren().add(actionsItem);
            ruleItem.getChildren().add(new TreeItem<>("Activation"));
            rulesItem.getChildren().add(ruleItem);
        }


        TreeItem<String> terminationItem = new TreeItem<>("Termination Conditions");

        TreeItem<String> gridItem = new TreeItem<>("Grid: " + simulationDetailsDTO.getRowsInGrid() + " X " + simulationDetailsDTO.getColsInGrid());

        rootItem.getChildren().add(entitiesItem);
        rootItem.getChildren().add(envVarsItem);
        rootItem.getChildren().add(rulesItem);
        rootItem.getChildren().add(terminationItem);
        rootItem.getChildren().add(gridItem);
        this.simulationTV.setRoot(rootItem);
        this.simulationTV.refresh();
    }

    private Node createActionComponent(ActionDTO actionDTO)
    {
        try {
            FXMLLoader loaderComponent = new FXMLLoader();
            Node component = null;
            ActionControllerAPI actionController = null;

            switch (ActionType.valueOf(actionDTO.getType().toUpperCase())) {
                case INCREASE:
                case DECREASE:
                    loaderComponent.setLocation(getClass().getResource("/desktop/details/action/impl/increase.fxml"));
                    break;
                case CALCULATION:
                    loaderComponent.setLocation(getClass().getResource("/desktop/details/action/impl/calculation.fxml"));
                    break;
                case CONDITION:
                    if (actionDTO instanceof SingleConditionDTO)
                        loaderComponent.setLocation(getClass().getResource("/desktop/details/action/impl/simpleCondition.fxml"));
                    else // (actionDTO instanceof MultipleConditionDTO)
                        loaderComponent.setLocation(getClass().getResource("/desktop/details/action/impl/multipleCondition.fxml"));
                    break;
                case SET:
                    loaderComponent.setLocation(getClass().getResource("/desktop/details/action/impl/set.fxml"));
                    break;
                case KILL:
                    loaderComponent.setLocation(getClass().getResource("/desktop/details/action/impl/kill.fxml"));
                    break;
                case REPLACE:
                    loaderComponent.setLocation(getClass().getResource("/desktop/details/action/impl/replace.fxml"));
                    break;
                case PROXIMITY:
                    loaderComponent.setLocation(getClass().getResource("/desktop/details/action/impl/proximity.fxml"));
                    break;
            }

            component = loaderComponent.load();
            actionController = loaderComponent.getController();
            actionController.setDataFromDTO(actionDTO);

            return component;
//            name2envVarController.put(envVarDTO.getName(), envVarController);
//            name2envVarComponent.put(envVarDTO.getName(), component);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
