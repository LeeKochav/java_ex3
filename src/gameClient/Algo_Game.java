package gameClient;

import Server.game_service;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import oop_dataStructure.oop_edge_data;
import oop_dataStructure.oop_graph;
import org.json.JSONException;
import org.json.JSONObject;
import utils.Point3D;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Algo_Game extends Thread {

    private Game my_game;

    public Algo_Game(Game game) {
        my_game = game;
    }

    @Override
    public void run() {
        game_service g = my_game.getMy_game();
        while (g.isRunning()) {
            int dest = -1;
                for (int i=0; i<my_game.getRobot_size(); i++) {
                    Robot robot=my_game.getRobots().get(i);
                    if (robot.getDest() == -1) {
                        dest = nextNode(robot.getSrc());
                        my_game.getMy_game().chooseNextEdge(robot.getId(), dest);
                    }
                }
            }
        }



    private  int nextNode( int src) {
        int ans = -1;
        Collection<edge_data> ee = my_game.getGraph().getE(src);
        Iterator<edge_data> itr = ee.iterator();
        int s = ee.size();
        int r = (int)(Math.random()*s);
        int i=0;
        while(i<r) {itr.next();i++;}
        ans = itr.next().getDest();
        return ans;
    }
}
