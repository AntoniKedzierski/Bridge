package tools;

public class Settings {
    public static String theme = "default";

    public static String[] defaultNames = { "North", "East", "South", "West" };
    public static String[] falenicaNames = { "Przejazd", "Dziku", "213", "No internet" };

    public static double windowWidth = 800;
    public static double windowHeight = 600;

    public static double minWindowWidth = 800;
    public static double minWindowHeight = 600;

    // This value represents the height of a card, the width is 2/3 of this value
    public static double cardSize = 240;
    public static double cardHeight = cardSize;
    public static double cardWidth = 2 / 3.0 * cardSize;

    // Span size
    public static double cardSpanAngle = 36.0;
    public static double cardSpanPitch = 4.0;

    // Card table params
    public static double cardTableRatio = 0.75;

    public static boolean showSouthPC = true;
}
