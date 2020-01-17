package Tests;

import dataStructure.DGraph;
import dataStructure.graph;
import gameClient.Fruit;
import gameClient.Game;
import gameClient.Robot;
import gameClient.VAL_COMP;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    static int stage;
    static Game game_test;
    static Game game_test2;
    static VAL_COMP COMP=new VAL_COMP();

    @BeforeAll
    static void init()
    {
        Random rand=new Random();
        stage=rand.nextInt(24);
        game_test=new Game(stage);
        game_test2=new Game();
        game_test2.setMy_game(game_test.getMy_game());
        //initialize game_test2 game with robots
        try {
            JSONObject robots = new JSONObject(game_test2.getMy_game().toString());
            robots = robots.getJSONObject("GameServer");
            int num_robots = robots.getInt("robots");
            for (int i = 0; i < num_robots; i++) {

                game_test2.getMy_game().addRobot(i);
            }
            for (String robot : game_test2.getMy_game().getRobots()) {
                Robot robot_tmp = new Robot(robot);
                game_test2.getRobots().put(robot_tmp.getId(),robot_tmp);
            }

        }
        catch (Exception ex)
        {

        }
    }
    @Test
    void setGetGraph() {
        graph g_test=new DGraph(game_test.getMy_game().getGraph());
        game_test2.setGraph(g_test);
        assertEquals(game_test2.getGraph().toString(),game_test.getGraph().toString());
    }

    @Test
    void setGetFruits() {
        ArrayList<Fruit> fruitsSTR = new ArrayList<>();
        for (String f:game_test.getMy_game().getFruits())
        {
            fruitsSTR.add(new Fruit(f));
        }
        fruitsSTR.sort(COMP);
        assertTrue(fruitsSTR.toString().equals(game_test.getFruits().toString()));
    }

    @Test
    void setGetRobots() {

        for(int i=0; i<game_test2.getMy_game().getRobots().size(); i++)
        {
            String rob_str=game_test2.getMy_game().getRobots().get(i);
            Robot robot_tmp = new Robot(rob_str);
            Robot robot_game=game_test2.getRobots().get(i);
            assertEquals(robot_tmp.toString(),robot_game.toString());
        }
    }

    //set game service of game_test2 to the game service of game_test , both games points to the same game_service.
    @Test
    void setGetMy_game() {
        assertTrue(game_test.getMy_game().equals(game_test2.getMy_game()));
    }

    @Test
    void update() {
        ArrayList<Fruit> beforeUpdateFruits=game_test.getFruits();
        Hashtable<Integer,Robot> beforeUpdateRobot=game_test.getRobots();
        game_test.Update();
        assertEquals(game_test.getFruits().toString(),beforeUpdateFruits.toString());
        assertEquals(game_test.getRobots().values().toString(),beforeUpdateRobot.values().toString());
    }

    @Test
    void getRobot_size() {
        int r_s=game_test.getRobot_size();
        assertEquals(r_s,game_test.getRobots().size());
    }
}