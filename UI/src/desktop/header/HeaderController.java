package desktop.header;

import desktop.AppController;
import ex2.ThreadpoolDTO;
import ex2.runningSimulationDTO;
import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.util.Duration;

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
    @FXML
    private HBox headerHBox;
    @FXML
    private CheckBox animationsCB = new CheckBox();
    private AppController mainController;
    private String lastAccessedFolderPath = "";
    private SimpleStringProperty selectedFileProperty;
    private Circle circle;
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

        circle = new Circle(100, 100, 20);
        headerHBox.getChildren().add(circle);
        circle.setVisible(false);

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

            if(mainController.getIsFileLoaded()){ //already had working one
                mainController.clearResultsTab();
                mainController.getSystemEngine().clearPastSimulations();
            }

            if (!ThreadpoolDataPullingThread.isAlive()){
                ThreadpoolDataPullingThread.start();
            }

            mainController.setIsFileLoaded(true);

            // Update the text field with the selected file path
            selectedFileProperty.set(selectedFile.getAbsolutePath());


            System.out.println("The xml file has loaded successfully!" + System.lineSeparator());

            if(this.animationsCB.isSelected()){
                startFileLoadedAnimation();
            }

            mainController.addDataToSimulationTreeView();
            mainController.addDataToExecutionTab();
            mainController.moveToDetailsTab();

        }
        catch (Exception e) {
            mainController.showPopUpAlert("Invalid xml file", null, e.getMessage());
            System.out.println(e.getMessage() + System.lineSeparator());
        }
    }

    private void startFileLoadedAnimation(){
//        Rectangle rect = new Rectangle(100, 100, 50, 50);
//        rect.setFill(Color.BLUE);
//        headerHBox.getChildren().add(rect);
//        RotateTransition rt = new RotateTransition(Duration.millis(1000), rect);
//        rt.setByAngle(180);
//        rt.play();

        FillTransition ft = new FillTransition(Duration.millis(1000), circle, Color.RED, Color.YELLOW);
        ft.setCycleCount(1);
        ft.setAutoReverse(true);
        circle.setVisible(true);
        ft.play();

    }


//TODO: כשטוענים קובץ  אחד אחרי השני הוא לא מראה את הסימולציה רצה
}
