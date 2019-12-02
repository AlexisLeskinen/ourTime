package cn.finalHomework.data;

import java.io.Serializable;
import java.util.ArrayList;


public class EventLabel extends ArrayList<String> implements Serializable {
    private static final long serialVersionUID = -683086117245353610L;

    public EventLabel() {
        add("生日");
        add("工作");
        add("节假日");
        add("学习");
    }
}
