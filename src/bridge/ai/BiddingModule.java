package bridge.ai;

import bridge.bidding.Bid;
import bridge.game.Card;
import bridge.game.Deck;
import tools.Map;

import java.util.Vector;

public class BiddingModule {
    // ============================================== FIELDS ==================================================
    private Card[] hand = new Card[13];
    private Map<String, Integer> colorsLength = new Map<String, Integer>();
    private double points;



    // ============================================== METHODS =================================================
    public BiddingModule(Card[] hand) {
        this.hand = hand;
        this.colorsLength = getColorsLength(this.hand);
        this.points = Deck.getStrength(this.hand);
        System.out.println(Deck.toString(hand));
        System.out.println(colorsLength);
    }

    public static Map<String, Integer> getColorsLength(Card[] hand) {
        // Calculate color length
        Map<String, Integer> colorsLength = new Map<String, Integer>();
        int spades = 0, hearts = 0, diamonds = 0, clubs = 0;
        for (Card x: hand) {
            if (x.getColor().equals("SPADES")) spades++;
            else if(x.getColor().equals("HEARTS")) hearts++;
            else if(x.getColor().equals("DIAMONDS")) diamonds++;
            else if(x.getColor().equals("CLUBS")) clubs++;
        }

        String[] colors = { "SPADES", "HEARTS", "DIAMONDS", "CLUBS" };
        int[] cardsInColor = { spades, hearts, diamonds, clubs };

        // Sort result decreasing
        for (int i = 0; i < 4; ++i) {
            for (int j = i + 1; j < 4; ++j) {
                if (cardsInColor[j] > cardsInColor[i]) {
                    int buffer = cardsInColor[i];
                    cardsInColor[i] = cardsInColor[j];
                    cardsInColor[j] = buffer;

                    String colorBuffer = colors[i];
                    colors[i] = colors[j];
                    colors[j] = colorBuffer;
                }
            }
        }

        for (int i = 0; i < 4; ++i) {
            colorsLength.put(colors[i], cardsInColor[i]);
        }

        return colorsLength;
    }

    public static Vector<Card>[] splitColors(Card[] hand) {
        Vector<Card>[] result = new Vector[4];
        result[0] = new Vector<Card>();
        result[1] = new Vector<Card>();
        result[2] = new Vector<Card>();
        result[3] = new Vector<Card>();
        for (Card x: hand) {
            if (x.getColor().equals("SPADES")) result[0].add(x);
            else if(x.getColor().equals("HEARTS")) result[1].add(x);
            else if(x.getColor().equals("DIAMONDS")) result[2].add(x);
            else if(x.getColor().equals("CLUBS")) result[3].add(x);
        }

        return result;
    }
}
