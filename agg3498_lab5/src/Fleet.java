/*
 * Fleet.java
 *
 * Tracks the fleet of ships and their damage status.
 * This class is identical to your Lab 4 Fleet implementation.
 * Copy it in directly — no changes are required for Lab 5.
 *
 * If you did not finish Fleet in Lab 4, implement it here:
 *   - registerHit(int row, int col) — find which ship occupies that cell,
 *     decrement its hit counter, return its name if it just sank, else null
 *   - allSunk() — return true when every ship's hit counter has reached 0
 */
public class Fleet {

    private final String[] shipNames;
    private final int[]    shipSizes;
    private final int[][]  shipRows;
    private final int[][]  shipCols;
    private final int[]    hitsRemaining;
    private final boolean[] sunk;
    private int shipsAfloat;

    public Fleet(int[][] shipRows, int[][] shipCols) {
        this.shipNames      = GameConfiguration.SHIP_NAMES;
        this.shipSizes      = GameConfiguration.SHIP_SIZES;
        this.shipRows       = shipRows;
        this.shipCols       = shipCols;
        this.hitsRemaining  = new int[shipNames.length];
        this.sunk           = new boolean[shipNames.length];
        this.shipsAfloat    = shipNames.length;

        for (int i = 0; i < shipNames.length; i++) {
            hitsRemaining[i] = shipSizes[i];
        }
    }

    /**
     * Registers a hit at (row, col).
     * @param row row being targeted
     * @param col column being targeted
     * @return the name of the ship that sank, or null if no ship sank yet
     */
    public String registerHit(int row, int col) {
        // TODO (copy from Lab 4)
        //throw new UnsupportedOperationException("Not implemented");
        // Loop over all 5 ships
        for (int i = 0; i < shipNames.length; i++) {

            // Skip this ship if it is already sunk
            if (sunk[i]) continue;

            // Loop through all the coordinate cells this ship occupies
            for (int j = 0; j < shipSizes[i]; j++) {

                // If the shot matches one of this ship's cells...
                if (shipRows[i][j] == row && shipCols[i][j] == col) {
                    hitsRemaining[i]--; // Damage the ship

                    // Did this shot sink the ship?
                    if (hitsRemaining[i] == 0) {
                        sunk[i] = true;
                        shipsAfloat--;
                        return shipNames[i]; // Return the name (e.g., "Battleship")
                    }

                    return null; // It was a hit, but the ship didn't sink yet
                }
            }
        }
        return null; // Should not be reached if called correctly on a known hit
    }

    public boolean allSunk()     { return shipsAfloat == 0; }
    public int getShipsAfloat()  { return shipsAfloat; }
    public boolean isSunk(int i) { return sunk[i]; }
    public String getName(int i) { return shipNames[i]; }
    public int getSize(int i)    { return shipSizes[i]; }
    public int getShipCount()    { return shipNames.length; }
}
