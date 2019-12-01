package cn.finalHomework.data;


import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Event implements Serializable {
    private static final long serialVersionUID = -2834052242012114329L;

    private Date eventDate;             //时间信息
    private String imageUri;            //背景图
    private String title;               //标题
    private String remarks;             //备注
    private ArrayList<String> label;    //标签
    private String loop;                //循环设置

    static private SimpleDateFormat sdf =
            new SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.getDefault());;

    public Event() {
        eventDate = null;
        imageUri = null;
        title = null;
        remarks = null;
        label = null;
        loop = null;
    }

    public void setDate(int year, int mon, int day, int hour, int min) {
        try {
            eventDate = sdf.parse(year + "年" + mon + "月" + day + "日 " + hour + ":" + min);
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

    public void setImageUri(String imgUrl) {
        imageUri = imgUrl;
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

    public String getImageUri() {
        return imageUri;
    }

    public ArrayList<String> getLabel() {
        return label;
    }

    public String getLoop() {
        return loop;
    }

    //返回格式化的时间信息
    public String dateToString() {
        return sdf.format(getEventDate());
    }
}
