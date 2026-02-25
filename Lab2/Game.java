/**
 * ECE 422C - Shogun's Challenge Lab
 * 
 * Author: <Aidan Gonzales>
 * EID: <AGG3498>
 * Date: <1/28/26>
 * 
 * The main game logic for Shogun's Challenge.
 * Controls the flow of combat through chambers.
 */
import java.util.List;

public final class Game {
    
    private final Samurai samurai;
    private final List<Chamber> chambers;

    /**
     * Constructs a new Game.
     * @param samurai  The hero character
     * @param chambers The list of chambers to traverse
     */
    public Game(Samurai samurai, List<Chamber> chambers) {
        this.samurai = samurai;
        this.chambers = chambers;
    }

    /**
     * The main game loop.
     * 
     * Algorithm:
     * 1. Print welcome banner:
     *    "       Welcome to Shogun's Challenge!"
     *    "       ------------------------------"
     *    ""
     *    "[name] starts with health: [health] speed: [speed] attack: [attack]"
     *    ""
     * 
     * 2. For each chamber:
     *    a. Print: "Chamber: [name]. [samurai] encounters a [oni name]"
     *    b. Print oni stats: "    [oni] - health: [h] speed: [s] attack: [a] speed damage: [sd]"
     *    c. Determine who goes first (higher speed goes first; oni wins ties)
     *    d. Combat loop until someone dies:
     *       - If samurai dead: print "[name] is dead - GAME OVER!" and return
     *       - If oni dead: print "[oni] is defeated!" and break
     *       - Execute current turn's attack
     *       - Switch turns
     *    e. Print: "[samurai] finds [artifact name]"
     *    f. Apply artifact to samurai
     * 
     * 3. Print: "[samurai] wins!"
     */
    public void play() {
        // TODO: Implement the main game loop

        System.out.println("       Welcome to Shogun's Challenge!\n" +
                           "       ------------------------------");
        System.out.println("\n" + samurai.getName() + " starts with health: " + samurai.getHealth() +
                        " speed: " + samurai.getSpeed() + " attack: " + samurai.getAttack() + "\n");

        for (Chamber tempChamber : chambers) {
            System.out.println("Chamber: " + tempChamber.getName() + ". " + samurai.getName() + " encounters a " + tempChamber.getOni().getName());
            Oni tempOni = tempChamber.getOni();
            System.out.println("    " + tempOni.getName() + " - health: " + tempOni.getHealth() +
                    " speed: " + tempOni.getSpeed() + " attack: " + tempOni.getAttack() +
                    " speed damage: " + tempOni.getSpeedDamage());

            boolean oniTurn = (tempOni.getSpeed() >= samurai.getSpeed());

            while (true) {
                if (samurai.isDead()) {
                    System.out.println(samurai.getName() + " is dead - GAME OVER!");
                    return;
                }

                if (tempOni.isDead()) {
                    System.out.println(tempOni.getName() + " is defeated!");
                    break;
                }

                if (oniTurn) {
                    tempOni.fights(samurai);
                    oniTurn = false;
                } else {
                    samurai.fights(tempOni);
                    oniTurn = true;
                }
            }

            Artifact tempArtifact = tempChamber.getArtifact();

            System.out.println(samurai.getName() + " finds " + tempArtifact.getName() + "\n");
            samurai.useArtifact(tempArtifact);


        }

        System.out.println(samurai.getName() + " wins!");
    }
}
