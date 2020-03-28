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
    private Map<String, Double> tricksInColors = new Map<String, Double>();


    // ============================================== METHODS =================================================
    public BiddingModule(Card[] hand) {
        this.hand = hand;
        this.colorsLength = getColorsLength(this.hand);
        this.points = Deck.getStrength(this.hand);
        double[] tricks = trickInColors(splitColors(hand));

        tricksInColors.put("SPADES", tricks[0]);
        tricksInColors.put("HEARTS", tricks[1]);
        tricksInColors.put("DIAMONDS", tricks[2]);
        tricksInColors.put("CLUBS", tricks[3]);
    }

    public void printAnalyze() {
        double totalTricks = 0.0;
        System.out.println(Deck.toString(hand));
        for (int i = 0; i < 4; ++i) {
            String key = colorsLength.getPair(i).getKey();
            totalTricks += tricksInColors.getValue(key);
            System.out.println(key + " - Length: " + colorsLength.getPair(i).getValue() +
                    "  Tricks: " + tricksInColors.getValue(key));
        }
        System.out.println("Total points: " + points);
        System.out.println("Total tricks: " + totalTricks);
        System.out.println();
    }

    // ============================================== STATIC ==================================================
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

    public static double[] trickInColors(Vector<Card>[] splitCards) {
        double[] result = new double[4];
        for (int i = 0; i < 4; ++i) {
            double tricks = 0;
            if (splitCards[i].size() == 0) tricks += 1.0;
            for (Card x: splitCards[i]) {
                if (x.getValue() == 'A') tricks += 1.0;
                if (x.getValue() == 'K') {
                    if (splitCards[i].size() == 1) tricks += 0.333;
                    else if (splitCards[i].size() == 2) tricks += 0.666;
                    else if (splitCards[i].size() >= 3) tricks += 1.0;
                }
                if (x.getValue() == 'Q') {
                    if (splitCards[i].size() == 1) tricks += 0.111;
                    else if (splitCards[i].size() == 2 && isCard('A', splitCards[i])) tricks += 0.666;
                    else if (splitCards[i].size() == 3) tricks += 0.888;
                    else if (splitCards[i].size() >= 4) tricks += 1.0;
                }
                if (x.getValue() == 'J') {
                    if (splitCards[i].size() == 1) tricks += 0.037;
                    else if (splitCards[i].size() == 2 && isCard('A', splitCards[i])) tricks += 0.333;
                    else if (splitCards[i].size() == 3 && isCard('A', splitCards[i])) tricks += 0.5;
                    else if (splitCards[i].size() == 3 && isCard('K', splitCards[i])) tricks += 0.5;
                    else if (splitCards[i].size() == 4) tricks += 0.963;
                    else if (splitCards[i].size() >= 5) tricks += 1.0;
                }
            }
            result[i] = tricks;
        }
        return result;
    }

    // Assume that cards are in one color
    public static boolean isCard(char value, Vector<Card> v) {
        for (Card x: v) {
            if (x.getValue() == value) return true;
        }
        return false;
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
