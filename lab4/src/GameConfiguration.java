public class GameConfiguration {
    public static final int BOARD_SIZE = 10;
    public static final int MAX_SHOTS = 50;

    // Ship names and their sizes (index-matched arrays)
    public static final String[] SHIP_NAMES = {
        "Carrier", "Battleship", "Cruiser", "Submarine", "Destroyer"
    };
    public static final int[] SHIP_SIZES = {5, 4, 3, 3, 2};

    // Display characters
    public static final char WATER      = '~';
    public static final char HIT        = 'X';
    public static final char MISS       = 'O';
    public static final char SHIP       = 'S';  // only shown in testing mode
}
