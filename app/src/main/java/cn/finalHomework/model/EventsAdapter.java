package cn.finalHomework.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import cn.finalHomework.EventDetailActivity;
import cn.finalHomework.R;
import cn.finalHomework.data.Event;

import static cn.finalHomework.MainActivity.BUNDLEMARK;
import static cn.finalHomework.MainActivity.EVENTMARK;
import static cn.finalHomework.HomeFragment.EVENTORDINAL;

public class EventsAdapter extends ArrayAdapter<Event> {
    private Context mContext;
    private int requestCode;

    public EventsAdapter(@NonNull Context context, int resource,
                         @NonNull List<Event> objects, int requestCode) {
        super(context, resource, objects);
        mContext = context;
        this.requestCode = requestCode;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View itemView = layoutInflater.inflate(R.layout.home_events_list, parent, false);

        RelativeLayout wholeItem = itemView.findViewById(R.id.events_list_view);
        ImageView backgroundImg = itemView.findViewById(R.id.item_img);
        TextView eventStatus = itemView.findViewById(R.id.item_status);
        TextView eventDateImg = itemView.findViewById(R.id.item_img_date);
        TextView eventTitle = itemView.findViewById(R.id.item_title);
        TextView eventDate = itemView.findViewById(R.id.item_date);
        TextView eventRemarks = itemView.findViewById(R.id.item_remarks);

        final Event eventItem = getItem(position);

        //把图片模糊处理
        Bitmap fuzzyImg = eventItem.getEventBitmap(mContext);
        fuzzyImg = ImageFilter.blurBitmap(mContext, fuzzyImg, 10f);
        backgroundImg.setImageBitmap(fuzzyImg);
        eventTitle.setText(eventItem.getTitle());
        eventDate.setText(eventItem.dateToString());
        eventRemarks.setText(eventItem.getRemarks());

        //左侧图片提示信息
        Calendar now = Calendar.getInstance();

        if (now.getTime().after(eventItem.getEventDate())) {
            eventStatus.setText(R.string.time_status_pass);
        } else {
            eventStatus.setText(R.string.time_status_not_yet);
        }
        long intervalTime = Math.abs(now.getTimeInMillis() - eventItem.getEventDate().getTime());
        //获得两者相差的Date对象
        now.setTime(new Date(intervalTime));
        //把时间转换成标准时区
        now.setTimeZone(TimeZone.getTimeZone("GMT+0"));

        String imgTimeString = intervalTime / (1000 * 3600 * 24) + "天";
        if (intervalTime / (1000 * 3600 * 24) == 0) {
            imgTimeString = now.get(Calendar.HOUR_OF_DAY) + "小时";
            if (now.get(Calendar.HOUR_OF_DAY) == 0) {
                imgTimeString = now.get(Calendar.MINUTE) + "分钟";
                if (now.get(Calendar.MINUTE) == 0) {
                    imgTimeString = now.get(Calendar.SECOND) + "秒";
                }
            }
        }

        eventDateImg.setText(imgTimeString);

        //点击进入修改event
        wholeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnToEdit(mContext, eventItem, position, requestCode);
            }
        });

        return itemView;
    }

    public static void turnToEdit(Context context, Event e, int position, int requestCode) {
        Intent toDetail = new Intent(context, EventDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EVENTMARK, e);
        //因为是修改对象，所以得把事件的序号传过去
        bundle.putInt(EVENTORDINAL, position);
        toDetail.putExtra(BUNDLEMARK, bundle);
        ((Activity) context).startActivityForResult(toDetail, requestCode);
    }

}

