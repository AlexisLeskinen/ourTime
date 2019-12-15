package cn.finalHomework;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static cn.finalHomework.EditEventActivity.addStatusViewWithColor;
import static cn.finalHomework.ThemeFragment.backgroundColor;
import static cn.finalHomework.ThemeFragment.themeColor;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 77722;
    public static String BUNDLEMARK = "BUNDLE";
    public static String EVENTMARK = "EVENT";
    public static String DELETEMARK = "DELETE";

    private AppBarConfiguration mAppBarConfiguration;
    private NavController navController;
    //向Fragment传参用
    private Bundle bundle;
    int bgColor;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //回传参数检查
        if (data != null) {
            bundle = data.getBundleExtra(BUNDLEMARK);
            if (bundle != null) {
                navController.setGraph(R.navigation.mobile_navigation, bundle);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //加载主题颜色
        SharedPreferences sp = getSharedPreferences(themeColor, MODE_PRIVATE);
        bgColor = getThemeColor(sp);

        setContentView(R.layout.activity_main);

        addStatusViewWithColor(this, bgColor);
        //顶部工具栏
        Toolbar toolbar = findViewById(R.id.main_top_bar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(bgColor);
        //左侧抽屉
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.getHeaderView(0).setBackgroundColor(bgColor);

        //将每个菜单ID作为一组ID传递，因为每个菜单都应该被视为顶级目的地。
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_theme, R.id.nav_settings, R.id.nav_about)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //请求读取外部存储的权限
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    //获取动态主题颜色
    public static int getThemeColor(SharedPreferences sp) {
        int bgColor = 0xFF03A9F4;
        if (sp != null) {
            bgColor = sp.getInt(backgroundColor, bgColor);
        }
        return bgColor;
    }
}
