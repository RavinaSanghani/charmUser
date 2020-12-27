package com.charm.user;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.charm.user.retrofit.ApiCall;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;

@RequiresApi(api = Build.VERSION_CODES.M)
public class EmployeeRegisterActivity extends AppCompatActivity implements android.view.View.OnClickListener {

    private static final String TAG = "EmployeeRegisterActivit";
    private ImageView img_women, img_man, img_employee_profile, img_close;
    private Button btn_register;
    private TextView txt_icon;
    private EditText et_name, et_email, et_email_verification, et_mobile, et_nick_name, et_password, et_password_verification, et_saloon_code;
    private String str_select_icon = "", str_name, str_email, str_email_verification, str_mobile, str_nick_name, str_password, str_password_verification, str_saloon_code;
    private int[] img_women_list = {R.drawable.women_1, R.drawable.women_2, R.drawable.women_3, R.drawable.women_4, R.drawable.women_5};
    private int[] img_man_list = {R.drawable.man_1, R.drawable.man_2, R.drawable.man_3, R.drawable.man_4, R.drawable.man_5, R.drawable.man_6};
    private ProfileDialogLayout profileLayout;
    private boolean isMan = false,opened;
    private LinearLayout linearLayout;
    private TextInputLayout txt_password,txt_password_verification;

    private SoftInputAssist softInputAssist;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.setLocale(EmployeeRegisterActivity.this, "iw");
        setContentView(R.layout.activity_employee_register);
        softInputAssist=new SoftInputAssist(EmployeeRegisterActivity.this);
        init();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int[] sourceCoordinates = new int[2];
            v.getLocationOnScreen(sourceCoordinates);
            float x = ev.getRawX() + v.getLeft() - sourceCoordinates[0];
            float y = ev.getRawY() + v.getTop() - sourceCoordinates[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom()) {
                Utility.hideSoftKeyboard(EmployeeRegisterActivity.this);
            }

        }
        return super.dispatchTouchEvent(ev);
    }


    @Override
    protected void onResume() {
        softInputAssist.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        softInputAssist.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        softInputAssist.onDestroy();
        super.onDestroy();
    }

    private void init() {
        txt_icon = findViewById(R.id.txt_icon);
        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        et_email_verification = findViewById(R.id.et_email_verification);
        et_mobile = findViewById(R.id.et_mobile);
        et_nick_name = findViewById(R.id.et_nick_name);
        et_password = findViewById(R.id.et_password);
        et_password_verification = findViewById(R.id.et_password_verification);
        et_saloon_code = findViewById(R.id.et_saloon_code);
        txt_password = findViewById(R.id.txt_password);
        txt_password_verification = findViewById(R.id.txt_password_verification);


        btn_register = findViewById(R.id.btn_register);

        img_employee_profile = findViewById(R.id.img_employee_profile);
        img_close = findViewById(R.id.img_close);
        img_women = findViewById(R.id.img_women);
        img_man = findViewById(R.id.img_man);

        img_close.setOnClickListener(this);
        img_women.setOnClickListener(this);
        img_man.setOnClickListener(this);
        btn_register.setOnClickListener(this);

        linearLayout = findViewById(R.id.linearLayout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {

            txt_password.setTextDirection(View.TEXT_DIRECTION_RTL);
            txt_password_verification.setTextDirection(View.TEXT_DIRECTION_RTL);
            et_password.setTextDirection(View.TEXT_DIRECTION_RTL);
            et_password_verification.setTextDirection(View.TEXT_DIRECTION_RTL);

            et_password.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_PASSWORD |
                    InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

            et_password_verification.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_PASSWORD |
                    InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        }

    }

    public void clearData(){
        et_name.setText("");
        et_email.setText("");
        et_email_verification.setText("");
        et_mobile.setText("");
        et_nick_name.setText("");
        et_password.setText("");
        et_password_verification.setText("");
        et_saloon_code.setText("");


    }
    @Override
    public void onClick(android.view.View v) {
        switch (v.getId()) {
            case R.id.img_close:
                setVisibilityProfileHide();
                break;
            case R.id.img_man:
                isMan = true;
                img_women.setImageResource(R.drawable.disable_women);
                profileLayout = new ProfileDialogLayout(EmployeeRegisterActivity.this, img_man_list, img_employee_profile);
                profileLayout.show();
                profileLayout.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        img_women.setImageResource(R.drawable.enable_women);
                    }
                });
                profileLayout.getWindow().setWindowAnimations(R.style.DialogAnimation);
                break;
            case R.id.img_women:
                isMan = false;
                img_man.setImageResource(R.drawable.disable_male);
                profileLayout = new ProfileDialogLayout(EmployeeRegisterActivity.this, img_women_list, img_employee_profile);
                profileLayout.show();

                profileLayout.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        img_man.setImageResource(R.drawable.enable_male);
                    }
                });
                profileLayout.getWindow().setWindowAnimations(R.style.DialogAnimation);
                break;
            case R.id.btn_register:
                str_name = et_name.getText().toString();
                str_email = et_email.getText().toString();
                str_email_verification = et_email_verification.getText().toString();
                str_mobile = et_mobile.getText().toString();
                str_nick_name = et_nick_name.getText().toString();
                str_password = et_password.getText().toString();
                str_password_verification = et_password_verification.getText().toString();
                str_saloon_code = et_saloon_code.getText().toString();

                if (validation()) {
                    verificationCode(str_mobile);
                }

                break;
        }
    }


    public void resendVerificationCode() {
        btn_register.performClick();
    }

    public void setVisibilityProfileShow(int position) {
        img_man.setVisibility(android.view.View.GONE);
        img_women.setVisibility(android.view.View.GONE);
        txt_icon.setVisibility(android.view.View.GONE);
        img_employee_profile.setVisibility(android.view.View.VISIBLE);
        img_close.setVisibility(android.view.View.VISIBLE);

        profileLayout.dismiss();

        if (isMan) {
            str_select_icon = "man_" + (position + 1) + ".png";
        } else {
            str_select_icon = "Women_" + (position + 1) + ".png";
        }
    }

    private void setVisibilityProfileHide() {
        img_man.setVisibility(android.view.View.VISIBLE);
        img_women.setVisibility(android.view.View.VISIBLE);
        txt_icon.setVisibility(android.view.View.VISIBLE);
        img_employee_profile.setVisibility(android.view.View.GONE);
        img_close.setVisibility(android.view.View.GONE);
        img_man.setImageResource(R.drawable.enable_male);
        img_women.setImageResource(R.drawable.enable_women);
        str_select_icon = null;
        profileLayout.dismiss();
    }

    private boolean validation() {

        if (TextUtils.isEmpty(str_select_icon)) {
            Utility.showDialog(EmployeeRegisterActivity.this, getResources().getString(R.string.EMPTY_MSG), getResources().getString(R.string.select_image));
            return false;
        }
        if (TextUtils.isEmpty(str_name)) {
            ValidationDialog dialog = new ValidationDialog(EmployeeRegisterActivity.this, getResources().getString(R.string.EMPTY_MSG), getResources().getString(R.string.NAME_MSG), et_name);
            dialog.validationDialog();
            return false;
        }
       /* if (!str_name.matches("^[_A-Za-z\\s]+")) {
            ValidationDialog dialog = new ValidationDialog(EmployeeRegisterActivity.this, Constants.INVALID_MSG, Constants.name_valid_msg, et_name);
            dialog.validationDialog();
            return false;
        }*/
        if (TextUtils.isEmpty(str_email)) {
            ValidationDialog dialog = new ValidationDialog(EmployeeRegisterActivity.this, getResources().getString(R.string.EMPTY_MSG), getResources().getString(R.string.EMAIL_MSG), et_email);
            dialog.validationDialog();
            return false;
        }
        /*if (!str_email.matches("\\s")) {
            ValidationDialog dialog = new ValidationDialog(EmployeeRegisterActivity.this, Constants.EMPTY_MSG, Constants.email_msg,et_email);
            dialog.validationDialog();
            return false;
        }*/
        if (!str_email.matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
            ValidationDialog dialog = new ValidationDialog(EmployeeRegisterActivity.this, getResources().getString(R.string.INVALID_MSG), getResources().getString(R.string.EMAIL_VALID_MSG), et_email);
            dialog.validationDialog();
            return false;
        }
        if (TextUtils.isEmpty(str_email_verification)) {
            ValidationDialog dialog = new ValidationDialog(EmployeeRegisterActivity.this, getResources().getString(R.string.EMPTY_MSG), getResources().getString(R.string.EMAIL_VERIFICATION_MSG), et_email_verification);
            dialog.validationDialog();
            return false;
        }
        if (!str_email_verification.equals(str_email)) {
            ValidationDialog dialog = new ValidationDialog(EmployeeRegisterActivity.this, getResources().getString(R.string.MISMATCH_MSG), getResources().getString(R.string.EMAIL_MATCH_MSG), et_email_verification);
            dialog.validationDialog();
            return false;
        }
        if (TextUtils.isEmpty(str_mobile)) {
            ValidationDialog dialog = new ValidationDialog(EmployeeRegisterActivity.this, getResources().getString(R.string.EMPTY_MSG), getResources().getString(R.string.MOBILE_MSG), et_mobile);
            dialog.validationDialog();
            return false;
        }
       /* if (!str_mobile.matches("^[0-9]{10}")) {
            ValidationDialog dialog = new ValidationDialog(EmployeeRegisterActivity.this, Constants.INVALID_MSG, Constants.mobile_valid_msg,et_mobile);
           dialog.validationDialog();
            return false;
        }*/
        if (TextUtils.isEmpty(str_nick_name)) {
            ValidationDialog dialog = new ValidationDialog(EmployeeRegisterActivity.this, getResources().getString(R.string.EMPTY_MSG), getResources().getString(R.string.NICK_NAME_MSG), et_nick_name);
            dialog.validationDialog();
            return false;
        }
        if (TextUtils.isEmpty(str_password)) {
            ValidationDialog dialog = new ValidationDialog(EmployeeRegisterActivity.this, getResources().getString(R.string.EMPTY_MSG), getResources().getString(R.string.PASSWORD_MSG), et_password);
            dialog.validationDialog();
            return false;
        }
        if (str_password.length() < 8) {
            ValidationDialog dialog = new ValidationDialog(EmployeeRegisterActivity.this, getResources().getString(R.string.INVALID_MSG), getResources().getString(R.string.PASSWORD_VALID_MSG), et_password);
            dialog.validationDialog();
            return false;
        }
        if (TextUtils.isEmpty(str_password_verification)) {
            ValidationDialog dialog = new ValidationDialog(EmployeeRegisterActivity.this, getResources().getString(R.string.EMPTY_MSG), getResources().getString(R.string.PASSWORD_VERIFICATION_MSG), et_password_verification);
            dialog.validationDialog();
            return false;
        }
        if (!str_password_verification.equals(str_password)) {
            ValidationDialog dialog = new ValidationDialog(EmployeeRegisterActivity.this, getResources().getString(R.string.MISMATCH_MSG), getResources().getString(R.string.PASSWORD_MATCH_MSG), et_password_verification);
            dialog.validationDialog();
            return false;
        }
        if (TextUtils.isEmpty(str_saloon_code)) {
            ValidationDialog dialog = new ValidationDialog(EmployeeRegisterActivity.this, getResources().getString(R.string.EMPTY_MSG), getResources().getString(R.string.SALOON_CODE_MSG), et_saloon_code);
            dialog.validationDialog();
            return false;
        }
        return true;
    }

    private void verificationCode(String phone) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty(Constants.KEY_API_PHONE, phone);
        Utility.printLog(TAG, "verificationCode:jsonObject:" + jsonObject);

        if (Utility.isConnectedToInternet(EmployeeRegisterActivity.this)) {
            Utility.progressBarDialogShow(EmployeeRegisterActivity.this);
            ApiCall.verificationCode(EmployeeRegisterActivity.this, jsonObject);
        } else {
            Utility.showDialog(EmployeeRegisterActivity.this, getResources().getString(R.string.KEY_ALERT), getResources().getString(R.string.NO_INTERNET_CONNECTION));
        }

    }

    public void registerEmployee(String verificationCode) {

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty(Constants.KEY_ICON_ID, str_select_icon);
        jsonObject.addProperty(Constants.KEY_API_PHONE, str_mobile);
        jsonObject.addProperty(Constants.KEY_STORE_ID, str_saloon_code);
        jsonObject.addProperty(Constants.KEY_API_PASSWORD, str_password);
        jsonObject.addProperty(Constants.KEY_API_EMAIL, str_email);
        jsonObject.addProperty(Constants.KEY_API_VERIFICATION_CODE, verificationCode);
        jsonObject.addProperty(Constants.KEY_API_USERNAME, str_name);
        jsonObject.addProperty(Constants.KEY_API_NICKNAME, str_nick_name);
        Utility.printLog(TAG, "registerEmployee:jsonObject:" + jsonObject);

        if (Utility.isConnectedToInternet(EmployeeRegisterActivity.this)) {
            Utility.progressBarDialogShow(EmployeeRegisterActivity.this);
            ApiCall.register(EmployeeRegisterActivity.this, jsonObject);
        } else {
            Utility.showDialog(EmployeeRegisterActivity.this, getResources().getString(R.string.KEY_ALERT), getResources().getString(R.string.NO_INTERNET_CONNECTION));
        }

    }
}
