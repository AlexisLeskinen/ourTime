package cn.finalHomework;

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
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import cn.finalHomework.data.Event;
import cn.finalHomework.data.EventLabel;
import cn.finalHomework.model.DataSource;

import static cn.finalHomework.MainActivity.BUNDLEMARK;
import static cn.finalHomework.MainActivity.EVENTMARK;
import static cn.finalHomework.HomeFragment.EVENTORDINAL;


public class EditEventActivity extends AppCompatActivity {
    private static final int requestCode = 1001;
    //循环设置参数
    final static public String WEEK = "每周";
    final static public String MONTH = "每月";
    final static public String YEAR = "每年";
    final static public String CUSTOM = "自定义";
    final static public String NONE = "无";
    final static private String[] cycle = new String[]{WEEK, MONTH, YEAR, CUSTOM, NONE};

    //控件变量
    private EditText title, remark;
    private TextView dateDetail, loopDetail, labelsDetail;
    private LinearLayout date, loop, photo, tag;
    private ImageView headerImg;
    private Toolbar toolbar;

    private Event event;
    private int eventOrder;
    private DataSource labelData;
    private EventLabel labels;

    //复选框参数
    boolean[] hasSelect;
    String[] tempLabels;

    Bundle bundle;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //保存图片的uri
            if (data.getData() != null) {
                event.setImageUri(data.getData().toString());
                //获取到图片之后刷新header背景
                updateHeaderBG();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        //设置状态栏颜色
        addStatusViewWithColor(this);

        //获取标签数据
        labelData = new DataSource(this);
        labelData.loadLabels();
        if (labelData.getLabels() != null)
            labels = labelData.getLabels();
        else
            labels = new EventLabel();

        //获取传入该页面的事件参数
        bundle = getIntent().getBundleExtra(BUNDLEMARK);
        if (bundle != null) {
            event = (Event) bundle.getSerializable(EVENTMARK);
            eventOrder = bundle.getInt(EVENTORDINAL, -1);
        }
        if (event == null)
            event = new Event();

        //初始化控件
        headerImg = findViewById(R.id.header_background);
        title = findViewById(R.id.title_edit);
        remark = findViewById(R.id.remark_edit);

        dateDetail = findViewById(R.id.date_detail);
        loopDetail = findViewById(R.id.loop_detail);
        labelsDetail = findViewById(R.id.labels_detail);

        date = findViewById(R.id.date);
        loop = findViewById(R.id.loop);
        photo = findViewById(R.id.photo);
        tag = findViewById(R.id.labels);

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

        showEvent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //保存标签信息
        labelData.saveLabels(labels);
    }

    //设置状态栏颜色

    /**
     * 添加状态栏占位视图
     *
     * @param activity
     */
    public static void addStatusViewWithColor(Activity activity) {
        ViewGroup contentView = activity.findViewById(android.R.id.content);
        View statusBarView = new View(activity);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(activity));
        statusBarView.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorPrimary));
        contentView.addView(statusBarView, lp);
    }

    /**
     * 利用反射获取状态栏高度
     *
     * @return
     */
    public static int getStatusBarHeight(Activity activity) {
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
            event.setTitle(title.getText().toString());
            event.setRemarks(remark.getText().toString());
            if (event.getTitle().equals("")) {
                Toast.makeText(getApplicationContext(), "标题不能为空！", Toast.LENGTH_SHORT).show();
                return false;
            } else if (event.getEventDate() == null) {
                Toast.makeText(getApplicationContext(), "事件日期不能为空！", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                Intent toHome = new Intent(EditEventActivity.this, MainActivity.class),
                        toDetail = new Intent(EditEventActivity.this, EventDetailActivity.class);
                bundle = new Bundle();
                bundle.putSerializable(EVENTMARK, event);
                if (eventOrder != -1)
                    bundle.putInt(EVENTORDINAL, eventOrder);
                toHome.putExtra(BUNDLEMARK, bundle);
                toDetail.putExtra(BUNDLEMARK, bundle);
                setResult(RESULT_OK, toHome);
                setResult(RESULT_OK, toDetail);
                EditEventActivity.this.finish();
            }

            return true;
        }
    }

    //展示事件信息
    protected void showEvent() {
        if (event.getTitle() != null)
            title.setText(event.getTitle());
        if (event.getRemarks() != null)
            remark.setText(event.getRemarks());
        if (event.getEventDate() != null)
            dateDetail.setText(event.dateToString());
        if (event.getLoop() != null)
            loopDetail.setText(event.getLoop());
        updateHeaderBG();
    }

    //点击设置时间
    private class setDate implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            final Calendar setter = Calendar.getInstance();
            //如果event.date非空，那么应该是修改时间
            if (event.getEventDate() != null) {
                setter.setTime(event.getEventDate());
            }
            //设置日期
            DatePickerDialog datePicker = new DatePickerDialog(EditEventActivity.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            //获取的月数默认从0开始，要加1
                            final int Y = year, M = monthOfYear + 1, D = dayOfMonth;
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
            loopSelectDialog.setTitle(R.string.loop_dialog_title);
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
                        customDialog.setTitle(R.string.loop_dialog_title);
                        //输入框设置
                        final EditText editText = new EditText(EditEventActivity.this);
                        editText.setHint(R.string.loop_dialog_hint);
                        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                        customDialog.setView(editText);
                        //取消输入
                        customDialog.setNegativeButton(R.string.back, null);
                        //确认
                        customDialog.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String inputDay = editText.getText().toString();
                                if (!inputDay.isEmpty()) {
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
            AlertDialog.Builder labelsSelectDialog =
                    new AlertDialog.Builder(EditEventActivity.this);
            //复选框的参数设置真蛋疼。。。
            setMultiChoiceArg();

            //设置复选框内容
            labelsSelectDialog.setMultiChoiceItems(tempLabels, hasSelect,
                    //必须要设置OnMultiChoiceClickListener，不然选中复选框会没反应
                    new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                        }
                    });
            //取消
            labelsSelectDialog.setNegativeButton(R.string.back, null);
            //确定
            labelsSelectDialog.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //向event对象写入新的标签信息
                    for (int i = 0; i < hasSelect.length; i++) {
                        if (hasSelect[i])
                            event.addLabel(tempLabels[i]);
                        else
                            event.deleteLabel(tempLabels[i]);
                    }
                    updateLabelsDetail();
                }
            });
            //添加新标签
            labelsSelectDialog.setNeutralButton(R.string.labels_new, new LabelsAdd(labelsSelectDialog));
            labelsSelectDialog.show();
        }
    }

    //输入新标签
    class LabelsAdd implements DialogInterface.OnClickListener {
        //保存一开始的复选框对象，这样能够在添加完新标签后
        //再次自动调出复选框页面
        private AlertDialog.Builder labelsSelectDialog;

        LabelsAdd(AlertDialog.Builder labelsSelectDialog) {
            this.labelsSelectDialog = labelsSelectDialog;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            AlertDialog.Builder addLabelDialog =
                    new AlertDialog.Builder(EditEventActivity.this);
            addLabelDialog.setTitle(R.string.labels_new);
            //输入框设置
            final EditText editText = new EditText(EditEventActivity.this);
            editText.setHint(R.string.labels_add_hint);
            addLabelDialog.setView(editText);
            //取消输入
            addLabelDialog.setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    labelsSelectDialog.show();
                }
            });
            //确认
            addLabelDialog.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String inputLabel = editText.getText().toString();
                    inputLabel = inputLabel.trim();
                    if (!inputLabel.isEmpty()) {
                        labels.add(inputLabel);

                        //刷新临时标签列表
                        setMultiChoiceArg();
                        labelsSelectDialog.setMultiChoiceItems(tempLabels, hasSelect,
                                new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                                    }
                                });
                        labelsSelectDialog.show();
                    }
                }
            });
            addLabelDialog.show();
        }
    }

    private void setMultiChoiceArg() {
        //复选框的参数设置真蛋疼。。。
        //初始化化布尔数组全为false，长度为labels个
        hasSelect = new boolean[labels.size()];
        Arrays.fill(hasSelect, false);
        //初始化复选框的名称，放在一个新的String数组里面保存
        tempLabels = new String[labels.size()];
        tempLabels = labels.toArray(tempLabels);
        ArrayList<String> labelOfEvent = event.getLabel();
        //获得已经被选择标签的布尔数组
        if (labelOfEvent != null) {
            for (int i = 0; i < labelOfEvent.size(); i++) {
                if (labels.contains(labelOfEvent.get(i))) {
                    hasSelect[labels.indexOf(labelOfEvent.get(i))] = true;
                }
            }
        }
    }

    //更新背景图像
    private void updateHeaderBG() {
        if (event.getImageUri() != null) {
            //把背景改为黑色，调节透明度，再设置背景图片
//            headerImg.setBackgroundColor(ContextCompat.getColor(this,R.color.backgroundImg));
            headerImg.setAlpha(0.8f);
            headerImg.setImageBitmap(event.getEventBitmap(this));
        } else
            headerImg.setBackgroundResource(R.drawable.side_nav_bar);
    }

    //更新标签信息
    private void updateLabelsDetail() {
        if (event.getLabel() != null) {
            String[] tempLabels = new String[event.getLabel().size()];
            tempLabels = event.getLabel().toArray(tempLabels);
            StringBuffer detail = new StringBuffer();
            for (String l : tempLabels) {
                detail.append(l + " ");
            }
            labelsDetail.setText(detail.toString());
        }
    }
}
