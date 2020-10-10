package com.charm.user;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

class ProfileDialogRecyclerViewAdapter extends  RecyclerView.Adapter<ProfileDialogRecyclerViewAdapter.ProfileDialogRecyclerViewHolder>{

    private static final String TAG = "ProfileDialogRVA";
    private int[] img_list;
    private ImageView img_select_profile;
    private Context context;

    public ProfileDialogRecyclerViewAdapter(Context context, int[] img_list, ImageView img_select_profile) {
        this.context=context;
        this.img_list = img_list;
        this.img_select_profile=img_select_profile;
    }

    @androidx.annotation.NonNull
    @Override
    public ProfileDialogRecyclerViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_dialog_item, parent, false);
        return new ProfileDialogRecyclerViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull ProfileDialogRecyclerViewHolder holder, final int position) {


        holder.img_profile.setImageResource(img_list[position]);
        holder.itemView.setOnClickListener(new android.view.View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(android.view.View v) {
                ((EmployeeRegisterActivity) context).setVisibilityProfileShow(position);
                img_select_profile.setImageResource(img_list[position]);
            }
        });

    }

    @Override
    public int getItemCount() {
        return img_list.length;
    }

    public static class ProfileDialogRecyclerViewHolder extends RecyclerView.ViewHolder{

        ImageView img_profile;
        public ProfileDialogRecyclerViewHolder(@androidx.annotation.NonNull android.view.View itemView) {
            super(itemView);
            img_profile=itemView.findViewById(R.id.img_profile);
        }
    }
}
