/**
 * ECE 422C - Shogun's Challenge Lab
 * 
 * Author: <Aidan Gonzales>
 * EID: <AGG3498>
 * Date: <1/28/26>
 * 
 * Samurai is the hero character controlled by the player.
 * Extends GameCharacter and adds samurai-specific combat behavior.
 */
public class Samurai extends GameCharacter {

    /**
     * Constructs a new Samurai warrior.
     * @param name   The samurai's name
     * @param health Starting health points
     * @param attack Attack power
     * @param speed  Speed (determines turn order)
     */
    public Samurai(String name, int health, int attack, int speed) {
        super(name, health, attack, speed);
    }

    /**
     * The samurai fights an Oni by hitting it.
     * @param oni The Oni to fight
     */
    public void fights(Oni oni) {
        // Hint: Use the inherited hit() method
        hit(oni);
    }
}
