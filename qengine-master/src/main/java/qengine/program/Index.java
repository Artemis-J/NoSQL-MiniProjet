package qengine.program;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Index {

    Map<Integer, Map<Integer, ArrayList<Integer>>> index = new HashMap<>();

    public Map<Integer, Map<Integer, ArrayList<Integer>>> getIndex() {
        return index;
    }

    //exemple index SPO = one s, two p, three o
    public void fill(Integer one, Integer two, Integer three){
        if(index.containsKey(one)){
            if(index.get(one).containsKey(two)){
                index.get(one).get(two).add(three);
            }else {
                ArrayList<Integer> third = new ArrayList<>();
                third.add(three);
                index.get(one).put(two,third);
            }
        }else{
            ArrayList<Integer> third = new ArrayList<>();
            third.add(three);
            Map<Integer,ArrayList<Integer>> second = new HashMap<>();
            second.put(two,third);
            index.put(one,second);
        }
    }



}


