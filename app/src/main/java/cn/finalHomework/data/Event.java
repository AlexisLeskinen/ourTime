package cn.finalHomework.data;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.annotation.NonNull;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.finalHomework.R;

import static cn.finalHomework.EditEventActivity.MONTH;
import static cn.finalHomework.EditEventActivity.NONE;
import static cn.finalHomework.EditEventActivity.WEEK;
import static cn.finalHomework.EditEventActivity.YEAR;

public class Event implements Serializable {
    private static final long serialVersionUID = -2834052242012114329L;

    private Date eventDate;             //时间信息
    private String imageUri;            //背景图
    private String title;               //标题
    private String remarks;             //备注
    private ArrayList<String> labels;    //标签
    private String loop;                //循环设置

    static private SimpleDateFormat sdf =
            new SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.getDefault());

    public Event() {
        eventDate = null;
        imageUri = null;
        title = null;
        remarks = null;
        labels = null;
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

    public void addLabel(String label) {
        if (labels == null)
            labels = new ArrayList<>();

        if (!labels.contains(label))
            labels.add(label);
    }

    public void deleteLabel(String label) {
        if (labels != null && labels.contains(label)) {
            labels.remove(label);
            //如果标签列表是空的，将对象置null
            if (labels.isEmpty())
                labels = null;
        }
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
        return labels;
    }

    public String getLoop() {
        return loop;
    }

    public void nextLoop() {
        if (loop == null || loop.equals(NONE))
            return;

        Calendar now = Calendar.getInstance(),
                newDate = Calendar.getInstance();
        newDate.setTime(eventDate);
        if (now.after(newDate)) {
            switch (loop) {
                case WEEK: {
                    newDate.add(Calendar.DAY_OF_YEAR, 7);
                    break;
                }
                case MONTH: {
                    newDate.add(Calendar.MONTH, 1);
                    break;
                }
                case YEAR: {
                    newDate.add(Calendar.YEAR, 1);
                    break;
                }
                default: {
                    String loopTime = loop.replace("天", "");
                    newDate.add(Calendar.DAY_OF_YEAR, Integer.parseInt(loopTime));
                    break;
                }
            }
        }

        eventDate = newDate.getTime();

        if (now.after(newDate))
            nextLoop();
    }

    //返回格式化的时间信息
    public String dateToString() {
        return sdf.format(getEventDate());
    }

    //根据图片的Uri返回Bitmap
    public Bitmap getEventBitmap(@NonNull Context context) {

        Bitmap bgImg = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.defeault_bitmap);
        if (getImageUri() != null) {
            try {
                //记得给外部存储读取权限
                bgImg = BitmapFactory.decodeStream(context.getContentResolver().
                        openInputStream(Uri.parse(getImageUri())));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return bgImg;
    }
}
