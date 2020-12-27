package com.charm.user;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

@RequiresApi(api = Build.VERSION_CODES.M)
@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private PrefManager prefManager;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.setLocale(MainActivity.this, "iw");
        setContentView(R.layout.activity_main);


        prefManager = new PrefManager(MainActivity.this);

        WebView webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);

        String url = Constants.BASE_URL_WEB_VIEW + "?app=Charm&upg=open&lt=" + prefManager.getString(PrefManager.KEY_LOGIN_TOKEN, "");

        Utility.printLog(TAG,"url main:"+url);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Utility.printLog(TAG, "url:" + url);
                if (url.contains("pg=blank")){
                    prefManager.setString(PrefManager.KEY_LOGIN_TOKEN,"");
                    Utility.startActivity(MainActivity.this,LoginActivity.class,true);
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });

    }

}