package graphics;

import bridge.game.Game;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class Main extends Application {
    private Game game;
    private Stage window;

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setMinHeight(600);
        window.setMinWidth(800);

        // Load resources
        Font.loadFont(Main.class.getResourceAsStream("styles/Windlass.ttf"), 60);

        // Run start menu
        startMenu();

        window.setTitle("Bridge");
        window.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void startMenu() {
        VBox layout = new VBox(12);
        layout.getStylesheets().add(getClass().getResource("styles/menuWindow.css").toExternalForm());

        // Label
        Label title = new Label("The King of Spades");

        // Some spacing downwards the title
        Label fakeTitle = new Label(" ");
        fakeTitle.setFont(Font.font("Segoe Print", 20));


        // New game button
        Button newGame = new Button("Nowa gra");
        newGame.setMinWidth(450);
        newGame.setMinHeight(50);
        newGame.setOnAction(event -> {
            gamePanel();
        });

        // Knowledge base button
        Button knowledgeBase = new Button("Baza wiedzy");
        knowledgeBase.setMinWidth(450);
        knowledgeBase.setMinHeight(50);

        // Settings button
        Button settings = new Button("Ustawienia");
        settings.setMinWidth(450);
        settings.setMinHeight(50);

        // Quit button
        Button quit = new Button("Koniec");
        quit.setMinWidth(450);
        quit.setMinHeight(50);
        quit.setOnAction(event -> window.close());

        // Add all that stuff to the layout
        layout.getChildren().addAll(title, fakeTitle, newGame, knowledgeBase, settings, quit);
        layout.setAlignment(Pos.CENTER);
        window.setScene(new Scene(layout, 800, 600));
        window.setMaximized(true);
    }

    private void gamePanel() {
        System.out.println("Zaczynamy grę!");
    }
}
