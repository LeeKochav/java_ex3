//package gameClient;
//
//import Server.game_service;
//import dataStructure.edge_data;
//import dataStructure.graph;
//import dataStructure.node_data;
//import org.json.JSONException;
//import org.json.JSONObject;
//import utils.Point3D;
//
//import java.util.List;
//
//public class Algo_Game extends Thread{
//
//    private Game my_game;
//
//    public Algo_Game(int num_scenario)
//    {
//        my_game=new Game(num_scenario);
//        this.start();
//    }
//
//    @Override
//    public void run() {
//        super.run();
//    }
//
//    private void moveRobots(game_service game, graph graph) { //choseNextEdge= next node, move= moves all rovots
//        List<String> log = game.move();
//        if (log != null) {
//            long t = game.timeToEnd();
//            for (int i = 0; i < log.size(); i++) {
//                String robot_json = log.get(i);
//                try {
//                    JSONObject line = new JSONObject(robot_json);
//                    JSONObject ttt = line.getJSONObject("Robot");
//                    int rid = ttt.getInt("id");
//                    int src = ttt.getInt("src");
//                    int dest = ttt.getInt("dest");
//                    String pos = ttt.getString("pos");
//                    int dst = -1;
//
//                    if (dest == -1) {
//
//                        dest = nextNode(rid, dst);
//                        game.chooseNextEdge(rid, dest);
//                        System.out.println("Turn to node: " + dest + "  time to end:" + (t / 1000));
//                        System.out.println(ttt);
//                    }
//                    this.robots.get(rid).setLocation(new Point3D(pos));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    private int nextNode(int rid, int dst)
//    {
//        Robot r=this.robots.get(rid);
//        for (edge_data e:this.graph.getE(r.getSrc())) {
//            if(e.getDest()==dst)
//                return dst;
//        }
//        return -1;
//    }
//
//}
