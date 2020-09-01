package com.charm.user;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.widget.RelativeLayout;

public class HomeActivity extends AppCompatActivity implements android.view.View.OnClickListener {

    private RelativeLayout type_employee, type_owner;
    private android.widget.TextView txt_time_of_day;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        loadLocale();

        txt_time_of_day = findViewById(R.id.txt_time_of_day);
        txt_time_of_day.setOnClickListener(this);

        type_employee = findViewById(R.id.type_employee);
        type_owner = findViewById(R.id.type_owner);
        type_employee.setOnClickListener(this);
        type_owner.setOnClickListener(this);
    }

    @Override
    public void onClick(android.view.View v) {

        switch (v.getId()) {
            case R.id.type_employee:
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                break;
            case R.id.type_owner:
                startActivity(new Intent(HomeActivity.this, OwnerRegisterActivity.class));
                break;
            case R.id.txt_time_of_day:
                languageChange();
                break;
        }
    }

    private void languageChange() {
        String[] languageList = {"English", "Hebrew"};
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setTitle("Choose Language");
        builder.setSingleChoiceItems(languageList, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (which == 0) {
                    setLocale("en");
                    recreate();
                } else if (which == 1) {
                    setLocale("iw");
                    recreate();
                }

                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void setLocale(String language) {
        java.util.Locale locale = new java.util.Locale(language);
        java.util.Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("select_language", language);
        editor.apply();

    }

    public void loadLocale() {
        SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);
        String language = preferences.getString("select_language", "");
        setLocale(language);
    }

}
