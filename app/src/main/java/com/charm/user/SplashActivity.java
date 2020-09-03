package com.charm.user;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.widget.ProgressBar;

import com.charm.user.retrofit.ApiCall;

import static com.charm.user.PrefManager.KEY_LOGIN_TOKEN;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";

    private PrefManager prefManager;
    private String loginToken;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        prefManager = new PrefManager(SplashActivity.this);

        loginToken = prefManager.getString(PrefManager.KEY_LOGIN_TOKEN, "");
        Utility.printLog(TAG, "loginToken: " + loginToken);

        if (Utility.isConnectedToInternet(SplashActivity.this)) {
            Utility.progressBarDialogShow(SplashActivity.this);
            ApiCall.checkUser(SplashActivity.this, loginToken);
        } else {
            Utility.showDialog(SplashActivity.this,Constants.KEY_ALERT,Constants.NO_INTERNET_CONNECTION);
        }

    }
}
