package graphics;

import bridge.bidding.Bid;
import bridge.game.Game;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tools.AlertBox;

import java.util.Stack;


public class Main extends Application {
    private Stage window;

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;

        StackPane layout = new StackPane();
        Button button = new Button("Click me!");
        button.setOnAction(event -> AlertBox.display("Hey boomer", "Don't forget the coffee!"));
        layout.getChildren().add(button);

        window.setScene(new Scene(layout, 600, 400));
        window.setTitle("Hello World!");
        window.show();
    }

    public static void main(String[] args) {
        Game g = new Game();
        launch(args);
    }
}
