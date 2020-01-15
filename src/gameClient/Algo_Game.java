package gameClient;

import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import oop_dataStructure.oop_edge_data;
import oop_dataStructure.oop_graph;
import org.json.JSONException;
import org.json.JSONObject;
import utils.Point3D;

import java.util.*;

public class Algo_Game extends Thread {

    private Game my_game;
    private Graph_Algo ag;
    private Hashtable<Integer, List<node_data>> robots_paths;

    public Algo_Game(Game game) {
        my_game = game;
        robots_paths = new Hashtable<>();
        ag = new Graph_Algo(my_game.getGraph());
    }

    @Override
    public void run() {
        game_service g = my_game.getMy_game();
        initRobotPath();
        while (g.isRunning()) {
            int dest = -1;
            for (int i = 0; i < my_game.getRobot_size(); i++) {
                Robot robot = my_game.getRobots().get(i);
                if (robot.getDest() == -1) {
                    dest = nextNode(robot.getId());
                    my_game.getMy_game().chooseNextEdge(robot.getId(), dest);
                }
            }
        }
    }

    private void initRobotPath() {
        for (int i = 0; i < my_game.getRobot_size(); i++) {
            Fruit fruit = my_game.getFruits().get(i);
            Robot robot = my_game.getRobots().get(i);
            List<node_data> tmp = ag.shortestPath(robot.getSrc(), fruit.getEdge().getDest());
            robots_paths.put(robot.getId(), tmp);
        }
    }


    private int nextNode(int rid) {
        if (!my_game.getMy_game().isRunning()) {
            return -1;
        }
        Robot robot = my_game.getRobots().get(rid);
        List<node_data> tmp = robots_paths.get(rid);
        if (tmp.isEmpty()) {
            synchronized (my_game.getFruits()) {
                if (my_game.getFruits().size() > 0) {
                    for (Fruit fruit: my_game.getFruits()) {
                            tmp = ag.shortestPath(robot.getSrc(), fruit.getEdge().getSrc());
                            node_data des = my_game.getGraph().getNode(fruit.getEdge().getDest());
                            tmp.add(des);
                            robots_paths.put(robot.getId(), tmp);
                            break;
                        }
                }
            }
        }
        for (int i = 0; i < tmp.size(); i++) {
            node_data n = tmp.get(i);
            tmp.remove(i);
            if (n.getKey() == robot.getSrc())
                continue;
            return n.getKey();
        }

        return -1;
    }
}
