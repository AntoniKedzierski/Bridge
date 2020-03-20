package graphics;

import bridge.bidding.Bid;
import bridge.game.Game;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    public static void main(String[] args) {
        Game g = new Game();
        g.newDeal();

        g.addBid(new Bid("PASS"));
        g.addBid(new Bid("PASS"));
        g.addBid(new Bid("PASS"));
        g.addBid(new Bid(2, "HEARTS"));
        g.addBid(new Bid("DOUBLE"));
        g.addBid(new Bid("REDOUBLE"));
        g.addBid(new Bid(3, "CLUBS"));
        g.addBid(new Bid("PASS"));
        g.addBid(new Bid(5, "CLUBS"));
        g.addBid(new Bid("PASS"));
        g.addBid(new Bid("PASS"));
        g.addBid(new Bid("PASS"));

        System.out.println(g.toString());
        System.out.println(g.biddingSummary());
        launch(args);
    }
}
