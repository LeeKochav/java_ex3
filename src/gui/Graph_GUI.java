package gui;

import algorithms.Graph_Algo;
import dataStructure.*;
import utils.Point3D;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

public class Graph_GUI extends JFrame implements ActionListener , MouseListener {

    private graph graph;
    private Graph_Algo algoGraph;
    private  int mc;
    final int NODE_WIDTH_HEIGHT=10;

    public Graph_GUI(graph g)  {
        this.graph=g;
        algoGraph = new Graph_Algo();
        mc=g.getMC();
        initGui(1000, 1000);
    }
    /*
    Default constructor
     */
    public Graph_GUI()
    {
        this.graph=new DGraph();
        algoGraph=new Graph_Algo();
        mc=0;
        initGui(1000,1000);
    }

    private void initGui(int width, int height)  {
        this.setBounds(200,0,width,height);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Graph_GUI");
        MenuBar menuBar=new MenuBar();
        Menu menu=new Menu("File");
        Menu menu2=new Menu("Algorithms");
        Menu menu3=new Menu("Add/Remove Node");
        Menu menu4=new Menu("Add/Remove Edge");

        menu.setFont(new Font("deafult", Font.BOLD,12));
        menu2.setFont(new Font("deafult", Font.BOLD,12));
        menu3.setFont(new Font("deafult", Font.BOLD,12));
        menu4.setFont(new Font("deafult", Font.BOLD,12));

        MenuItem itemAlgo=new MenuItem("isConnected");
        itemAlgo.setFont(new Font("deafult", Font.BOLD,12));
        itemAlgo.addActionListener(this);

        MenuItem itemAlgo1=new MenuItem("ShortestPathDistance");
        itemAlgo1.setFont(new Font("deafult", Font.BOLD,12));
        itemAlgo1.addActionListener(this);

        MenuItem itemAlgo2=new MenuItem("ShortestPath");
        itemAlgo2.setFont(new Font("deafult", Font.BOLD,12));
        itemAlgo2.addActionListener(this);

        MenuItem itemAlgo3=new MenuItem("TSP");
        itemAlgo3.setFont(new Font("deafult", Font.BOLD,12));
        itemAlgo3.addActionListener(this);

        menu2.add(itemAlgo);
        menu2.add(itemAlgo1);
        menu2.add(itemAlgo2);
        menu2.add(itemAlgo3);

        MenuItem item1=new MenuItem("Save");
        item1.setFont(new Font("deafult", Font.BOLD,12));
        item1.addActionListener(this);

        MenuItem item2=new MenuItem("Load");
        item2.setFont(new Font("deafult", Font.BOLD,12));
        item2.addActionListener(this);

        menu.add(item1);
        menu.add(item2);

        MenuItem itemNode1=new MenuItem("addNode");
        itemNode1.setFont(new Font("deafult", Font.BOLD,12));
        itemNode1.addActionListener(this);

        MenuItem itemNode2=new MenuItem("removeNode");
        itemNode2.setFont(new Font("deafult", Font.BOLD,12));
        itemNode2.addActionListener(this);

        menu3.add(itemNode1);
        menu3.add(itemNode2);

        MenuItem itemEdge1=new MenuItem("Connect/addEdge");
        itemEdge1.setFont(new Font("deafult", Font.BOLD,12));
        itemEdge1.addActionListener(this);

        MenuItem itemEdge2=new MenuItem("removeEdge");
        itemEdge2.setFont(new Font("deafult", Font.BOLD,12));
        itemEdge2.addActionListener(this);

        menu4.add(itemEdge1);
        menu4.add(itemEdge2);

        this.setMenuBar(menuBar);
        menuBar.add(menu);
        menuBar.add(menu2);
        menuBar.add(menu3);
        menuBar.add(menu4);


     //   setLocations();
        this.addMouseListener(this);
        this.setVisible(true);
        Thread t=new Thread(new Runnable() { //repaint the graph gui using thread if there has been change in the graph
            @Override
            public void run() {
                while(true) {
                    synchronized (graph) {
                        if (mc != graph.getMC()) {
                            mc = graph.getMC();
                            repaint();
                        }
                    }
                }
            }
        });
        t.start();

    }

    @Override
     public void actionPerformed(ActionEvent e) {
        String str = e.getActionCommand();
        switch (str) {
            case "Save":
                saveGraph();
                break;
            case "Load":
                loadGraph();
                break;
            case "isConnected":
                isConnectedGui();
                break;
            case "ShortestPathDistance":
                ShortestPathDistCalc();
                break;
            case "ShortestPath":
                ShortestPathDistCalcList();
                break;
            case "TSP":
                ShortestPathDistTargetsList();
                break;
            case "addNode":
                JOptionPane.showMessageDialog(this,"Press on the gui screen to insert a new node");
                break;
            case "removeNode":
                removeNodeGui();
                break;
            case "Connect/addEdge":
                addEdgeGui();
                break;
            case "removeEdge":
                removeEdgeGui();
                break;
            default:
                break;
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
            System.out.println(node.getGuiLocation());
        }



    }

    public void paint(Graphics graphics)
    {
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
        for (node_data node: graph.getV() ){
            if(graph.getE(node.getKey())!=null) {
                for (edge_data edge : graph.getE(node.getKey())) {
                    g.setColor(Color.BLACK);
                    g.setFont(new Font("deafult", Font.BOLD,14));
                    String weight = edge.getWeight() + "";
                    node_data dst = graph.getNode(edge.getDest());
                    g.drawLine(node.getGuiLocation().ix(), node.getGuiLocation().iy(), dst.getGuiLocation().ix(), dst.getGuiLocation().iy());
                    double dist = node.getGuiLocation().distance2D(dst.getGuiLocation());
                    g.drawString(weight, (int) ((node.getGuiLocation().x() + dst.getGuiLocation().x()) / 2), (int) ((node.getGuiLocation().y() + dst.getGuiLocation().y()) / 2));
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
    private void isConnectedGui()
    {
        algoGraph.init(graph);
        boolean b=algoGraph.isConnected();
        JOptionPane.showMessageDialog(this, "Graph is connected? \n"+b, "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
    }
    private void removeNodeGui()
    {
        String str_key= JOptionPane.showInputDialog(this,"Please insert node key to remove");
        try
        {
            int key=Integer.parseInt(str_key);
            node_data n=graph.removeNode(key);
            if(n!=null) {
                JOptionPane.showMessageDialog(this, "Remove node succeeded", "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Key does not exist in graph", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
        catch (Exception ex)
        {
            JOptionPane.showMessageDialog(this, "ERROR: " + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void addEdgeGui()
    {
        String str_src= JOptionPane.showInputDialog(this,"Please insert source node");
        String str_dst=JOptionPane.showInputDialog(this,"Please insert destination node");
        String str_w=JOptionPane.showInputDialog(this,"Please insert weight");
        try
        {
            int src=Integer.parseInt(str_src);
            int dst=Integer.parseInt(str_dst);
            double w=Double.parseDouble(str_w);
            graph.connect(src,dst,w);
            JOptionPane.showMessageDialog(this, "Connect succeed", "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (Exception ex)
        {
            JOptionPane.showMessageDialog(this, "ERROR: " + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void removeEdgeGui()
    {
        String str_src= JOptionPane.showInputDialog(this,"Please insert source node");
        String str_dst= JOptionPane.showInputDialog(this,"Please destination source node");

        try
        {
            int src=Integer.parseInt(str_src);
            int dst=Integer.parseInt(str_dst);
            edge_data e=graph.removeEdge(src,dst);
            if(e!=null) {
                JOptionPane.showMessageDialog(this, "Removed edge succeeded", "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Invalid source and destination ", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
        catch (Exception ex)
        {
            JOptionPane.showMessageDialog(this, "ERROR: " + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }

    }
    private void saveGraph()
    {
        algoGraph.init(graph);
        FileDialog fd = new FileDialog(this, "Save graph", FileDialog.SAVE);
        fd.setFile("*.txt");
        fd.setFilenameFilter(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".txt");
            }
        });
        fd.setVisible(true);
        if(fd.getDirectory()!=null&&fd.getFile()!=null) {
            algoGraph.save(fd.getDirectory() + fd.getFile());
            JOptionPane.showMessageDialog(this, "Graph saved to " + fd.getFile(), "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
        }
        else
        {
            JOptionPane.showMessageDialog(this, "File did not saved" , "ERROR", JOptionPane.ERROR_MESSAGE);

        }
    }
    private void loadGraph()
    {
        FileDialog fd = new FileDialog(this, "Open text file", FileDialog.LOAD);
        fd.setFile("*.txt");
        fd.setFilenameFilter(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".txt");
            }
        });
        fd.setVisible(true);
        algoGraph.init(fd.getDirectory() + fd.getFile());
        this.graph = algoGraph.copy();
        algoGraph.init(this.graph);
        repaint();
    }
    private void ShortestPathDistCalc()
    {
        algoGraph.init(graph);
        String str_src= JOptionPane.showInputDialog(this,"Please insert source node");
        String str_dst=JOptionPane.showInputDialog(this,"Please insert destination node");
        try
        {
            int src=Integer.parseInt(str_src);
            int dst=Integer.parseInt(str_dst);
            double path=algoGraph.shortestPathDist(src,dst);
            if(path==Double.MAX_VALUE)
            {
                JOptionPane.showMessageDialog(this, "The shortest path distance does not exist ", "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
            }
            else
                JOptionPane.showMessageDialog(this, "The shortest path distance is: " + path, "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (Exception ex)
        {
            JOptionPane.showMessageDialog(this, "ERROR: " + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }

    }
    private void ShortestPathDistCalcList()
    {
        algoGraph.init(graph);
        String str_src= JOptionPane.showInputDialog(this,"Please insert source node");
        String str_dst=JOptionPane.showInputDialog(this,"Please insert destination node");
        LinkedList<node_data> res;
        try
        {
            int src=Integer.parseInt(str_src);
            int dst=Integer.parseInt(str_dst);
            res= (LinkedList<node_data>) algoGraph.shortestPath(src,dst);
            String ans="";
            if(res!=null)
            {
                ans+="[";
                for(int i=0; i<res.size()-1; i++)
                    ans+=res.get(i)+"-->";
                ans+=res.get(res.size()-1);
                ans+="]";
            }
            else
            {
                ans="no path between source and destination";
            }
            JOptionPane.showMessageDialog(this, "The shortest path is: " + ans, "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (Exception ex)
        {
            JOptionPane.showMessageDialog(this, "ERROR: " + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void ShortestPathDistTargetsList()
    {
        algoGraph.init(graph);
        String str_numberOfTargets=JOptionPane.showInputDialog(this,"Please enter the number of targets");
        LinkedList<Integer> targets=new LinkedList<Integer>();
        LinkedList<node_data> res;
        try
        {
            int n_targets=Integer.parseInt(str_numberOfTargets);
            for(int i=1; i<=n_targets; i++) {
                String str_target = JOptionPane.showInputDialog(this, "Please insert target node: "+"#"+i);
                int target=Integer.parseInt(str_target);
                targets.add(target);
            }
            res= (LinkedList<node_data>) algoGraph.TSP(targets);
            String ans="";
            if(res!=null)
            {
                ans+="[";
                for(int i=0; i<res.size()-1; i++)
                    ans+=res.get(i)+"-->";
                ans+=res.get(res.size()-1);
                ans+="]";
            }
            else
            {
                ans="no path between source and destination";
            }
            JOptionPane.showMessageDialog(this, "The shortest path is: " + ans, "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (Exception ex)
        {
            JOptionPane.showMessageDialog(this, "ERROR: " + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }

    }
    public void setLocations() {
        Random rand = new Random();
        for (node_data node : graph.getV()) {
            double x = rand.nextInt((int) (this.getWidth() / 1.5)) + 50;
            double y = rand.nextInt((int) (this.getHeight() / 1.5)) + 70;
            Point3D p = new Point3D(x, y);
            node.setLocation(p);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
            String str_key = JOptionPane.showInputDialog(this, "Please insert node key");
            int x = e.getX();
            int y = e.getY();
            Point3D p = new Point3D(x, y);
            try {
                int key = Integer.parseInt(str_key);
                node_data newNode = new Node(key);
                newNode.setLocation(p);
                graph.addNode(newNode);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "ERROR: " + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
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
