package gameClient;

import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.node_data;

import java.util.*;

/**
 * This class represent the game when the mode is automate.
 * Algo_Game attributes:
 * 1. Game
 * 2. Graph_Algo ag= relevant for shortestPath calculation.
 * 3. robots_paths- HashTable that holds for each robot the list of nodes to the current targeted fruit.
 */
public class Algo_Game extends Thread {

    private Game my_game;
    private Graph_Algo ag;
    private Hashtable<Integer, List<node_data>> robots_paths;

    /**
     * Constructor initialize Algo_game attributes
     * @param game
     */
    public Algo_Game(Game game) {
        my_game = game;
        robots_paths = new Hashtable<>();
        ag = new Graph_Algo(my_game.getGraph());
    }

    /**
     * While game is running, for each robot allocate it's next destination, if the robots destination is -1, based of the robot list path.
     */
    @Override
    public void run() {
            game_service g = my_game.getMy_game();
            initRobotPath();
        try {
            while (g.isRunning()) {
                int dest = -1;
                for (int i = 0; i < my_game.getRobot_size(); i++) {
                    Robot robot = my_game.getRobots().get(i);
                    if (robot.getDest() == -1) {
                        dest = nextNode(i);
                        g.chooseNextEdge(i, dest);
                    }
                }
            }
        }
        catch (Exception ex)
        {

        }
    }

    /**
     * First init of the hashtable that hold each robot it's path to the targeted fruit.
     */
    private void initRobotPath() {
        for (int i = 0; i < my_game.getRobot_size(); i++) {
            Fruit fruit = my_game.getFruits().get(i);
            Robot robot = my_game.getRobots().get(i);
            List<node_data> tmp = ag.shortestPath(robot.getSrc(), fruit.getEdge().getDest());
            robots_paths.put(robot.getId(), tmp);
        }
    }

    /**
     * Set the next node of the robot according to the list that holds the path to the targeted fruit.
     * When the robot moves to the next destination in the list, this destination is removed.
     * When the path list is empty it indicated that the robot reached the fruit and need to reallocate a new fruit to the robot.
     * @param rid
     * @return
     */
    private int nextNode(int rid) {
        if (!my_game.getMy_game().isRunning()) {
            return -1;
        }
        Robot robot = my_game.getRobots().get(rid);
        List<node_data> tmp = robots_paths.get(rid);
        if (tmp.isEmpty()) {
            synchronized (my_game.getFruits()) {
                if (my_game.getFruits().size() > 0) {
                    Fruit fruit=my_game.getFruits().get(rid);
                            tmp = ag.shortestPath(robot.getSrc(), fruit.getEdge().getSrc());
                            node_data des = my_game.getGraph().getNode(fruit.getEdge().getDest());
                            tmp.add(des);
                            robots_paths.put(robot.getId(), tmp);
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
