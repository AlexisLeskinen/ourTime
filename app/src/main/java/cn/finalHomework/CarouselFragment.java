package cn.finalHomework;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import cn.finalHomework.data.Event;

import static cn.finalHomework.HomeFragment.EVENTORDINAL;
import static cn.finalHomework.MainActivity.BUNDLEMARK;
import static cn.finalHomework.MainActivity.EVENTMARK;
import static cn.finalHomework.model.EventsAdapter.turnToEdit;


/**
 * A simple {@link Fragment} subclass.
 */
public class CarouselFragment extends Fragment {
    private Event event;
    private int eventOrder;
    private Context mContext;
    private int requestCode;
    private boolean isClickable;
    static private SimpleDateFormat sdf =
            new SimpleDateFormat(" H小时 m分钟 s秒", Locale.getDefault());

    private Handler handler;

    public CarouselFragment(Context context, Event event, int eventOrder, int requestCode,
                            boolean isClickable) {
        this.mContext = context;
        this.event = event;
        this.eventOrder = eventOrder;
        this.requestCode = requestCode;
        this.isClickable = isClickable;
        //创建属于主线程的handler
        handler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carousel, container, false);


        ImageView img = view.findViewById(R.id.carousel_img);
        final TextView title = view.findViewById(R.id.carousel_img_title),
                date = view.findViewById(R.id.carousel_img_date),
                timeDown = view.findViewById(R.id.carousel_img_timedown);

        img.setImageBitmap(event.getEventBitmap(mContext));
        title.setText(event.getTitle());
        date.setText(event.dateToString());

        //获取现在和时间的时间
        final Calendar interval = Calendar.getInstance(),
                eventDate = Calendar.getInstance();
        eventDate.setTime(event.getEventDate());

        //计算时间差，获取新的Calender对象
        long intervalTime = Math.abs(interval.getTimeInMillis() - eventDate.getTimeInMillis());
        interval.setTime(new Date(intervalTime));
        //把时间转换成标准时区
        interval.add(Calendar.HOUR_OF_DAY, -8);


        // 构建Runnable对象，在runnable中更新界面
        final Runnable updateCountDown = new Runnable() {
            @Override
            public void run() {
                String dayString = Math.abs(interval.getTimeInMillis()) / (1000 * 3600 * 24) + " 天";
                timeDown.setText(dayString + sdf.format(interval.getTime()));
            }
        };
        //为倒计时独立建立一个线程
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Calendar now = Calendar.getInstance();
                int addSecond = now.after(eventDate) ? 1 : -1;
                interval.add(Calendar.SECOND, addSecond);
                handler.post(updateCountDown);
            }
        }, 0, 1000);

        if (isClickable)
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    turnToEdit(mContext, event, eventOrder, requestCode);
                }
            });


        return view;
    }
}
