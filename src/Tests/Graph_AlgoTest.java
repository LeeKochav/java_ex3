package Tests;

import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.graph;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Graph_AlgoTest {

   static Graph_Algo algo;

    @BeforeAll
    static void initialize()
    {
        DGraph g = new DGraph(6);
        algo=new Graph_Algo();
        g.connect(0, 5, 10);
        g.connect(0, 2, 20);
        g.connect(5, 1, 25);
        g.connect(5, 3, 7);
        g.connect(2, 3, 30);
        g.connect(1, 4, 4);
        g.connect(3, 4, 2);
        g.connect(1,6,1);
        g.connect(4,6,3);
        algo.init(g);
    }

    @Test
    void init() {
        Graph_Algo algoTest=new Graph_Algo();
        graph graph=new DGraph(2);
        graph.connect(0,1,10);
        graph.connect(1,2,20);
        algoTest.init(graph);
    }

    @Test
    void testInitAndSave() {
        algo.save("test.txt");
        Graph_Algo afterInit=new Graph_Algo();
        afterInit.init("test.txt");
        assertEquals(algo.isConnected(),afterInit.isConnected());
        assertEquals(algo.shortestPathDist(0,5),afterInit.shortestPathDist(0,5));
        assertEquals(algo.shortestPath(2,6).toString(),afterInit.shortestPath(2,6).toString());
    }

    @Test
    void isConnected() {
        assertTrue(algo.isConnected()==false);
        Graph_Algo algoTest=new Graph_Algo();
        assertTrue(algoTest.isConnected()==false);
    }

    @Test
    void shortestPathDist() {
        double path=algo.shortestPathDist(0,1);
        assertEquals(35,path);
        assertEquals(algo.shortestPathDist(5,2),Double.MAX_VALUE);
            assertEquals(algo.shortestPathDist(20, 10), Double.MAX_VALUE);
    }

    @Test
    void shortestPath() {
        assertTrue(algo.shortestPath(0,6)!=null);
        assertTrue(algo.shortestPath(5,2)==null);
    }

    @Test
    void TSP() {
        List<Integer> tmp=  Arrays.asList(3,5,6);
        assertTrue(algo.TSP(tmp)==null);
        tmp= Arrays.asList(5,3,6);
        assertTrue(algo.TSP(tmp)!=null);
    }

    @Test
    void copy() {
        graph souce=algo.copy();
        Graph_Algo algoCopy=new Graph_Algo();
        algoCopy.init(souce);
        graph equalToSource=algoCopy.copy();
        souce.removeNode(0);
        assertNotEquals(souce.nodeSize(),equalToSource.nodeSize());
    }
}