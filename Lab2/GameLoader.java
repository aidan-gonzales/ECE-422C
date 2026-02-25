/**
 * ECE 422C - Shogun's Challenge Lab
 * 
 * THIS FILE IS PROVIDED COMPLETE - DO NOT MODIFY
 * 
 * Loads game configuration from a text file.
 */
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class GameLoader {

    /**
     * Loads a game from the specified file.
     * 
     * File format:
     * Line 1: Samurai - name,health,attack,speed
     * Lines 2+: Chamber - chamberName,oniName,health,attack,speed,speedDamage,artifactName,health,attack,speed
     */
    public Game loadGame(String fileName) throws IOException {
        List<String> gameLines = Files.readAllLines(Path.of(fileName));
        
        final Samurai samurai = parseSamurai(gameLines.remove(0));
        
        List<Chamber> chambers = new ArrayList<Chamber>();
        for (String line : gameLines) {
            chambers.add(parseChamber(line));
        }
        
        return new Game(samurai, chambers);
    }

    private Samurai parseSamurai(String line) {
        final String[] parts = line.split(",");
        final String name = parts[0].trim();
        final int health = Integer.parseInt(parts[1].trim());
        final int attack = Integer.parseInt(parts[2].trim());
        final int speed = Integer.parseInt(parts[3].trim());
        return new Samurai(name, health, attack, speed);
    }

    private Chamber parseChamber(String line) {
        final String[] parts = line.split(",");
        final String chamberName = parts[0].trim();
        final Oni oni = parseOni(parts);
        final Artifact artifact = parseArtifact(parts);
        return new Chamber(chamberName, oni, artifact);
    }

    private Oni parseOni(String[] parts) {
        final String name = parts[1].trim();
        final int health = Integer.parseInt(parts[2].trim());
        final int attack = Integer.parseInt(parts[3].trim());
        final int speed = Integer.parseInt(parts[4].trim());
        final int speedDamage = Integer.parseInt(parts[5].trim());
        return new Oni(name, health, attack, speed, speedDamage);
    }

    private Artifact parseArtifact(String[] parts) {
        final String name = parts[6].trim();
        final int health = Integer.parseInt(parts[7].trim());
        final int attack = Integer.parseInt(parts[8].trim());
        final int speed = Integer.parseInt(parts[9].trim());
        return new Artifact(name, health, attack, speed);
    }
}
