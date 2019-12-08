package cn.finalHomework;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
        }
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

        //事件详情页面的倒计时，采用和home一样的方法实现
        ViewPager detailHeader = this.findViewById(R.id.detail_header);
        CarouselAdapter detailBannerAdapter = new CarouselAdapter(getSupportFragmentManager(),
                BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        ArrayList<Fragment> carouselList = new ArrayList<>();
        carouselList.add(new CarouselFragment(this, event, eventOrder, requestCode, false));
        detailBannerAdapter.setFragmentList(carouselList);
        detailHeader.setAdapter(detailBannerAdapter);

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
            case R.id.icon_share: {
                Toast.makeText(getApplicationContext(), "分享", Toast.LENGTH_SHORT).show();
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

}
