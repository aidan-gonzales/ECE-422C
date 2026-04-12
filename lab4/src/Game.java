/*  ECE422C Battleship submission by
 * Aidan Gonzales
 * AGG3498
 * Spring 2026
 */

import java.util.Scanner;

/**
 * This class contains all the game logic and interprets user inputs while in game.
 */
public class Game {

    private boolean testMode;
    private Scanner kb;
    private Board board;
    private int numShots = GameConfiguration.MAX_SHOTS;

    private boolean quitFlag;

    /**
     * Creates a new Game object when the user wants to play a new game.
     * @param test true if in testMode, false otherwise
     * @param input The scanner that the user inputs are read through
     */
    public Game(boolean test, Scanner input) {
        testMode = test;
        kb = input;

        ShipPlacementGenerator helper = ShipPlacementGenerator.getInstance();
        ShipPlacementGenerator.ShipPlacements placements = helper.generatePlacements(kb, testMode);

        board = new Board(placements);

        quitFlag = false;
    }

    /**
     * The main game loop that interprets user inputs and updates the state of the game.
     */
    public void runGame() {


        System.out.println();
        System.out.println("Deploying ships ...");
        if (testMode) {
            System.out.println("(TEST MODE) Ship positions:");
            board.printTestBoard();
        }
        System.out.println();
//        if (testMode) {
//            System.out.println();
//            System.out.println("Deploying ships ...\n" +
//                    "(TEST MODE) Ship positions:");
//            board.printTestBoard();
//            System.out.println();
//        }

        String in = "";
        String uppercaseString = "";
        while (!uppercaseString.equals("QUIT") && (board.checkRemainingShips() != 0) && (numShots > 0)) {
            System.out.println("You have " + numShots + " shots remaining. " +
                    board.checkRemainingShips() + " ships afloat.\n" +
                    "Enter target coordinate (e.g., A" + (GameConfiguration.BOARD_SIZE / 2) + "): ");
            System.out.print("Fire at: ");
            in = kb.nextLine();
            System.out.println();
            uppercaseString = in.trim().toUpperCase();

            switch (uppercaseString) {
                case "BOARD":
                    board.printBoard();
                    System.out.println();
                    break;
                case "FLEET":
                    board.printFleet();
                    System.out.println();
                    break;
                case "HISTORY":
                    board.printHistory();
                    System.out.println();
                    break;
                case "HELP":
                    printHelp();
                    System.out.println();
                    break;
                case "QUIT":
                    quitFlag = true;
                    break;
                default:
                    try {
                        // start new test
                        int tempInt2 = Integer.parseInt(uppercaseString.substring(1));
                        String tempString = (uppercaseString.substring(0, 1) + (tempInt2));

                        // end new test
                        if ((tempString.length() > 3) || (tempString.length() < 2)) throw new Exception();

                        if ((tempString.charAt(0) > ('A' + GameConfiguration.BOARD_SIZE - 1)) || (tempString.charAt(0) < 'A')) throw new Exception();


                        String tempInt = tempString.substring(1);
                        int num = Integer.parseInt(tempInt); // could throw a NumberFormatException

                        //if (!tempInt.equals(String.valueOf(num))) throw new Exception();

                        if (num > GameConfiguration.BOARD_SIZE) throw new Exception();

                        if (num < 1) throw new Exception();

                        if (board.fire(tempString)) {
                            numShots--;
                        }


                        System.out.println();

                    } catch (Exception e) {
                        System.out.println(in + " -> Invalid coordinate");
                        System.out.println("Enter a letter A-" + (char)('A' + GameConfiguration.BOARD_SIZE - 1) +
                                " followed by a number 1-" + GameConfiguration.BOARD_SIZE + " (e.g., A" +
                                (GameConfiguration.BOARD_SIZE / 2) + ", " + (char)('A' + GameConfiguration.BOARD_SIZE - 1) +
                                GameConfiguration.BOARD_SIZE + ").");
                        System.out.println();
                    }
            }
        }

        int shipsRemaining = board.checkRemainingShips();

        if (uppercaseString.equals("QUIT")) {
            System.out.println("You have forfeited the game.\n" +
                    "Ships remaining: " + shipsRemaining);
            System.out.println();
        } else if (shipsRemaining == 0) {
            System.out.println("All ships sunk! You win in " + (GameConfiguration.MAX_SHOTS - numShots) + " shots!");
            System.out.println();
        } else {
            System.out.println("Sorry, you are out of shots. You lose, boo-hoo.");
            System.out.println("Ships remaining: " + shipsRemaining);
            System.out.println();
        }

        board.printBoard();
        System.out.println();


        if (testMode && quitFlag) {
            System.out.println("(TEST MODE) Hidden board:");
            board.printTestBoard();
            System.out.println();
            quitFlag = false;
        }
    }

    /**
     * Prints the help message when the user types "HELP"
     */
    private static void printHelp() {
        System.out.println("Available commands:\n" +
                "  <coordinate>  Fire a shot (e.g., A" + (GameConfiguration.BOARD_SIZE / 2) +
                ", " + (char)('A' + GameConfiguration.BOARD_SIZE - 1) +
                GameConfiguration.BOARD_SIZE + ")\n" +
                "  BOARD         Show your current board\n" +
                "  FLEET         Show the status of each ship\n" +
                "  HISTORY       Show all previous shots and results\n" +
                "  HELP          Show this help message\n" +
                "  QUIT          Forfeit the current game");
    }
}
