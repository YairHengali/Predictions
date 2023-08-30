package desktop.execution.envvar.impl;

import desktop.execution.envvar.api.EnvVarControllerAPI;
import engineAnswers.PropertyDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class FloatEnvVarController implements EnvVarControllerAPI {


    @FXML private Label nameLabel;
    @FXML private Label typeLabel;
    @FXML private Label rangeLabel;
    @FXML private TextField valueTextField;

    private Number from;
    private Number to;

    @Override
    public void setDataFromDTO(PropertyDTO envVarDTO) {
        nameLabel.setText(envVarDTO.getName());
        typeLabel.setText(envVarDTO.getType());
        from = envVarDTO.getFrom();
        to = envVarDTO.getTo();
        rangeLabel.setText( from + " to " + to);
    }


    @FXML
    private void initialize(){
        // Add a listener to restrict input to only floating-point numbers
        valueTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("-?\\d*\\.?\\d*")) {
                valueTextField.setText(newValue.replaceAll("[^-?\\d*\\.?\\d*]", ""));
            }
        });


    }

    @Override
    public PropertyDTO createEnvVarDTO(){

        return new PropertyDTO(nameLabel.getText(), typeLabel.getText(), from, to, valueTextField.getText().isEmpty(), valueTextField.getText());

    }
}
