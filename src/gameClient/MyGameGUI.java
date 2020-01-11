package gameClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MyGameGUI extends JFrame implements ActionListener, MouseListener {

    public MyGameGUI() {
        addMouseListener(this);
        JTextPane welcome = new JTextPane();
        JButton manual = new JButton("Manual mode");
        JButton automatic = new JButton("Automatic mode");
        welcome.setFont(new Font("deafult", Font.BOLD, 14));
        welcome.setText("Welcome! " + "\n" + "This is the Maze of the Waze game, " +
                        "there are two play modes for this game: manual , automatic. " + "\n" +
                        "Given a graph, robots and fruits the goal is to collect as many fruits as you can until the end of the time"+
                "\n" + "Press on the following buttons to start play according to the mode");
        this.setBounds(200, 0, 1000, 1000);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("The Maze to Waze");
        manual.addActionListener(this);
        automatic.addActionListener(this);
        this.add(welcome);
        this.add(manual);
        this.add(automatic);
        this.setLayout(new FlowLayout());
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action=e.getActionCommand();

        switch (action) {
            case "Manual mode":
            String stage_str =JOptionPane.showInputDialog(this,"Please insert stage to play [0-23");
            try
            {
                int stage=Integer.parseInt(stage_str);
                this.setVisible(false);
                Thread t=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            My_Game game=new My_Game(stage);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                t.start();
            }
            catch (Exception ex)
            {
                JOptionPane.showMessageDialog(this,"ERROR: "+ex.getCause(),"ERROR",JOptionPane.ERROR_MESSAGE);
            }
            break;
            default:
                break;
        }


    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
    public static void main(String[]args)
    {
        MyGameGUI t=new MyGameGUI();
    }
}
