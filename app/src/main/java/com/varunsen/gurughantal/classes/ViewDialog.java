package com.varunsen.gurughantal.classes;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.varunsen.gurughantal.R;

public class ViewDialog {

    Activity activity;
    Dialog dialog;

    public ViewDialog(Activity activity) {
        this.activity = activity;
    }

    public void showDialog() {

        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(true);

        dialog.setContentView(R.layout.custom_loading_layout);

        ImageView gifImageView = dialog.findViewById(R.id.custom_loading_imageView);

        Glide.with(activity).asGif().load(R.drawable.custom_loading_image).into(gifImageView);


        dialog.show();
    }

    public void hideDialog() {
        dialog.dismiss();
    }
}
