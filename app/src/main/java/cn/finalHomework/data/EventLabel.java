package cn.finalHomework.data;

import java.io.Serializable;
import java.util.ArrayList;

public class EventLabel implements Serializable {
    private static final long serialVersionUID = -683086117245353610L;

    private ArrayList<String> labels;    //标签

    EventLabel(){
        labels.add("生日");
        labels.add("学习");
        labels.add("工作");
        labels.add("节假日");
    }

    public void addLabel(String label){
        labels.add(label);
    }

    public void deleteLabel(String label){
        labels.remove(label);
    }

    public ArrayList<String> getLabels(){
        return labels;
    }
}
