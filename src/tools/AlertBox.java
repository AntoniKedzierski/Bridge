package tools;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AlertBox {
    public static void display(String title, String message) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        window.setMinHeight(100);
        window.initStyle(StageStyle.UNIFIED);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(8);
        grid.setVgap(8);

        Label userName = new Label("Username:");
        GridPane.setConstraints(userName, 0, 0);

        TextField nameInput = new TextField();
        nameInput.setPromptText("Enter");
        GridPane.setConstraints(nameInput, 1, 0);

        grid.getChildren().addAll(userName, nameInput);

        window.setScene(new Scene(grid, 600, 400));
        window.showAndWait();
    }
}
