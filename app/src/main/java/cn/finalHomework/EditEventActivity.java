package cn.finalHomework;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

import cn.finalHomework.data.Event;
import cn.finalHomework.data.EventLabel;

import static cn.finalHomework.MainActivity.BUNDLEMARK;
import static cn.finalHomework.MainActivity.EVENTMARK;

public class EditEventActivity extends AppCompatActivity {
    private static final int requestCode = 1001;
    final static private String WEEK = "每周";
    final static private String MONTH = "每月";
    final static private String YEAR = "每年";
    final static private String CUSTOM = "自定义";
    final static private String NONE = "无";

    final static private String[] cycle = new String[]{WEEK, MONTH, YEAR, CUSTOM, NONE};

    private EditText title, remark;
    private TextView dateDetail, loopDetail;
    private LinearLayout date, loop, photo, tag;
    private Toolbar toolbar;
    private Event event;
    private EventLabel labels;
    Color theme;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //保存图片的uri
            if (data.getData() != null)
                event.setImageUri(data.getData().toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        //设置状态栏颜色
        addStatusViewWithColor(this);

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

        //点击设置日期事件
        date.setOnClickListener(new setDate());
        //点击设置循环参数
        loop.setOnClickListener(new setLoop());
        //点击设置背景图
        photo.setOnClickListener(new setPhoto());
        //点击设置标签
        tag.setOnClickListener(new setLabels());

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

        if (event != null) {
            showEvent();
        }
    }

    //设置状态栏颜色

    /**
     * 添加状态栏占位视图
     *
     * @param activity
     */
    private void addStatusViewWithColor(Activity activity) {
        ViewGroup contentView = activity.findViewById(android.R.id.content);
        View statusBarView = new View(activity);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(activity));
        statusBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
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

            Intent toHome = new Intent(EditEventActivity.this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(EVENTMARK, event);
            toHome.putExtra(BUNDLEMARK, bundle);
            setResult(RESULT_OK, toHome);
            EditEventActivity.this.finish();
            return true;
        }
    }

    //展示事件信息
    protected void showEvent() {
        title.setText(event.getTitle());
        remark.setText(event.getRemarks());
        dateDetail.setText(event.dateToString());
//        loopDetail.setText();
    }

    //点击设置时间
    private class setDate implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            final Calendar setter = Calendar.getInstance();
            //如果event非空，那么应该是修改时间
            if (event != null) {
                setter.setTime(event.getEventDate());
            }
            //设置日期
            DatePickerDialog datePicker = new DatePickerDialog(EditEventActivity.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            final int Y = year, M = monthOfYear, D = dayOfMonth;
                            //设置时分
                            TimePickerDialog timePicker = new TimePickerDialog(EditEventActivity.this,
                                    new TimePickerDialog.OnTimeSetListener() {
                                        @Override
                                        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                                            event.setDate(Y, M, D, hour, minute);
                                            dateDetail.setText(event.dateToString());
                                        }
                                    },
                                    setter.get(Calendar.HOUR),
                                    setter.get(Calendar.MINUTE),
                                    true);
                            timePicker.show();
                        }
                    },
                    setter.get(Calendar.YEAR),
                    setter.get(Calendar.MONTH),
                    setter.get(Calendar.DAY_OF_MONTH));
            datePicker.show();
        }
    }

    //点击设置循环参数
    private class setLoop implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder loopSelectDialog =
                    new AlertDialog.Builder(EditEventActivity.this);
            loopSelectDialog.setTitle("周期");
            loopSelectDialog.setItems(cycle, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //非自定义周期
                    if (!cycle[i].equals(CUSTOM)) {
                        event.setLoop(cycle[i]);
                        loopDetail.setText(event.getLoop());
                    } else {
                        AlertDialog.Builder customDialog =
                                new AlertDialog.Builder(EditEventActivity.this);
                        customDialog.setTitle("周期");
                        //输入框设置
                        final EditText editText = new EditText(EditEventActivity.this);
                        editText.setHint("输入周期(天)");
                        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                        editText.setLeftTopRightBottom(100, 0, 100, 0);
                        customDialog.setView(editText);
                        //取消输入
                        customDialog.setNegativeButton(R.string.back, null);
                        //确认
                        customDialog.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String inputDay = editText.getText().toString();
                                if (!inputDay.equals("")) {
                                    event.setLoop(inputDay + "天");
                                } else
                                    event.setLoop(NONE);
                                loopDetail.setText(event.getLoop());
                            }
                        });
                        customDialog.show();
                    }
                }
            });
            loopSelectDialog.show();
        }
    }

    //点击设置背景图
    private class setPhoto implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            //调用系统的图片选择器获取图片路径
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, requestCode);
        }
    }

    //点击设置标签
    private class setLabels implements View.OnClickListener {
        @Override
        public void onClick(View view) {

        }
    }
}
