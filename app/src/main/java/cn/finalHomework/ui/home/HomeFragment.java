package cn.finalHomework.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import cn.finalHomework.EditEventActivity;
import cn.finalHomework.R;
import cn.finalHomework.model.DataSource;

public class HomeFragment extends Fragment {
    private static final int requestCode = 2000;
    private DataSource appDAta;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        appDAta = new DataSource(getContext());
        //读取保存的数据
        appDAta.loadData();

        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toAddEvent = new Intent(getContext(), EditEventActivity.class);
                startActivityForResult(toAddEvent,requestCode);
            }
        });

        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //保存数据
        appDAta.saveData();
    }
}