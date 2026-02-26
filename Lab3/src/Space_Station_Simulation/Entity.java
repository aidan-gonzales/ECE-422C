package Space_Station_Simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map; // I added this
import java.util.HashMap; // I added this
import java.util.Iterator; // I added this

/**
 * The parent class for all entity types.
 */
public abstract class Entity {
    private static List<Entity> population = new java.util.ArrayList<Entity>();
    private static List<Entity> babies = new java.util.ArrayList<Entity>();

    protected static List<Entity> getPopulation() {
        return population;
    } // I added this
    
    // 2D world representation as 1D list for efficiency
    // Each grid cell holds a list of entities at that location
    private static List<List<Entity>> world = new ArrayList<>(Params.world_width * Params.world_height);
    private static List<List<Boolean>> hasWalked = new ArrayList<>(Params.world_width * Params.world_height);
    private static Entity[] fighters = new Entity[2];

    static {
        for (int i = 0; i < Params.world_width * Params.world_height; i++) {
            world.add(new ArrayList<Entity>());
            hasWalked.add(new ArrayList<Boolean>());
        }
    }

    /* ========================================================================
     * Random number generation — DO NOT MODIFY
     * ======================================================================== */

    private static java.util.Random rand = new java.util.Random();
    public static int getRandomInt(int max) {
        return rand.nextInt(max);
    }

    public static void setSeed(long new_seed) {
        rand = new java.util.Random(new_seed);
    }

    /* ========================================================================
     * Instance fields — DO NOT MODIFY these field declarations
     * ======================================================================== */

    private int energy = 0;
    protected int getEnergy() { return energy; }
    protected void setEnergy(int energy) { this.energy = energy; }

    private int x_coord;
    private int y_coord;

    // I ADDED THESE HELPERS

    /**
     * Returns the x position of an entity
     * @return the x value
     */
    protected int getX() {
        return x_coord;
    }

    /**
     * Returns the y position of an entity
     * @return the y value
     */
    protected int getY() {
        return y_coord;
    }


    /* ========================================================================
     * Movement
     * 
     * Entities can walk (move 1 square) or run (move 2 squares) in one of
     * 8 directions: 0=E, 1=NE, 2=N, 3=NW, 4=W, 5=SW, 6=S, 7=SE
     * 
     * The world wraps around at the edges (toroidal topology).
     * Movement costs energy as defined in Params.
     * 
     * The moveConditionals method handles the special case where an entity
     * is currently in an encounter (fight). During encounters, movement is
     * restricted — an entity that has already moved cannot move again, and
     * fighters can only move to occupied cells.
     * ======================================================================== */

    protected final void walk(int direction) {
        moveConditionals(direction, 1, Params.walk_energy_cost);
    }

    protected final void run(int direction) {
        moveConditionals(direction, 2, Params.run_energy_cost);
    }

    private void moveConditionals(int direction, int distance, int energyCost) {
        boolean isFighter = (fighters[0] == this || fighters[1] == this);
        
        if (isFighter && checkIfWalked(this)) {
            energy -= energyCost;
            return;
        } else if (isFighter) {
            int prevX = x_coord;
            int prevY = y_coord;
            move(this, direction, distance);
            if (world.get(convertTo1D(x_coord, y_coord)).isEmpty()) {
                x_coord = prevX;
                y_coord = prevY;
            } else {
                x_coord = prevX;
                y_coord = prevY;
                energy -= energyCost;
                return;
            }
        }
        
        removeFromWorld(this);
        move(this, direction, distance);
        energy -= energyCost;
        addToWorld(this);
        markAsWalked(this);
    }

    private static void move(Entity entity, int direction, int distance) {
        // Direction: 0=E, 1=NE, 2=N, 3=NW, 4=W, 5=SW, 6=S, 7=SE
        if (direction == 7 || direction == 0 || direction == 1) {
            entity.x_coord = (entity.x_coord + distance) % Params.world_width;
        }
        if (direction == 3 || direction == 4 || direction == 5) {
            entity.x_coord -= distance;
            if (entity.x_coord < 0) {
                entity.x_coord += Params.world_width;
            }
        }
        if (direction == 5 || direction == 6 || direction == 7) {
            entity.y_coord = (entity.y_coord + distance) % Params.world_height;
        }
        if (direction == 1 || direction == 2 || direction == 3) {
            entity.y_coord -= distance;
            if (entity.y_coord < 0) {
                entity.y_coord += Params.world_height;
            }
        }
    }

    /* ========================================================================
     * Reproduction
     * 
     * When an entity reproduces, the parent's energy is split between parent
     * and offspring. The offspring is placed adjacent to the parent in the
     * given direction.
     * 
     * The parent keeps ceil(energy/2) and the offspring gets floor(energy/2).
     * ======================================================================== */


    // TODO: Implement the reproduce method

    /**
     * Creates a new entity of the same class as the parent, and splits the energy between the parent and child
     * @param direction the direction that the child is placed in relation to the parent
     * @return the child entity created
     */
    public Entity reproduce(int direction) {
        try {
            Entity childEnt = this.getClass().getDeclaredConstructor().newInstance();

            if ((this.getEnergy() % 2) != 0) {
                this.setEnergy(this.getEnergy() / 2 + 1);
                childEnt.setEnergy(this.getEnergy() - 1);
            } else {
                this.setEnergy(this.getEnergy() / 2);
                childEnt.setEnergy(this.getEnergy());
            }

            // set child's location
            childEnt.x_coord = this.getX();
            childEnt.y_coord = this.getY();
            move(childEnt, direction, 1);

            babies.add(childEnt);

            return childEnt;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create entity", e);
        }
    }

    /* ========================================================================
     * Abstract methods
     * 
     * Think carefully about what behaviors differ between entity types and
     * must be implemented by each subclass.
     * ======================================================================== */

    // TODO: Declare the abstract methods that subclasses must implement

    /**
     * Performs the action for the entity that its called on
     */
    public abstract void doTimeStep();

    /**
     * Returns whether this entity wants to fight or not
     * @param other a string containing the class name of the other entity in the fight
     * @return true if the entity wants to fight, false if it doesn't
     */
    public abstract boolean fight(String other);

    /* ========================================================================
     * Entity creation and lookup
     * 
     * makeEntity creates a new entity of the given class name using reflection.
     * It should:
     *   - Reject names that start with a lowercase letter
     *   - Use Class.forName() to find the class
     *   - Create an instance and set its starting energy and random position
     *   - Add it to the population and the world grid
     *   - Throw InvalidEntityException if anything goes wrong
     * 
     * getInstances returns a list of all living entities that are instances
     * of the given class name.
     * ======================================================================== */

    // TODO: Implement makeEntity

    /**
     * Creates an entity of the specified class and initializes its instance variables to the default values
     * @param className the entity type to be created
     */
    public static void makeEntity(String className) {
        if (Character.isLowerCase(className.charAt(0))) {
            throw new InvalidEntityException("Invalid class name. Class Name must start with an uppercase letter.");
        }

        try {
            Class<?> temp = Class.forName(className); // makes a new class with the class name given

            Object tempObj = temp.getDeclaredConstructor().newInstance(); // create instance
/*
            if (!(tempObj instanceof Entity)) {
                throw new InvalidEntityException(className + " is not a subclass of Entity.");
            }
            */


            Entity newEnt = (Entity) tempObj; // cast to entity

            // initialize fields
            newEnt.setEnergy(Params.start_energy);
            newEnt.x_coord = Entity.getRandomInt(Params.world_width);
            newEnt.y_coord = Entity.getRandomInt(Params.world_height);

            // add to population
            population.add(newEnt);

            // add to world
            addToWorld(newEnt);


        } catch (ClassNotFoundException e) {
            throw new InvalidEntityException("Class not found.");
        } catch (Exception e) {
            throw new RuntimeException("Failed to create entity.", e);
        }

    }

    // TODO: Implement getInstances

    /**
     * Gets all of the instances of the class name specified
     * @param className the class to be returned
     * @return a list containing all of the entities of the specified class type
     */
    public static List<Entity> getInstances(String className) {
        if (Character.isLowerCase(className.charAt(0))) {
            throw new InvalidEntityException("Invalid class name. Class Name must start with an uppercase letter.");
        }

        try {
            Class<?> temp = Class.forName(className);

            List<Entity> result = new ArrayList<>();

            for (Entity e : population) {
                if (temp.isInstance(e)) {
                    result.add(e);
                }
            }

            return result;
        } catch (ClassNotFoundException e) {
            throw new InvalidEntityException("Invalid class name. Class not found.");
        }
    }


        /* ========================================================================
     * Statistics
     * 
     * runStats prints a summary of the given entity list. The default
     * implementation counts how many of each display character exist.
     * Individual entity classes may provide their own runStats method
     * with more detailed information.
     * ======================================================================== */

    // TODO: Implement runStats

    /**
     * Prints out the number of each entity type
     * @param entities the list of entities to check the stats of
     */
    public static void runStats(List<Entity> entities) {

        Map<String, Integer> counts = new HashMap<>(); // instantiates map

        for (Entity e : entities) {
            String symbol = e.toString();
            counts.put(symbol, counts.getOrDefault(symbol, 0) + 1); // adds key and value pairs to the map
        }

        for (String symbol : counts.keySet()) {
            System.out.println(symbol + ": " + counts.get(symbol)); // prints the key and value pairs
        }
    }

    /* ========================================================================
     * World grid helpers — DO NOT MODIFY
     * ======================================================================== */

    private static int convertTo1D(int x, int y) {
        return y * Params.world_width + x;
    }

    private static void addToWorld(Entity entity) {
        world.get(convertTo1D(entity.x_coord, entity.y_coord)).add(entity);
        hasWalked.get(convertTo1D(entity.x_coord, entity.y_coord)).add(false);
    }

    private static void removeFromWorld(Entity entity) {
        List<Entity> location = world.get(convertTo1D(entity.x_coord, entity.y_coord));
        int index = location.indexOf(entity);
        if (index >= 0) {
            location.remove(index);
            hasWalked.get(convertTo1D(entity.x_coord, entity.y_coord)).remove(index);
        }
    }

    private static void markAsWalked(Entity entity) {
        List<Entity> location = world.get(convertTo1D(entity.x_coord, entity.y_coord));
        int index = location.indexOf(entity);
        if (index >= 0) {
            hasWalked.get(convertTo1D(entity.x_coord, entity.y_coord)).set(index, true);
        }
    }

    private static boolean checkIfWalked(Entity entity) {
        List<Entity> location = world.get(convertTo1D(entity.x_coord, entity.y_coord));
        int index = location.indexOf(entity);
        if (index >= 0) {
            return hasWalked.get(convertTo1D(entity.x_coord, entity.y_coord)).get(index);
        }
        return false;
    }

    /* ========================================================================
     * TestEntity — DO NOT MODIFY
     * Used internally for testing. You do not need to use this class.
     * ======================================================================== */

    static abstract class TestEntity extends Entity {

        protected void setX_coord(int new_x_coord) {
            super.x_coord = new_x_coord;
        }

        protected void setY_coord(int new_y_coord) {
            super.y_coord = new_y_coord;
        }

        protected int getX_coord() {
            return super.x_coord;
        }

        protected int getY_coord() {
            return super.y_coord;
        }

        protected static List<Entity> getPopulation() {
            return population;
        }

        protected static List<Entity> getBabies() {
            return babies;
        }
    }

    /* ========================================================================
     * World management
     * ======================================================================== */

    public static void clearWorld() {
        population.clear();
        babies.clear();
        for (List<Entity> location : world) {
            location.clear();
        }
        for (List<Boolean> walkStatus : hasWalked) {
            walkStatus.clear();
        }
    }

    // does the fight logic

    /**
     * Executes the fight logic, including checking if the entity wants to fight, and rolling based on energy
     * @param a the first fighting entity
     * @param b the second fighting entity
     */
    private static void doFight(Entity a, Entity b) {
        String aName;
        String bName;
        if (a instanceof MaintenanceBot) {
            aName = "MaintenanceBot";
        } else if (a instanceof PowerCell) {
            aName = "PowerCell";
        } else if (a instanceof Commander) {
            aName = "Commander";
        } else {
            aName = "Engineer";
        }

        if (b instanceof MaintenanceBot) {
            bName = "MaintenanceBot";
        } else if (b instanceof PowerCell) {
            bName = "PowerCell";
        } else if (b instanceof Commander) {
            bName = "Commander";
        } else {
            bName = "Engineer";
        }
        boolean aWantsToFight = a.fight(bName);
        boolean bWantsToFight = b.fight(aName);

        // if one entity fleed successfully, then don't continue with the fight
        if ((a.x_coord != b.x_coord) || (a.y_coord != b.y_coord)) {
            return;
        }

        // roll based on energy level
        int aRoll = 0;
        int bRoll = 0;

        if (aWantsToFight && !(a instanceof MaintenanceBot)) {
            aRoll = getRandomInt(a.getEnergy());
        }

        if (bWantsToFight && !(b instanceof MaintenanceBot)) {
            bRoll = getRandomInt(b.getEnergy());
        }

        // do the actual fight
        if (aRoll >= bRoll) {
            a.setEnergy(a.getEnergy() + (b.getEnergy() / 2));
            b.setEnergy(0);
            removeFromWorld(b);
            population.remove(b);
        } else {
            b.setEnergy(b.getEnergy() + (a.getEnergy() / 2));
            a.setEnergy(0);
            removeFromWorld(a);
            population.remove(a);
        }
    }

    /**
     * Executes the repair encounter between maintenance bots and engineers
     * @param engineer the engineer that is doing the repair
     * @param bot the maintenance bot that is getting repaired
     */
    public static void heal(Engineer engineer, MaintenanceBot bot) {
        int healAmount = 20; // amount of energy the bot gains
        int engineerHeal = 50; // energy rejuvinated for Engineer

        if (engineer.getEnergy() < 10) { // too tired to heal
            return; // not enough energy to heal
        }

        engineer.setEnergy(engineer.getEnergy() + engineerHeal);
        bot.setEnergy(bot.getEnergy() + healAmount);


        engineer.incrementHealCount();
    }


    // resolve encounters between entities in the same cell

    /**
     * Resolves encounters between entities in the same cell
     * @param cell the list of entities in the cell being checked
     */
    private static void resolveEncounters(List<Entity> cell) {

        for (int i = 0; i < cell.size() - 1; i++) {
            for (int j = i + 1; j < cell.size(); j++) {
                Entity a = cell.get(i);
                Entity b = cell.get(j);

                // if either died
                if (a.getEnergy() <= 0 || b.getEnergy() <= 0) {
                    continue;
                }

                // if one fled
                if (a.x_coord != b.x_coord || a.y_coord != b.y_coord) {
                    continue;
                }

                fighters[0] = a;
                fighters[1] = b;

                // if a is a powercell
                if ((a instanceof PowerCell) && !(b instanceof PowerCell)) {
                    if (b instanceof MaintenanceBot) {
                        b.setEnergy((int) ((double)b.getEnergy() + (a.getEnergy() * 1.5)));
                    } else {
                        b.setEnergy(b.getEnergy() + a.getEnergy());
                    }

                    a.setEnergy(0);
                    //removeFromWorld(a);
                    //population.remove(a);
                    fighters[0] = null;
                    fighters[1] = null;
                    continue;
                }

                // if b is a powercell
                if ((b instanceof PowerCell) && !(a instanceof PowerCell)) {
                    if (a instanceof MaintenanceBot) {
                        a.setEnergy((int) ((double)a.getEnergy() + (b.getEnergy() * 1.5)));
                    } else {
                        a.setEnergy(a.getEnergy() + b.getEnergy());
                    }
                    b.setEnergy(0);
                    //removeFromWorld(b);
                    //population.remove(b);
                    fighters[0] = null;
                    fighters[1] = null;
                    continue;
                }

                // if a is an engineer and b is a maintenancebot
                if ((a instanceof Engineer) && (b instanceof MaintenanceBot)) {
                    if (((MaintenanceBot) b).getHealed() == false) {
                        heal((Engineer) a, (MaintenanceBot) b);
                        ((MaintenanceBot) b).setHealed(true);
                    }
                    fighters[0] = null;
                    fighters[1] = null;
                    continue;
                }

                // if b is an engineer and a is a maintenancebot
                if ((b instanceof Engineer) && (a instanceof MaintenanceBot)) {
                    if (((MaintenanceBot) a).getHealed() == false) {
                        heal((Engineer) b, (MaintenanceBot) a);
                        ((MaintenanceBot) a).setHealed(true);
                    }
                    fighters[0] = null;
                    fighters[1] = null;
                    continue;
                }

                doFight(a, b);

                fighters[0] = null;
                fighters[1] = null;
            }
        }
        /*
        while (cell.size() > 1) {

            Entity a = cell.get(0);
            Entity b = cell.get(1);

            fighters[0] = a;
            fighters[1] = b;

            // if a is a powercell
            if ((a instanceof PowerCell) && !(b instanceof PowerCell)) {
                b.setEnergy(b.getEnergy() + a.getEnergy());
                a.setEnergy(0);
                removeFromWorld(a);
                population.remove(a);
                fighters[0] = null;
                fighters[1] = null;
                continue;
            }

            // if b is a powercell
            if ((b instanceof PowerCell) && !(a instanceof PowerCell)) {
                a.setEnergy(a.getEnergy() + b.getEnergy());
                b.setEnergy(0);
                removeFromWorld(b);
                population.remove(b);
                fighters[0] = null;
                fighters[1] = null;
                continue;
            }

            // if a is an engineer and b is a maintenancebot
            if ((a instanceof Engineer) && (b instanceof MaintenanceBot)) {
                if (((MaintenanceBot) b).getHealed() == false) {
                    heal((Engineer) a, (MaintenanceBot) b);
                    ((MaintenanceBot) b).setHealed(true);
                }
                fighters[0] = null;
                fighters[1] = null;
                continue;
            }

            // if b is an engineer and a is a maintenancebot
            if ((b instanceof Engineer) && (a instanceof MaintenanceBot)) {
                if (((MaintenanceBot) a).getHealed() == false) {
                    heal((Engineer) b, (MaintenanceBot) a);
                    ((MaintenanceBot) a).setHealed(true);
                }
                fighters[0] = null;
                fighters[1] = null;
                continue;
            }

            doFight(a, b);

            fighters[0] = null;
            fighters[1] = null;
        }
        */

    }

    /**
     * Advances the simulation by one time step. The order of operations
     * within a single time step is:
     *   1. Each existing entity performs its action for this step
     *   2. Resolve encounters — when multiple entities share a location
     *   3. Deduct rest energy cost and remove dead entities
     *   4. Add any offspring born this step to the world
     *   5. Reset movement tracking for the next step
     *   6. Spawn new PowerCells (Params.refresh_powercell_count per step)
     */

    // TODO: Implement worldTimeStep and any helper methods it needs

    public static void worldTimeStep() {

        // 1. each entity performs its action
        for (Entity e : population) {
            e.doTimeStep();
        }


        // 2. resolve encounters
        for (int i = 0; i < world.size(); i++) {
            List<Entity> cell = world.get(i); // cell is the list of entities at this specific coordinate
            if (cell.size() > 1) {
                resolveEncounters(cell);
            }
        }


        // 3. deduct rest energy cost and remove dead entities
        Iterator<Entity> it = population.iterator();
        while (it.hasNext()) {
            Entity e = it.next();
            e.setEnergy(e.getEnergy() - Params.rest_energy_cost);
            if (e.getEnergy() <= 0) {
                removeFromWorld(e);
                it.remove();
            }
        }

        // 4. Add offspring to world
        Iterator<Entity> babyIt = babies.iterator();
        while (babyIt.hasNext()) {
            Entity e = babyIt.next();
            addToWorld(e);
            population.add(e);
            babyIt.remove();
        }


        // 5. Reset movement tracking
        for (List<Boolean> cellStatus : hasWalked) {
            for (int i = 0; i < cellStatus.size(); i++) {
                cellStatus.set(i, false);
            }
        }


        // 6. Spawn new PowerCells
        for (int i = 0; i < Params.refresh_powercell_count; i++) {
            makeEntity("PowerCell");
        }



    }

    /* ========================================================================
     * Display — DO NOT MODIFY
     * ======================================================================== */

    public static void displayWorld() {
        System.out.print("+");
        for (int i = 0; i < Params.world_width; i++) {
            System.out.print("-");
        }
        System.out.println("+");

        for (int y = 0; y < Params.world_height; y++) {
            System.out.print("|");
            for (int x = 0; x < Params.world_width; x++) {
                List<Entity> location = world.get(convertTo1D(x, y));
                if (location.isEmpty()) {
                    System.out.print(" ");
                } else {
                    System.out.print(location.get(0).toString());
                }
            }
            System.out.println("|");
        }

        System.out.print("+");
        for (int i = 0; i < Params.world_width; i++) {
            System.out.print("-");
        }
        System.out.println("+");
    }
}
