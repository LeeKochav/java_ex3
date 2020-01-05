package algorithms;

import java.io.*;
import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;
import dataStructure.*;


/**
 * This empty class represents the set of graph-theory algorithms
 * which should be implemented as part of Ex2 - Do edit this class.
 * @author
 *
 */
public class Graph_Algo implements graph_algorithms {

	private static final VER_COMP comp = new VER_COMP();
	private graph graph;

	public Graph_Algo(){}

	public Graph_Algo(graph g)
	{
		init(g);
	}
	@Override
	public void init(graph g) {
		this.graph = g;
	}

	@Override
	public void init(String file_name) {

		try {
			FileInputStream file = new FileInputStream(file_name);
			ObjectInputStream in = new ObjectInputStream(file);

			this.graph = (graph) in.readObject();
			in.close();
			file.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void save(String file_name){

		try {
			FileOutputStream file = new FileOutputStream(file_name);
			ObjectOutputStream out = new ObjectOutputStream(file);

			out.writeObject(this.graph);

			out.close();
			file.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public boolean isConnected() {
		if(this.graph ==null) return false;
		else if(this.graph.getV().size()==0) return true;
		for (node_data nodeSrc: this.graph.getV()) {
			for (node_data nodeDst: this.graph.getV()) {
				if (shortestPathDist(nodeSrc.getKey(), nodeDst.getKey()) == Double.MAX_VALUE)
					return false;
				if (shortestPathDist(nodeDst.getKey(), nodeSrc.getKey()) == Double.MAX_VALUE)
					return false;
			}
		}
		return true;
	}

	/*
	Implementation of shortest path is based on Dijkstra algorithm.
	 */
	@Override
	public double shortestPathDist(int src, int dest) {
		try {
		node_data srC=this.graph.getNode(src);
		node_data dsT=this.graph.getNode(dest);
			Collection<node_data> vertices = this.graph.getV();
			PriorityBlockingQueue<node_data> q = new PriorityBlockingQueue<>(this.graph.nodeSize(), new VER_COMP());
			for (node_data node : vertices) {
				if (node.getKey() == src) {
					node.setWeight(0);
				} else {
					node.setWeight(Double.MAX_VALUE);
				}
				node.setTag(1);
				q.add(node);
			}
			while (!q.isEmpty() && dsT.getTag() != 3) { // tag=3 means visited
				node_data tmp_src = q.poll();
				Collection<edge_data> edgesTmp = this.graph.getE(tmp_src.getKey());
				if (edgesTmp != null) {
					for (edge_data edge : edgesTmp) {
						node_data tmp_dst = this.graph.getNode(edge.getDest());
						if (tmp_dst.getTag() != 3) {
							double tmp_w = tmp_src.getWeight() + edge.getWeight();
							if (tmp_dst.getWeight() > tmp_w) {
								tmp_dst.setWeight(tmp_w);
								tmp_dst.setInfo(tmp_src.getKey() + "");	//set the parent of the node
								//decrease key :
								PriorityBlockingQueue<node_data> tmp_q = new PriorityBlockingQueue<>(this.graph.nodeSize(), new VER_COMP());
								while (!q.isEmpty()) {
									tmp_q.add(q.poll());
								}
								q = tmp_q;
							}
						}
					}
				}
				tmp_src.setTag(3);
			}
			return dsT.getWeight();
		}
		catch (RuntimeException error)
		{
			System.out.println("Invalid input");
			return Double.MAX_VALUE;
		}
	}

	@Override
	public List<node_data> shortestPath(int src, int dest) {

			double dist=shortestPathDist(src, dest);
			if(dist==Double.MAX_VALUE) return null;

		LinkedList<node_data> res=new LinkedList<>();
		int t=dest;
		while(this.graph.getNode(t).getKey()!=src)
		{
			if(this.graph.getNode(t).getInfo()==null)
				return null;
			int next=Integer.parseInt(this.graph.getNode(t).getInfo());
			res.add(this.graph.getNode(next));
			t=next;
		}
		res.add(this.graph.getNode(dest));
		res.sort(comp);
		return res;

	}

	/*
            Given a list of targets find if exist a path in the graph that travels targets list by the insertion order.
             */
	@Override
	public List<node_data> TSP(List<Integer> targets) {

		LinkedList<node_data> res=new LinkedList<node_data>();
		LinkedList<node_data> tmp;
		if(targets.size()==1)
		{
			return null;
		}
		for(int i=0; i<targets.size()-1; i++)
		{
			try {
				tmp = (LinkedList<node_data>) shortestPath(targets.get(i), targets.get(i + 1));
			}
			catch(RuntimeException e)
			{
				System.out.println(e.getMessage());
				return null;
			}
			if(tmp==null) return null;

			else if (tmp.size() > 1 && res.contains(graph.getNode(targets.get(i))))
				tmp.remove(graph.getNode(targets.get(i)));

			res.addAll(tmp);
		}
		return res;
	}

	/*
    Graph deep copy
     */
	@Override
	public graph copy() {
		graph g = new DGraph();
		Collection<node_data> vertices = this.graph.getV();
		for (node_data n : vertices)
			g.addNode(new Node(n));
		for (node_data n : vertices) {
			Collection<edge_data> edges = this.graph.getE(n.getKey());
			if(edges!=null) {
				for (edge_data e : edges) {
					g.connect(e.getSrc(), e.getDest(), e.getWeight());
				}
			}
		}
		return g;

	}
}
