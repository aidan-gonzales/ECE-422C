import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.sql.SQLOutput;
import java.util.Scanner;

public class Main {

    static Scanner kb;
    private static String inputFile;

    public static void main(String[] args) {
        if (args.length != 0) {
            try {
                inputFile = args[0];
                kb = new Scanner(new File(inputFile));
            } catch (FileNotFoundException e) {
                System.out.println("USAGE: java Main OR java Main <input file> <test output>");
                e.printStackTrace();
            } catch (NullPointerException e) {
                System.out.println("USAGE: java Main OR java Main <input file>  <test output>");
            }
            // possibly add same stuff from lab3 here, but that might not be necessary
        } else {
            kb = new Scanner(System.in);
        }


        String in = "";

        printStartMessage();

        while (!in.equals("Y")) {
            in = kb.nextLine();
            switch (in) {
                case "Y":
                    break;
                case "N":
                    return;
                default:
                    System.out.println("Invalid input. Please type Y/N.");
            }
        }


        while (!in.equals("QUIT")) {
            System.out.print("Fire at: ");
            in = kb.nextLine();

            switch (in) {
                case "BOARD":
                    // printboard command from board class
                    break;
                case "FLEET":
                    // printfleet command from either ship or board class
                    break;
                case "HISTORY":
                    // printhistory command from probably board class
                    break;
                case "HELP":
                    printHelp();
                    break;
                case "QUIT":
                    break;
                default:
                    try {
                        if ((in.length() > 3) || (in.length() < 2)) throw new Exception();

                        if ((in.charAt(0) > 'J') || (in.charAt(0) < 'A')) throw new Exception();

                        if (in.length() == 3) {
                            if (in.charAt(1) != '1') throw new Exception();
                            if (in.charAt(2) != '0') throw new Exception();
                        } else {
                            if ((in.charAt(1) < '1') || (in.charAt(1) > '9')) throw new Exception();
                        }

                        // check the coordinate to see if it's a hit or not
                        // if already targeted, then print the already targeted message
                        // if a hit, then print the hit message
                        // if a miss, then print the miss message

                    } catch (Exception e) {
                        System.out.println(in + " -> Invalid coordinate");
                        System.out.println("Enter a letter A-J followed by a number 1-10 (e.g., A5, J10).");
                    }
            }
        }
    }

    public static void printStartMessage() {
        System.out.println("Welcome to Battleship. Here are the rules.\n" +
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
    }

    public static void printHelp() {
        System.out.println("Available commands:\n" +
                "  <coordinate>  Fire a shot (e.g., A5, J10)\n" +
                "  BOARD         Show your current board\n" +
                "  FLEET         Show the status of each ship\n" +
                "  HISTORY       Show all previous shots and results\n" +
                "  HELP          Show this help message\n" +
                "  QUIT          Forfeit the current game");
    }
}
