package cn.finalHomework;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

import cn.finalHomework.data.Event;
import cn.finalHomework.model.CarouselAdapter;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;
import static cn.finalHomework.EditEventActivity.addStatusViewWithColor;
import static cn.finalHomework.HomeFragment.EVENTORDINAL;
import static cn.finalHomework.MainActivity.BUNDLEMARK;
import static cn.finalHomework.MainActivity.DELETEMARK;
import static cn.finalHomework.MainActivity.EVENTMARK;
import static cn.finalHomework.MainActivity.getThemeColor;
import static cn.finalHomework.ThemeFragment.themeColor;


public class EventDetailActivity extends AppCompatActivity {
    private static final int requestCode = 2001;

    private Bundle bundle;
    private int eventOrder;
    private Event event;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            bundle = data.getBundleExtra(BUNDLEMARK);
            event = (Event) bundle.getSerializable(EVENTMARK);
            eventOrder = bundle.getInt(EVENTORDINAL);

            setHeader();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "Tips";
            String channelName = "日期提醒";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(channelId, channelName, importance);

        }

        //主题颜色设置
        int bgColor = getThemeColor(getSharedPreferences(themeColor, MODE_PRIVATE));
        addStatusViewWithColor(this, bgColor);

        //获取传入该页面的事件参数
        bundle = getIntent().getBundleExtra(BUNDLEMARK);
        if (bundle != null) {
            event = (Event) bundle.getSerializable(EVENTMARK);
            eventOrder = bundle.getInt(EVENTORDINAL, -1);
        }

        Toolbar toolbar = findViewById(R.id.time_detail_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(null);
        //返回键
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toHome = new Intent(EventDetailActivity.this, MainActivity.class);
                bundle = new Bundle();
                bundle.putSerializable(EVENTMARK, event);
                if (eventOrder != -1)
                    bundle.putInt(EVENTORDINAL, eventOrder);
                toHome.putExtra(BUNDLEMARK, bundle);
                setResult(RESULT_OK, toHome);
                EventDetailActivity.this.finish();
            }
        });
        toolbar.setBackgroundColor(bgColor);

        //事件详情页面的倒计时，采用和home一样的方法实现
        setHeader();

        Switch notification = findViewById(R.id.switch_notices);
        notification.setChecked(event.getNotificationStatus());
        notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sendTipsMsg(buttonView);
                    event.setNotificationStatus(isChecked);
                } else {
                    event.setNotificationStatus(isChecked);
                    cancelNotification();
                }
            }
        });

    }

    //在工具栏顶部添编辑图标，不知道为啥在xml里面绑定menu不生效
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event_detail, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int menuId = item.getItemId();
        switch (menuId) {
            //删除
            case R.id.icon_delete: {
                AlertDialog.Builder deleteDialog =
                        new AlertDialog.Builder(EventDetailActivity.this);
                deleteDialog.setMessage(R.string.delete_event);
                //确定按钮
                deleteDialog.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent toHome = new Intent(EventDetailActivity.this, MainActivity.class);
                        bundle = new Bundle();
                        bundle.putSerializable(DELETEMARK, eventOrder);
                        toHome.putExtra(BUNDLEMARK, bundle);
                        setResult(RESULT_OK, toHome);
                        EventDetailActivity.this.finish();
                    }
                });
                //取消
                deleteDialog.setNegativeButton(R.string.back, null);
                deleteDialog.show();
                break;
            }
            //修改对象
            case R.id.icon_edit: {
                Intent toDetail = new Intent(this, EditEventActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(EVENTMARK, event);
                //因为是修改对象，所以得把事件的序号传过去
                bundle.putInt(EVENTORDINAL, eventOrder);
                toDetail.putExtra(BUNDLEMARK, bundle);
                startActivityForResult(toDetail, requestCode);
                break;
            }
            //分享
            case R.id.icon_share: {
                Toast.makeText(getApplicationContext(), "分享", Toast.LENGTH_SHORT).show();
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    //状态栏通知权限版本检测
    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }

    //发送通知
    private void sendTipsMsg(View view) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(this, "Tips")
                .setContentTitle(event.getTitle())
                .setContentText(event.dateToString())
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(event.getEventBitmap(this))
                .build();
        //设置常驻通知栏
        notification.flags = Notification.FLAG_ONGOING_EVENT;

        manager.notify(eventOrder, notification);
    }

    // 取消通知
    private void cancelNotification() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(eventOrder);
    }

    /**
     * 设置倒计时图片
     */
    private void setHeader() {
        ViewPager detailHeader = this.findViewById(R.id.detail_header);
        CarouselAdapter detailBannerAdapter = new CarouselAdapter(getSupportFragmentManager(),
                BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        ArrayList<Fragment> carouselList = new ArrayList<>();
        carouselList.add(new CarouselFragment(this, event, eventOrder, requestCode, false));
        detailBannerAdapter.setFragmentList(carouselList);
        detailHeader.setAdapter(detailBannerAdapter);
    }
}
