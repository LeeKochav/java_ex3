package Tests;

import dataStructure.Edge;
import dataStructure.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EdgeTest {

    Node n1;
    Node n2;
    Node n3;
    Edge e;
    Edge e2;

    @BeforeEach
    void init()
    {
        n1=new Node(0);
        n2=new Node(1);
        n3=new Node(2);
        e=new Edge(n1,n2,10);
        e2=new Edge(n2,n3,20);
    }
    @Test
    void getSrc() {
        assertEquals(e.getSrc(),n1.getKey());
        assertEquals(e2.getSrc(),n2.getKey());
    }

    @Test
    void getDest() {
        assertEquals(e.getDest(),n2.getKey());
        assertEquals(e2.getDest(),n3.getKey());
    }

    @Test
    void getWeight() {
        assertEquals(10.0,e.getWeight());
        assertEquals(20.0,e2.getWeight());
    }

    @Test
    void getInfo() {
        assertEquals("",e.getInfo());
    }

    @Test
    void setInfo() {
        e.setInfo("[src:"+e.getSrc()+" ,dst:"+e.getDest()+ ", weight:"+e.getWeight()+"]");
        assertFalse(e.getInfo().equals(""));
    }

    @Test
    void getTag() {
        assertEquals(e.getTag(),e2.getTag());
    }

    @Test
    void setTag() {
        e.setTag(2);
        assertFalse(e.getTag()==e2.getTag());
    }
}