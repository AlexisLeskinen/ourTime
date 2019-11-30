package cn.finalHomework.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Event implements Serializable {
    private static final long serialVersionUID = -2834052242012114329L;

    private Date eventDate;             //时间信息
    private int imageId;                //背景图
    private String title;               //标题
    private String remarks;             //备注
    private ArrayList<String> label;    //标签
    private String loop;                //循环设置

    public Event() {
        eventDate = null;
        imageId = 0;
        title = null;
        remarks = null;
        label = null;
        loop = null;
    }

    public void setDate(Date date) {
        eventDate = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setImageId(int resourceId) {
        imageId = resourceId;
    }

    public void setLabel(ArrayList<String> label) {
        this.label = label;
    }

    public void setLoop(String loop) {
        this.loop = loop;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public String getTitle() {
        return title;
    }

    public String getRemarks() {
        return remarks;
    }

    public int getImageId() {
        return imageId;
    }

    public ArrayList<String> getLabel() {
        return label;
    }

    public String getLoop() {
        return loop;
    }
}
