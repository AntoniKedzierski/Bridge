package bridge.game;

import bridge.bidding.Bid;
import bridge.bidding.Bidding;
import tools.Settings;

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
    private Bidding bidding = new Bidding(gameStatus.dealer);

    // ============================================ METHODS ===================================================
    public Game() {
        for (int i = 0; i < 4; ++i) {
            players[i] = new Player(Settings.falenicaNames[i]);
        }
    }

    // Deal cards to player again, change the dealer and start new bidding
    public void newDeal() {
        Card[][] deal = deck.Deal();
        for (int i = 0; i < 4; ++i) {
            players[i].giveCards(deal[i]);
        }
        gameState = GAME_STATE.BIDDING;
        gameStatus.dealer = (gameStatus.dealer + 1) % 4;
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

    // Bidding summary
    public String biddingSummary() {
        return bidding.toString();
    }
}
