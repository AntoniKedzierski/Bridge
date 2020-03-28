package bridge.game;

import bridge.bidding.Bid;
import bridge.bidding.Bidding;
import javafx.collections.ObservableList;
import tools.Row;
import tools.Settings;

import java.util.Vector;

public class Game {
    // ============================================ CLASSES ==================================================
    private class GameStatus {
        int turn = 0;
        int dealer = -1;
        boolean isDoubled = false;
        boolean isRedoubled = false;
    }

    private enum GAME_STATE { NONE, BIDDING, GAME }

    // ============================================ FIELDS ====================================================
    // There is no game without players (hmmm, always 4 players)
    private Player[] players = new Player[4];

    /* The game should store many dynamically changed values, i.e. gameStatus, isDoubled, playerTurn etc.
       Here comes a metaclass with these values as fields. */
    private GameStatus gameStatus = new GameStatus();
    private GAME_STATE gameState = GAME_STATE.NONE;

    // Need to have one instance of card deck class
    private Deck deck = new Deck();

    // The game has a bidding as well
    private Bidding bidding;

    // ============================================ METHODS ===================================================
    public Game() {
        for (int i = 0; i < 4; ++i) {
            players[i] = new Player(Settings.falenicaNames[i]);
        }
    }

    public GAME_STATE getGameState() {
        return gameState;
    }

    // Deal cards to player again, change the dealer and start new bidding
    public void newDeal() {
        Card[][] deal = deck.Deal();
        for (int i = 0; i < 4; ++i) {
            players[i].giveCards(deal[i]);
        }
        gameState = GAME_STATE.BIDDING;
        gameStatus.dealer = (gameStatus.dealer + 1) % 4;
        bidding = new Bidding(gameStatus.dealer);
        //startBidding();
    }

    // Start bidding
    private void startBidding() {
        bidding = new Bidding(gameStatus.dealer);
    }

    // Communicate between app interface and bidding. Returns true if the game awaits for next bid, false if the bidding has ended.
    public boolean addBid(Bid bid) {
        boolean isCorrect = bidding.addBid(bid);
        if (bidding.checkBidding()) {
            System.out.println(bidding.getWinningContract());
            if (bidding.passed()) {
                this.newDeal();
                return false;
            }
            gameState = GAME_STATE.GAME;
        }
        return true;
    }

    // Accessors to fields
    public Card[] getPlayerCards(int player) {
        return players[player].getActiveCards();
    }

    public Card[] getPlayerGivenCards(int player) {
        return players[player].getGivenCards();
    }

    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < 4; ++i) {
            result += "[" + players[i].getName() + "] " + Deck.toString(players[i].getGivenCards()) + "\n";
        }
        return result;
    }

    // BiddingModule summary
    public String biddingSummary() {
        return bidding.toString();
    }

    public Vector<Bid> getAvailableBids() {
        return bidding.getPossibleBids();
    }

    public boolean mayDouble() {
        return bidding.checkBidCorrectness(new Bid("DOUBLE"));
    }

    public boolean mayRedouble() {
        return bidding.checkBidCorrectness(new Bid("REDOUBLE"));
    }

    public boolean inBidding() {
        return gameState == GAME_STATE.BIDDING;
    }

    public boolean inGame() {
        return gameState == GAME_STATE.GAME;
    }

    public String[][] getBiddingHistory() {
        return bidding.generateHistory();
    }
}
