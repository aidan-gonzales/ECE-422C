import java.util.List;

/**
 * Engineer — a custom entity type (your design).
 *
 * The Engineer helps sustain the life of maintenance bots. They repair maintenance bots and get some
 * energy in return. They are easily targeted by commanders, so their reproduction threshold is lower at
 * 130 energy. It moves to an adjacent maintenance bot and repairs it. If there is no maintenance bot nearby,
 * or the repair is on cooldown, then the engineer moves randomly. If all engineers are removed, the maintenance
 * bots will all die, and the space station will not be running correctly.
 *
 * Display character: E
 */
public class Engineer extends Entity {

    // TODO: Implement this class

    private int healCount;
    private int cooldown;

    /**
     * Called by the makeEntity method. Initializes instance variables to default values.
     */
    public Engineer() {
        //makeEntity("Engineer");
        healCount = 0;
        cooldown = 0;
    }

    /**
     * Performs the Engineer's action. If a maintenance bot is adjacent to the engineer, it will move to it and repair it. If no maintenance bots nearby and health is low, move to a nearby powercell. Otherwise, wander randomly. Reproduce if over 130 energy.
     */
    @Override
    public void doTimeStep() {

        if (cooldown == 0) {

            List<Entity> bots = getInstances("MaintenanceBot");

            int myX = getX();
            int myY = getY();

            // check all directions
            for (int dir = 0; dir < 8; dir++) {

                int targetX = adjacentX(myX, dir);
                int targetY = adjacentY(myY, dir);

                for (Entity e : bots) {
                    if ((e.getX() == targetX) && (e.getY() == targetY)) {
                        walk(dir);
                        return;
                    }
                }
            }
        } else {
            cooldown--;
        }

        // if no maintenace bots nearby and energy low, go to powercell
        if (this.getEnergy() < 40) {
            List<Entity> pCells = getInstances("PowerCell");

            int myX = getX();
            int myY = getY();
            for (int dir = 0; dir < 8; dir++) {
                int targetX = adjacentX(myX, dir);
                int targetY = adjacentY(myY, dir);

                for (Entity e : pCells) {
                    if ((e.getX() == targetX) && (e.getY() == targetY)) {
                        walk(dir);
                        return;
                    }
                }
            }
        }


        // if nothing nearby, wander around and check if energy high enough to reproduce
        walk(getRandomInt(8));

        if (this.getEnergy() > 130) {
            reproduce(getRandomInt(8));
        }

    }

    // engineers don't fight

    /**
     * Engineers always try to flee the fight
     * @param other the other entity in the fight
     * @return always false
     */
    @Override
    public boolean fight(String other) {
        //if (Entity.getRandomInt(2) == 0) {
        walk(getRandomInt(8)); // tries to flee
        //}
        return false;
    }


    // helper methods

    /**
     * Returns the x coordinate of the tile adjacent in the engineer's row in the given direction
     * @param x the x coordinate of the engineer
     * @param direction one of the 8 directions that we want to check
     * @return the x coordinate of the adjacent tile
     */
    private int adjacentX(int x, int direction) {
        if (direction == 7 || direction == 0 || direction == 1) {
            return (x + 1) % Params.world_width;
        }
        if (direction == 3 || direction == 4 || direction == 5) {
            return (x - 1 + Params.world_width) % Params.world_width;
        }
        return x;
    }

    /**
     * Returns the y coordinate of the tile adjacent in the engineer's column in the given direction
     * @param y the y coordinate of the engineer
     * @param direction one of the 8 directions that we want to check
     * @return the y coordinate of the adjacent tile
     */
    private int adjacentY(int y, int direction) {
        if (direction == 5 || direction == 6 || direction == 7) {
            return (y + 1) % Params.world_height;
        }
        if (direction == 1 || direction == 2 || direction == 3) {
            return (y - 1 + Params.world_height) % Params.world_height;
        }
        return y;
    }

    /**
     * Increments the total number of repairs performed by engineers. Used by runStats.
     */
    public void incrementHealCount() {
        healCount++;
        cooldown = 8;
    }


    /**
     * Prints the number of engineers and the average number of heals per engineer
     * @param entities The list to run the stats on
     */
    public static void runStats(List<Entity> entities) {
        int total = entities.size();

        if (total == 0) {
            System.out.println("0 Engineers");
            return;
        }

        int totalHeal = 0;

        for (Entity e : entities) {
            Engineer en = (Engineer) e;

            totalHeal += en.healCount;
        }

        System.out.println(total + " Engineers");

        System.out.println("Average Number of Heals: " + (1.0 * totalHeal / total));

    }

    /**
     * The toString is used by the show method
     * @return always returns "E"
     */
    public String toString() {
        return "E";
    }

}
