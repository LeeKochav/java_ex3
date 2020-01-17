package Tests;

import gameClient.Fruit;
import gameClient.VAL_COMP;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.Point3D;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class FruitTest {

    static VAL_COMP COMP=new VAL_COMP();
    static int FRUIT_SIZE=2;
    static ArrayList<Fruit> fruits;

    @BeforeAll
    static void init()
    {
        double pos_x= 35.207151268054346;
        double pos_y=32.10259023385377;
        Random rand=new Random();
        fruits=new ArrayList<>();
        for(int i=0; i<FRUIT_SIZE; i++)
        {
            String value=""+(double)rand.nextInt(20);
            String type=""+-1;
            String pos=""+(pos_x+i)+","+(pos_y+i)+"0,0";
            String fru=createFruitFromString(value,type,pos);
            fruits.add(new Fruit(fru));
        }
        fruits.sort(COMP);

    }

    @Test
    void getType() {
        for(int i=0; i<FRUIT_SIZE; i++)
        {
            assertEquals(-1,fruits.get(i).getType());
        }
    }

    @Test
    void getValAndCheckSort() {
        for(int i=0; i<FRUIT_SIZE-1; i++)
        {
            double val=fruits.get(i).getValue();
            assertEquals(val,fruits.get(i).getValue());
            assertTrue(fruits.get(i).getValue()>=fruits.get(i+1).getValue());
        }
    }

    @Test
    void getLocation() {
        for(int i=0; i<FRUIT_SIZE; i++)
        {
            Point3D p=fruits.get(i).getLocation();
            assertEquals(p.toString(),fruits.get(i).getLocation().toString());
        }
    }

    @Test
    void testToString() {
        for(int i=0; i<FRUIT_SIZE; i++)
        {
            String s=fruits.get(i).toString();
            assertEquals(s,fruits.get(i).toString());
        }
    }



    private static String createFruitFromString(String value, String type,String pos)
    {
        String s="{\"Fruit\":{\"value\":" + value + "," + "\"type\":" + type + "," + "\"pos\":\"" + pos.toString()+ "\"" + "}" + "}";
        return  s;
    }
}