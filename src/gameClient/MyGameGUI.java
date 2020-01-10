package gameClient;

import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import com.sun.deploy.security.SelectableSecurityManager;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import gui.Graph_GUI;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import utils.Point3D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Arc2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class MyGameGUI  extends JFrame implements ActionListener , MouseListener{

    public static final double EPS1 = 0.001, EPS2 = Math.pow(EPS1,2);
    private graph graph;
    private Graph_Algo algoGraph;
    private ArrayList<Fruit> fruits;
    final int NODE_WIDTH_HEIGHT=10;

    public MyGameGUI(int num_scenario) {
        game_service game = Game_Server.getServer(num_scenario);
        graph = new DGraph(game.getGraph());
        algoGraph = new Graph_Algo();
        fruits=new ArrayList<Fruit>();
        for (String fruit: game.getFruits()) {
            Fruit fruit_tmp=new Fruit(fruit);
            setFruitsEdge(fruit_tmp);
            fruits.add(fruit_tmp);
        }
        initGui(1000,1000);
    }
    private void setFruitsEdge(Fruit fruit)
    {
            for (node_data node:this.graph.getV()) {
                for (edge_data edge: this.graph.getE(node.getKey())) {
                    node_data dst=this.graph.getNode(edge.getDest());
                    double d1=node.getLocation().distance3D(fruit.getLocation());
                    double d2=fruit.getLocation().distance3D(dst.getLocation());
                    double dist=node.getLocation().distance3D(dst.getLocation());
                    double tmp=dist-(d1+d2);
                    int t;
                    if(node.getKey()>dst.getKey())
                    {
                        t=1;
                    }
                    else {t=-1;}

                    if((Math.abs(tmp)<=EPS2)&&(fruit.getType()==t))
                    {
                        fruit.setEdge(edge);
                        return;
                    }
            }
        }
    }
    private void initGui(int width, int height) {
        this.setBounds(200, 0, width, height);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("My Game GUI");

        this.setVisible(true);
//        Thread t=new Thread(new Runnable() { //repaint the graph gui using thread if there has been change in the graph
//            @Override
//            public void run() {
//                while(true) {
//                    synchronized (graph) {
//                        if (mc != graph.getMC()) {
//                            mc = graph.getMC();
//                            repaint();
//                        }
//                    }
//                }
//            }
//        });
//        t.start();

    }
    public void paint(Graphics graphics)
    {
        double []x_toScale=find_min_max_axis();
        double []y_toScale=find_min_max_ayis();

        Graphics2D g=(Graphics2D) graphics;
        setNodesForPaint();
        super.paint(g);
        for (node_data node: graph.getV()) {
            g.setColor(Color.BLACK);
            Shape circle= new Arc2D.Double(node.getGuiLocation().x()-3,node.getGuiLocation().y()-3,NODE_WIDTH_HEIGHT,NODE_WIDTH_HEIGHT,0,360,Arc2D.CHORD);
            g.fill(circle);
            String id=node.getKey()+"";
            g.setFont(new Font("deafult", Font.BOLD,14));
            g.setColor(Color.BLUE);
            g.drawString(id, node.getGuiLocation().ix()+3, node.getGuiLocation().iy());
        }

        for (Fruit fruit: this.fruits) {
            if(fruit.getType()==1) {
                g.setColor(Color.GREEN);
            }
            else
            {
                g.setColor(Color.ORANGE);
            }
            double x_gui = scale(fruit.getLocation().x(), x_toScale[0], x_toScale[1], 5, this.getWidth()-50);
            double y_gui = scale(fruit.getLocation().y(), y_toScale[0], y_toScale[1],70,this.getHeight()-70);
            fruit.setLocationGui(x_gui, y_gui);
            Shape circle= new Arc2D.Double(fruit.getLocationGui().x()-3,fruit.getLocationGui().y()-3,NODE_WIDTH_HEIGHT,NODE_WIDTH_HEIGHT,0,360,Arc2D.CHORD);
            g.fill(circle);
        }

        for (node_data node: graph.getV() ){
            if(graph.getE(node.getKey())!=null) {
                for (edge_data edge : graph.getE(node.getKey())) {
                    g.setColor(Color.BLACK);
                    g.setFont(new Font("deafult", Font.BOLD,14));
                    String weight = edge.getWeight() + "";
                    node_data dst = graph.getNode(edge.getDest());
                    g.drawLine(node.getGuiLocation().ix(), node.getGuiLocation().iy(), dst.getGuiLocation().ix(), dst.getGuiLocation().iy());
//                    double dist = node.getGuiLocation().distance2D(dst.getGuiLocation());
//                    g.drawString(weight, (int) ((node.getGuiLocation().x() + dst.getGuiLocation().x()) / 2), (int) ((node.getGuiLocation().y() + dst.getGuiLocation().y()) / 2));
                    g.setColor(Color.RED);

                    //calculate the direction oval location
                    int mid_x = ((node.getGuiLocation().ix() + dst.getGuiLocation().ix()) / 2);
                    int mid_y = ((node.getGuiLocation().iy() + dst.getGuiLocation().iy()) / 2);
                    int d_x=(((((mid_x+dst.getGuiLocation().ix())/2)+dst.getGuiLocation().ix())/2)+dst.getGuiLocation().ix())/2;
                    int d_y=(((((mid_y+dst.getGuiLocation().iy())/2)+dst.getGuiLocation().iy())/2)+dst.getGuiLocation().iy())/2;

                    g.fillOval(d_x-3,d_y-3,NODE_WIDTH_HEIGHT,NODE_WIDTH_HEIGHT);
                }
            }
        }
    }

    private  double [] find_min_max_axis()
    {
        double [] x_scale=new double[2];
        double min=Double.MAX_VALUE;
        double max=Double.MIN_VALUE;

        for (node_data node:this.graph.getV()) {

            if(node.getLocation().x()<min)
                min=node.getLocation().x();
            else {
                if (node.getLocation().x() > max) {
                    max = node.getLocation().x();
                }
            }
        }

        x_scale[0]=min;
        x_scale[1]=max;

        return x_scale;

    }

    private  double [] find_min_max_ayis()
    {
        double [] y_scale=new double[2];
        double min=Double.MAX_VALUE;
        double max=Double.MIN_VALUE;

        for (node_data node:this.graph.getV()) {

            if(node.getLocation().y()<min)
                min=node.getLocation().y();
            else {
                if (node.getLocation().y() > max) {
                    max = node.getLocation().y();
                }
            }
        }

        y_scale[0]=min;
        y_scale[1]=max;

        return y_scale;

    }

    private double scale(double data, double r_min, double r_max, double t_min, double t_max) {
        double res = ((data - r_min) / (r_max - r_min)) * (t_max - t_min) + t_min;
        return res;
    }
    private void setNodesForPaint()
    {
        double []x_toScale=find_min_max_axis();
        double []y_toScale=find_min_max_ayis();
        for (node_data node:this.graph.getV()) {
            double x_gui = scale(node.getLocation().x(), x_toScale[0], x_toScale[1], 5, this.getWidth()-50);
            double y_gui = scale(node.getLocation().y(), y_toScale[0], y_toScale[1],70,this.getHeight()-70);
            node.setGuiLocation(x_gui, y_gui);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {

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
    public static void main(String[] a) {
//        game_service game = Game_Server.getServer(2); // you have [0,23] games
//        System.out.println(game.getGraph());
        MyGameGUI m = new MyGameGUI(16);
      //  System.out.println(m.g);
    }


}
