package ru.searchman.services;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.fxmisc.richtext.InlineCssTextArea;
import org.fxmisc.richtext.StyledTextArea;
import ru.searchman.Main;
import ru.searchman.controllers.FragmentFormController;
import ru.searchman.models.BookFragment;

import java.util.List;


@Data
@AllArgsConstructor
public class GUIService {
    private Button searchButton;

    public void showSecondForm(String time, Integer countFragments, List<BookFragment> fragments){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("FragmentsViewForm.fxml"));
            Parent root = loader.load();

            FragmentFormController controller = loader.getController();
            controller.initData(time,countFragments,fragments);

            createFragmentStage(root).show();
        }catch (Exception ex){
            ex.printStackTrace();
            System.out.println("Ex: " + ex.getMessage());
        }
    }

    private Stage createFragmentStage(Parent root){
        Stage stage = new Stage();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();

        stage.setX((primScreenBounds.getWidth() - 570) / 2);
        stage.setY((primScreenBounds.getHeight() - 583) / 2);
        stage.setTitle("Результаты поиска");
        stage.setResizable(false);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Main.class.getResource("/css/form-fragment-style.css").toExternalForm());
        stage.setScene(scene);
        return stage;
    }
}
