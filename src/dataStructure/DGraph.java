package dataStructure;

//import elements.NodeData;
import gameClient.Main_Thread;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.Point3D;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class DGraph implements graph , Serializable {

	private LinkedHashMap<Integer,node_data> nodes;
	private LinkedHashMap<Integer,LinkedHashMap<Integer,edge_data>> edges;
	private int numVer=0;
	private int numEdg=0;
	private  int change=0;
	public static final double EPS1 = 1.0E-4D;
	public static final double EPS2 = Math.pow(1.0E-4D, 2.0D);
	public static final double EPS;
	static {
		EPS = EPS2;
	}


	public DGraph()
	{
		nodes =new LinkedHashMap<>();
		edges=new LinkedHashMap<>();
	}
	public DGraph(String g)
	{
		init(g);
	}
	/*
	Constructor for testing
	 */
	public DGraph(int num) {
		this();
		for (int i = 0; i <= num; i++) {
			node_data new_node = new Node(i);
			addNode(new_node);
		}
	}

	@Override
	public node_data getNode(int key) {
		return this.nodes.get(key);
	}

	@Override
	public edge_data getEdge(int src, int dest) {
		if(this.edges.get(src)==null)return null;
		return this.edges.get(src).get(dest);
	}

	@Override
	public void addNode(node_data n) {
		if(n==null) throw new RuntimeException("Invalid input");
		else {
			if (this.nodes.containsKey(n.getKey()))
				throw new RuntimeException("Invalid input, node already exist");
				this.nodes.put(n.getKey(), n);
				numVer++;
				change++;

			}
	}

	/*
	Connect if and only if both src and dest exist in graph, if src node did not have edges yet create new edges parameter otherwise, add the new edge.
	In addition, update the change parameter for MC.
	 */
	@Override
	public void connect(int src, int dest, double w) {
		node_data s=this.nodes.get(src);
		node_data d=this.nodes.get(dest);
		if(s!=null&&d!=null) {
			edge_data edgeTmp=this.getEdge(src,dest);
			if(edgeTmp!=null)
			{
				numEdg--;
			}
			edge_data edge = new Edge(s, d, w);
			LinkedHashMap<Integer, edge_data> newConnection;
			if (edges.get(src) == null) {
				newConnection = new LinkedHashMap<Integer, edge_data>();
			} else {
				newConnection = edges.get(src);
			}
			newConnection.put(dest, edge);
			this.edges.put(src, newConnection);
			numEdg++;
			change++;
		}
		else
		{
			throw new RuntimeException("Invalid source and destination nodes input");
		}
	}

	@Override
	public Collection<node_data> getV() {
		return this.nodes.values();
	}

	@Override
	public Collection<edge_data> getE(int node_id) {
		if(this.edges.get(node_id)==null)
			return null;
		return this.edges.get(node_id).values();
	}
	/*
	Remove node, his edges and all the edges that this node is the dest node.
	 */
	@Override
	public node_data removeNode(int key) {
		node_data rm=this.nodes.remove(key);
		if(rm!=null) {
			numVer--;
			change--;
			if(this.edges.get(key)!=null) {
				numEdg-=this.edges.get(key).size();
				this.edges.remove(key);
			}
			for (LinkedHashMap<Integer, edge_data> tmp : this.edges.values()) {
				edge_data rm2 = tmp.remove(key);
				if (rm2 != null) {
					numEdg--;
				}
			}
		}
		return rm;
	}

	@Override
	public edge_data removeEdge(int src, int dest) {

		edge_data rm=this.edges.get(src).remove(dest);
		if(rm!=null)
		{
			numEdg--;
			change--;
		}
		return rm;
	}

	@Override
	public int nodeSize() {
		return this.numVer;
	}

	@Override
	public int edgeSize() {
		return this.numEdg;
	}

	@Override
	public int getMC() {
		return this.change;
	}

	public String toString() {
		return this.toJSON();
	}

	public String toString1() {
		String ans = this.getClass().getName() + "|V|=" + this.getV().size() + ", |E|=" + this.edgeSize() + "\n";

		for(Iterator iter = this.getV().iterator(); iter.hasNext(); ans = ans + " *** new Node ***\n") {
			node_data c = (node_data)iter.next();
			ans = ans + c.toString() + "\n";

			edge_data ee;
			for(Iterator ei = this.getE(c.getKey()).iterator(); ei.hasNext(); ans = ans + ee.toString() + "\n") {
				ee = (edge_data)ei.next();
			}
		}

		return ans;
	}

	public void init(String jsonSTR) {
		try {
		//	NodeData.resetCount();
			this.init();
			this.numEdg = 0;
			JSONObject graph = new JSONObject(jsonSTR);
			JSONArray nodes = graph.getJSONArray("Nodes");
			JSONArray edges = graph.getJSONArray("Edges");

			int i;
			int s;
			for(i = 0; i < nodes.length(); ++i) {
				s = nodes.getJSONObject(i).getInt("id");
				String pos = nodes.getJSONObject(i).getString("pos");
				Point3D p = new Point3D(pos);
				node_data node=new Node(s,p);
				this.addNode(node);
			}

			for(i = 0; i < edges.length(); ++i) {
				s = edges.getJSONObject(i).getInt("src");
				int d = edges.getJSONObject(i).getInt("dest");
				double w = edges.getJSONObject(i).getDouble("w");
				this.connect(s, d, w);
			}
		} catch (Exception var10) {
			var10.printStackTrace();
		}

	}

	public String toJSON() {
		JSONObject allEmps = new JSONObject();
		JSONArray VArray = new JSONArray();
		JSONArray EArray = new JSONArray();
		Collection<node_data> V = this.getV();
		Iterator<node_data> iter = V.iterator();
		Collection<edge_data> E = null;
		Iterator itr = null;

		try {
			while(iter.hasNext()) {
				node_data nn = (node_data)iter.next();
				int n = nn.getKey();
				String p = nn.getLocation().toString();
				JSONObject node = new JSONObject();
				node.put("id", n);
				node.put("pos", p);
				VArray.put(node);
				itr = this.getE(n).iterator();

				while(itr.hasNext()) {
					edge_data ee = (edge_data)itr.next();
					JSONObject edge = new JSONObject();
					edge.put("src", ee.getSrc());
					edge.put("dest", ee.getDest());
					edge.put("w", ee.getWeight());
					EArray.put(edge);
				}
			}

			allEmps.put("Nodes", VArray);
			allEmps.put("Edges", EArray);
		} catch (Exception var14) {
			var14.printStackTrace();
		}

		return allEmps.toString();
	}

	private void init() {
		this.nodes = new LinkedHashMap<>();
		this.edges = new LinkedHashMap<>();
	}

}
