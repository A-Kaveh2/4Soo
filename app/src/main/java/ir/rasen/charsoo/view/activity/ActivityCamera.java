package ir.rasen.charsoo.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.CustomeCamera;
import ir.rasen.charsoo.view.interface_m.ICropResult;
import ir.rasen.charsoo.view.widget_customized.charsoo_activity.CharsooActivity;


public class ActivityCamera extends CharsooActivity implements ICropResult {

    private static String TAG = "CameraActivity";
    FrameLayout cameraPreview;
    CustomeCamera customeCamera;
    RelativeLayout rl_camera_cover;
    ImageView picView;
    ImageView imageView_capture;
    public static String FILE_PATH = "file_path";
    public static Integer CAPTURE_PHOTO = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_camera);


        cameraPreview = (FrameLayout) findViewById(R.id.camera_preview);
        imageView_capture = (ImageView) findViewById(R.id.imageView_capture);
        rl_camera_cover = (RelativeLayout) findViewById(R.id.rl_camera_cover);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels;
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width, height - width - actionBarHeight);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rl_camera_cover.setLayoutParams(lp);


        try {
            customeCamera = new CustomeCamera(this, cameraPreview, getResources().getInteger(R.integer.image_size), getResources().getInteger(R.integer.image_quality));
            customeCamera.delegate = this;
        } catch (Exception e) {
            //new DialogMessage(ActivityCamera.this, e.getMessage()).show();
            String s = e.getMessage();
        }

        imageView_capture.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            customeCamera.capturePhoto();
                        } catch (Exception e) {
                            Log.d(TAG, e.getMessage());
                        }

                    }
                }
        );
    }

    @Override
    protected void onPause() {
        super.onPause();
        customeCamera.stopPreview();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @Override
    public void getResult(String filePath) {

        Intent i = getIntent();
        i.putExtra(ActivityCamera.FILE_PATH, filePath);
        setResult(RESULT_OK, i);
        finish();
    }
}