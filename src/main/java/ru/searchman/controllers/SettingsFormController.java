package ru.searchman.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import ru.searchman.async.methods.BooksFinishedSearchMethod;
import ru.searchman.config.ApplicationEnvironments;
import ru.searchman.services.FragmentsSearchService;
import ru.searchman.services.GUIService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
    private Label mainDirectoryLabel;
    @FXML
    private Button startSearchButton;
    @FXML
    private TextField lengthFragmentTextField;
    @FXML
    private TextArea keyWordsTextArea;

    public static GUIService service;

    @FXML
    public void initialize(){
        ToggleGroup settingsToggleGroup = new ToggleGroup();
        defaultParameterRadioButton.setToggleGroup(settingsToggleGroup);
        customParameterRadioButton.setToggleGroup(settingsToggleGroup);

        ToggleGroup colorToggleGroup = new ToggleGroup();
        blueColorRadioButton.setToggleGroup(colorToggleGroup);
        yellowColorRadioButton.setToggleGroup(colorToggleGroup);

        service = new GUIService(startSearchButton);
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
    private void choiceMainDirectory(){
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        File dir = directoryChooser.showDialog(null);
        if (dir != null){
            mainDirectoryLabel.setText(dir.getAbsolutePath());
            startSearchButton.setDisable(false);
        }else{
            startSearchButton.setDisable(true);
        }
    }

    @FXML
    private void startSearch() throws Exception {
        /*System.out.println("Length fragment " + lengthFragmentTextField.getText());
        System.out.println("Keywords: ");
        for (String value: keyWordsTextArea.getText().split(";")){
            System.out.println(value);
        }
        System.out.println("Threads: " + countThreadLabel.getText());
        if (yellowColorRadioButton.isSelected()) System.out.println("Yellow");
        else System.out.println("Blue");*/
        this.startSearchButton.setDisable(true);
        List<String> keyWords = new ArrayList<>();
        for (String value: keyWordsTextArea.getText().split(";")) keyWords.add(value);

        FragmentsSearchService service = new FragmentsSearchService(
              keyWords,5,2,new File(mainDirectoryLabel.getText()),new BooksFinishedSearchMethod()
        );
        service.startSearch();
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
