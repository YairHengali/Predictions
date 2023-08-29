package desktop.execution;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import engineAnswers.PropertyDTO;

import javax.naming.InitialContext;

public class BooleanEnvVarController implements EnvVarControllerAPI{


    @FXML private Label nameLabel;
    @FXML private Label typeLabel;
    @FXML private ComboBox<String> valueComboBox;


    @Override
    public void setDataFromDTO(PropertyDTO envVarDTO) {
        nameLabel.setText(envVarDTO.getName());
        typeLabel.setText(envVarDTO.getType());
    }


    @FXML
    private void initialize(){
        valueComboBox.getItems().addAll("Random", "True", "False");
    }

    @Override
    public PropertyDTO createEnvVarDTO(){

        return new PropertyDTO(nameLabel.getText(), typeLabel.getText(), null, null, valueComboBox.getValue().equals("Random"), valueComboBox.getValue().toLowerCase());

    }
}
