package gameClient;

import dataStructure.edge_data;
import dataStructure.node_data;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;


public class MyGameGUI extends JFrame implements ActionListener, MouseListener {

    private Game my_game;
    public int mode;
    private static int HEIGHT=1000;
    private static int WIDTH=1000;
    private JLabel time;
    private  JLabel robot_info;

    public MyGameGUI(Game g, int mode) {
        this.my_game = g;
        this.mode = mode;
        init();
    }

    private void init() {
        time=new JLabel( "Time: " + my_game.getMy_game().timeToEnd() / 1000);
        robot_info=new JLabel();
        robot_info.setFont(new Font("deafult", Font.BOLD, 12));
        time.setFont(new Font("deafult", Font.BOLD, 20));
        this.setBounds(200, 0, 1000, 1000);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("My Game GUI");
        this.add(robot_info,BorderLayout.CENTER);
        this.add(time, BorderLayout.NORTH);
        if(this.mode==0) {
            addMouseListener(this);
        }
        this.setVisible(true);
    }

    public void setTime()
    {
        time.setText("Time: " + my_game.getMy_game().timeToEnd() / 1000);
    }
    private double scale(double data, double r_min, double r_max, double t_min, double t_max) {
        double res = ((data - r_min) / (r_max - r_min)) * (t_max - t_min) + t_min;
        return res;
    }

     public void paint(Graphics g) {
         BufferedImage bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
         Graphics2D g2d = bufferedImage.createGraphics();
         g2d.setBackground(new Color(240, 240, 240));
         g2d.clearRect(0, 0, WIDTH, HEIGHT);
        paintGraph(g2d);
        paintFruits(g2d);
        paintRobots(g2d);
         Graphics2D g2dComponent = (Graphics2D) g;
         g2dComponent.drawImage(bufferedImage, null, 0, 0);
     }

     private void paintGraph(Graphics2D g) {
        double[] x_toScale = my_game.getScale_x();
        double[] y_toScale = my_game.getScale_y();
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Time left: " + (my_game.getMy_game().timeToEnd() / 1000), 70, 70);
        for (node_data node : this.my_game.getGraph().getV()) {

            int node_x = (int) scale(node.getLocation().x(), x_toScale[0], x_toScale[1], 50, WIDTH-50);
            int node_y = (int) scale(node.getLocation().y(), y_toScale[0], y_toScale[1], 200, HEIGHT-200);

            g.setColor(Color.BLACK);
            g.fillOval(node_x - 5, node_y - 5, 10, 10);

            g.setColor(Color.BLUE);
            g.setFont(new Font("Arial", Font.BOLD, 15));
            g.drawString(node.getKey() + "", node_x + 5, node_y + 5);

            if (my_game.getGraph().getE(node.getKey()) != null) {
                for (edge_data edge : my_game.getGraph().getE(node.getKey())) {

                    node_data src = my_game.getGraph().getNode(edge.getSrc());
                    node_data dest = my_game.getGraph().getNode(edge.getDest());

                    int src_x = (int) scale(src.getLocation().x(), x_toScale[0], x_toScale[1], 50, WIDTH-50);
                    int src_y = (int) scale(src.getLocation().y(), y_toScale[0], y_toScale[1], 200, HEIGHT-200);
                    int dest_x = (int) scale(dest.getLocation().x(), x_toScale[0], x_toScale[1], 50, WIDTH-50);
                    int dest_y = (int) scale(dest.getLocation().y(), y_toScale[0], y_toScale[1], 200, HEIGHT-200);

                    g.setColor(Color.BLACK);
                    g.drawLine(src_x, src_y, dest_x, dest_y);


                    g.setColor(Color.RED);
                    int dir_x = (((((((src_x + dest_x) / 2) + dest_x) / 2) + dest_x) / 2) + dest_x) / 2;
                    int dir_y = (((((((src_y + dest_y) / 2) + dest_y) / 2) + dest_y) / 2) + dest_y) / 2;

                    g.fillOval(dir_x - 5, dir_y - 5, 10, 10);

                }
            }

        }
    }

     private void paintFruits(Graphics2D g) {
         synchronized (my_game.getFruits()) {
             double[] x_toScale = my_game.getScale_x();
             double[] y_toScale = my_game.getScale_y();
             for (Fruit fruit : my_game.getFruits()) {
                 g.setColor(Color.ORANGE);
                 if (fruit.getType() == 1) {
                     g.setColor(Color.GREEN);
                 }
                 int fruit_x = (int) scale(fruit.getLocation().x(), x_toScale[0], x_toScale[1], 50, WIDTH - 50);
                 int fruit_y = (int) scale(fruit.getLocation().y(), y_toScale[0], y_toScale[1], 200, HEIGHT - 200);

                 g.fillOval(fruit_x - 7, fruit_y - 7, 15, 15);
                 g.setColor(Color.BLACK);
                 g.drawString(fruit.getValue() + "", fruit_x + 10, fruit_y + 10);
             }
         }
     }

    private void paintRobots(Graphics2D g) {
        double[] x_toScale = my_game.getScale_x();
        double[] y_toScale = my_game.getScale_y();
        List<String> rob = my_game.getMy_game().getRobots();
        for (int i = 1; i <= rob.size(); i++) {
            g.drawString(rob.get(i - 1), 150, 70 + (20 * i));
        }
        for(int i=0; i<my_game.getRobot_size(); i++)
        {
            Robot robot=my_game.getRobots().get(i);
            int robot_x = (int) scale(robot.getLocation().x(), x_toScale[0], x_toScale[1], 50, WIDTH - 50);
                int robot_y = (int) scale(robot.getLocation().y(), y_toScale[0], y_toScale[1], 200, HEIGHT - 200);
                g.setColor(Color.GRAY);
                g.drawOval(robot_x - 15, robot_y - 15, 30, 30);
                g.setFont(new Font("Arial", Font.BOLD, 15));
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
                for (int i=0; i<my_game.getRobot_size(); i++) {
                    Robot robot=my_game.getRobots().get(i);
                    if (robot.getDest() == -1) {
                        String dst_str = JOptionPane.showInputDialog(this, "Please insert robot " + robot.getId() + " next node destination");
                      try {
                          int dest = Integer.parseInt(dst_str);
                          this.my_game.getMy_game().chooseNextEdge(robot.getId(), dest);
                      }
                      catch (Exception ex)
                      {
                          JOptionPane.showMessageDialog(this,"ERROR","ERROR",JOptionPane.ERROR_MESSAGE);
                      }
                    }
                }
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
}
