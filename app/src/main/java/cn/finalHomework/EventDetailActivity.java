package cn.finalHomework;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import cn.finalHomework.data.Event;

import static cn.finalHomework.EditEventActivity.addStatusViewWithColor;
import static cn.finalHomework.HomeFragment.EVENTORDINAL;
import static cn.finalHomework.MainActivity.BUNDLEMARK;
import static cn.finalHomework.MainActivity.DELETEMARK;
import static cn.finalHomework.MainActivity.EVENTMARK;


public class EventDetailActivity extends AppCompatActivity {
    private static final int requestCode = 2001;

    private Bundle bundle;
    private int eventOrder;
    private Event event;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        addStatusViewWithColor(this);

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

//        toolbar.setOnMenuItemClickListener()
    }

    //在工具栏顶部添加确认图标，不知道为啥在xml里面绑定menu不生效
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event_detail, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int menuId = item.getItemId();
        switch (menuId){
            //删除
            case R.id.icon_delete:{
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
                deleteDialog.setNegativeButton(R.string.back,null);
                deleteDialog.show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    //toolbar的菜单选项事件
    private class DetailToolbarListener implements Toolbar.OnMenuItemClickListener{
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int menuId = item.getItemId();
            return false;
        }
    }
}
