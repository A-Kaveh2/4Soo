package ir.rasen.charsoo.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import ir.rasen.charsoo.ActivityCamera;
import ir.rasen.charsoo.ActivityGallery;
import ir.rasen.charsoo.R;
import ir.rasen.charsoo.classes.Comment;
import ir.rasen.charsoo.interfaces.ICommentChange;
import ir.rasen.charsoo.interfaces.IWebserviceResponse;
import ir.rasen.charsoo.ui.TextViewFont;


public class PopupCameraGallery extends MyPopup {




    @SuppressLint("NewApi")
    public PopupCameraGallery(final Activity activity) {
        super(activity);

        int height = getRowHeight();
        int width = getRowWidth();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        TextViewFont textViewCamera = new TextViewFont(activity);
        textViewCamera.setGravity(Gravity.CENTER);
        textViewCamera.setLayoutParams(params);
        textViewCamera.setText(activity.getResources().getString(R.string.camera));
        textViewCamera.setBackgroundResource(R.drawable.selector_popup_top_item);

        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(width,1);
        TextViewFont textViewDevider = new TextViewFont(activity);
        textViewDevider.setLayoutParams(params2);
        textViewDevider.setBackgroundColor(Color.GRAY);


        TextViewFont textViewGallery = new TextViewFont(activity);
        textViewGallery.setGravity(Gravity.CENTER);
        textViewGallery.setLayoutParams(params);
        textViewGallery.setText(activity.getResources().getString(R.string.gallery));
        textViewGallery.setBackgroundResource(R.drawable.selector_popup_bottom_item);

        LinearLayout ll_body = getBody();
        ll_body.addView(textViewCamera);
        ll_body.addView(textViewDevider);
        ll_body.addView(textViewGallery);

        textViewCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(activity, ActivityCamera.class);
                activity.startActivityForResult(myIntent, ActivityCamera.CAPTURE_PHOTO);
                dismiss();
            }
        });
        textViewGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(activity, ActivityGallery.class);
                activity.startActivityForResult(myIntent, ActivityGallery.CAPTURE_GALLERY);
                dismiss();
            }
        });
    }





}
