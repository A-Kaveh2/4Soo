package ir.rasen.charsoo.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;

import java.io.File;

import eu.janmuller.android.simplecropimage.CropImage;
import ir.rasen.charsoo.R;
import ir.rasen.charsoo.view.widgets.charsoo_activity.CharsooActivity;

public class ActivityCamera extends CharsooActivity {

    private static String TAG = "CameraActivity";
    public static String FILE_PATH = "file_path";
    public static final int CAPTURE_PHOTO = 123;
    private String filePath;
    private static final int PIC_CROP = 4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_camera);
        takePhoto();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }


    public void takePhoto() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        filePath=Environment.getExternalStorageDirectory()+"/"+ "4352643565.jpg";
        File photo = new File(filePath);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(photo));
        startActivityForResult(intent, CAPTURE_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAPTURE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    Intent intent = new Intent(this, CropImage.class);
                    intent.putExtra(CropImage.IMAGE_PATH, filePath);

                    // allow CropImage activity to rescale image
                    intent.putExtra(CropImage.SCALE, true);

                    // if the aspect ratio is fixed to ratio 1/1
                    intent.putExtra(CropImage.ASPECT_X, 1);
                    intent.putExtra(CropImage.ASPECT_Y, 1);

                    // start activity CropImage with certain request code and listen
                    // for result
                    startActivityForResult(intent, PIC_CROP);
                }
                break;
            case PIC_CROP:
                String path = data.getStringExtra(CropImage.IMAGE_PATH);
                Intent i = getIntent();
                i.putExtra(ActivityCamera.FILE_PATH, path);
                setResult(RESULT_OK, i);
                finish();
                break;
        }
    }


}