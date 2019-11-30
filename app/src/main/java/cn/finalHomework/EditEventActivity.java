package cn.finalHomework;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.finalHomework.data.Event;
import cn.finalHomework.data.EventLabel;

public class EditEventActivity extends AppCompatActivity {

    private EditText title, remark;
    private TextView dateDetail, loopDetail;
    private LinearLayout date, loop, photo, tag;
    private Toolbar toolbar;
    private Event event;
    private EventLabel labels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        //设置状态栏颜色
        addStatusViewWithColor(this, 0xFF03A9F4);

        //获取传入该页面的事件参数
//        Intent intent = getIntent();
//        event = (Event) intent.getSerializableExtra("event");

        event = new Event();
        event.setDate(new Date());
        event.setTitle("安卓期末作业");
        event.setRemarks("赶紧做完吧！");

        //初始化控件
        title = findViewById(R.id.title_edit);
        remark = findViewById(R.id.remark_edit);

        dateDetail = findViewById(R.id.date_detail);
        loopDetail = findViewById(R.id.loop_detail);

        date = findViewById(R.id.date);
        loop = findViewById(R.id.loop);
        photo = findViewById(R.id.photo);
        tag = findViewById(R.id.tag);

        date.setOnClickListener(new setDate());

        //设置工具栏选项
        toolbar = this.findViewById(R.id.edit_time_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(null);
        //返回键
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditEventActivity.this.finish();
            }
        });
        //确认键
        toolbar.setOnMenuItemClickListener(new ConfirmListener());

        if(event != null){
            showEvent();
        }
    }

    //设置状态栏颜色

    /**
     * 添加状态栏占位视图
     *
     * @param activity
     */
    private void addStatusViewWithColor(Activity activity, int color) {
        ViewGroup contentView = activity.findViewById(android.R.id.content);
        View statusBarView = new View(activity);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(activity));
        statusBarView.setBackgroundColor(color);
        contentView.addView(statusBarView, lp);
    }

    /**
     * 利用反射获取状态栏高度
     *
     * @return
     */
    public int getStatusBarHeight(Activity activity) {
        int result = 0;
        //获取状态栏高度的资源id
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    //在工具栏顶部添加确认图标，不知道为啥在xml里面绑定menu不生效
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_event, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //点击确认图标，保存编辑数据
    private class ConfirmListener implements Toolbar.OnMenuItemClickListener {
        @Override
        public boolean onMenuItemClick(MenuItem item) {

            EditEventActivity.this.finish();
            return true;
        }
    }

    //展示事件信息
    protected void showEvent(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.getDefault());
        dateDetail.setText(sdf.format(event.getEventDate()));

//        loopDetail.setText();
    }

    //点击设置时间
    private class setDate implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Calendar setter = Calendar.getInstance();
            //如果event非空，那么应该是修改时间
            if (event != null) {
                setter.setTime(event.getEventDate());
            }

            //设置日期
            DatePickerDialog datePicker = new DatePickerDialog(EditEventActivity.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {


//                            TimePickerDialog timePicker = new TimePickerDialog(EditEventActivity.this,
//                                    new TimePickerDialog.OnTimeSetListener() {
//                                        @Override
//                                        public void onTimeSet(TimePicker timePicker, int i, int i1) {
//
//                                        }
//                                    },setter.);
                        }
                    },
                    setter.get(Calendar.YEAR),
                    setter.get(Calendar.MONTH),
                    setter.get(Calendar.DAY_OF_MONTH));
            datePicker.show();
        }
    }
}
