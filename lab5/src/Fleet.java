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
     * @return the name of the ship that sank, or null if no ship sank yet
     */
    public String registerHit(int row, int col) {
        // TODO (copy from Lab 4)
        throw new UnsupportedOperationException("Not implemented");
    }

    public boolean allSunk()     { return shipsAfloat == 0; }
    public int getShipsAfloat()  { return shipsAfloat; }
    public boolean isSunk(int i) { return sunk[i]; }
    public String getName(int i) { return shipNames[i]; }
    public int getSize(int i)    { return shipSizes[i]; }
    public int getShipCount()    { return shipNames.length; }
}
