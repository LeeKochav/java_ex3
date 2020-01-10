package gameClient;

import dataStructure.Edge;
import dataStructure.edge_data;
import org.json.JSONObject;
import utils.Point3D;

public class Fruit {

    private Point3D _pos;
    private Point3D guiPos;
    private double _value;
    private int type;
    private edge_data _edge;


    public Fruit() {}

    public Fruit(double v, Point3D p) {
        this._value = v;
        this._pos = new Point3D(p);
    }

    public Fruit(String jsonSTR)
    {
        this();
        try {
            JSONObject fruit = new JSONObject(jsonSTR);
            fruit=fruit.getJSONObject("Fruit");
            double val = fruit.getDouble("value");
            this._value=val;
            String pos=fruit.getString("pos");
            this._pos=new Point3D(pos);
            int t=fruit.getInt("type");
            this.type=t;
        }
        catch (Exception e)
        {

            e.printStackTrace();
        }
    }
    public int getType() {
      return this.type;
    }
    public Point3D getLocation() {
        return new Point3D(this._pos);
    }

    public Point3D getLocationGui() {
        return new Point3D(this.guiPos);
    }

    public void setLocationGui(double x,double y) {
        this.guiPos=new Point3D(x,y);}

    public String toString() {
        return this.toJSON();
    }

    public String toJSON() {

        String ans = "{\"Fruit\":{\"value\":" + this._value + "," + "\"type\":" + this.type + "," + "\"pos\":\"" + this._pos.toString() + "\"" + "}" + "}";
        return ans;
    }

    public double getValue() {
        return this._value;
    }
    public void setEdge(edge_data e)
    {
        this._edge=e;
    }

    public edge_data getEdge()
    {
        return this._edge;
    }

}
