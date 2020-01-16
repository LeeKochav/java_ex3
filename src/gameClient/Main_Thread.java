package gameClient;

import Server.game_service;

import javax.swing.*;

/**
 * This class represents the engine of the the "MAZE OF WAZE" game.
 * Main_Thread attributes:
 * 1. Game
 * 2.MyGameGui
 * 3.stage - game scenario [0-24]
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
        km=new KML_Logger(stage);
        game =new Game(stage);
        game_gui=new MyGameGUI(game,mode);

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
        game_service client_game = this.game.getMy_game();
        client_game.startGame();
        if(mode==1)
        {
            Algo_Game ag=new Algo_Game(game);
            ag.start();
        }
        while (client_game.isRunning()) {
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            client_game.move();
            this.game.Update();
            game_gui.repaint();
        }
        km.kmlEnd();
        JOptionPane.showMessageDialog(game_gui,this.game.getMy_game().toString(),"GAME OVER",JOptionPane.INFORMATION_MESSAGE);
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
