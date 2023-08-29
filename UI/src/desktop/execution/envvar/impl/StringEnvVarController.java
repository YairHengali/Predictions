package desktop.execution.envvar.impl;

import desktop.execution.envvar.api.EnvVarControllerAPI;
import engineAnswers.PropertyDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class StringEnvVarController implements EnvVarControllerAPI {

    @FXML
    private Label nameLabel;
    @FXML private Label typeLabel;
    @FXML private TextField valueTextField;


    @Override
    public void setDataFromDTO(PropertyDTO envVarDTO) {
        nameLabel.setText(envVarDTO.getName());
        typeLabel.setText(envVarDTO.getType());
    }


    @FXML
    private void initialize(){
        // Add a listener to restrict input to a custom set of characters
        valueTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z0-9 !?,\\-_.()]*")) {
                valueTextField.setText(newValue.replaceAll("[^a-zA-Z0-9 !?,\\-_.()]", ""));
            }
        });


    }

    @Override
    public PropertyDTO createEnvVarDTO(){

        return new PropertyDTO(nameLabel.getText(), typeLabel.getText(), null, null, valueTextField.getText().equals(""), valueTextField.getText());

    }
}
