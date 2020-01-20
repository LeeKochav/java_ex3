package gameClient;

import Server.Game_Server;
import Server.game_service;

import javax.swing.*;

/**
 * This class represents the engine of the the "MAZE OF WAZE" game.
 * Main_Thread attributes:
 * 1. Game
 * 2.MyGameGui
 * 3.stage - game scenario [0-23]
 * 4.mode- manual/ automate
 * 5-km - KML_Logger element for the KML files.
 */

public class Main_Thread extends Thread {

    private Game game;
    private MyGameGUI game_gui;
    private static int stage;
    private  static int mode;
    public static KML_Logger km=null;


    /**
     * Constructor initialize Main_Thread attributes
     */
    public Main_Thread()
    {
        game =new Game(stage);
        game_gui=new MyGameGUI(game,mode);
        km=new KML_Logger(stage);
    }

    /**
     * Initialize the welcome fame frame and sets the mode and stage attributes by user input.
      */
    public static void init()
    {
        String [] modes={"Manual","Automate"};
        JFrame welcome_screen=new JFrame();
        welcome_screen.setBounds(200, 0, 500, 500);
        welcome_screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        welcome_screen.setTitle("The Maze Of Waze");
        welcome_screen.setVisible(true);
        JOptionPane.showMessageDialog(welcome_screen,"Welcome!","WELCOME",JOptionPane.INFORMATION_MESSAGE);
        String stage_str=JOptionPane.showInputDialog(welcome_screen,"Please insert stage [0-23] to play");
        try
        {
             stage=Integer.parseInt(stage_str);
             if(stage<0||stage>23)
             {
                 JOptionPane.showMessageDialog(welcome_screen,"Invalid input for stage, game default will start in stage 0","ERROR",JOptionPane.ERROR_MESSAGE);
                 stage=0;
             }
        }
        catch (Exception ex)
        {
            JOptionPane.showMessageDialog(welcome_screen,"Invalid input for stage, game default will start in stage 0","ERROR",JOptionPane.ERROR_MESSAGE);
            stage=0;
        }
        int option=JOptionPane.showOptionDialog(welcome_screen,"Please choose mode","INFORMATIN",JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null,modes,modes[1]);
        if(option==0) //mode =manual
        {
            mode=0;
        }
        if(option==1) //mode =automate
        {
            mode=1;
        }
        welcome_screen.setVisible(false);
    }

    /**
     * This function starts the game based on the mode selected, move the robots ,update the robots,fruits collection and repaint the GUI.
     */
    @Override
    public void run() {
        int id = 999;
     //   Game_Server.login(id);
            game_service client_game = this.game.getMy_game();
            client_game.startGame();
            Algo2 ag=null;
            if (mode == 1) {
                //Algo_Game ag = new Algo_Game(game);
                //ag.start();
                ag=new Algo2(game);
            }
            int dt=45;
            int ind=0;
        try {
            while (client_game.isRunning()) {
                if(mode==1)
                {
                    ag.MoveRobots();
                }
                if(ind%2==0) {
                    client_game.move();
                    this.game.Update();
                }
                game_gui.repaint();
                Thread.sleep(dt);
                ind++;
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        String s=km.kmlEnd();
        JOptionPane.showMessageDialog(null,this.game.getMy_game().toString(),"GAME OVER",JOptionPane.INFORMATION_MESSAGE);
        game_gui.setVisible(false);
    //    System.out.println( client_game.sendKML(s));
        System.exit(0);
    }

    /**
     * main function , run the thread
     * @param args
     */
    public static void main(String[]args)
    {
        init();
        Main_Thread client=new Main_Thread();
        client.start();
    }
}
