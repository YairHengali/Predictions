package desktop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

/////////////desktop main:
public class Main extends Application {

    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("predictions");

        Parent load = FXMLLoader.load(getClass().getResource("Predictions-Ex2-FXML.fxml"));

        //set stage
        Scene scene = new Scene(load);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
