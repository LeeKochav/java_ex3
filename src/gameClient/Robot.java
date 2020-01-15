package gameClient;

import dataStructure.node_data;
import org.json.JSONObject;
import utils.Point3D;

import java.util.LinkedList;

public class Robot {

    private int id;
    private double value;
    private int src;
    private int dest;
    private  double speed;
    private Point3D location;
    private Point3D gui_location;


    public  Robot(){}

    public Robot(String jsonSTR)
    {
        this();
        try {
            JSONObject robot = new JSONObject(jsonSTR);
            robot=robot.getJSONObject("Robot");
            double val = robot.getDouble("value");
            int src=robot.getInt("src");
            int id=robot.getInt("id");
            int dst=robot.getInt("dest");
            double speed=robot.getDouble("speed");
            String pos=robot.getString("pos");
            this.value=val;
            this.location=new Point3D(pos);
            this.src=src;
            this.dest=dst;
            this.speed=speed;
            this.id=id;
            this.setGui_location(0,0);
        }
        catch (Exception e)
        {

            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getSrc() {
        return src;
    }

    public void setSrc(int src) {
        this.src = src;
    }

    public int getDest() {
        return dest;
    }

    public void setDest(int dest) {
        this.dest = dest;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public Point3D getLocation() {
        return location;
    }

    public void setLocation(Point3D location) {
        this.location = location;
    }

    public String toJSON() {
        String ans = "{\"Robot\":{\"id\":" + this.id + "," + "\"value\":" + this.value + "," + "\"src\":" + this.src + "," + "\"dest\":" + this.dest + "," + "\"speed\":" + this.getSpeed() + "," + "\"pos\":\"" + this.location.toString() + "\"" + "}" + "}";
        return ans;
    }
    public String toJSON2() {
        String ans = "{\"Robot\":{\"id\":" + this.id + "," + "\"value\":" + this.value + "," + "\"src\":" + this.src + "," + "\"dest\":" + this.dest + "," + "\"speed\":" + this.getSpeed() + "," + "\"pos\":\"" + this.location.toString() +"\"pos_gui\":\"" + this.getGui_location().toString() + "\"" + "}" + "}";
        return ans;
    }
    public String toString() {
        return this.toJSON2();
    }

    public Point3D getGui_location() {
        return gui_location;
    }

    public void setGui_location(double x, double y) {
        this.gui_location = new Point3D(x,y);
    }


}
