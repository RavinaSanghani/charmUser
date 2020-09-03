
package com.charm.user;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.charm.user.retrofit.ApiCall;
import com.charm.user.Constants;
import com.google.gson.JsonObject;

@RequiresApi(api = Build.VERSION_CODES.M)
public class LoginActivity extends AppCompatActivity implements android.view.View.OnClickListener {

    private static final String TAG = "LoginActivity";
    private Button btn_login, btn_register, btn_forgot_password;
    private EditText et_mobile, et_password;
    private String str_mobile, str_password,str_user_type;

    private DialogForgotPassword forgotPasswordDialog;
    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

    }

    private void init() {

        et_mobile = findViewById(R.id.et_mobile);
        et_password = findViewById(R.id.et_password);

        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        btn_forgot_password = findViewById(R.id.btn_forgot_password);

        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        btn_forgot_password.setOnClickListener(this);

        /*et_mobile.setText("0508768674");
        et_password.setText("1234567890");
    */
    }

    @Override
    public void onClick(android.view.View v) {

        switch (v.getId()) {
            case R.id.btn_login:
                str_mobile = et_mobile.getText().toString();
                str_password = et_password.getText().toString();
                str_user_type="END_USER";

                if (validation()) {
                    loginEmployee();
                }
                break;
            case R.id.btn_register:
                startActivity(new Intent(LoginActivity.this, EmployeeRegisterActivity.class));
                break;
            case R.id.btn_forgot_password:
                forgotPasswordDialog = new DialogForgotPassword(LoginActivity.this);
                forgotPasswordDialog.show();
                break;
        }
    }

    public void forgotPasswordDialogDismiss(){
        forgotPasswordDialog.dismiss();
    }

    private void loginEmployee(){

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty(Constants.KEY_API_PASSWORD, str_password);
        jsonObject.addProperty(Constants.KEY_API_PHONE, str_mobile);
        jsonObject.addProperty(Constants.KEY_API_USER_TYPE, str_user_type);
        Utility.printLog(TAG,"loginEmployee:jsonObject:"+jsonObject);

        if (Utility.isConnectedToInternet(LoginActivity.this)){
            Utility.progressBarDialogShow(LoginActivity.this);
            ApiCall.login(LoginActivity.this,jsonObject);
        }else {
            Utility.showDialog(LoginActivity.this,Constants.KEY_ALERT,Constants.NO_INTERNET_CONNECTION);

        }

    }

    private boolean validation() {

        if (TextUtils.isEmpty(str_mobile)) {
            ValidationDialog dialog = new ValidationDialog(LoginActivity.this, Constants.EMPTY_MSG, Constants.MOBILE_MSG,et_mobile);
            dialog.validationDialog();
            return false;
        }
       /* if (!str_mobile.matches("^[0-9]{10}")) {
            ValidationDialog dialog = new ValidationDialog(LoginActivity.this, Constants.INVALID_MSG, Constants.mobile_valid_msg,et_mobile);
            dialog.validationDialog();
            return false;
        }*/
        if (TextUtils.isEmpty(str_password)) {
            ValidationDialog dialog = new ValidationDialog(LoginActivity.this, Constants.EMPTY_MSG, Constants.PASSWORD_MSG,et_password);
            dialog.validationDialog();
            return false;
        }
        if (str_password.length()< 8 ) {
            ValidationDialog dialog = new ValidationDialog(LoginActivity.this, Constants.INVALID_MSG, Constants.PASSWORD_VALID_MSG,et_password);
            dialog.validationDialog();
            return false;
        }
        return true;
    }

}
