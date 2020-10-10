package com.charm.user;

import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.M)
public class DialogVerificationCode extends android.app.Dialog {

    private static final String TAG = "DialogVerificationCode";
    private String str_code;
    private Long verificationCode;
    private Context context;

    public DialogVerificationCode(@NonNull Context context, Long verificationCode,int themeResId) {
        super(context, themeResId);
        this.context = context;
        this.verificationCode = verificationCode;
    }

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_verification_code);

        EditText et_code = findViewById(R.id.et_code);

        Button btn_resend = findViewById(R.id.btn_resend);
        btn_resend.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                dismiss();
                ((EmployeeRegisterActivity)context).resendVerificationCode();
            }
        });


        et_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                str_code=editable.toString();
                if (str_code.equals(String.valueOf(verificationCode))){
                    dismiss();
                    ((EmployeeRegisterActivity)context).registerEmployee(String.valueOf(verificationCode));
                }
            }
        });
    }

}
