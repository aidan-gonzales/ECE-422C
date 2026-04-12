/*  ECE422C Battleship submission by
 * Aidan Gonzales
 * AGG3498
 * Spring 2026
 */

import java.awt.Point;
import java.util.ArrayList;

/**
 * Handles all the game logic for individual ships.
 */
public class Ship {

    private String name;
    private ArrayList<Point> cells = new ArrayList<>();
    private int size;
    private int unhit;

    /**
     * Ship constructor
     * @param n name of the ship
     * @param s size of the ship
     * @param rows list of all the ships row coordinates
     * @param cols list of all the ships column coordinates
     */
    public Ship(String n, int s, int[] rows, int[] cols) {
        name = n;
        size = s;
        unhit = size;
        for (int i = 0; i < size; i++) {
            cells.add(new Point(rows[i], cols[i]));
        }
    }

    /**
     * Helper method to get the name of the ship
     * @return ship name
     */
    public String getName() {
        return name;
    }

    /**
     * Helper method to see if the ship is sunk or not
     * @return true of sunk, false if afloat
     */
    public boolean isSunk() {
        return (unhit == 0);
    }

    /**
     * Helper method to see if this ship got hit, and decrements the number of unhit cells for this ship
     * @param row row to check
     * @param col column to check
     * @return true if hit, false if not hit
     */
    public boolean checkHit(int row, int col) {

        for (Point cell : cells) {
            if ((row == cell.x) && (col == cell.y)) {
                unhit--;
                return true;
            }
        }
        return false;
    }

    /**
     * Helper method that returns the size of the ship
     * @return the size of the ship
     */
    public int getSize() {
        return size;
    }

}
