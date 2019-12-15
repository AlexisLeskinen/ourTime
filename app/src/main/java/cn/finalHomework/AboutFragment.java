package cn.finalHomework;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;



public class AboutFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_about, container, false);


        WebView webView = root.findViewById(R.id.web_view);

        //在当前视图打开
        webView.setWebViewClient(new WebViewClient());
        //一般允许JavaScript
        webView.getSettings().setJavaScriptEnabled(true);
        //加载URL
        webView.loadUrl("https://github.com/AlexisLeskinen/ourTime");

        return root;
    }
}