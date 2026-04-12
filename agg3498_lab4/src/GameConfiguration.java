/*  ECE422C Battleship submission by
 * Aidan Gonzales
 * AGG3498
 * Spring 2026
 */

/**
 * Contains all the constant values for the game logic
 */
public class GameConfiguration {

    /**
     * Creates a default GameConfiguration object
     */
    public GameConfiguration() {}

    /**
     * Size of the board
     */
    public static final int BOARD_SIZE = 10;

    /**
     * Max number of shots allocated to the player
     */
    public static final int MAX_SHOTS = 50;


    /**
     * Ship names (index-matched array with SHIP_SIZES)
     */
    public static final String[] SHIP_NAMES = {
        "Carrier", "Battleship", "Cruiser", "Submarine", "Destroyer"
    };

    /**
     * Ship sizes (index-matched array with SHIP_NAMES)
     */
    public static final int[] SHIP_SIZES = {5, 4, 3, 3, 2};

    // Display characters

    /**
     * Display character for water
     */
    public static final char WATER      = '~';

    /**
     * Display character for a hit
     */
    public static final char HIT        = 'X';

    /**
     * Display character for a miss
     */
    public static final char MISS       = 'O';

    /**
     * Display character for a ship. Only shown in testing mode.
     */
    public static final char SHIP       = 'S';  // only shown in testing mode
}
