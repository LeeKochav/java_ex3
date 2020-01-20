package dataStructure;

import utils.Point3D;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class Node implements node_data, Serializable {
    private int key;
    private Point3D location;
    private String info;
    private int tag;
    private double weight;
    private Point3D gui_location;

    public Node(){}
    public Node(int key, Point3D p) {
        this.key=key;
        this.setLocation(p);
    }

    public Node(int key)
    {
        this.key=key;
        setWeight(Double.MAX_VALUE);
        setTag(1);
        this.info=null;
        setLocation(new Point3D((double)this.getKey(),(double)this.getKey()));
    }
    /*
    Copy constructor
     */
    public Node(node_data n)
    {
        this.key=n.getKey();
        setWeight(n.getWeight());
        setTag(n.getTag());
        setInfo(n.getInfo());
        setLocation(n.getLocation());
    }


    @Override
    public int getKey() {
        return this.key;
    }

    @Override
    public Point3D getLocation() {
        return new Point3D(this.location);
    }

    @Override
    public void setLocation(Point3D p) {
        this.location=new Point3D(p);
    }

    /** Allows changing this node's Gui location for drawing the node.
     *
     * @param x,y -  new location  (position) of this node.
     */
    @Override
    public void setGuiLocation(double x, double y) {
        this.gui_location=new Point3D(x,y);
    }

    /** Return the Gui location (of applicable) of this node, if
     * none return null.
     *
     * @return
     */
    @Override
    public Point3D getGuiLocation() { return this.gui_location; }

    @Override
    public double getWeight() {
        return this.weight;
    }

    @Override
    public void setWeight(double w) {
        this.weight =w;
    }

    @Override
    public String getInfo() {
        return this.info;
    }

    @Override
    public String toString()  {return new String("Node"+this.key);}

    @Override
    public void setInfo(String s) {
        if(s==null) {
            return;
        }
        this.info=new String(s);
    }

    @Override
    public int getTag() {
        return this.tag;
    }

    /*
    Temporal data (aka color: e,g, white, gray, black)
    white=1, grey=2, black=3
     */
    @Override
    public void setTag(int t) {
        this.tag=t;
    }

    public String toJSON() {
        String ans = "";
        ans = ans + "{key:" + this.getKey() + ",info:" + this.getInfo() + "}";
        return ans;
    }
}
