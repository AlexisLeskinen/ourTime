package cn.finalHomework;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import cn.finalHomework.EditEventActivity;
import cn.finalHomework.R;
import cn.finalHomework.data.Event;
import cn.finalHomework.model.CarouselAdapter;
import cn.finalHomework.model.DataSource;
import cn.finalHomework.model.EventsAdapter;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;
import static cn.finalHomework.MainActivity.BUNDLEMARK;
import static cn.finalHomework.MainActivity.EVENTMARK;

public class HomeFragment extends Fragment {
    final public static String EVENTORDINAL = "ORDER";
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
        if (appDAta.getEvents() != null)
            eventList = appDAta.getEvents();
        else
            eventList = new ArrayList<>();

        //读取mainactivity传进来的参数
        Bundle bundle = getArguments();
        if (bundle != null) {
            Event event = (Event) bundle.getSerializable(EVENTMARK);
            int eventOrder = bundle.getInt(EVENTORDINAL);
            if (event != null) {
                if (eventList.get(eventOrder) != null)
                    eventList.set(eventOrder, event);

                else
                    //如果页面返回了新event对象，将他添加到eventList中
                    eventList.add(event);
            }
        }

        //顶部走马灯
        ViewPager carousel = root.findViewById(R.id.home_banner);
        CarouselAdapter homeBannerAdapter = new CarouselAdapter(getChildFragmentManager(),
                BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        ArrayList<Fragment> carouselList = new ArrayList<>();
        for (int e = 0; e < eventList.size(); e++) {
            carouselList.add(new CarouselFragment(getContext(), eventList.get(e), e, requestCode));
        }
        homeBannerAdapter.setFragmentList(carouselList);
        carousel.setAdapter(homeBannerAdapter);

        //event列表展示
        ListView eventListView = root.findViewById(R.id.events_list);
        if (getContext() != null) {
            EventsAdapter eventsAdapter = new EventsAdapter(getContext(),
                    R.layout.home_events_list, eventList, requestCode);
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