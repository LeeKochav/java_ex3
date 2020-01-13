package gameClient;

import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.node_data;
import org.json.JSONObject;
import utils.Point3D;

import java.util.ArrayList;

public class Game {

    public static final double EPS1 = 0.001, EPS2 = Math.pow(EPS1,2);
    private static final VAL_COMP comp = new VAL_COMP();
    private dataStructure.graph graph;
    private ArrayList<Fruit> fruits;
    private  ArrayList<Robot> robots;
    private game_service my_game;
    private double[] scale_x=new double[2];
    private double[] scale_y=new double[2];



    public Game(int num_scenario)
    {
        my_game = Game_Server.getServer(num_scenario);
        graph = new DGraph(my_game.getGraph());
        find_min_max_axis();
        find_min_max_yais();
        fruits=new ArrayList<Fruit>();
        robots=new ArrayList<Robot>();
        setFruits();
        initRobots();
        setRobots();
    }


    public dataStructure.graph getGraph() {
        return graph;
    }

    public void setGraph(dataStructure.graph graph) {
        this.graph = graph;
    }

    public ArrayList<Fruit> getFruits() {
        return this.fruits;
    }

    public void setFruits() {
        synchronized (this.fruits) {
            this.fruits.clear();
            for (String fruit : my_game.getFruits()) {
                Fruit fruit_tmp = new Fruit(fruit);
                setFruitsEdge(fruit_tmp);
                this.fruits.add(fruit_tmp);
            }
            this.fruits.sort(comp);
        }
    }

    public ArrayList<Robot> getRobots() {
        return this.robots;
    }

    private void initRobots()
    {
        try
        {
            JSONObject robots=new JSONObject(my_game.toString());
            robots=robots.getJSONObject("GameServer");
            int num_robots=robots.getInt("robots");
            for(int i=0; i<num_robots; i++)
            {
                if(i<this.fruits.size()) {
                    int src = this.fruits.get(i).getEdge().getSrc();
                    node_data node_src = this.graph.getNode(src);
                    Point3D src_p = node_src.getLocation();
                    my_game.addRobot(src);
                }
                else
                {
                    my_game.addRobot(i);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void setRobots() {
        synchronized (this.robots) {
            this.robots.clear();
            for (String robot : my_game.getRobots()) {
                Robot robot_tmp = new Robot(robot);
                robots.add(robot_tmp);
            }
        }
    }

    public game_service getMy_game() {
        return my_game;
    }

    public void setMy_game(game_service my_game) {
        this.my_game = my_game;
    }

    public void Update()
    {
        setFruits();
        setRobots();
    }
    private void setFruitsEdge(Fruit fruit)
    {
        for (node_data node:this.graph.getV()) {
            for (edge_data edge: this.graph.getE(node.getKey())) {
                node_data dst=this.graph.getNode(edge.getDest());
                double d1=node.getLocation().distance3D(fruit.getLocation());
                double d2=fruit.getLocation().distance3D(dst.getLocation());
                double dist=node.getLocation().distance3D(dst.getLocation());
                double tmp=dist-(d1+d2);
                int t;
                if(node.getKey()>dst.getKey())
                {
                    t=1;
                }
                else {t=-1;}

                if((Math.abs(tmp)<=EPS2)&&(fruit.getType()==t))
                {
                    fruit.setEdge(edge);
                    return;
                }
            }
        }
    }


    private void find_min_max_axis()
    {
        double min=Double.MAX_VALUE;
        double max=Double.MIN_VALUE;

        for (node_data node:this.graph.getV()) {

            if(node.getLocation().x()<min)
                min=node.getLocation().x();
            else {
                if (node.getLocation().x() > max) {
                    max = node.getLocation().x();
                }
            }
        }

        this.scale_x[0]=min;
        this.scale_x[1]=max;

    }
    private double scale(double data, double r_min, double r_max, double t_min, double t_max) {
        double res = ((data - r_min) / (r_max - r_min)) * (t_max - t_min) + t_min;
        return res;
    }

    private void find_min_max_yais()
    {
        double [] y_s=new double[2];
        double min=Double.MAX_VALUE;
        double max=Double.MIN_VALUE;

        for (node_data node:this.graph.getV()) {

            if(node.getLocation().y()<min)
                min=node.getLocation().y();
            else {
                if (node.getLocation().y() > max) {
                    max = node.getLocation().y();
                }
            }
        }

        this.scale_y[0]=min;
        this.scale_y[1]=max;
    }

    public double[] getScale_x() {
        return scale_x;
    }

    public void setScale_x(double[] scale_x) {
        this.scale_x = scale_x;
    }

    public double[] getScale_y() {
        return scale_y;
    }

    public void setScale_y(double[] scale_y) {
        this.scale_y = scale_y;
    }
}
