package cn.finalHomework.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.finalHomework.R;
import cn.finalHomework.data.Event;

public class EventsAdapter extends ArrayAdapter<Event> {
    private Context mContext;
    private int i = 0;
    private List<Event> mData;

    public EventsAdapter(@NonNull Context context, int resource, @NonNull List<Event> objects) {
        super(context, resource, objects);
        mContext = context;
        mData=objects;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View item = layoutInflater.inflate(R.layout.home_events_list, parent, false);

        ImageView backgroundImg = item.findViewById(R.id.item_img);
        TextView eventStatus = item.findViewById(R.id.item_status);
        TextView eventDateImg = item.findViewById(R.id.item_img_date);
        TextView eventTitle = item.findViewById(R.id.item_title);
        TextView eventDate = item.findViewById(R.id.item_date);
        TextView eventRemarks = item.findViewById(R.id.item_remarks);

        Event eventItem = getItem(position);

        backgroundImg.setImageBitmap(eventItem.getEventBitmap(mContext));
        i++;
        eventTitle.setText(eventItem.getTitle());
        eventDate.setText(eventItem.dateToString());
        eventRemarks.setText(eventItem.getRemarks());

        //左侧图片提示信息
        Calendar now = Calendar.getInstance();
        long intervalTime;
        if (now.after(eventItem.getEventDate())) {
            eventStatus.setText(R.string.time_status_pass);
            intervalTime = now.getTime().getTime() - eventItem.getEventDate().getTime();
        } else {
            eventStatus.setText(R.string.time_status_not_yet);
            intervalTime = eventItem.getEventDate().getTime() - now.getTime().getTime();
        }
        //获得两者相差的Date对象
//        now.setTime(new Date(intervalTime));
//        eventDateImg.setText(now.get(Calendar.DAY_OF_YEAR) + "天");
        eventDateImg.setText(intervalTime / (1000 * 3600 * 24) + "天");

        return item;
    }

}

