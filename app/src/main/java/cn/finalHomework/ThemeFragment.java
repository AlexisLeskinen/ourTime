package cn.finalHomework;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.rtugeek.android.colorseekbar.ColorSeekBar;

import static android.content.Context.MODE_PRIVATE;
import static cn.finalHomework.EditEventActivity.addStatusViewWithColor;
import static cn.finalHomework.MainActivity.getThemeColor;

public class ThemeFragment extends Fragment {
    public final static String themeColor = "THEME";
    public final static String backgroundColor = "BACKGROUND";
    private SharedPreferences sp;
    private SharedPreferences.Editor spe;
    private ColorSeekBar mColorSeekBar;

    int bgColor;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_theme, container, false);

        //用全局SharedPreferences对象去存储主题颜色设置
        sp = getContext().getSharedPreferences(themeColor, MODE_PRIVATE);
        spe = sp.edit();
        bgColor = getThemeColor(sp);

        mColorSeekBar = root.findViewById(R.id.colorSlider);
        final Toolbar toolbar = getActivity().findViewById(R.id.main_top_bar);
        final NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        final TextView test = root.findViewById(R.id.color_test);

        mColorSeekBar.setMaxPosition(100);
        mColorSeekBar.setThumbHeight(30);
        mColorSeekBar.setDisabledColor(Color.GRAY);
        mColorSeekBar.setBarHeight(10);                 //颜色条高度
        mColorSeekBar.setBarRadius(20);                 //颜色条圆角


        final FloatingActionButton fab = root.findViewById(R.id.confirm_color);
        fab.setBackgroundTintList(ColorStateList.valueOf(bgColor));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //立刻更改sp对象存储的颜色
                spe.putInt(backgroundColor, mColorSeekBar.getColor());
                spe.apply();
                //保存颜色之后立即更改现在的颜色参数
                bgColor = getThemeColor(sp);
                Toast.makeText(getContext(), "主题设置成功！", Toast.LENGTH_SHORT).show();
                //然后关闭当前Fragment
                getActivity().onBackPressed();
            }
        });

        //拖动颜色条的时候实时改变布局颜色
        mColorSeekBar.setOnColorChangeListener(new ColorSeekBar.OnColorChangeListener() {
            @Override
            public void onColorChangeListener(int colorBarPosition, int alphaBarPosition, int color) {
                test.setBackgroundColor(color);
                fab.setBackgroundTintList(ColorStateList.valueOf(color));
                addStatusViewWithColor(getActivity(), color);
                toolbar.setBackgroundColor(color);
                navigationView.getHeaderView(0).setBackgroundColor(color);
            }
        });
        mColorSeekBar.setColor(bgColor);

        return root;
    }

    @Override
    public void onStop() {
        super.onStop();
        //如果没有点击确认修改，那么主题颜色恢复成之前设置的
        addStatusViewWithColor(getActivity(), bgColor);
        getActivity().findViewById(R.id.main_top_bar).setBackgroundColor(bgColor);
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.getHeaderView(0).setBackgroundColor(bgColor);

        spe.commit();
    }
}