package desktop.results;

import desktop.AppController;
import engineAnswers.EntityCountDTO;
import engineAnswers.EntityDTO;
import engineAnswers.PropertyDTO;
import engineAnswers.pastSimulationDTO;
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

    @FXML
    private Button showBTN;

    @FXML
    void showResultsTesting(ActionEvent event) {
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

    public void initialize()
    {
        executionList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            showSimulationDetails(newValue);
        });

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
