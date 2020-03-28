package bridge.bidding;

public class Bid {
    // ========================================== FIELDS ===========================================
    // Bid level. 0 is default to ease handling PASS, DOUBLE and REDOUBLE
    private int level = 0;

    // Use the same convention as in Card.java... Color may equal to PASS, DOUBLE and REDOUBLE
    private String color;

    // Meaning of a bid is really important, so TODO!
    private String meaning = "There is no meaning! Buy a premium version to see meanings of bids... ($49.99)";

    // ========================================== METHODS ==========================================
    public Bid(String color) {
        this.color = color;
    }

    public Bid(int level, String color) {
        this.level = level;
        this.color = color;
    }

    // Accessors
    public int getLevel() { return level; }
    public String getColor() { return color; }

    // Check the relation between two bids - they are always comparable
    public boolean isGreater(Bid bid) {
        String colorOrder = "CDHSN";
        if (level > bid.level) return true;
        if (level == bid.level && colorOrder.indexOf(color.charAt(0)) > colorOrder.indexOf(bid.color.charAt(0))) return true;
        return false;
    }

    @Override
    public String toString() {
        if (color.equals("PASS")) return "pas";
        if (color.equals("DOUBLE")) return "X";
        if (color.equals("REDOUBLE")) return "XX";

        // Add some nice colors :D
        String result = String.valueOf(level);
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
            case "NOTRUMP":
                result += "NT";
                break;
            default:
                return "???";
        }
        return result;
    }

    public String toStringNoColor() {
        if (color.equals("PASS")) return "pas";
        if (color.equals("DOUBLE")) return "X";
        if (color.equals("REDOUBLE")) return "XX";

        String result = String.valueOf(level);
        if (color.equals("NOTRUMP")) result += "NT";
        else result += color.charAt(0);
        return result;
    }
}
