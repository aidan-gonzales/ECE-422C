/*
 * Board.java
 *
 * Adapted from Lab 4.  The core shot-firing logic is identical; the
 * text-based display methods have been removed because Lab 5 has no
 * terminal UI.  Two JSON serialization methods have been added for the
 * network protocol.
 *
 * You may reuse your Lab 4 Board implementation directly.  You only
 * need to add the two methods marked TODO below.
 */
public class Board {

    private final int size;
    private final char[][] hiddenBoard;  // actual ship positions
    private final char[][] playerView;   // what the opponent can see (hits/misses)

    public Board(char[][] hiddenBoard) {
        this.size        = hiddenBoard.length;
        this.hiddenBoard = hiddenBoard;
        this.playerView  = new char[size][size];
        for (int r = 0; r < size; r++)
            for (int c = 0; c < size; c++)
                playerView[r][c] = GameConfiguration.WATER;
    }

    /**
     * Fires a shot at (row, col).
     * @return true if hit, false if miss
     * @throws IllegalArgumentException if out of bounds
     * @throws IllegalStateException    if already targeted
     */
    public boolean fireShot(int row, int col) {
        if (row < 0 || row >= size || col < 0 || col >= size)
            throw new IllegalArgumentException("Out of bounds: " + row + "," + col);
        if (playerView[row][col] != GameConfiguration.WATER)
            throw new IllegalStateException("Already targeted: " + row + "," + col);

        if (hiddenBoard[row][col] == GameConfiguration.SHIP) {
            playerView[row][col]  = GameConfiguration.HIT;
            hiddenBoard[row][col] = GameConfiguration.HIT;
            return true;
        }
        playerView[row][col] = GameConfiguration.MISS;
        return false;
    }

    /** Returns true if the cell at (row, col) has already been fired upon. */
    public boolean isTargeted(int row, int col) {
        return playerView[row][col] != GameConfiguration.WATER;
    }

    public int  getSize()                     { return size; }
    public char getPlayerViewCell(int r, int c) { return playerView[r][c]; }

    // -----------------------------------------------------------------------
    // JSON serialization — new for Lab 5
    // -----------------------------------------------------------------------


    /**
     * Returns a JSON 2-D array representing where this player's ships are placed.
     * Encoding: 1 = ship cell, 0 = water.
     *
     * Used in the GAME_START message so the client can draw the player's own fleet.
     *
     * Example output for a 3×3 board with a ship at (0,0) and (0,1):
     *   [[1,1,0],[0,0,0],[0,0,0]]
     *
     * Build the string with a StringBuilder; do not use Arrays.deepToString
     * (it adds spaces that are harder to parse on the client side).
     * @return returns the ship layout in json string form
     */
    public String shipLayoutToJson() {
        // TODO: iterate over hiddenBoard; emit 1 if the cell is SHIP or HIT, else 0
        //       (HIT cells were ships, so they should still count as ship cells here)
        //throw new UnsupportedOperationException("Not implemented");
        StringBuilder sb = new StringBuilder("[");
        for (int r = 0; r < size; r++) {
            sb.append("[");
            for (int c = 0; c < size; c++) {
                // Check if the cell contains a ship
                if (hiddenBoard[r][c] == GameConfiguration.SHIP || hiddenBoard[r][c] == GameConfiguration.HIT) {
                    sb.append("1");
                } else {
                    sb.append("0");
                }

                // Add commas between elements, but not after the last one
                if (c < size - 1) sb.append(",");
            }
            sb.append("]");
            // Add commas between rows, but not after the last row
            if (r < size - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Returns a JSON 2-D array encoding the full revealed state of this board.
     * Used in the GAME_OVER message so both players can see the final board.
     *
     * Encoding:
     *   0 = water (never targeted)
     *   1 = ship cell that was never hit (revealed at game end)
     *   2 = hit
     *   3 = miss
     * @return the json string form of the full state of the board
     */
    public String fullStateToJson() {
        // TODO: combine hiddenBoard and playerView to determine the correct code
        //       for each cell, then build the JSON array string
        //throw new UnsupportedOperationException("Not implemented");
        StringBuilder sb = new StringBuilder("[");
        for (int r = 0; r < size; r++) {
            sb.append("[");
            for (int c = 0; c < size; c++) {
                int code = 0;

                // Determine the cell's status based on both boards
                if (playerView[r][c] == GameConfiguration.HIT) {
                    code = 2;
                } else if (playerView[r][c] == GameConfiguration.MISS) {
                    code = 3;
                } else if (hiddenBoard[r][c] == GameConfiguration.SHIP) {
                    code = 1; // It's a ship, but was never hit
                } else {
                    code = 0; // Pure water
                }

                sb.append(code);
                if (c < size - 1) sb.append(",");
            }
            sb.append("]");
            if (r < size - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }
}
