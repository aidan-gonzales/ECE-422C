/**
 * ECE 422C - Shogun's Challenge Lab
 * 
 * Author: <Aidan Gonzales>
 * EID: <AGG3498>
 * Date: <1/28/26>
 * 
 * Represents an artifact that can boost a character's stats.
 * Artifacts are found in chambers after defeating the Oni.
 */
final class Artifact {
    
    private String name;
    private int health;
    private int attack;
    private int speed;

    /**
     * Constructs a new Artifact.
     * @param name   The artifact's name
     * @param health Health bonus when used
     * @param attack Attack bonus when used
     * @param speed  Speed bonus when used
     */
    public Artifact(String name, int health, int attack, int speed) {
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
}
