package bridge.game;

import com.sun.istack.internal.NotNull;
import tools.Paths;
import tools.Settings;

public class Card {
    // ========================================== FIELDS ===============================================
    // Card value, i.e. '4', '0' (ten), 'Q'
    private char value;

    // Card color: "SPADES", "HEARTS", "DIAMONDS", "CLUBS"
    private String color;

    // Paths to directory containing .png files
    private static String pathToGraphic = Paths.projectPath + "/textures/cards/";

    // Path to this card face and back
    private String face;
    private String back;

    // ========================================== METHODS ==============================================
    public Card() {
        value = ' ';
        color = "NONE";
        face = "";
        back = "";
    }

    public Card(char value, String color) {
        this.value = value;
        this.color = color;
        this.face = pathToGraphic + Settings.theme + "_" + this.value + this.color.charAt(0) + ".png";
        this.back = pathToGraphic + Settings.theme + "_back.png";
    }

    @Override
    public String toString() {
        String result = String.valueOf(value);
        if (value == '0') result = "10";

        switch(color) {
            case "SPADES":
                result += "♠";
                break;
            case "HEARTS":
                result += "\033[31m♥\033[0m";
                break;
            case "DIAMONDS":
                result += "\033[31m♦\033[0m";
                break;
            case "CLUBS":
                result += "♣";
                break;
            default:
                return "";
        }
        return result;
    }

    public char getValue() { return value; }
    public String getColor() { return color; }
    public String getPathToFace() { return face; }
    public String getPathToBack() { return back; }

    public double getStrength() {
        switch(value) {
            case 'A': return 4.0;
            case 'K': return 3.0;
            case 'Q': return 2.0;
            case 'J': return 1.0;
//          case '0': return 0.5;
//          case '9': return 0.25;
            default: return 0.0;
        }
    }

    public boolean isBigger(@NotNull Card card) {
        if (!color.equals(card.color)) return false;

        // Map to card comparison
        String cardOrder = "234567890JQKA";
        return cardOrder.indexOf(value) > cardOrder.indexOf(value);
    }

    public boolean equals(@NotNull Card card) {
        if (!color.equals(card.color)) return false;
        return value == card.value;
    }
}