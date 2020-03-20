package bridge.game;

import bridge.bidding.Bid;
import bridge.bidding.Bidding;
import tools.Settings;

public class Game {
    // ============================================ CLASSES ==================================================
    private class GameStatus {
        int turn = 0;
        int dealer = 0;
        boolean isDoubled = false;
        boolean isRedoubled = false;
    }

    // ============================================ FIELDS ====================================================
    // There is no game without players (hmmm, always 4 players)
    Player[] players = new Player[4];

    /* The game should store many dynamically changed values, i.e. gameStatus, isDoubled, playerTurn etc.
       Here comes a metaclass with these values as fields. */
    GameStatus gameStatus = new GameStatus();

    // Need to have one instance of card deck class
    Deck deck = new Deck();

    // The game has a bidding as well
    Bidding bidding = new Bidding(gameStatus.dealer);

    // ============================================ METHODS ===================================================
    public Game() {
        for (int i = 0; i < 4; ++i) {
            players[i] = new Player(Settings.falenicaNames[i]);
        }
    }

    // Deal cards to player again
    public void newDeal() {
        Card[][] deal = deck.Deal();
        for (int i = 0; i < 4; ++i) {
            players[i].giveCards(deal[i]);
        }
    }

    // Comunicate between app interface and bidding
    public boolean addBid(Bid bid) {
        return bidding.addBid(bid);
    }

    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < 4; ++i) {
            result += "[" + players[i].getName() + "] " + Deck.toString(players[i].getGivenCards()) + "\n";
        }
        return result;
    }

    // Bidding summary
    public String biddingSummary() {
        return bidding.toString();
    }
}
