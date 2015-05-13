package ir.rasen.charsoo.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import ir.rasen.charsoo.ActivityCamera;
import ir.rasen.charsoo.ActivityGallery;
import ir.rasen.charsoo.R;
import ir.rasen.charsoo.classes.Comment;
import ir.rasen.charsoo.interfaces.ICommentChange;
import ir.rasen.charsoo.interfaces.IWebserviceResponse;
import ir.rasen.charsoo.ui.TextViewFont;


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

/*



        LinearLayout.LayoutParams paramsLayout = new LinearLayout.LayoutParams(width, height);

        LinearLayout linearLayoutCamera = new LinearLayout(activity);
        linearLayoutCamera.setOrientation(LinearLayout.HORIZONTAL);
        linearLayoutCamera.setGravity(Gravity.RIGHT);
        linearLayoutCamera.setLayoutParams(paramsLayout);
        linearLayoutCamera.setBackgroundResource(R.drawable.selector_popup_top_item);

        LinearLayout.LayoutParams paramsIcons = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsIcons.setMargins(width/10,0,width/10,0);

        ImageView imageViewCamera = new ImageView(activity);
        imageViewCamera.setLayoutParams(paramsLayout);
        imageViewCamera.setImageResource(R.drawable.ic_camera_alt_grey600_24dp);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextViewFont textViewCamera = new TextViewFont(activity);
        textViewCamera.setGravity(Gravity.CENTER);
        textViewCamera.setLayoutParams(params);
        textViewCamera.setText(activity.getResources().getString(R.string.camera));
        textViewCamera.setTextSize(activity.getResources().getDimension(R.dimen.popup_font));

        //linearLayoutCamera.addView(textViewCamera);
        linearLayoutCamera.addView(imageViewCamera);


        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(width,1);
        TextViewFont textViewDevider = new TextViewFont(activity);
        textViewDevider.setLayoutParams(params2);
        textViewDevider.setBackgroundColor(Color.GRAY);


        TextViewFont textViewGallery = new TextViewFont(activity);
        textViewGallery.setGravity(Gravity.CENTER);
        textViewGallery.setLayoutParams(params);
        textViewGallery.setText(activity.getResources().getString(R.string.gallery));
        textViewGallery.setBackgroundResource(R.drawable.selector_popup_bottom_item);
        textViewGallery.setTextSize(activity.getResources().getDimension(R.dimen.popup_font));

        LinearLayout ll_body = getBody();
        ll_body.addView(linearLayoutCamera);
        ll_body.addView(textViewDevider);
        ll_body.addView(textViewGallery);
*/

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
