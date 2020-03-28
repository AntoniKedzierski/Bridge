package graphics;

import bridge.bidding.Bid;
import bridge.game.Card;
import bridge.game.Deck;
import bridge.game.Game;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
import tools.Row;
import tools.Settings;

import java.util.Set;
import java.util.Vector;


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
        window.setScene(new Scene(layout, 1200, 800));
        // window.setMaximized(true);
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

        boolean[] openCards = { true, true, true, true };
        Canvas canvas = renderCards(openCards);
        canvas.setId("CARDS");
        GridPane.setConstraints(canvas, 0, 0, 3, 1);
        layout.getChildren().add(canvas);

        drawAuctionPanel(layout);

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
        double width = window.getScene().getWidth() * 0.75;
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
        Card[] southHand = game.getPlayerCards(2);
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
        if (Settings.showSouthPC) gc.fillText("Hand PC: " + (int)Deck.getStrength(southHand), 10, 40);
        gc.fillText("Dealer: " + game.getDealer(), 10, 20);
        
        // West:
        double westPitchAngle = 90;
        Card[] westHand = game.getPlayerCards(3);
        hand = new Image[westHand.length];
        for (int i = 0; i < westHand.length; ++i) {
            // Get .png file
            String path = path = westHand[i].getPathToBack();
            if (openCards[3]) path = westHand[i].getPathToFace();
            hand[i] = new Image(path);

            double angle = -Settings.cardSpanAngle + (2 * i * Settings.cardSpanAngle / westHand.length) + Settings.cardSpanPitch + westPitchAngle;
            double x = - radius + radius * Math.sin(Math.toRadians(angle));
            double y = 0.5 * height - 0.5 * radius - radius * Math.cos(Math.toRadians(angle));

            drawRotatedImage(gc, hand[i], angle, x, y, Settings.cardWidth, Settings.cardHeight);
        }

        return canvas;
    }

    private void drawAuctionPanel(GridPane presentGrid) {
        if (!game.inBidding()) {
            if (!game.inGame()) return;

            VBox leftPanel = new VBox(10);
            leftPanel.getStylesheets().add(getClass().getResource("styles/auctionPanel.css").toExternalForm());
            leftPanel.setPadding(new Insets(5));

            TableView history = createBiddingTable();
            final ObservableList<Row> data = FXCollections.observableArrayList();
            String[][] biddingHistory = game.getBiddingHistory();

            for (int i = 0; i < biddingHistory.length; ++i) {
                data.add(new Row(biddingHistory[i]));
            }

            history.setItems(data);

            leftPanel.getChildren().add(history);

            GridPane.setConstraints(leftPanel, 3, 0);
            presentGrid.getChildren().add(leftPanel);
            return;
        }

        GridPane buttons = new GridPane();
        buttons.getStylesheets().add(getClass().getResource("styles/auctionPanel.css").toExternalForm());
        buttons.setHgap(5);
        buttons.setVgap(5);
        Vector<Bid> activeButtons = game.getAvailableBids();

        double minWidth = 0.25 * window.getScene().getWidth() - 30;
        buttons.setPadding(new Insets(5));

        String[] colors = { "CLUBS", "DIAMONDS", "HEARTS", "SPADES", "NOTRUMP" };
        Button[] bids = new Button[35];

        for (int level = 1; level < 8; level++) {
            for (int i = 0; i < 5; ++i) {
                final Bid bid = new Bid(level, colors[i]);
                bids[(level - 1) * 5 + i] = new Button(bid.toStringNoColor());

                bids[(level - 1) * 5 + i].setDisable(true);
                for (Bid active : activeButtons) {
                    if (active.getColor().equals(colors[i])) {
                        if (active.getLevel() == level) {
                            bids[(level - 1) * 5 + i].setDisable(false);
                            break;
                        }
                    }
                }

                bids[(level - 1) * 5 + i].setOnAction(event -> {
                    game.addBid(bid);
                    presentGrid.getChildren().remove(buttons);
                    drawAuctionPanel(presentGrid);
                });


                bids[(level - 1) * 5 + i].setMinWidth(minWidth / 5.0);
                GridPane.setConstraints(bids[(level - 1) * 5 + i], i, level - 1);
                buttons.getChildren().add(bids[(level - 1) * 5 + i]);
            }
        }


        Button pass = new Button("PASS");
        pass.setOnAction(event -> {
            if (!game.addBid(new Bid("PASS"))) {
                presentGrid.getChildren().removeAll();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Game aborted!");
                alert.setHeaderText("All players passed");
                alert.showAndWait();

                gamePanel();
            }
            presentGrid.getChildren().remove(buttons);
            drawAuctionPanel(presentGrid);
        });

        pass.setMinWidth(3 * minWidth / 5.0 + 10);
        GridPane.setConstraints(pass, 1, 7, 3, 1);
        buttons.getChildren().add(pass);



        Button doubleBtn = new Button("X");
        doubleBtn.setOnAction(event -> {
            game.addBid(new Bid("DOUBLE"));
            presentGrid.getChildren().remove(buttons);
            drawAuctionPanel(presentGrid);
        });

        if (!game.mayDouble()) doubleBtn.setDisable(true);

        doubleBtn.setMinWidth(minWidth / 5.0);
        GridPane.setConstraints(doubleBtn, 0, 7);
        buttons.getChildren().add(doubleBtn);



        Button redoubleBtn = new Button("XX");
        redoubleBtn.setOnAction(event -> {
            game.addBid(new Bid("REDOUBLE"));
            presentGrid.getChildren().remove(buttons);
            drawAuctionPanel(presentGrid);
        });

        if (!game.mayRedouble()) redoubleBtn.setDisable(true);

        redoubleBtn.setMinWidth(minWidth / 5.0);
        GridPane.setConstraints(redoubleBtn, 4, 7);
        buttons.getChildren().add(redoubleBtn);

        TableView history = createBiddingTable();
        final ObservableList<Row> data = FXCollections.observableArrayList();
        String[][] biddingHistory = game.getBiddingHistory();

        for (int i = 0; i < biddingHistory.length; ++i) {
            data.add(new Row(biddingHistory[i]));
        }

        history.setItems(data);

        GridPane.setConstraints(history, 0, 8, 5, 1);
        buttons.getChildren().add(history);

        GridPane.setConstraints(buttons, 3, 0);
        presentGrid.getChildren().add(buttons);
    }

    private TableView<Row> createBiddingTable() {
        double minWidth = 0.25 * window.getScene().getWidth() - 10;
        double minHeight = 144;

        TableView table = new TableView();
        table.setEditable(false);
        table.setPrefSize(minWidth, minHeight);
        table.setMaxHeight(400);

        TableColumn north = new TableColumn("North");
        TableColumn east = new TableColumn("East");
        TableColumn south = new TableColumn("South");
        TableColumn west = new TableColumn("West");

        north.setPrefWidth(minWidth / 4.0);
        north.setCellValueFactory(new PropertyValueFactory<Row, String>("north"));

        east.setPrefWidth(minWidth / 4.0);
        east.setCellValueFactory(new PropertyValueFactory<Row, String>("east"));

        south.setPrefWidth(minWidth / 4.0);
        south.setCellValueFactory(new PropertyValueFactory<Row, String>("south"));

        west.setPrefWidth(minWidth / 4.0);
        west.setCellValueFactory(new PropertyValueFactory<Row, String>("west"));

        table.getColumns().addAll(north, east, south, west);

        return table;
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
