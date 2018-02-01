package ru.searchman.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import ru.searchman.config.ApplicationEnvironments;

public class SettingsFormController {
    @FXML
    private Slider choiceCountThreadSlider;
    @FXML
    private Label countThreadLabel;
    @FXML
    private RadioButton defaultParameterRadioButton;
    @FXML
    private RadioButton customParameterRadioButton;
    @FXML
    private RadioButton blueColorRadioButton;
    @FXML
    private RadioButton yellowColorRadioButton;

    @FXML
    public void initialize(){
        ToggleGroup settingsToggleGroup = new ToggleGroup();
        defaultParameterRadioButton.setToggleGroup(settingsToggleGroup);
        customParameterRadioButton.setToggleGroup(settingsToggleGroup);

        ToggleGroup colorToggleGroup = new ToggleGroup();
        blueColorRadioButton.setToggleGroup(colorToggleGroup);
        yellowColorRadioButton.setToggleGroup(colorToggleGroup);
    }

    @FXML
    public void changeChoiceCountThreadSlider(){
        double defaultPart = 100 / ApplicationEnvironments.maxCountThread;
        double currentValue = choiceCountThreadSlider.getValue();
        for (int i = 1; i <= ApplicationEnvironments.maxCountThread; i++){
            if (currentValue <= (i * defaultPart) && currentValue >= ((i - 1) * defaultPart)){
                countThreadLabel.setText(String.valueOf(i));
                break;
            }else{
                //?#$@$@#
                if (100 - currentValue <= defaultPart){
                    countThreadLabel.setText(String.valueOf(ApplicationEnvironments.maxCountThread));
                    break;
                }
            }
        }
    }

    @FXML
    private void setDefaultThreadSettings(){
        choiceCountThreadSlider.setValue(0.0);
        choiceCountThreadSlider.setDisable(true);
        countThreadLabel.setText("4");
    }

    @FXML
    private void setCustomThreadSettings(){
        choiceCountThreadSlider.setValue(0.0);
        choiceCountThreadSlider.setDisable(false);
        countThreadLabel.setText("1");
    }

    @FXML
    private void setBlueColorInFoundKeyWord(){
        ApplicationEnvironments.selectedKeyWordColor = Color.AQUA;
    }

    @FXML
    private void setYellowColorInFoundKeyWord(){
        ApplicationEnvironments.selectedKeyWordColor = Color.YELLOW;
    }
}
