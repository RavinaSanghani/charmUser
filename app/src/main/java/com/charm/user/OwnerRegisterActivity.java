package com.charm.user;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.charm.user.retrofit.ApiCall;
import com.google.gson.JsonObject;

import static com.charm.user.PrefManager.KEY_PROFILE_SELECT;
import static com.charm.user.PrefManager.KEY_REGISTRATION_STEP;

@RequiresApi(api = Build.VERSION_CODES.M)
public class OwnerRegisterActivity extends AppCompatActivity implements android.view.View.OnClickListener {

    private static final String TAG = "OwnerRegisterActivity";

    private ImageView img_owner_male, img_owner_woman;
    private EditText et_name, et_mobile, et_email, et_password, et_password_verification;
    private String str_nickname, str_name, str_mobile, str_email, str_registration_step, str_password, str_password_verification, verification_code;

    private String loginToken;
    private Button btn_register;
    private boolean str_profile;

    private Context context;
    private PrefManager prefManager;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_register);
        init();
    }

    private void init() {

        context = this;
        prefManager = new PrefManager(context);


        et_name = findViewById(R.id.et_name);
        et_mobile = findViewById(R.id.et_mobile);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_password_verification = findViewById(R.id.et_password_verification);

        img_owner_male = findViewById(R.id.img_owner_male);
        img_owner_woman = findViewById(R.id.img_owner_woman);
        btn_register = findViewById(R.id.btn_register);
        progressBar = findViewById(R.id.progressBar);

        btn_register.setOnClickListener(this);
        img_owner_male.setOnClickListener(this);
        img_owner_woman.setOnClickListener(this);

        et_name.setText("test");
        et_mobile.setText("0508768674");
        et_email.setText("testDemo@gmail.com");
        et_password.setText("1234567890");
        et_password_verification.setText("1234567890");
    }

    @Override
    public void onClick(android.view.View v) {

        switch (v.getId()) {
            case R.id.img_owner_male:
                img_owner_male.setImageResource(R.drawable.img_selected_owner_male);
                img_owner_woman.setImageResource(R.drawable.img_unselected_owner_women);
                img_owner_male.setBackgroundColor(getResources().getColor(R.color.themeOwnerUnselectedColor));
                img_owner_woman.setBackgroundColor(getResources().getColor(R.color.themeOwnerSelectedColor));

                prefManager.putBoolean(KEY_PROFILE_SELECT, true);

                break;
            case R.id.img_owner_woman:
                img_owner_woman.setImageResource(R.drawable.img_selected_owner_women);
                img_owner_male.setImageResource(R.drawable.img_unselected_owner_male);
                img_owner_woman.setBackgroundColor(getResources().getColor(R.color.themeOwnerUnselectedColor));
                img_owner_male.setBackgroundColor(getResources().getColor(R.color.themeOwnerSelectedColor));

                prefManager.putBoolean(KEY_PROFILE_SELECT, false);
                break;
            case R.id.btn_register:
                str_name = et_name.getText().toString();
                str_mobile = et_mobile.getText().toString();
                str_email = et_email.getText().toString();
                str_password = et_password.getText().toString();
                str_password_verification = et_password_verification.getText().toString();
                str_nickname = "";

                str_registration_step = String.valueOf(prefManager.getInteger(PrefManager.KEY_REGISTRATION_STEP, 1));
                str_profile = prefManager.getBoolean(PrefManager.KEY_PROFILE_SELECT, true);
                Utility.printLog(TAG, "onClick:str_registration_step: " + str_registration_step);

                if (validation()) {
                    verificationCode(str_mobile);
                }

                break;
        }
    }

    private boolean validation() {

        if (TextUtils.isEmpty(str_name)) {
            ValidationDialog dialog = new ValidationDialog(OwnerRegisterActivity.this, Constants.EMPTY_MSG, Constants.NAME_MSG, et_name);
            dialog.validationDialog();
            return false;
        }
       /* if (!str_name.matches("^[_A-Za-z\\s]+")) {
            ValidationDialog dialog = new ValidationDialog(OwnerRegisterActivity.this, Constants.INVALID_MSG, Constants.name_valid_msg, et_name);
            dialog.validationDialog();
            return false;
        }*/
        if (TextUtils.isEmpty(str_mobile)) {
            ValidationDialog dialog = new ValidationDialog(OwnerRegisterActivity.this, Constants.EMPTY_MSG, Constants.MOBILE_MSG, et_mobile);
            dialog.validationDialog();
            return false;
        }
       /* if (!str_mobile.matches("^[0-9]{10}")) {
            ValidationDialog dialog = new ValidationDialog(OwnerRegisterActivity.this, Constants.INVALID_MSG, Constants.mobile_valid_msg,et_mobile);
            dialog.validationDialog();
            return false;
        }*/
        if (TextUtils.isEmpty(str_email)) {
            ValidationDialog dialog = new ValidationDialog(OwnerRegisterActivity.this, Constants.EMPTY_MSG, Constants.EMAIL_MSG, et_email);
            dialog.validationDialog();
            return false;
        }
        if (!str_email.matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
            ValidationDialog dialog = new ValidationDialog(OwnerRegisterActivity.this, Constants.INVALID_MSG, Constants.EMAIL_VALID_MSG, et_email);
            dialog.validationDialog();
            return false;
        }
        if (TextUtils.isEmpty(str_password)) {
            ValidationDialog dialog = new ValidationDialog(OwnerRegisterActivity.this, Constants.EMPTY_MSG, Constants.PASSWORD_MSG, et_password);
            dialog.validationDialog();
            return false;
        }
        if (str_password.length() < 8) {
            ValidationDialog dialog = new ValidationDialog(OwnerRegisterActivity.this, Constants.INVALID_MSG, Constants.PASSWORD_VALID_MSG, et_password);
            dialog.validationDialog();
            return false;
        }
        if (TextUtils.isEmpty(str_password_verification)) {
            ValidationDialog dialog = new ValidationDialog(OwnerRegisterActivity.this, Constants.EMPTY_MSG, Constants.PASSWORD_VERIFICATION_MSG, et_password_verification);
            dialog.validationDialog();
            return false;
        }
        if (!str_password_verification.equals(str_password)) {
            ValidationDialog dialog = new ValidationDialog(OwnerRegisterActivity.this, Constants.MISMATCH_MSG, Constants.PASSWORD_MATCH_MSG, et_password_verification);
            dialog.validationDialog();
            return false;
        }
        return true;
    }

    private void verificationCode(String phone) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty(Constants.KEY_API_PHONE, phone);
        Utility.printLog(TAG, "verificationCode:jsonObject:" + jsonObject);

        if (Utility.isConnectedToInternet(context)) {
            showProgressBar();
            ApiCall.verificationCode(OwnerRegisterActivity.this, jsonObject);
        } else {
            Utility.showDialog(OwnerRegisterActivity.this,Constants.KEY_ALERT,Constants.NO_INTERNET_CONNECTION);
        }

    }

    public void registerOwner(String verificationCode) {

        //verificationCode = "007";

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty(Constants.KEY_ICON_ID, "");
        jsonObject.addProperty(Constants.KEY_API_PHONE, str_mobile);
        jsonObject.addProperty(Constants.KEY_STORE_ID, "");
        jsonObject.addProperty(Constants.KEY_API_PASSWORD, str_password);
        jsonObject.addProperty(Constants.KEY_API_EMAIL, str_email);
        jsonObject.addProperty(Constants.KEY_API_VERIFICATION_CODE, verificationCode);
        jsonObject.addProperty(Constants.KEY_API_USERNAME, str_name);
        jsonObject.addProperty(Constants.KEY_API_NICKNAME, str_nickname);
        Utility.printLog(TAG, "registerOwner:jsonObject:" + jsonObject);

        if (Utility.isConnectedToInternet(context)) {
            showProgressBar();
            ApiCall.register(OwnerRegisterActivity.this, jsonObject);
        } else {
            Utility.showDialog(OwnerRegisterActivity.this,Constants.KEY_ALERT,Constants.NO_INTERNET_CONNECTION);

        }

    }

    public void showProgressBar() {
        progressBar.setVisibility(android.view.View.VISIBLE);
    }

    public void hideProgressBar() {
        progressBar.setVisibility(android.view.View.GONE);
    }

}
