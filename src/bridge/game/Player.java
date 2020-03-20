package bridge.game;

public class Player {
    // ============================================= FIELDS ===================================================
    // It is good to know "What's your name?"
    private String name;

    /* NOTE:
     *  Distinguishing IS important. Player obtains certain cards and may play some of those during the game.
     *  You need to save his hand for the future, some users may want to see all hands after the game.
     *  You also could swap givenCards with activeCards and use first while bidding and second while game. */

    // Active cards - those which player can put on the table
    private Card[] activeCards;

    // Player cards - those given on the beginning of a game
    private Card[] givenCards;

    /* Player's hand strength. From my experience using the distribution points often leads to misunderstanding beetwen
       two players. The program does not even take a look at the distribution, not yet. */
    private double handStrength;

    // ============================================== METHODS =================================================
    public Player(String name) {
        this.name = name;
    }

    // Give some cards to the player
    public void giveCards(Card[] hand) {
        activeCards = Deck.sort(hand);
        givenCards = activeCards.clone();
    }

    // Accessors
    public Card[] getActiveCards() { return activeCards; }
    public Card[] getGivenCards() { return givenCards; }
    public String getName() { return name; }
}
