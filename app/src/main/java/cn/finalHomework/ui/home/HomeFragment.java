package cn.finalHomework.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import cn.finalHomework.EditEventActivity;
import cn.finalHomework.R;
import cn.finalHomework.data.Event;
import cn.finalHomework.model.DataSource;
import cn.finalHomework.model.EventsAdapter;

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

        Bundle bundle = getArguments();
        if (bundle != null) {
            Event event = (Event) bundle.getSerializable(EVENTMARK);
            if (event != null) {
                Toast.makeText(getContext(), event.getRemarks(), Toast.LENGTH_SHORT).show();
                eventList.add(event);
            }
        }

        ListView eventListView = root.findViewById(R.id.events_list);
        if (getContext() != null) {
            EventsAdapter eventsAdapter = new EventsAdapter(getContext(), R.layout.home_events_list, eventList);
            eventListView.setAdapter(eventsAdapter);
        }


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
    public void onDestroy() {
        super.onDestroy();

        //保存数据
        appDAta.saveEvents(eventList);
    }
}