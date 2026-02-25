import java.util.ArrayList;
import java.util.List;

/**
 * MaintenanceBot — an autonomous robot that evolves its movement over time.
 * 
 * MaintenanceBots use a simple genetic algorithm to determine how they turn.
 * Each bot has 8 genes (one per direction offset 0-7) that sum to GENE_TOTAL.
 * At each step, a random roll against the gene distribution determines
 * how much the bot turns relative to its current direction.
 * 
 * When a MaintenanceBot reproduces, the offspring inherits the parent's genes
 * with a small random mutation: one gene loses a point and another gains one.
 * Over many generations, bots that move efficiently (finding more PowerCells)
 * survive longer and reproduce more, so the population evolves toward
 * better movement strategies.
 * 
 * MaintenanceBots always fight when they encounter another entity.
 * 
 * Display character: M
 */
public class MaintenanceBot extends Entity {

    private static final int GENE_TOTAL = 24;
    private int[] genes = new int[8];
    private int dir;

    private boolean healed;

    public MaintenanceBot() {
        for (int k = 0; k < 8; k += 1) {
            genes[k] = GENE_TOTAL / 8;
        }
        dir = Entity.getRandomInt(8);

        healed = false;
    }

    public boolean getHealed() {
        return healed;
    }

    public void setHealed(boolean b) {
        healed = b;
    }

    // TODO: Implement the required methods for this entity.
    //
    // Behavior summary:
    //   - Each step: walk forward in the current direction
    //   - If energy > 150: reproduce with gene mutation
    //     (copy genes to child, randomly subtract 1 from a nonzero gene,
    //      randomly add 1 to any gene)
    //   - After moving: pick a new direction by rolling against the gene
    //     distribution. The roll determines a turn offset (0-7) which is
    //     added to the current direction (mod 8).
    //
    // Also implement a static runStats method that reports:
    //   - Total number of bots
    //   - Percentage of gene weight allocated to straight, right, back, left
    //     (straight = genes[0], right = genes[1]+[2]+[3],
    //      back = genes[4], left = genes[5]+[6]+[7])

    @Override
    public Entity reproduce(int direction) {
        Entity childEntity = super.reproduce(direction);

        MaintenanceBot child = (MaintenanceBot) childEntity;

        for (int k = 0; k < 8; k += 1) {
            child.genes[k] = this.genes[k];
        }

        // decrement a random gene
        int rand1 = Entity.getRandomInt(8);
        while (child.genes[rand1] == 0) {
            rand1 = Entity.getRandomInt(8);
        }

        child.genes[rand1] = child.genes[rand1] - 1;

        // increment a random gene
        int rand2 = Entity.getRandomInt(8);
        while (rand2 == rand1) {
            rand2 = Entity.getRandomInt(8);
        }

        child.genes[rand2] = child.genes[rand2] + 1;

        return child;
    }

    @Override
    public void doTimeStep() {
        // walk
        this.walk(dir);

        // reproduce if possible
        if (this.getEnergy() > 150) {
            this.reproduce(getRandomInt(8));
        }

        // pick new direction
        int rand = Entity.getRandomInt(GENE_TOTAL);
        int count = 0;
        int newDir = -1;
        while (count < rand) {
            newDir++; // starts at 0
            count = count + this.genes[newDir];
        }

        dir = newDir;

        healed = false; // reset to false at beginning of new timestep

    }

    // tries to run away from the fight
    @Override
    public boolean fight(String other) {
        this.doTimeStep();
        return true;
    }


    public static void runStats(List<Entity> entities) {
        int total = entities.size();

        if (total == 0) {
            System.out.println("0 MaintenanceBots");
            return;
        }

        int totalStraight = 0;
        int totalRight = 0;
        int totalBack = 0;
        int totalLeft = 0;

        for (Entity e : entities) {
            MaintenanceBot bot = (MaintenanceBot) e;

            totalStraight += bot.genes[0];
            totalRight += bot.genes[1] + bot.genes[2] + bot.genes[3];
            totalBack += bot.genes[4];
            totalLeft += bot.genes[5] + bot.genes[6] + bot.genes[7];
        }

        int geneSum = total * GENE_TOTAL;

        System.out.println(total + " MaintenanceBots");

        System.out.println("Straight: " + (100.0 * totalStraight / geneSum) + "%");
        System.out.println("Right: " + (100.0 * totalRight / geneSum) + "%");
        System.out.println("Back: " + (100.0 * totalBack / geneSum) + "%");
        System.out.println("Left: " + (100.0 * totalLeft / geneSum) + "%");

    }

    public String toString() {
        return "M";
    }
}
