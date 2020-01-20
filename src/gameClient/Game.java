package gameClient;

import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.node_data;
import org.json.JSONObject;
import utils.Point3D;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * This class represents a game scenario of the "MAZE OF WAZE game" depends of a stage, [0-24], that is given.
 * This class uses a given an imported jar file -Game_Server that implemented a set of operations applicable on a game.
 * Game attributes:
 * 1.graph - directed weighted graph, this is the board that the robots and fruits are located on.
 * 2. fruits- data structure that hold the fruits elements -arrayList , that the robots need to collect.
 * 3.robots- data structure that hold the robots elements- hashTable , this a synchronize data structure.
 * 4.scale_x, scale_y- attributes that holds the minimum and maximum of the Xaxis and Yaxis of the graph nodes relevant for drawing.
 */
public class Game {

    public static final double EPS1 = 0.001, EPS2 = Math.pow(EPS1, 2);
    private static final VAL_COMP comp = new VAL_COMP(); //comparator by value, for fruits collection.
    private dataStructure.graph graph;
    private ArrayList<Fruit> fruits;
    private Hashtable<Integer, Robot> robots;
    private game_service my_game;
    private int robot_size;
    private double[] scale_x = new double[2];
    private double[] scale_y = new double[2];

    /**
     * Default constructor
     */
    public Game(){
        fruits = new ArrayList<Fruit>();
        robots = new Hashtable<>();
    }

    /**
     * Constructor given a game scenario [0-24], launch a game service and init the game attributes.
     *
     * @param num_scenario
     */
    public Game(int num_scenario) {
        my_game = Game_Server.getServer(num_scenario);
        graph = new DGraph(my_game.getGraph());
        find_min_max_axis();
        find_min_max_yais();
        fruits = new ArrayList<Fruit>();
        robots = new Hashtable<>();
        setFruits();
        initRobots();
        setRobots();
        setKML();
    }

    /**
     * Getter for the graph attribute.
     *
     * @return graph
     */
    public dataStructure.graph getGraph() {
        return graph;
    }

    /**
     * Setter for the graph attribute .
     *
     * @param graph
     */
    public void setGraph(dataStructure.graph graph) {
        this.graph = graph;
    }

    /**
     * Getter for the fruits collection attribute.
     *
     * @return
     */
    public ArrayList<Fruit> getFruits() {
        return this.fruits;
    }

    /**
     * Setter for the fruits collection attribute.
     * This method call the "getFruits" function that method returns a list of strings that updates the fruits collection,implemented in the game server.
     * After setting the fruits the collection of the fruits is being sorted by the fruits value.
     * The km parameter is an attribute of the Main_Thread class the relevant for the creation of the KML files for each game.
     */
    public void setFruits() {
        synchronized (this.fruits) {  // relevant to avoid ConcurrentModification Exception while using multi threading
            this.fruits.clear();
            for (String fruit : my_game.getFruits()) {
                Fruit fruit_tmp = new Fruit(fruit);
                if (Main_Thread.km != null) {
                    if (fruit_tmp.getType() == 1) {
                        Main_Thread.km.addPlaceMark("fruit-apple", fruit_tmp.getLocation().toString());
                    } else {
                        Main_Thread.km.addPlaceMark("fruit-banana", fruit_tmp.getLocation().toString());
                    }
                }
                setFruitsEdge(fruit_tmp);
                this.fruits.add(fruit_tmp);
            }
            this.fruits.sort(comp);
        }
    }

    /**
     * Getter for the robots collection
     *
     * @return
     */
    public Hashtable<Integer, Robot> getRobots() {
        return this.robots;
    }

    /**
     * In the beginning of the game this function add the robots to the game and locate them in a strategic location.
     * The initialization location of each robot determinate's by the source node of the fruit edge.
     * This method call the function "toString" that method returns a string that represents the game state ,implemented in the game server.
     * This string contains the number of robots that are applicable for this stage.
     */
    private void initRobots() {
        try {
            JSONObject robots = new JSONObject(my_game.toString());
            robots = robots.getJSONObject("GameServer");
            int num_robots = robots.getInt("robots");
            for (int i = 0; i < num_robots; i++) {
                if (i < this.fruits.size()) {          //check for remaining fruits to allocate robot location.
                    int src = this.fruits.get(i).getEdge().getSrc();
                    node_data node_src = this.graph.getNode(src);
                    Point3D src_p = node_src.getLocation();
                    my_game.addRobot(src);
                } else //no fruits left, then locate index position
                {
                    my_game.addRobot(i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Setter for the robots collection attribute.
     * This method call the "getRobots" function that method returns a list of strings that updates the robots collection,implemented in the game server.
     * The km parameter is an attribute of the Main_Thread class the relevant for the creation of the KML files for each game.
     */
    public void setRobots() {
        for (String robot : my_game.getRobots()) {
            Robot robot_tmp = new Robot(robot);
            if (Main_Thread.km != null) {
                Main_Thread.km.addPlaceMark("robot", robot_tmp.getLocation().toString());
            }
            robots.put(robot_tmp.getId(), robot_tmp);
        }
    }

    /**
     * Getter for the game_service attribute
     *
     * @return
     */
    public game_service getMy_game() {
        return my_game;
    }

    /**
     * Setter for the game_service attribute
     *
     * @param my_game
     */
    public void setMy_game(game_service my_game) {
        this.my_game = my_game;
    }

    /**
     * This function updates the robots and fruits collections by using their set functions.
     */
    public void Update() {
        setFruits();
        setRobots();
    }

    /**
     * This method connect fruit to his edge on the graph based on the fruit type , location and the nodes location.
     * Fruit is connect to an edge if and only if the distance between the the source node and the fruit,
     * and the fruit to the destination node is equivalent,(<=Epsilon), to the distance between the source and the destination nodes.
     * Apple= type 1 -indicates that the fruit edge is from source lower key node to higher destination node key.
     * Banana= type -1 -indicates that the fruit edge is from source higher key node to lower destination node key.
     *
     * @param fruit
     */
    private void setFruitsEdge(Fruit fruit) {
        for (node_data node : this.graph.getV()) {
            for (edge_data edge : this.graph.getE(node.getKey())) {
                node_data dst = this.graph.getNode(edge.getDest());
                double d1 = node.getLocation().distance3D(fruit.getLocation());
                double d2 = fruit.getLocation().distance3D(dst.getLocation());
                double dist = node.getLocation().distance3D(dst.getLocation());
                double tmp = dist - (d1 + d2);
                int t;
                if (node.getKey() < dst.getKey()) {
                    t = 1; // fruit= apple
                } else {
                    t = -1;
                } //fruit=banana

                if ((Math.abs(tmp) <= EPS2) && (fruit.getType() == t)) {
                    fruit.setEdge(edge);
                    return;
                }
            }
        }
    }

    /**
     * Iterate over the graph nodes and find the min, max X axis
     */
    private void find_min_max_axis() {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        for (node_data node : this.graph.getV()) {

            if (node.getLocation().x() < min)
                min = node.getLocation().x();
            else {
                if (node.getLocation().x() > max) {
                    max = node.getLocation().x();
                }
            }
        }

        this.scale_x[0] = min;
        this.scale_x[1] = max;

    }

    /**
     * Iterate over the graph nodes and find the min, max Y axis
     */
    private void find_min_max_yais() {
        double[] y_s = new double[2];
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        for (node_data node : this.graph.getV()) {

            if (node.getLocation().y() < min)
                min = node.getLocation().y();
            else {
                if (node.getLocation().y() > max) {
                    max = node.getLocation().y();
                }
            }
        }

        this.scale_y[0] = min;
        this.scale_y[1] = max;
    }

    /**
     * Getter for scale_x attribute
     *
     * @return
     */
    public double[] getScale_x() {
        return scale_x;
    }

    /**
     * Setter for scale_x attribute
     *
     * @return
     */
    public void setScale_x(double[] scale_x) {
        this.scale_x = scale_x;
    }

    /**
     * Getter for scale_y attribute
     *
     * @return
     */
    public double[] getScale_y() {
        return scale_y;
    }

    /**
     * Setter for scale_y attribute
     *
     * @return
     */
    public void setScale_y(double[] scale_y) {
        this.scale_y = scale_y;
    }

    /**
     * Getter for robot_size attribute
     *
     * @return
     */
    public int getRobot_size() {
        return this.robots.size();
    }

   public void setKML()
   {
       for (node_data node: graph.getV())
       {
           if (Main_Thread.km != null) {
               Main_Thread.km.addPlaceMark("node", node.getLocation().toString());

           }
        }

       for (node_data node: graph.getV()) {
           for (edge_data edge: graph.getE(node.getKey())){
               if (Main_Thread.km != null) {
                   node_data src=graph.getNode(edge.getSrc());
                   node_data dst=graph.getNode(edge.getDest());
                   Main_Thread.km.addEdgePlacemark(src.getLocation(),dst.getLocation());
               }
           }
       }
   }

}
