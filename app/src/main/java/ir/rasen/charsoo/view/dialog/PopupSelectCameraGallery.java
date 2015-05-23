package ir.rasen.charsoo.view.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import ir.rasen.charsoo.view.activity.ActivityCamera;
import ir.rasen.charsoo.view.activity.ActivityGallery;
import ir.rasen.charsoo.R;


public class PopupSelectCameraGallery extends MyPopup {




    @SuppressLint("NewApi")
    public PopupSelectCameraGallery(final Activity activity) {
        super(activity);

        int height = getRowHeight();
        int width = getRowWidth();


        View view = LayoutInflater.from(activity).inflate(
                R.layout.layout_popup_select_camera_gallery, null);

        LinearLayout ll_body = (LinearLayout) view.findViewById(R.id.ll_body);
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(width, 2* height);
        ll_body.setLayoutParams(layoutParams2);

        LinearLayout linearLayoutCamera = (LinearLayout) view.findViewById(R.id.ll_camera);
        LinearLayout linearLayoutGallery = (LinearLayout) view.findViewById(R.id.ll_gallery);

        LinearLayout ll_body_super = getBody();
        ll_body_super.addView(view);

        linearLayoutCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(activity, ActivityCamera.class);
                activity.startActivityForResult(myIntent, ActivityCamera.CAPTURE_PHOTO);
                dismiss();
            }
        });
        linearLayoutGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(activity, ActivityGallery.class);
                activity.startActivityForResult(myIntent, ActivityGallery.CAPTURE_GALLERY);
                dismiss();
            }
        });
    }





}
