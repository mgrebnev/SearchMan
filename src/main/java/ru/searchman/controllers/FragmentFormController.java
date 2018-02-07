package ru.searchman.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import lombok.Data;
import org.fxmisc.richtext.InlineCssTextArea;
import org.fxmisc.richtext.StyledTextArea;
import ru.searchman.config.ApplicationEnvironments;
import ru.searchman.models.BookFragment;
import java.util.List;

@Data
public class FragmentFormController {
    @FXML
    private String elapsedTime;
    @FXML
    private Integer countFragments;
    @FXML
    private List<BookFragment> fragments;

    @FXML
    private Label elapsedTimeLabel;
    @FXML
    private Label countFragmentsLabel;
    @FXML
    private ChoiceBox<BookFragment> fragmentsChoiceBox;
    @FXML
    private InlineCssTextArea fragmentTextArea;

    @FXML
    public void initialize(){
        fragmentsChoiceBox.getSelectionModel().selectedItemProperty().addListener(o -> this.choiceFragment());
    }

    public void initData(String time, Integer countFragments, List<BookFragment> fragments){
        this.setElapsedTime(time);
        this.setCountFragments(countFragments);
        this.setFragments(fragments);
        this.initGUI();
    }

    public void initGUI(){
        this.elapsedTimeLabel.setText("Затраченное время: " + elapsedTime + " сек.");
        this.countFragmentsLabel.setText("Количество найденных фрагментов: " + countFragments.toString());
        fragmentTextArea.setWrapText(true);
        ObservableList<BookFragment> items = FXCollections.observableList(fragments);
        this.fragmentsChoiceBox.setItems(items);
        if (items.size() > 0) this.fragmentsChoiceBox.setValue(items.get(0));
        else fragmentsChoiceBox.setDisable(true);
    }

    public void choiceFragment(){
        fragmentTextArea.clear();
        BookFragment item = fragmentsChoiceBox.getSelectionModel().getSelectedItem();
        List<String> text = item.getTextFragment();
        for (int i = 0; i < text.size(); i++){
            String currentLine = text.get(i);
            fragmentTextArea.appendText(currentLine + "\n");
            if (currentLine.contains("[") && currentLine.contains("]")){
                String color = "black";
                if (ApplicationEnvironments.selectedKeyWordColor == Color.AQUA) color = "#003d99";
                fragmentTextArea.setStyle(
                        i,currentLine.indexOf("["),currentLine.indexOf("]"),
                        "-fx-font-weight: bold;-fx-fill: " + color + ";"
                );
            }

            //fragmentTextArea.setText(fragmentTextArea.getText() + line + "\n");
        }
    }
}
