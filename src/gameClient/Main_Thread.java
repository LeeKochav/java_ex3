package gameClient;

import Server.game_service;

import javax.swing.*;
import java.awt.*;

public class Main_Thread extends Thread {

    private Game game;
    private Game_Gui game_gui;
    private static int stage;
    private  static int mode;

    public Main_Thread()
    {
        game =new Game(stage);
        game_gui=new Game_Gui(game,mode);

    }

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
        int option=JOptionPane.showOptionDialog(welcome_screen,"Please choose mode","INFORMATIN",JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null,modes,modes[1]);
        try
        {
             stage=Integer.parseInt(stage_str);
             if(option==0) //mode =manual
             {
                 mode=0;
             }
             if(option==1) //mode =automate
             {
                 mode=1;
             }
        }
        catch (Exception ex)
        {
            JOptionPane.showMessageDialog(welcome_screen,"ERROR!","ERROR",JOptionPane.ERROR_MESSAGE);
        }
        welcome_screen.setVisible(false);
    }

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
        JOptionPane.showMessageDialog(game_gui,this.game.getMy_game().toString(),"GAME OVER",JOptionPane.INFORMATION_MESSAGE);
    }
    public static void main(String[]args)
    {
        init();
        Main_Thread client=new Main_Thread();
        client.start();
    }
}
