package gameClient;

import dataStructure.edge_data;
import org.json.JSONObject;
import utils.Point3D;

/**
 * This class represents the set of operations applicable on a fruit.
 * Fruit sttributes are:
 * 1.Location
 * 2.Value
 * 3.Type- Apple/Banana.
 * 4.Edge- which fruit is located on
 */

public class Fruit {

    private Point3D _pos;
    private Point3D guiPos;
    private double _value;
    private int type;
    private edge_data _edge;


    //Default constructor
    public Fruit() {
    }


    public Fruit(double v, Point3D p) {
        this._value = v;
        this._pos = new Point3D(p);
    }

    /**
     * Constructor init fruit attributes from a json string input.
     *
     * @param jsonSTR
     */
    public Fruit(String jsonSTR) {
        this();
        try {
            JSONObject fruit = new JSONObject(jsonSTR);
            fruit = fruit.getJSONObject("Fruit");
            double val = fruit.getDouble("value");
            this._value = val;
            String pos = fruit.getString("pos");
            this._pos = new Point3D(pos);
            int t = fruit.getInt("type");
            this.type = t;
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * Getter for type attribute.
     * Type is terminate for each fruit by game server.
     * Type can be - Apple/Banana
     * Apple - indicates that the fruit edge is from source lower key node to higher destination node key.
     * Banana- indicates that the fruit edge is from source higher key node to lower destination node key.
     *
     * @return type
     */
    public int getType() {
        return this.type;
    }

    /**
     * Getter for location attribute
     *
     * @return return pos
     */
    public Point3D getLocation() {
        return new Point3D(this._pos);
    }

    /**
     * Getter for location of the fruit on the gui window
     * This parameter is relevant for fruit drawing in the game.
     *
     * @return guiPos
     */
    public Point3D getLocationGui() {
        return new Point3D(this.guiPos);
    }

    /**
     * Setter for location of the fruit on the gui window.
     *
     * @param x  - x point coordinate
     * @param y- y point coordinate
     */

    public void setLocationGui(double x, double y) {
        this.guiPos = new Point3D(x, y);
    }

    /**
     * toString- return the string that represents a fruit, using a helper function- toJson
     *
     * @return fruit- as string
     */
    public String toString() {
        return this.toJSON();
    }

    /**
     * Return the string that represents a fruit
     *
     * @return
     */
    public String toJSON() {

        String ans = "{\"Fruit\":{\"value\":" + this._value + "," + "\"type\":" + this.type + "," + "\"pos\":\"" + this._pos.toString()+ "\"" + "}" + "}";
        return ans;
    }

    /**
     * Getter for the value attribute of the fruit.
     *
     * @return value
     */
    public double getValue() {
        return this._value;
    }

    /**
     * Setter for the edge that the fruit is on it/
     *
     * @param e - edge
     */
    public void setEdge(edge_data e) {
        this._edge = e;
    }

    /**
     * Getter for the edge that the fruit is on it
     *
     * @return this.edge
     */
    public edge_data getEdge() {
        return this._edge;
    }

}
