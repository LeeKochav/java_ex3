package algorithms;

import dataStructure.node_data;

import java.util.Comparator;

public class VER_COMP implements Comparator<node_data> {
    public VER_COMP(){;}

    @Override
    public int compare(node_data v2,node_data v1)
        {
            int res=0;
            if(v1.getWeight()-v2.getWeight()<0) res=1;
               else if (v1.getWeight()-v2.getWeight()>0) res = -1;
            return res;
        }
}
