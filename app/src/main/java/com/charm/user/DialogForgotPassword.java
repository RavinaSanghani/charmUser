package com.charm.user;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.charm.user.retrofit.ApiCall;
import com.google.gson.JsonObject;

@RequiresApi(api = Build.VERSION_CODES.M)
public class DialogForgotPassword extends android.app.Dialog {

    private static final String TAG = "DialogForgotPassword";
    private String str_mobile;
    private EditText et_mobile;
    private Activity activity;

    public DialogForgotPassword(@NonNull Context context,Activity activity, int themeResId) {
        super(context, themeResId);
        this.activity=activity;
    }

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_forgot_password);

        et_mobile = findViewById(R.id.et_mobile);

        Button btn_reset=findViewById(R.id.btn_reset);
        btn_reset.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {

                str_mobile=et_mobile.getText().toString();

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty(Constants.KEY_API_PHONE, str_mobile);
                Utility.printLog(TAG,"jsonObject:"+jsonObject);

                if (validation()){
                    if (Utility.isConnectedToInternet(activity)){
                        Utility.progressBarDialogShow(activity);
                        ApiCall.resetPassword(activity,jsonObject);
                    }else {
                        Utility.showDialog(activity,Constants.KEY_ALERT,Constants.NO_INTERNET_CONNECTION);
                    }
                }
            }
        });
    }

    private boolean validation() {

        if (TextUtils.isEmpty(str_mobile)) {
            ValidationDialog dialog = new ValidationDialog(getContext(), Constants.EMPTY_MSG, Constants.MOBILE_MSG, et_mobile);
            dialog.validationDialog();
            return false;
        }
       /* if (!str_mobile.matches("^[0-9]{10}")) {
            ValidationDialog dialog = new ValidationDialog(getContext(), Constants.INVALID_MSG, Constants.mobile_valid_msg, et_mobile);
            dialog.validationDialog();
            return false;
        }*/
        return true;
    }
}
