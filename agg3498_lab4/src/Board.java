/*  ECE422C Battleship submission by
 * Aidan Gonzales
 * AGG3498
 * Spring 2026
 */

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The board class contains all of the logic for the game board.
 */
public class Board {

    private final char[][] board = new char[GameConfiguration.BOARD_SIZE][GameConfiguration.BOARD_SIZE];
    private final char[][] testBoard = new char[GameConfiguration.BOARD_SIZE][GameConfiguration.BOARD_SIZE];
    private final ArrayList<Ship> fleet = new ArrayList<>();
    private final LinkedHashMap<String, String> history = new LinkedHashMap<>();


    /**
     * Board constructor. Copies values from ship placement generator.
     * @param placements The ship placement generator values to be copied.
     */
    public Board(ShipPlacementGenerator.ShipPlacements placements) {

        for (int i = 0; i < GameConfiguration.BOARD_SIZE; i++) {
            for (int j = 0; j < GameConfiguration.BOARD_SIZE; j++) {
                testBoard[i][j] = placements.board[i][j];
            }
        }

        for (int i = 0; i < GameConfiguration.BOARD_SIZE; i++) {
            for (int j = 0; j < GameConfiguration.BOARD_SIZE; j++) {
                board[i][j] = GameConfiguration.WATER;
            }
        }



        for (int i = 0; i < GameConfiguration.SHIP_NAMES.length; i++) {
            fleet.add(new Ship(GameConfiguration.SHIP_NAMES[i], GameConfiguration.SHIP_SIZES[i],
                    placements.shipRows[i], placements.shipCols[i]));
        }
    }

    /**
     * Prints the board based on board size and current game status
     */
    public void printBoard() {

        System.out.print("  ");
        for (int i = 1; i <= GameConfiguration.BOARD_SIZE; i++) {
            System.out.printf("%3d", i);
        }
        System.out.println();
        //System.out.println("    1  2  3  4  5  6  7  8  9 10");
        char row = 'A';
        for (int i = 0; i < GameConfiguration.BOARD_SIZE; i++) {
            System.out.print(row + "   ");
            for (int j = 0; j < GameConfiguration.BOARD_SIZE - 1; j++) {
                System.out.print(board[i][j] + "  ");
            }
            System.out.println(board[i][GameConfiguration.BOARD_SIZE - 1]);
            row++;
        }
    }

    /**
     * Prints the board based on board size and current game status
     */
    public void printTestBoard() {
        System.out.print("  ");
        for (int i = 1; i <= GameConfiguration.BOARD_SIZE; i++) {
            System.out.printf("%3d", i);
        }
        System.out.println();
        //System.out.println("    1  2  3  4  5  6  7  8  9 10");
        char row = 'A';
        for (int i = 0; i < GameConfiguration.BOARD_SIZE; i++) {
            System.out.print(row + "   ");
            for (int j = 0; j < GameConfiguration.BOARD_SIZE - 1; j++) {
                System.out.print(testBoard[i][j] + "  ");
            }
            System.out.println(testBoard[i][GameConfiguration.BOARD_SIZE - 1]);
            row++;
        }
    }

    /**
     * Prints the names of the ships and whether they're still afloat or sunk.
     */
    public void printFleet() {
        for (Ship ship : fleet) {
            System.out.print("  " + ship.getName() + " (" + ship.getSize() + "): ");
            if (ship.isSunk()) {
                System.out.println("SUNK");
            } else {
                System.out.println("afloat");
            }
        }
    }


    /**
     * Checks to see if it's a hit
     * @param coord The coordinate being fired at
     * @return true if a valid shot, false if already targeted
     */
    public boolean fire(String coord) {

        System.out.print(coord + " -> ");

        int row = coord.charAt(0) - 65;
        int col;
        String tempInt = coord.substring(1);
        col = Integer.parseInt(tempInt) - 1;

        String tempString = (coord.substring(0, 1) + (col + 1));

        if (board[row][col] != GameConfiguration.WATER) {
            System.out.println("Already targeted");
            return false;
        }


        boolean hitSomething = false;
        for (int i = 0; i < fleet.size(); i++) {
            if (fleet.get(i).checkHit(row, col)) {
                if (!hitSomething) {
                    System.out.print("Hit!");
                    history.put(tempString, "hit");
                    hitSomething = true;
                }

                if (fleet.get(i).isSunk()) {
                    System.out.print(" You sunk the " + fleet.get(i).getName() + "!");
                    String existing = history.get(tempString);
                    if (existing != null && !existing.equals("hit") && !existing.equals("miss")) {
                        history.put(tempString, existing + " and " + fleet.get(i).getName());
                    } else {
                        history.put(tempString, fleet.get(i).getName());
                    }
                    //history.put(tempString, fleet.get(i).getName());
                }


            }
        }

        if (hitSomething) {
            System.out.println();
            board[row][col] = GameConfiguration.HIT;
            testBoard[row][col] = GameConfiguration.HIT;
            return true;
        }

        System.out.println("Miss");
        board[row][col] = GameConfiguration.MISS;
        history.put(tempString, "miss");
        return true;
        /*
        for (int i = 0; i < fleet.size(); i++) {
            if (fleet.get(i).checkHit(row, col)) {
                System.out.print("Hit!");
                if (fleet.get(i).isSunk()) {
                    System.out.println(" You sunk the " + fleet.get(i).getName() + "!");
                    history.put(coord, fleet.get(i).getName());
                } else {
                    System.out.println();
                    history.put(coord, "hit");
                }
                board[row][col] = GameConfiguration.HIT;
                testBoard[row][col] = GameConfiguration.HIT;
                return true;
            }
        }

        System.out.println("Miss");
        board[row][col] = GameConfiguration.MISS;
        //testBoard[row][col] = GameConfiguration.MISS;
        history.put(coord, "miss");
        return true;
        */
    }

    /**
     * Prints the history of the user's shots and whether the shot missed, hit, or sunk a ship.
     */
    public void printHistory() {
        for (Map.Entry<String, String> entry : history.entrySet()) {
            String temp;
            if (entry.getValue().equals("hit")) {
                temp = "Hit";
            } else if (entry.getValue().equals("miss")) {
                temp = "Miss";
            } else {
                temp = "Hit (sunk " + entry.getValue() + ")";
            }
            System.out.println(entry.getKey() + "\t\t" + temp);
            //System.out.printf("%-16s%s%n", entry.getKey(), temp);
        }
    }

    /**
     * Checks the number of remaining ships to determine if the game is won or not.
     * @return Returns the number of remaining ships.
     */
    public int checkRemainingShips() {
        int retval = fleet.size();
        for (Ship ship : fleet) {
            if (ship.isSunk()) {
                retval--;
            }
        }
        return retval;
    }
}
