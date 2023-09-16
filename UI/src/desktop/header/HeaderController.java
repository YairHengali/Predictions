package desktop.header;

import desktop.AppController;
import ex2.ThreadpoolDTO;
import ex2.runningSimulationDTO;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;

public class HeaderController {

    @FXML private Button loadFileBTN;
    @FXML private TextField loadFileTF;
    @FXML
    private Label waitingLabel;

    @FXML
    private Label runningLabel;

    @FXML
    private Label endedLabel;


    private AppController mainController;
    private String lastAccessedFolderPath = "";
    private SimpleStringProperty selectedFileProperty;

    Thread ThreadpoolDataPullingThread;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void initialize(){
        loadFileTF.textProperty().bind(selectedFileProperty);

        ThreadpoolDataPullingThread = new Thread(() -> {
            while (true) {
                ThreadpoolDTO threadpoolData = mainController.getSystemEngine().getThreadpoolData();

                Platform.runLater(() -> {
                    waitingLabel.setText(String.valueOf(threadpoolData.getWaitingSimulations()));
                    runningLabel.setText(String.valueOf(threadpoolData.getRunningSimulations()));
                    endedLabel.setText(String.valueOf(threadpoolData.getEndedSimulations()));
                    });
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
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
            mainController.getSystemEngine().clearPastSimulations(); //TODO: validate that working
            if (!ThreadpoolDataPullingThread.isAlive()){
                ThreadpoolDataPullingThread.start();
            }
            mainController.clearResultsTab();
            mainController.addDataToSimulationTreeView();
            mainController.addDataToEntitiesTable();
            mainController.addDataToEnvVarsTable();
        }
        catch (Exception e) {
            System.out.println(e.getMessage() + System.lineSeparator());
        }
    }


}
