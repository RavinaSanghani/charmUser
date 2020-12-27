package com.charm.user;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProfileDialogLayout extends Dialog {

    private static final String TAG = "ProfileDialogLayout";
    private int[] img_list;
    private ImageView img_select_profile;
    private Context context;

    public ProfileDialogLayout(Context context, int[] img_list, ImageView img_select_profile) {
        super(context);
        this.context = context;
        this.img_list = img_list;
        this.img_select_profile = img_select_profile;

    }

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.profile_dialog_layout);

        RecyclerView recycler_dialog = findViewById(R.id.recycler_dialog);
        ProfileDialogRecyclerViewAdapter adapter = new ProfileDialogRecyclerViewAdapter(context, img_list, img_select_profile);
        recycler_dialog.setLayoutManager(new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false));
        recycler_dialog.setAdapter(adapter);

        recycler_dialog.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@androidx.annotation.NonNull Rect outRect, @androidx.annotation.NonNull android.view.View view, @androidx.annotation.NonNull RecyclerView parent, @androidx.annotation.NonNull RecyclerView.State state) {
                outRect.set(15, 20, 15, 20);
            }
        });


        /*//Animation slide_down = AnimationUtils.loadAnimation(context, R.anim.slide_bottom);
        Animation slide_down_top = AnimationUtils.loadAnimation(context, R.anim.slide_down_top);
        LinearLayout linearLayout=findViewById(R.id.linearLayout);
        linearLayout.startAnimation(slide_down_top);
*/
    }

}
