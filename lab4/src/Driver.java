/*  ECE422C Battleship submission by
 * Aidan Gonzales
 * AGG3498
 * Spring 2026
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


/**
 * This class contains the main program that spawns game objects.
 */
public class Driver {

    private static Scanner kb;
    private static String inputFile;
    private static boolean testMode;

    private static boolean firstGame = true;

    /**
     * Creates a default Driver object
     */
    public Driver() {}

    /**
     * Main program that starts at launch
     * @param args arguments passed in by the user to the main program.
     *             If the first arg is 1, then boot in test mode, other args
     *             are treated as input files
     */
    public static void main(String[] args) {

        testMode = false;
        if (args.length != 0) {
            try {
                if (args[0].equals("1")) {
                    testMode = true;
                    if (args.length > 1) {
                        inputFile = args[1];
                        kb = new Scanner(new File(inputFile));
                    } else {
                        kb = new Scanner(System.in);
                    }
                } else {
                    inputFile = args[0];
                    kb = new Scanner(new File(inputFile));
                }
            } catch (FileNotFoundException e) {
                System.out.println("USAGE: java Main OR java Main <input file> <test output>");
                e.printStackTrace();
            } catch (NullPointerException e) {
                System.out.println("USAGE: java Main OR java Main <input file>  <test output>");
            }
        } else {
            kb = new Scanner(System.in);
        }




        printStartMessage();

        while (true) {
            String in = "";

            while (!in.equals("Y")) {
                in = kb.nextLine().trim().toUpperCase();
                switch (in) {
                    case "Y":
                        break;
                    case "N":
                        return;
                    default:
                        //System.out.println();
                        //System.out.println("Invalid input. Please type Y/N.");
                }
            }

            if (firstGame) {
                System.out.println();
                firstGame = false;
            }


            Game game = new Game(testMode, kb);
            game.runGame();
            System.out.print("Are you ready for another game (Y/N): ");
            //System.out.println();
        }
    }

    /**
     * Prints the start message on bootup
     */
    private static void printStartMessage() {
        System.out.print("Welcome to Battleship. Here are the rules.\n" +
                "\n" +
                "This is a text version of the classic board game Battleship.\n" +
                "\n" +
                "The computer will secretly place a fleet of ships on a " + GameConfiguration.BOARD_SIZE + "x" +
                GameConfiguration.BOARD_SIZE + " grid.\n" +
                "The fleet consists of:\n");
        for (int i = 0; i < GameConfiguration.SHIP_NAMES.length; i++) {
            System.out.println("  " + GameConfiguration.SHIP_NAMES[i] +
                    " (" + GameConfiguration.SHIP_SIZES[i] + " cells)");
        }
        System.out.print(
                "\n" +
                "You try to sink all the ships by firing shots at grid coordinates.\n" +
                "Enter coordinates as a letter (row) followed by a number (column),\n" +
                "for example: A5, C10, J1.\n" +
                "Rows are labeled A-" + (char)('A' + GameConfiguration.BOARD_SIZE - 1) + " and columns are labeled 1-" +
                        GameConfiguration.BOARD_SIZE + ".\n" +
                "\n" +
                "After each shot, the result will be displayed as a Hit or Miss.\n" +
                "When all cells of a ship are hit, it sinks and you are notified.\n" +
                "\n" +
                "You have " + GameConfiguration.MAX_SHOTS + " shots to sink the entire fleet or you lose.\n" +
                "Type BOARD to see your current board, FLEET to see ship status,\n" +
                "HISTORY to see your shot history, HELP to see available commands,\n" +
                "or QUIT to forfeit the current game.\n" +
                "\n" +
                "Are you ready to play? (Y/N):");

        /*
        System.out.print("Welcome to Battleship. Here are the rules.\n" +
                "\n" +
                "This is a text version of the classic board game Battleship.\n" +
                "\n" +
                "The computer will secretly place a fleet of ships on a 10x10 grid.\n" +
                "The fleet consists of:\n" +
                "  Carrier (5 cells)\n" +
                "  Battleship (4 cells)\n" +
                "  Cruiser (3 cells)\n" +
                "  Submarine (3 cells)\n" +
                "  Destroyer (2 cells)\n" +
                "\n" +
                "You try to sink all the ships by firing shots at grid coordinates.\n" +
                "Enter coordinates as a letter (row) followed by a number (column),\n" +
                "for example: A5, C10, J1.\n" +
                "Rows are labeled A-J and columns are labeled 1-10.\n" +
                "\n" +
                "After each shot, the result will be displayed as a Hit or Miss.\n" +
                "When all cells of a ship are hit, it sinks and you are notified.\n" +
                "\n" +
                "You have 50 shots to sink the entire fleet or you lose.\n" +
                "Type BOARD to see your current board, FLEET to see ship status,\n" +
                "HISTORY to see your shot history, HELP to see available commands,\n" +
                "or QUIT to forfeit the current game.\n" +
                "\n" +
                "Are you ready to play? (Y/N):");
                */

    }
}
