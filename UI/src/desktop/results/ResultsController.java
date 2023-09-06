package desktop.results;

import desktop.AppController;
import engineAnswers.EntityCountDTO;
import engineAnswers.EntityDTO;
import engineAnswers.PropertyDTO;
import engineAnswers.pastSimulationDTO;
import ex2.runningSimulationDTO;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

import java.util.List;

public class ResultsController {
    @FXML
    private ListView<pastSimulationDTO> executionList;

    @FXML
    private TextArea textResults;


    public void updateSimulationsList() { //TO RUN WHEN CLICK RUN
        executionList.getItems().clear();
        List<pastSimulationDTO> pastSimulationsDetails = mainController.getSystemEngine().getPastSimulationsDetails();
        for (pastSimulationDTO pastSimulationDetails : pastSimulationsDetails) {
            executionList.getItems().add(pastSimulationDetails);
        }
    }

    private AppController mainController;
    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    Thread dataPullingThread;
    int currentChosenSimulationID;

    public void initialize()
    {
//        executionList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//            showSimulationDetails(newValue);
//        });

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
                                    System.lineSeparator() + "Current Seconds:  " + testINFO.getCurrentSeconds());
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

}
