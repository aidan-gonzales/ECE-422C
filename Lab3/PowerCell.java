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

    /**
     * Does nothing. No instance variables to initialize in this class.
     */
    public PowerCell() {
        //makeEntity("PowerCell");
    }

    /**
     * Increments the powercell's energy by the solar energy amount specified by Params.
     */
    @Override
    public void doTimeStep() {
        this.setEnergy(this.getEnergy() + Params.solar_energy_amount);
    }

    // shouldn't ever get called

    /**
     * PowerCells don't fight.
     * @param other The other entity in the fight
     * @return always false
     */
    @Override
    public boolean fight(String other) {
        return false;
    }

    /**
     * Prints the number of PowerCells and the average energy per PowerCell.
     * @param entities the list of entities to run the stats on
     */
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

    /**
     * Called by the show method
     * @return always returns "*"
     */
    public String toString() {
        return "*";
    }

}
