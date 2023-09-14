package desktop.results;

import desktop.AppController;
import desktop.results.simulations.simulationControllerAPI;
import engineAnswers.pastSimulationDTO;
import ex2.runningSimulationDTO;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ResultsController {
    @FXML
    private ListView<pastSimulationDTO> executionList;

    @FXML
    private TextArea textResults;

    @FXML
    private Button pauseBTN;

    @FXML
    private Button stopBTN;

    @FXML
    private Button resumeBTN;

    private AppController mainController;
    Thread dataPullingThread;
    int currentChosenSimulationID = -1;
    boolean isCurrSimulationTerminatesByUser;
    @FXML
    private HBox simulationHBox;

    private Map<Integer, Node> id2simulationComponent = new HashMap<>();
    private Map<Integer, simulationControllerAPI> id2simulationController = new HashMap<>();

    private SimpleBooleanProperty disablePause = new SimpleBooleanProperty(true);
    private SimpleBooleanProperty disableStop = new SimpleBooleanProperty(true);
    private SimpleBooleanProperty disableResume = new SimpleBooleanProperty(true);

    public void addItemToSimulationsList(pastSimulationDTO pastSimulationDTO) { //TO RUN WHEN CLICK RUN
//        executionList.getItems().clear();
//        List<pastSimulationDTO> pastSimulationsDetails = mainController.getSystemEngine().getPastSimulationsDetails();
//        for (pastSimulationDTO pastSimulationDetails : pastSimulationsDetails) {
//            executionList.getItems().add(pastSimulationDetails);
//    }
        executionList.getItems().add(pastSimulationDTO);

    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize(){
//        executionList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//            showSimulationDetails(newValue);
//        });
        this.pauseBTN.disableProperty().bind(disablePause);
        this.resumeBTN.disableProperty().bind(disableResume);
        this.stopBTN.disableProperty().bind(disableStop);

        executionList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setChosenID(newValue);
        });



        dataPullingThread = new Thread(() -> {
            while (true) {
                if (currentChosenSimulationID != -1) { // if there is chosen option
                    runningSimulationDTO testINFO = mainController.getSystemEngine().pullData(currentChosenSimulationID);
                    this.isCurrSimulationTerminatesByUser = testINFO.isTerminateByUser();
                    // Update UI using Platform.runLater()
                    Platform.runLater(() -> {
                        // Update UI with the collected data:
                    showRunningSimulationDetails(testINFO, currentChosenSimulationID);
                        ///TESTING:
                        textResults.setText("Entities Count:\n" +
                                testINFO.getEntityCountDTOS().toString() +
                                System.lineSeparator() + "Current Tick:  " + testINFO.getCurrentTick() +
                                System.lineSeparator() + "Current Seconds:  " + testINFO.getCurrentSeconds() +
                                System.lineSeparator() + "Status:  " + testINFO.getStatus());
                        calcDisableValueToAllBTNs(testINFO.getStatus());
                        ///////////

                        //NEXT MIGHT NEED THIS:
//                    if (simulationInfoDTO.getState() != ENDED){
//                        //INFO ON RUNING SIMULATION
//                    }
//                    else{
//                        //INFO ON ENDED SIMULATION
//                    }
                    });
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    private void calcDisableValueToAllBTNs(String status)
    {
        if(!this.isCurrSimulationTerminatesByUser){
            disableStop.set(true);
        }
        else {
            if (status.equals("TERMINATED") || status.equals("CREATED")) {
                disablePause.set(true);
                disableStop.set(true);
                disableResume.set(true);

            } else if (status.equals("PAUSED")) {
                disablePause.set(true);
                disableStop.set(false);
                disableResume.set(false);
            } else if (status.equals("RUNNING")) {
                disablePause.set(false);
                disableStop.set(false);
                disableResume.set(true);
            }
        }

    }
    private void setChosenID(pastSimulationDTO newValue) {
        if(newValue == null){
            currentChosenSimulationID = -1;
        }
        else{
            this.currentChosenSimulationID = newValue.getId();
            if (!dataPullingThread.isAlive()){
                dataPullingThread.start();
            }
        }
    }

    @FXML
    void pauseButtonPressed(ActionEvent event) {
        mainController.getSystemEngine().pauseSimulation(currentChosenSimulationID);
    }

    @FXML
    void resumeButtonPressed(ActionEvent event) {
        mainController.getSystemEngine().resumeSimulation(currentChosenSimulationID);
    }
    @FXML
    void stopButtonPressed(ActionEvent event) {
        mainController.getSystemEngine().stopSimulation(currentChosenSimulationID);
    }

    private void showRunningSimulationDetails(runningSimulationDTO simulationDTO, int simulationID) {
        if (!id2simulationComponent.containsKey(simulationID)) {
            createRunningSimulationComponent(simulationDTO, simulationID);
        }
        else{
            id2simulationController.get(simulationID).setDataFromDTO(simulationDTO);
        }

        if(!simulationHBox.getChildren().isEmpty())
            simulationHBox.getChildren().clear();

        simulationHBox.getChildren().add(id2simulationComponent.get(simulationID));
    }
    private void createRunningSimulationComponent(runningSimulationDTO simulationDTO, int simulationID)
    {
        try {
            FXMLLoader loaderComponent = new FXMLLoader();
            Node component = null;
            simulationControllerAPI simulationController = null;
            loaderComponent.setLocation(getClass().getResource("/desktop/results/simulations/running/runningProgressedSimulation.fxml"));


            component = loaderComponent.load();
            simulationController = loaderComponent.getController();
            simulationController.setDataFromDTO(simulationDTO);
            id2simulationController.put(simulationID, simulationController);
            id2simulationComponent.put(simulationID, component);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void clearExecutionList() {
        executionList.getItems().clear();
    }
}
