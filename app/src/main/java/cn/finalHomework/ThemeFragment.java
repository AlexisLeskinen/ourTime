package cn.finalHomework;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rtugeek.android.colorseekbar.ColorSeekBar;

import cn.finalHomework.R;

import static android.content.Context.MODE_PRIVATE;

public class ThemeFragment extends Fragment {
    private SharedPreferences sp;
    private SharedPreferences.Editor spe;
    private ColorSeekBar mColorSeekBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_theme, container, false);

        sp = getActivity().getPreferences(MODE_PRIVATE);
        spe = sp.edit();

        mColorSeekBar = root.findViewById(R.id.colorSlider);
        final TextView test = root.findViewById(R.id.color_test);

        mColorSeekBar.setMaxPosition(100);
        mColorSeekBar.setThumbHeight(30);
        mColorSeekBar.setDisabledColor(Color.GRAY);
        mColorSeekBar.setBarHeight(10);                 //颜色条高度
        mColorSeekBar.setBarRadius(20);                 //颜色条圆角

        mColorSeekBar.setOnColorChangeListener(new ColorSeekBar.OnColorChangeListener() {
            @Override
            public void onColorChangeListener(int colorBarPosition, int alphaBarPosition, int color) {

                test.setBackgroundColor(mColorSeekBar.getColor());
            }
        });
        mColorSeekBar.setColor(sp.getInt("color", 0));

        FloatingActionButton fab = root.findViewById(R.id.confirm_color);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "主题设置成功！", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    @Override
    public void onStop() {
        super.onStop();
        spe.putInt("color", mColorSeekBar.getColor());
        spe.commit();
    }
}