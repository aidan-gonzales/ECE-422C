import java.util.ArrayList;
import java.util.List;

/**
 * PowerCell — the basic energy source for the space station.
 * 
 * PowerCells represent solar panels and batteries. They do not move on their
 * own, but they gain energy each step through solar charging. They never
 * fight — any entity that encounters a PowerCell absorbs its energy.
 * 
 * New PowerCells spawn automatically each time step (see Params).
 * 
 * Display character: *
 */
public class PowerCell extends Entity {

    // TODO: Implement this class

    public PowerCell() {
        //makeEntity("PowerCell");
    }

    @Override
    public void doTimeStep() {
        this.setEnergy(this.getEnergy() + Params.solar_energy_amount);
    }

    // shouldn't ever get called
    @Override
    public boolean fight(String other) {
        return false;
    }

    public static void runStats(List<Entity> entities) {
        int total = entities.size();

        if (total == 0) {
            System.out.println("0 PowerCells");
            return;
        }

        int totalEnergy = 0;

        for (Entity e : entities) {
            PowerCell pc = (PowerCell) e;

            totalEnergy += pc.getEnergy();
        }

        System.out.println(total + " PowerCells");

        System.out.println("Average Energy: " + (1.0 * totalEnergy / total));

    }

    public String toString() {
        return "*";
    }

}
