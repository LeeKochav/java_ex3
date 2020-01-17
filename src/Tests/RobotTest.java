package Tests;

import gameClient.Robot;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class RobotTest {

   static int ROBOT_SIZE=2;
   static ArrayList<Robot> robots;

    @BeforeAll
    static void init()
    {
        double pos_x= 35.207151268054346;
        double pos_y=32.10259023385377;
        Random rand=new Random();
        robots=new ArrayList<>();
        for(int i=0; i<ROBOT_SIZE; i++)
        {
         String id=""+i;
         String src=""+rand.nextInt(ROBOT_SIZE);
         String dst=""+ -1;
         String value=""+0;
         String speed=""+(double)(rand.nextInt(5));
         String pos=""+(pos_x+i)+","+(pos_y+i)+"0,0";
         String rob=createRobotFromString(id,value, src, dst, speed, pos);
         robots.add(new Robot(rob));
        }
    }

    @Test
    void getId() {

     for(int i=0; i<ROBOT_SIZE; i++)
     {
      assertEquals(i,robots.get(i).getId());
     }
    }

    @Test
    void getValue() {

     for(int i=0; i<ROBOT_SIZE; i++)
     {
      assertEquals(0,robots.get(i).getValue());
     }
    }

    @Test
    void setGetSrc() {
     for(int i=0; i<ROBOT_SIZE; i++)
     {
      robots.get(i).setSrc(i);
      assertEquals(i,robots.get(i).getSrc());
     }
    }

    @Test
    void setGetDest() {
     for(int i=0; i<ROBOT_SIZE; i++)
     {
      robots.get(i).setDest(i);
      assertEquals(i,robots.get(i).getDest());
     }
    }

    @Test
    void setGetSpeed() {
     Random rand=new Random();
     for(int i=0; i<ROBOT_SIZE; i++)
     {
      double s=(double)rand.nextInt(7);
      robots.get(i).setSpeed(s);
      assertEquals(s,robots.get(i).getSpeed());
     }
    }


    @Test
    void testToString() {
     for(int i=0; i<ROBOT_SIZE; i++)
     {
      String s=robots.get(i).toString();
      assertEquals(s,robots.get(i).toString());
     }
    }

    private static String createRobotFromString(String id,String value, String src, String dest, String speed, String pos)
    {
     String s="{\"Robot\":{\"id\":"+id+",\"value\":"+value+",\"src\":"+src+",\"dest\":"+dest+",\"speed\":"+speed+",\"pos\":\""+pos+"\"}}";
    return  s;
    }
}