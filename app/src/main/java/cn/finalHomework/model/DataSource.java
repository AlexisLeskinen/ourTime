package cn.finalHomework.model;

import android.content.Context;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import cn.finalHomework.data.Event;
import cn.finalHomework.data.EventLabel;

public class DataSource {
    private final static String eventsFile = "EventData";
    private final static String labelsFile = "LabelsData";
    private Context context;
    private EventLabel labels;              //保存标签
    private ArrayList<Event> events;        //保存事件

    public DataSource(Context context) {
        this.context = context;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public EventLabel getLabels() {
        return labels;
    }

    public void saveEvents(ArrayList<Event> events) {
        saveObject(events, eventsFile);
    }

    public void saveLabels(EventLabel labels) {
        saveObject(labels, labelsFile);
    }

    //保存对象
    private void saveObject(Object object, String fileName) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    context.openFileOutput(fileName, Context.MODE_PRIVATE)
            );
            outputStream.writeObject(object);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //去除warning
    @SuppressWarnings("unchecked")
    public void loadEvents() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(
                    context.openFileInput(eventsFile)
            );
            events = (ArrayList<Event>) inputStream.readObject();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadLabels() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(
                    context.openFileInput(labelsFile)
            );
            labels = (EventLabel) inputStream.readObject();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
