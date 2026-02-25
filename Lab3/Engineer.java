import java.util.ArrayList;
import java.util.List;

/**
 * Engineer — a custom entity type (your design).
 * 
 * Design an Engineer entity that fills a different role from your Commander.
 * The two custom entities should complement each other in keeping the
 * ecosystem balanced. Think about:
 *   - How does it move? (random, patrol pattern, toward/away from others?)
 *   - When does it fight? (always, never, conditionally?)
 *   - When does it reproduce? (at what energy threshold?)
 *   - What happens to the ecosystem if you remove all Engineers?
 * 
 * Display character: E
 */
public class Engineer extends Entity {

    // TODO: Implement this class

    private int healCount;
    private int cooldown;

    public Engineer() {
        //makeEntity("Engineer");
        healCount = 0;
        cooldown = 0;
    }

    @Override
    public void doTimeStep() {

        if (cooldown == 0) {

            List<Entity> bots = Entity.getInstances("MaintenanceBot");

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
            List<Entity> pCells = Entity.getInstances("PowerCell");

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
    @Override
    public boolean fight(String other) {
        //if (Entity.getRandomInt(2) == 0) {
        walk(getRandomInt(8)); // tries to flee
        //}
        return false;
    }


    // helper methods
    private int adjacentX(int x, int direction) {
        if (direction == 7 || direction == 0 || direction == 1) {
            return (x + 1) % Params.world_width;
        }
        if (direction == 3 || direction == 4 || direction == 5) {
            return (x - 1 + Params.world_width) % Params.world_width;
        }
        return x;
    }

    private int adjacentY(int y, int direction) {
        if (direction == 5 || direction == 6 || direction == 7) {
            return (y + 1) % Params.world_height;
        }
        if (direction == 1 || direction == 2 || direction == 3) {
            return (y - 1 + Params.world_height) % Params.world_height;
        }
        return y;
    }
    public void incrementHealCount() {
        healCount++;
        cooldown = 8;
    }


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

    public String toString() {
        return "E";
    }

}
