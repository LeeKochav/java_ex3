package gameClient;

import org.json.JSONObject;
import utils.Point3D;

/**
 * This class represents the set of operations applicable on a robot.
 * Robot attributes:
 * 1. id
 * 2.value- represent the sum of collecting fruits values in the game.
 * 3. src- related to the source node that the robot is on it.
 * 4. dest- related to the destination node that the robot is moving to.
 * 5.Location
 * 6. speed- robot's speed, increases during the game when the robot collects fruits.
 */
public class Robot {

    private int id;
    private double value;
    private int src;
    private int dest;
    private double speed;
    private Point3D location;
    private Point3D gui_location;

    //Default constructor
    public Robot() {
    }

    /**
     * Constructor init robot attributes from a json string input.
     *
     * @param jsonSTR
     */
    public Robot(String jsonSTR) {
        this();
        try {
            JSONObject robot = new JSONObject(jsonSTR);
            robot = robot.getJSONObject("Robot");
            double val = robot.getDouble("value");
            int src = robot.getInt("src");
            int id = robot.getInt("id");
            int dst = robot.getInt("dest");
            double speed = robot.getDouble("speed");
            String pos = robot.getString("pos");
            this.value = val;
            this.location = new Point3D(pos);
            this.src = src;
            this.dest = dst;
            this.speed = speed;
            this.id = id;
            this.setGui_location(0, 0);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * Getter for the id of the robot
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Setter for the robot id
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter for the robot value
     *
     * @return
     */
    public double getValue() {
        return value;
    }

    /**
     * Setter for the robot value
     *
     * @param value
     */
    public void setValue(double value) {
        this.value = value;
    }

    /**
     * Getter for the robot source node that the robot is on it.
     *
     * @return
     */
    public int getSrc() {
        return src;
    }

    /**
     * Setter for the robot source node that the robot is on it.
     *
     * @param src
     */
    public void setSrc(int src) {
        this.src = src;
    }

    /**
     * Getter for the robot destination node
     *
     * @return dest
     */
    public int getDest() {
        return dest;
    }

    /**
     * Setter for the robot destination node
     *
     * @param dest
     */
    public void setDest(int dest) {
        this.dest = dest;
    }

    /**
     * Getter for the robot's speed attribute
     *
     * @return
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Setter for the robot's speed
     *
     * @param speed
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * Getter for the robot's location
     *
     * @return
     */
    public Point3D getLocation() {
        return location;
    }

    /**
     * Setter for the robot's location
     *
     * @param location
     */
    public void setLocation(Point3D location) {
        this.location = location;
    }

    /**
     * Return a string that represents the robot
     *
     * @return
     */
    public String toJSON() {
        String ans = "{\"Robot\":{\"id\":" + this.id + "," + "\"value\":" + this.value + "," + "\"src\":" + this.src + "," + "\"dest\":" + this.dest + "," + "\"speed\":" + this.getSpeed() + "," + "\"pos\":\"" + this.location.toString() + "\"" + "}" + "}";
        return ans;
    }

    /**
     * Return a string that represents a robot using a helper funtion- toJson
     *
     * @return
     */
    public String toString() {
        return this.toJSON();
    }

    /**
     * Getter for the gui location of the robot, relevant for drawing a robot in the gui.
     *
     * @return
     */
    public Point3D getGui_location() {
        return gui_location;
    }

    /**
     * Setter for the gui location of the robot .
     *
     * @param x  - x point coordinate
     * @param y- y point coordinate
     */
    public void setGui_location(double x, double y) {
        this.gui_location = new Point3D(x, y);
    }


}
