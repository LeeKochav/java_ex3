package Tests;

import dataStructure.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Point3D;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {

     Node n;
     Node n1;

    @BeforeEach
     void init()
    {
        n=new Node(0);
        n1=new Node(1);
    }

    @Test
    void getKey() {
        int n_key=n.getKey();
        int n1_key=n1.getKey();
        assertEquals(1,n_key+n1_key);
    }

    @Test
    void getLocation() {
        Point3D p=n.getLocation();
        assertEquals(p.toString(),n.getLocation().toString());
        n1=new Node(n);
        assertEquals(n.getLocation().toString(),n1.getLocation().toString());
    }

    @Test
    void setLocation() {
        Point3D p=n.getLocation();
        n.setLocation(p);
        assertEquals(p.toString(),n.getLocation().toString());
    }

    @Test
    void getWeight() {
       assertEquals(n.getWeight(),n1.getWeight());
    }

    @Test
    void setWeight() {
        n.setWeight(0);
        assertFalse(n.getWeight()==n1.getWeight());
    }

    @Test
    void getInfo() {
        assertEquals(n.getInfo(),n1.getInfo());
    }

    @Test
    void setInfo() {
        n.setInfo("Test");
        assertEquals("Test",n.getInfo());
    }

    @Test
    void getTag() {
        assertEquals(1,n.getTag());
    }

    @Test
    void setTag() {
        assertTrue(n1.getTag()==n.getTag());
        n.setTag(2);
        assertFalse(n1.getTag()==n.getTag());
    }
}