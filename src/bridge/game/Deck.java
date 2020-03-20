package bridge.game;

import java.util.Random;

public class Deck {
    // ========================================== FIELDS ==========================================
    // Card deck, we may assume its length as 52
    private Card[] deck;

    // Random instance
    private static Random generator = new Random();

    // ========================================= METHODS ==========================================
    public Deck() {
        // Card order in deck
        char[] cardOrder = { 'A', 'K', 'Q', 'J', '0', '9', '8', '7', '6', '5', '4', '3', '2' };
        deck = new Card[52];
        for (int i = 0; i < 13; ++i) {
            deck[i] = new Card(cardOrder[i], "SPADES");
            deck[i + 13] = new Card(cardOrder[i], "HEARTS");
            deck[i + 26] = new Card(cardOrder[i], "DIAMONDS");
            deck[i + 39] = new Card(cardOrder[i], "CLUBS");
        }
    }

    // Shuffle the deck
    public void Shuffle() {
        for (int i = 52; i > 1; --i) {
            int swapPosition = generator.nextInt(i);
            Card buffer = deck[i - 1];
            deck[i - 1] = deck[swapPosition];
            deck[swapPosition] = buffer;
        }
    }

    // Deal cards amongst four players
    public Card[][] Deal() {
        Card[][] result = new Card[4][13];
        Shuffle();
        for (int i = 0; i < 52; ++i) {
            result[i / 13][i % 13] = deck[i];
        }
        return result;
    }

    // Get cards as an array
    public Card[] getDeck() { return deck; }

    // Convert deck to string
    @Override
    public String toString() {
        return Deck.toString(deck);
    }

    // ====================================== STATIC METHODS =======================================
    // Users like having decks sorted
    public static Card[] sort(Card[] deck) {
        // Work on a copy of a deck
        Card[] deckCopy = deck.clone();
        
        // Give special ID for every card, which set card order
        int[] cardIDs = new int[deckCopy.length];
        for (int i = 0; i < deckCopy.length; ++i) {
            // This looks a little bit tricky... Fortunately while converted to int: S > H > D > C - this is the color order!
            cardIDs[i] = (int)deckCopy[i].getColor().charAt(0) * 1000 + (int)deckCopy[i].getStrength() * 100 + Character.getNumericValue(deckCopy[i].getValue());
            if (deckCopy[i].getValue() == '0') cardIDs[i] += 10;     // Eh... those tens...
        }

        // Some bubble sort here...
        for (int i = 0; i < deckCopy.length; ++i) {
            for (int j = i + 1; j < deckCopy.length; ++j) {
                if (cardIDs[i] < cardIDs[j]) {
                    int IDBuffer = cardIDs[j];
                    Card cardBuffer = deckCopy[j];
                    cardIDs[j] = cardIDs[i];
                    deckCopy[j] = deckCopy[i];
                    cardIDs[i] = IDBuffer;
                    deckCopy[i] = cardBuffer;
                }
            }
        }

        return deckCopy;
    }

    public static String toString(Card[] hand) {
        String result = "";
        for (Card x: hand) {
            result += x.toString() + " ";
        }
        return result;
    }

    public static double getStrength(Card[] hand) {
        double result = 0.0;
        for (Card x: hand) {
            result += x.getStrength();
        }
        return result;
    }
}