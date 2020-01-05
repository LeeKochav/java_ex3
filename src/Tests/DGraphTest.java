package Tests;

import dataStructure.DGraph;
import dataStructure.Node;
import dataStructure.node_data;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class DGraphTest {
   static Node n0;
    static Node n1;
    static Node n2;
    static Node n3;
    static Node n4;
    static Node n5;
    static DGraph g;

    @BeforeAll
    static void init() {
        n0 = new Node(0);
        n1 = new Node(1);
        n2 = new Node(2);
        n3 = new Node(3);
        n4 = new Node(4);
        n5 = new Node(5);
        g = new DGraph();
        g.addNode(n0);
        g.addNode(n1);
        g.addNode(n2);
        g.addNode(n3);
        g.addNode(n4);
        g.addNode(n5);
        g.connect(0, 5, 10);
        g.connect(0, 2, 20);
        g.connect(5, 1, 25);
        g.connect(5, 3, 7);
        g.connect(2, 3, 30);
        g.connect(1, 4, 4);
        g.connect(3, 4, 2);

    }

    @Test
    void getNode() {
        assertEquals(g.getNode(0).getKey(),n0.getKey());
        assertEquals(g.getNode(5).getKey(), n5.getKey());
    }

    @Test
    void getEdge() {
        assertTrue(g.getEdge(1, 6) == null);
        assertEquals(g.getEdge(3, 4).getWeight(), 2.0);
    }

    @Test
    void addNode() {
        Node n6 = new Node(6);
        int size=g.nodeSize();
        g.addNode(n6);
        assertEquals(g.nodeSize(), size+1);
        try {
            g.addNode(null);
            fail("addNode function did not throw exception when node is null");
        } catch (RuntimeException e) {
            System.out.println("addNode did throw exception when node is null : " + e.getMessage());
        }
    }

    @Test
    void connect() {
        g.connect(4, 0, 3);
        g.connect(1, 3, 88);
        assertEquals(g.getEdge(4, 0).getWeight(), 3.0);
        assertEquals(g.getEdge(1, 3).getWeight(), 88.0);
        try {
            g.connect(10, 20, 5);
            fail("Connect function did not throw exception when input is not valid");
        } catch (RuntimeException error) {
            System.out.println("Connect function throw exception when input is not valid: " + error.getMessage());
        }
    }

    @Test
    void getV() {
        DGraph g2 = new DGraph();
        assertTrue(g.getV().size()==g.nodeSize());
        assertTrue(g2.getV().size() == 0);
    }

    @Test
    void getE() {
        assertTrue(g.getE(0).size() == 2);
    }

    @Test
    void removeNode() {
        assertTrue(g.removeNode(8) == null);
        System.out.println(g.getV().toString());
        int tmpEdgeSize = g.edgeSize();
        int tmpNodeSize = g.nodeSize();
        g.removeNode(2);
        assertTrue(g.nodeSize() == tmpNodeSize - 1);
        assertTrue(g.edgeSize() == tmpEdgeSize - 2);

    }

    @Test
    void removeEdge() {
        assertTrue(g.removeEdge(1, 7) == null);
        int tmpEdgeSize = g.edgeSize();
        g.removeEdge(0, 5);
        assertEquals(g.edgeSize(), tmpEdgeSize - 1);

    }

    @Test
    void nodeSize() {
        int s = g.nodeSize();
        assertEquals(g.nodeSize(), s);
    }

    @Test
    void edgeSize() {
        int s = g.edgeSize();
        assertEquals(g.edgeSize(), s);
    }

    @Test
    void performance() {
        assertTimeout(Duration.ofMillis(10000), () -> {
            int million = 1000000;
            DGraph d = new DGraph(million);
            for (node_data n : d.getV()) {
                for (int i = 1; i <= 5; i++) {
                    try {
                        d.connect(n.getKey(), n.getKey() + i, i * 5);
                    } catch (RuntimeException e) {
                        continue;
                    }
                }
            }
        });
    }
}