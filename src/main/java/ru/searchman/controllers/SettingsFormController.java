package ru.searchman.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import ru.searchman.async.methods.BooksFinishedSearchMethod;
import ru.searchman.config.ApplicationEnvironments;
import ru.searchman.services.FragmentsSearchService;
import ru.searchman.services.GUIService;

import java.io.File;
import java.io.IOException;
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
    private RadioButton blackColorRadioButton;
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
    public void initialize() throws IOException {
        ToggleGroup settingsToggleGroup = new ToggleGroup();
        defaultParameterRadioButton.setToggleGroup(settingsToggleGroup);
        customParameterRadioButton.setToggleGroup(settingsToggleGroup);

        ToggleGroup colorToggleGroup = new ToggleGroup();
        blueColorRadioButton.setToggleGroup(colorToggleGroup);
        blackColorRadioButton.setToggleGroup(colorToggleGroup);

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
    private void startSearch() {
        String validate = validateTextFields();
        if (validate.isEmpty()){
            Integer lengthFragment = Integer.valueOf(lengthFragmentTextField.getText());
            Integer countThreads = Integer.valueOf(countThreadLabel.getText());

            this.startSearchButton.setDisable(true);

            List<String> keyWords = new ArrayList<>();
            for (String value: keyWordsTextArea.getText().split(";")) keyWords.add(value);

            FragmentsSearchService service = new FragmentsSearchService(
                    keyWords,
                    lengthFragment,
                    countThreads,
                    new File(mainDirectoryLabel.getText()),
                    new BooksFinishedSearchMethod()
            );
            if (countThreads == 1) service.notParallelSearch();
            else service.startSearch();

            if (blackColorRadioButton.isSelected()) System.out.println("Black");
        }else{
            showDialog(Alert.AlertType.ERROR,"Произошла ошибка",validate);
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
    private void setBlackColorInFoundKeyWord(){
        ApplicationEnvironments.selectedKeyWordColor = Color.BLACK;
    }

    //#@*$&@#*$&(@#!!!
    private String validateTextFields(){
        String errors = "";
        Integer lengthFragment = null;
        try{
            lengthFragment = Integer.valueOf(lengthFragmentTextField.getText());
        }catch (Exception ex){
            if(lengthFragmentTextField.getText().isEmpty())
                errors = errors.concat("Поле \"Длина фрагмента\" не заполнено ");
            else
                errors = errors.concat("Поле \"Длина фрагмента\" заполнено некорректно ");
        }
        if (lengthFragment != null)
            if (lengthFragment < 2)
                errors = errors.concat("Длина фрагмента должна быть больше 2");

        if (keyWordsTextArea.getText().isEmpty())
            if (!errors.isEmpty())
                errors = errors.concat(", поле \"Ключевые слова\" должно быть заполнено ");
            else
                errors = errors.concat("Поле \"Ключевые слова\" должно быть заполнено ");

        return errors;
    }

    private void showDialog(Alert.AlertType type, String title, String text){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }
}
