package cn.finalHomework;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EditTimeActivity extends AppCompatActivity {

    private EditText title, remark;
    private TextView dateDetail, loopDetail;
    private LinearLayout date, loop, photo;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_time);

        //设置状态栏颜色
        addStatusViewWithColor(this, 0xFF03A9F4);

        //初始化控件
        title = findViewById(R.id.title_edit);
        remark = findViewById(R.id.remark_edit);
        dateDetail = findViewById(R.id.date_detail);
        loopDetail = findViewById(R.id.loop_detail);
        date = findViewById(R.id.date);
        loop = findViewById(R.id.loop);
        photo = findViewById(R.id.photo);

        //设置工具栏选项
        toolbar = this.findViewById(R.id.edit_time_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(null);
        //返回键
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditTimeActivity.this.finish();
            }
        });
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

    //在工具栏顶部添加确认图标
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_time_top_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
