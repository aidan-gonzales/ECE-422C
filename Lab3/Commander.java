import java.util.ArrayList;
import java.util.List;

/**
 * Commander — a custom entity type (your design).
 * 
 * Design a Commander entity that fills a meaningful role in the space
 * station ecosystem. Think about:
 *   - How does it move? (random, patrol pattern, toward/away from others?)
 *   - When does it fight? (always, never, conditionally?)
 *   - When does it reproduce? (at what energy threshold?)
 *   - What niche does it fill that helps the station stay self-sustaining?
 * 
 * Display character: C
 */
public class Commander extends Entity {

    // TODO: Implement this class

    private int fightCount;

    private int dir;
    private int stepCount;
    private static final int PATROL_LENGTH = 5;
    private int dx;
    private int dy;

    public Commander() {
        //makeEntity("Commander");
        dir = (getRandomInt(4) * 2); // a random direction, not diagonal
        stepCount = 0;

        fightCount = 0;

        dx = 0;
        dy = 0;
    }

    private Entity detectNearbyEntities() {
        Entity closest = null;
        int closestDistance = Integer.MAX_VALUE;
        int distance = closestDistance;
        int newDir = -1;


        for (Entity e : Entity.getPopulation()) {
            if (e instanceof Commander) continue;

            dx = e.getX() - this.getX();
            dy = e.getY() - this.getY();

            if (Math.abs(dx) > 2 || Math.abs(dy) > 2) continue;

            if (dx == 0) {
                distance = Math.abs(dy);
            } else if (dy == 0) {
                distance = Math.abs(dx);
            } else if (Math.abs(dx) == Math.abs(dy)) {
                distance = Math.abs(dy);
            }

            if (distance < closestDistance) { // square radius
                closest = e;
                closestDistance = distance;
            }
        }

        return closest;
    }

    @Override
    public void doTimeStep() {
        Entity e = detectNearbyEntities();
        if (e == null) { // if no entity found or if no maintenancebot found, patrol
            if (stepCount >= PATROL_LENGTH) {
                stepCount = 0;
                dir = (dir + 2) % 8;
            }

            this.walk(dir);
        } else { // if entity found, move to it
            // 8 directions: 0=E, 1=NE, 2=N, 3=NW, 4=W, 5=SW, 6=S, 7=SE
            if (dx == 0) {
                switch (dy) {
                    case -2:
                        dir = 2;
                        this.run(dir);
                        break;
                    case -1:
                        dir = 2;
                        this.walk(dir);
                        break;
                    case 1:
                        dir = 6;
                        this.walk(dir);
                        break;
                    case 2:
                        dir = 6;
                        this.run(dir);
                        break;
                }
            } else if (dy == 0) {
                switch (dx) {
                    case -2:
                        dir = 4;
                        this.run(dir);
                        break;
                    case -1:
                        dir = 4;
                        this.walk(dir);
                        break;
                    case 1:
                        dir = 0;
                        this.walk(dir);
                        break;
                    case 2:
                        dir = 0;
                        this.run(dir);
                        break;
                }
            } else if (Math.abs(dx) == Math.abs(dy)) {
                if ((dx > 0) && (dy > 0)) {
                    dir = 7;
                } else if ((dx > 0) && (dy < 0)) {
                    dir = 1;
                } else if ((dx < 0) && (dy > 0)) {
                    dir = 5;
                } else if ((dx < 0) && (dy < 0)) {
                    dir = 3;
                }

                if (dx == 2) {
                    this.run(dir);
                } else {
                    this.walk(dir);
                }

            }

        }


        if (this.getEnergy() > 200) {
            reproduce((dir + 2) % 8);
        }

        stepCount++;

    }

    @Override
    public boolean fight(String other) {
        int chance = Entity.getRandomInt(10);
        if (chance < 4) {
            fightCount++;
            return true;
        } else {
            return false;
        }
    }

    public static void runStats(List<Entity> entities) {
        int total = entities.size();

        if (total == 0) {
            System.out.println("0 Commanders");
            return;
        }

        int totalFights = 0;

        for (Entity e : entities) {
            Commander c = (Commander) e;

            totalFights += c.fightCount;
        }

        System.out.println(total + " Commanders");

        System.out.println("Average Number of Fights: " + (1.0 * totalFights / total));

    }

    public String toString() {
        return "C";
    }

}
