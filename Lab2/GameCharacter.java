/**
 * ECE 422C - Shogun's Challenge Lab
 * 
 * Author: <Aidan Gonzales>
 * EID: <AGG3498>
 * Date: <1/28/26>
 * 
 * Base class representing a character in the game.
 * Both Samurai (hero) and Oni (enemies) extend this class.
 */
public class GameCharacter {

    private String name;
    private int health;
    private int attack;
    private int speed;

    /**
     * Constructs a new GameCharacter.
     * @param name   The character's name
     * @param health The character's starting health points
     * @param attack The character's attack power
     * @param speed  The character's speed (determines turn order)
     */
    public GameCharacter(String name, int health, int attack, int speed) {
        this.name = name;
        this.health = health;
        this.attack = attack;
        this.speed = speed;
    }

    //getter methods:

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getAttack() {
        return attack;
    }

    public int getSpeed() {
        return speed;
    }
    
    //isAlive and isDead methods:

    public boolean isAlive() {
        return getHealth() > 0;
    }


    public boolean isDead() {
        return getHealth() <= 0;
    }

    /**
     * Attacks the target, reducing their health by this character's attack value.
     * Should print: "[attacker name] attacks [target name], causing [attack] damage."
     * @param target The GameCharacter to attack
     */
    public void hit(GameCharacter target) {

        // 1. Print the attack message
        // 2. Reduce target's health by this character's attack value

        //[attacker name] attacks [target name], causing [damage] damage.

        System.out.println(name + " attacks " + target.name + ", causing " + attack + " damage.");

        target.health -= attack;
    }

    /**
     * Reduces this character's speed by the given amount.
     * @param speedDamage The amount to reduce speed by
     */
    public void slowDown(int speedDamage) {
        speed -= speedDamage;
    }

    /**
     * Uses an artifact to boost this character's stats.
     * @param artifact The Artifact to use
     */
    public void useArtifact(Artifact artifact) {
        // Add the artifact's health, attack, and speed to this character's stats

        health += artifact.getHealth();
        attack += artifact.getAttack();
        speed += artifact.getSpeed();

    }
}
