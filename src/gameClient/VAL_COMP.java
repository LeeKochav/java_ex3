package gameClient;

import java.util.Comparator;

/**
 * This class represents a comparator that compare fruits values
 */
public class VAL_COMP implements Comparator<Fruit> {

    public VAL_COMP(){}

    @Override
    public int compare(Fruit f1, Fruit f2) {
        int res=0;
        if(f1.getValue()-f2.getValue()<0) return 1;
        else if(f1.getValue()-f2.getValue()>0) return -1;
        return 0;
    }
}
