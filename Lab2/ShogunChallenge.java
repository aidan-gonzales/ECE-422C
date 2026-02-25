/**
 * ECE 422C - Shogun's Challenge Lab
 * 
 * THIS FILE IS PROVIDED COMPLETE - DO NOT MODIFY
 * 
 * Main entry point for Shogun's Challenge game.
 * Usage: java ShogunChallenge <input_file>
 */
import java.io.IOException;

public class ShogunChallenge {

    public static void main(String[] args) throws IOException {
        if (args == null || args.length == 0) {
            System.out.println("Must provide a filename with game input!");
            System.exit(0);
        }
        
        new GameLoader().loadGame(args[0]).play();
    }
}
