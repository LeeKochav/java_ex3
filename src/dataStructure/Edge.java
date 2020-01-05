package dataStructure;

import java.io.Serializable;

public class Edge implements edge_data, Serializable {

    private node_data src;
    private node_data dst;
    private int tag;
    private double weight;
    private String info;

    public Edge(){}

    public Edge(node_data src,node_data dst ,double weight)
    {
        this.src=src;
        this.dst=dst;
        this.setTag(1);
        if(weight<0)
            throw new RuntimeException("Weight cannot be negative");
        this.weight=weight;
        this.setInfo("");
    }

    @Override
    public int getSrc() {
        return this.src.getKey();
    }

    @Override
    public int getDest() {
        return this.dst.getKey();
    }

    @Override
    public double getWeight() {
        return this.weight;
    }

    @Override
    public String getInfo() {
        return this.info;
    }
    @Override
    public String toString()
    {
        return this.getInfo();
    }

    @Override
    public void setInfo(String s) {
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
            this.tag = t;
    }
}
