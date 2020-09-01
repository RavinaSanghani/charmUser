package com.charm.user;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.M)
public class DialogVerificationCode extends android.app.Dialog {

    private String str_code;
    private EditText et_code;
    private Long verificationCode;
    private Context context;

    public DialogVerificationCode(@androidx.annotation.NonNull Context context, Long verificationCode) {
        super(context);
        this.context = context;
        this.verificationCode = verificationCode;
    }

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_verification_code);

        et_code = findViewById(R.id.et_code);

        et_code.setText(String.valueOf(verificationCode));

        Button btn_ok = findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                str_code = et_code.getText().toString();

                if (validation()) {
                    dismiss();
                    ((EmployeeRegisterActivity)context).registerEmployee(String.valueOf(verificationCode));
                }
            }
        });
    }

    private boolean validation() {

        if (TextUtils.isEmpty(str_code)) {
            ValidationDialog dialog = new ValidationDialog(getContext(), Constants.EMPTY_MSG, Constants.VERIFICATION_MSG, et_code);
            dialog.validationDialog();
            return false;
        }
        if (!str_code.equals(String.valueOf(verificationCode))) {
            ValidationDialog dialog = new ValidationDialog(getContext(), Constants.MISMATCH_MSG, Constants.VERIFICATION_VALID_MSG, et_code);
            dialog.validationDialog();
            return false;
        }
        return true;
    }
}
