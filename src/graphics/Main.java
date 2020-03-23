package graphics;

import bridge.game.Card;
import bridge.game.Deck;
import bridge.game.Game;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.canvas.Canvas;
import tools.Settings;

import java.util.Set;


public class Main extends Application {
    private Game game;
    private Stage window;

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setMinHeight(Settings.minWindowHeight);
        window.setMinWidth(Settings.minWindowWidth);

        window.setScene(new Scene(new StackPane()));

        Settings.windowWidth = window.getWidth();
        Settings.windowHeight = window.getHeight();

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

        // Knowledge base button
        Button knowledgeBase = new Button("Baza wiedzy");

        // Settings button
        Button settings = new Button("Ustawienia");

        // Quit button
        Button quit = new Button("Koniec");

        newGame.setOnAction(event -> {
            Node[] content = { newGame, knowledgeBase, settings, quit, title };
            animateContent(content, "gamePanel");

            // TODO - dialog box z userem

            game = new Game();
            game.newDeal();
        });

        quit.setOnAction(event -> {
            Node[] content = { newGame, knowledgeBase, settings, quit, title };
            animateContent(content, "quit");
        });

        // Add all that stuff to the layout
        layout.getChildren().addAll(title, fakeTitle, newGame, knowledgeBase, settings, quit);
        layout.setAlignment(Pos.CENTER);
        window.setScene(new Scene(layout, 800, 600));
        window.setMaximized(true);
    }

    private void gamePanel() {
        System.out.println("Zaczynamy grę!");
        double height = window.getScene().getHeight();
        double width = window.getScene().getWidth();

        // Update the settings
        Settings.cardSize = height / 4.0;
        Settings.cardHeight = Settings.cardSize;
        Settings.cardWidth = 2 / 3.0 * Settings.cardSize;

        GridPane layout = new GridPane();
        layout.setStyle("-fx-background-color: radial-gradient(center 50% 50% , radius 100% , #417025, #1f3b2c);");

        boolean[] openCards = { false, false, true, false };
        Canvas canvas = renderCards(openCards);
        GridPane.setConstraints(canvas, 0, 0, 3, 1);
        layout.getChildren().add(canvas);



        window.setScene(new Scene(layout, width, height));
    }

    private void animateContent(Node[] nodes, String action) {
        for (int i = 0; i < nodes.length - 1; ++i) {
            TranslateTransition transition = new TranslateTransition(Duration.millis(500), nodes[i]);
            if (i % 2 == 0)transition.setToX(window.getWidth());
            else transition.setToX(-window.getWidth());
            transition.play();
        }
        TranslateTransition transition = new TranslateTransition(Duration.millis(500), nodes[nodes.length - 1]);
        transition.setToY(-window.getHeight());
        transition.setOnFinished(event -> {
            if (action.equals("gamePanel")) gamePanel();
            if (action.equals("quit")) window.close();
        });
        transition.play();
    }

    // Render cards
    private Canvas renderCards(boolean[] openCards) {
        // Calculate size and cords
        double width = window.getScene().getWidth() * Settings.cardTableRatio;
        double height = window.getScene().getHeight();

        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Params
        double radius = Settings.cardSize;

        // North:
        double northPitchAngle = 180.0;
        Card[] northHand = game.getPlayerCards(0);
        Image[] hand = new Image[northHand.length];
        for (int i = 0; i < northHand.length; ++i) {
            // Get .png file
            String path = path = northHand[i].getPathToBack();
            if (openCards[0]) path = northHand[i].getPathToFace();
            hand[i] = new Image(path);

            double angle = -Settings.cardSpanAngle + (2 * i * Settings.cardSpanAngle / northHand.length) + Settings.cardSpanPitch + northPitchAngle;
            double x = 0.5 * width - 0.5 * radius + radius * Math.sin(Math.toRadians(angle));
            double y = -radius - radius * Math.cos(Math.toRadians(angle));

            drawRotatedImage(gc, hand[i], angle, x, y, Settings.cardWidth, Settings.cardHeight);
        }

        // East:
        double eastPitchAngle = 270;
        Card[] eastHand = game.getPlayerCards(1);
        hand = new Image[eastHand.length];
        for (int i = 0; i < eastHand.length; ++i) {
            // Get .png file
            String path = path = eastHand[i].getPathToBack();
            if (openCards[1]) path = eastHand[i].getPathToFace();
            hand[i] = new Image(path);

            double angle = -Settings.cardSpanAngle + (2 * i * Settings.cardSpanAngle / eastHand.length) + Settings.cardSpanPitch + eastPitchAngle;
            double x = width + radius * Math.sin(Math.toRadians(angle));
            double y = 0.5 * height - 0.5 * radius - radius * Math.cos(Math.toRadians(angle));

            drawRotatedImage(gc, hand[i], angle, x, y, Settings.cardWidth, Settings.cardHeight);
        }
        
        // South:
        Card[] southHand = game.getPlayerCards(1);
        hand = new Image[southHand.length];
        for (int i = 0; i < southHand.length; ++i) {
            // Get .png file
            String path = path = southHand[i].getPathToBack();
            if (openCards[2]) path = southHand[i].getPathToFace();
            hand[i] = new Image(path);

            double angle = -Settings.cardSpanAngle + (2 * i * Settings.cardSpanAngle / southHand.length) + Settings.cardSpanPitch;
            double x = 0.5 * width - 0.5 * radius + radius * Math.sin(Math.toRadians(angle));
            double y = canvas.getHeight() - 10 - radius * Math.cos(Math.toRadians(angle));

            drawRotatedImage(gc, hand[i], angle, x, y, Settings.cardWidth, Settings.cardHeight);
        }

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Windlass", 16));
        if (Settings.showSouthPC) gc.fillText("Hand PC: " + (int)Deck.getStrength(southHand), width * 0.25, height * 0.75);
        
        // West:
        double westPitchAngle = 90;
        Card[] westHand = game.getPlayerCards(1);
        hand = new Image[westHand.length];
        for (int i = 0; i < westHand.length; ++i) {
            // Get .png file
            String path = path = westHand[i].getPathToBack();
            if (openCards[3]) path = westHand[i].getPathToFace();
            hand[i] = new Image(path);

            double angle = -Settings.cardSpanAngle + (2 * i * Settings.cardSpanAngle / westHand.length) + Settings.cardSpanPitch + westPitchAngle;
            double x = -radius + radius * Math.sin(Math.toRadians(angle));
            double y = 0.5 * height - 0.5 * radius - radius * Math.cos(Math.toRadians(angle));

            drawRotatedImage(gc, hand[i], angle, x, y, Settings.cardWidth, Settings.cardHeight);
        }

        return canvas;
    }

    // ========================================= TOOLS ===============================================
    // Rotate function
    private void rotate(GraphicsContext gc, double angle, double px, double py) {
        Rotate r = new Rotate(angle, px, py);
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
    }

    // @param tlpx is translation the rotation center
    private void drawRotatedImage(GraphicsContext gc, Image image, double angle, double tlpx, double tlpy, double sx, double sy) {
        gc.save(); // saves the current state on stack, including the current transform
        rotate(gc, angle, tlpx + sx / 2, tlpy + sy / 2);
        gc.drawImage(image, tlpx, tlpy, sx, sy);
        gc.restore(); // back to original state (before rotation)
    }
}
