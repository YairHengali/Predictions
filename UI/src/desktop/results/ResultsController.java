package desktop.results;

import desktop.AppController;
import engineAnswers.EntityDTO;
import engineAnswers.PropertyDTO;
import engineAnswers.pastSimulationDTO;
import ex2.runningSimulationDTO;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import java.util.List;

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

    private AppController mainController;
    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    Thread dataPullingThread;
    int currentChosenSimulationID;

    public ResultsController() {


    }

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
                runningSimulationDTO testINFO = mainController.getSystemEngine().pullData(currentChosenSimulationID);
                // Update UI using Platform.runLater()
                Platform.runLater(() -> {
                    // Update UI with the collected data:

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
        });
    }

    private void calcDisableValueToAllBTNs(String status)
    {
        if(status.equals("TERMINATED") || status.equals("CREATED")){
            disablePause.set(true);
            disableStop.set(true);
            disableResume.set(true);

        }
        else if(status.equals("PAUSED")) {
            disablePause.set(true);
            disableStop.set(false);
            disableResume.set(false);
        }
        else if (status.equals("RUNNING")) {
            disablePause.set(false);
            disableStop.set(false);
            disableResume.set(true);
        }

    }
    private void setChosenID(pastSimulationDTO newValue) {
        this.currentChosenSimulationID = newValue.getId();
        if (!dataPullingThread.isAlive()){
            dataPullingThread.start();
        }
    }

    private void showSimulationDetails(pastSimulationDTO newValue) {
        textResults.setText("Entities Count:\n" +
                mainController.getSystemEngine().getPastSimulationEntityCount(newValue).toString() +
                "Properties Histogram:\n");

        List<EntityDTO> pastSimulationEntitiesDTO = mainController.getSystemEngine().getPastSimulationEntitiesDTO(newValue);
        for (EntityDTO entityDTO: pastSimulationEntitiesDTO) {
            for (PropertyDTO propertyDTO: entityDTO.getProperties()) {
                textResults.appendText(mainController.getSystemEngine().getHistogram(newValue.getId(),entityDTO.getName(), propertyDTO.getName()).toString());
                textResults.appendText(System.lineSeparator());
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
}
