import java.awt.Point;
import java.util.ArrayList;

public class Ship {

    private String name;
    private ArrayList<Point> cells = new ArrayList<>();
    private int size;
    private int unhit;

    /**
     * Ship constructor
     * @param n name of the ship
     * @param s size of the ship
     * @param start first input into the cells arraylist
     * @param d direction that the ship faces that determines the rest of the arraylist inputs
     *          0 = right, 1 = up, 2 = left, 3 = down
     */
    public Ship(String n, int s, Point start, int d) {
        name = n;
        size = s;
        unhit = size;
        Point currentPoint = new Point(start.x, start.y);
        cells.add(currentPoint);
        for (int i = 1; i < size; i++) {
            switch (d) {
                case 0:
                    currentPoint.x++;
                    break;
                case 1:
                    currentPoint.y--;
                    break;
                case 2:
                    currentPoint.x--;
                case 3:
                    currentPoint.y++;
                    break;
            }
            cells.add(currentPoint);
        }
    }

    /**
     * Helper method to get the name of the ship
     * @return ship name
     */
    public String getName() {
        return name;
    }

}
