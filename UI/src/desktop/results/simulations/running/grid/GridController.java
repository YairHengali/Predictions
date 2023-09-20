package desktop.results.simulations.running.grid;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.util.HashMap;
import java.util.Map;

public class GridController {
    Map<String,String> name2Style = new HashMap<>();

    @FXML
    private AnchorPane anchorPane;
    GridPane gridPane = new GridPane();
    public void addLabels(){

        gridPane = new GridPane();
        for (int i = 0; i < 10; i++) {
            Label label = new Label("   ");
            label.setStyle("-fx-background-color: blue;");
            gridPane.add(label, i, i);
        }

        anchorPane.getChildren().add(gridPane);

    }
//    private void addEntityToGrid(EntityDTO entityDTO){
//        Label label = new Label(entityDTO.getName());
//        label.setStyle(name2Style.get(entityDTO.getName()));
//        gridPane.add(label, 10, 10);
////        name2Style(entityDTO.getName())) {
////            label.setStyle("-fx-background-color: red;");
////        } else if ("Type Y".equals(entityType)) {
////            label.setStyle("-fx-background-color: blue;");
////        }
//    }
}
