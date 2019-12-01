package cn.finalHomework.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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


    public EventsAdapter(@NonNull Context context, int resource, @NonNull List<Event> objects) {
        super(context, resource, objects);
        mContext = context;
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

        Bitmap bgImg = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.defeault_bitmap);
        if (eventItem.getImageUri() != null) {
            try {
                bgImg = BitmapFactory.decodeStream(mContext.getContentResolver().
                        openInputStream(eventItem.getImageUri()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        backgroundImg.setImageBitmap(bgImg);
        eventTitle.setText(eventItem.getTitle());
        eventDate.setText(eventItem.dateToString());
        eventRemarks.setText(eventItem.getRemarks());

        //左侧图片提示信息
        Calendar now = Calendar.getInstance();
        long passTime;
        if (now.after(eventItem.getEventDate())) {
            eventStatus.setText("已经");
            passTime = now.getTime().getTime() - eventItem.getEventDate().getTime();
        } else {
            eventStatus.setText("只剩");
            passTime = eventItem.getEventDate().getTime() - now.getTime().getTime();
        }
        //获得两者相差的Date对象
        now.setTime(new Date(passTime));
        eventDateImg.setText(now.get(Calendar.DAY_OF_YEAR) + "天");

        return item;
    }
}

