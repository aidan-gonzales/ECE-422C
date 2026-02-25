/**
 * ECE 422C - Shogun's Challenge Lab
 * 
 * Author: <Aidan Gonzales>
 * EID: <AGG3498>
 * Date: <1/28/26>
 * 
 * Oni is a demon enemy that the Samurai must defeat.
 * Extends GameCharacter and adds a speed damage ability.
 */
public class Oni extends GameCharacter {

    private final int speedDamage;

    /**
     * Constructs a new Oni demon.
     * @param name        The oni's name
     * @param health      Starting health points
     * @param attack      Attack power
     * @param speed       Speed (determines turn order)
     * @param speedDamage How much speed the oni drains from enemies
     */
    public Oni(String name, int health, int attack, int speed, int speedDamage) {
        super(name, health, attack, speed);
        this.speedDamage = speedDamage;
    }

    /**
     * Returns the speed damage this Oni inflicts.
     */
    public int getSpeedDamage() {
        return speedDamage;
    }

    /**
     * The Oni fights a Samurai by hitting them AND slowing them down.
     * @param samurai The Samurai to fight
     */
    public void fights(Samurai samurai) {
        // 1. Hit the samurai (use inherited method)
        // 2. Slow down the samurai by this oni's speedDamage
        hit(samurai);
        samurai.slowDown(speedDamage);
    }
}
