package cn.finalHomework.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 事件标签对象，用于维护新增标签
 * 删除标签功能暂未完善
 */
public class EventLabel extends ArrayList<String> implements Serializable {
    private static final long serialVersionUID = -683086117245353610L;

    public EventLabel() {
        add("生日");
        add("工作");
        add("节假日");
        add("学习");
    }
}
