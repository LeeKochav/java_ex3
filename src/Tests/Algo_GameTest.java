package Tests;

import Server.game_service;
import gameClient.Algo_Game;
import gameClient.Game;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.fail;


class Algo_GameTest {

    static Game game;
    static Algo_Game algo_game;

    @BeforeAll
    static void  init()
    {
        Random rand=new Random();
        int stage=rand.nextInt(24);
        game=new Game(23);
        algo_game=new Algo_Game(game);
    }

    /*
    Test purpose is to check that game is running and no exceptions thrown.
    Print robots status in during the game which make it possible to track the robots movements.
    Print result in the end of the game.
    */
    @Test
    void play() {
        game_service client_game = game.getMy_game();
        client_game.startGame();
        algo_game.start(); //start Algo_game thread
        int dt=40;
        int ind=0;
        try {
            while (client_game.isRunning()) {
                Thread.sleep(dt);
                ind++;
                game.Update();
                if(ind%2==0) {

                    List<String> rob = client_game.getRobots();
                    for(int i=0; i<rob.size(); i++)
                    {
                        System.out.println(rob.get(i).toString());
                    }
                    client_game.move();
                }

            }
        }
        catch (Exception ex)
        {
            System.out.println("Game threw exception: "+ex.getCause());
        }
        String results = client_game.toString();
        System.out.println("Game Over: "+results);
    }
}