package cn.finalHomework.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import cn.finalHomework.EditEventActivity;
import cn.finalHomework.R;
import cn.finalHomework.data.Event;
import cn.finalHomework.model.DataSource;
import cn.finalHomework.model.EventsAdapter;

import static cn.finalHomework.MainActivity.BUNDLEMARK;
import static cn.finalHomework.MainActivity.EVENTMARK;

public class HomeFragment extends Fragment {
    private static final int requestCode = 2000;
    //事件信息存储对象
    private DataSource appDAta;
    private ArrayList<Event> eventList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        appDAta = new DataSource(getContext());
        //读取保存的数据
        appDAta.loadEvents();
        if(appDAta.getEvents()!= null)
            eventList = appDAta.getEvents();
        else
            eventList = new ArrayList<>();

        //读取mainactivity传进来的参数
        Bundle bundle = getArguments();
        if (bundle != null) {
            Event event = (Event) bundle.getSerializable(EVENTMARK);
            //如果页面返回了新event对象，将他添加到eventList中
            if (event != null && !eventList.contains(event)) {
                eventList.add(event);
            }
        }

        //顶部轮播图


        ListView eventListView = root.findViewById(R.id.events_list);
        if (getContext() != null) {
            EventsAdapter eventsAdapter = new EventsAdapter(getContext(),
                    R.layout.home_events_list, eventList,requestCode);
            eventListView.setAdapter(eventsAdapter);
        }


        //添加event
        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toAddEvent = new Intent(getContext(), EditEventActivity.class);
                startActivityForResult(toAddEvent, requestCode);
            }
        });

        return root;
    }

    @Override
    public void onStop() {
        super.onStop();
        //保存数据
        appDAta.saveEvents(eventList);
    }
}