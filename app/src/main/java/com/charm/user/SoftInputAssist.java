package com.charm.user;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

public class SoftInputAssist {
    private View rootView;
    private ViewGroup viewGroup;
    private ViewTreeObserver viewTreeObserver;
    private ViewTreeObserver.OnGlobalLayoutListener listener;
    private Rect rect =new Rect();
    private FrameLayout.LayoutParams rootViewLayout;
    private int usableHeightPrevious = 0;

    public SoftInputAssist(Activity activity) {
        viewGroup=(ViewGroup)activity.findViewById(android.R.id.content);
        rootView=viewGroup.getChildAt(0);
        rootViewLayout=(FrameLayout.LayoutParams)rootView.getLayoutParams();
        listener=new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                possibleResizeChildOfContent();
            }
        };
    }

    public void onPause(){
        if (viewTreeObserver.isAlive()){
            viewTreeObserver.removeOnGlobalLayoutListener(listener);
        }
    }

    public void onResume(){
        if (viewTreeObserver == null || !viewTreeObserver.isAlive()){
            viewTreeObserver=rootView.getViewTreeObserver();
        }
        viewTreeObserver.addOnGlobalLayoutListener(listener);
    }

    public void onDestroy(){
        rootView = null;
        viewGroup = null;
        viewTreeObserver = null;
    }

    private void possibleResizeChildOfContent(){
        viewGroup.getWindowVisibleDisplayFrame(rect);
        int usableHeightNow = rect.bottom;
        if (usableHeightNow != usableHeightPrevious){
            rootViewLayout.height = usableHeightNow;
            rootView.layout(rect.left,rect.top,rect.right,rect.bottom);
            rootView.requestLayout();

            usableHeightPrevious = usableHeightNow;
        }
    }
}
