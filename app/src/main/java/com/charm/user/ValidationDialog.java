package com.charm.user;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

@RequiresApi(api = Build.VERSION_CODES.M)
@SuppressLint("ResourceAsColor")
public class ValidationDialog {

    private static final String TAG = "ValidationDialog";
    private EditText editText;
    private String title, msg;
    private Context context;

    public ValidationDialog(Context context, String title, String msg, EditText editText) {
        this.context = context;
        this.title = title;
        this.msg = msg;
        this.editText = editText;
    }

    public void validationDialog() {
        TextView title_text = new TextView(context);
        title_text.setText(title);
        title_text.setPadding(10, 50, 10, 10);
        title_text.setGravity(Gravity.CENTER);
        title_text.setTextColor(android.graphics.Color.BLACK);
        title_text.setTextSize(23);

        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.DialogRounded);
        builder.setCustomTitle(title_text);
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editText.requestFocus();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        TextView message_text = alertDialog.findViewById(android.R.id.message);
        assert message_text != null;
        message_text.setGravity(Gravity.CENTER);
        message_text.setTextColor(android.graphics.Color.BLACK);

        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(R.color.themeStepsLinkColor);
        LinearLayout parent = (LinearLayout) positiveButton.getParent();
        parent.setGravity(Gravity.CENTER_HORIZONTAL);
        android.view.View leftSpacer = parent.getChildAt(1);
        leftSpacer.setVisibility(android.view.View.GONE);
    }

}
