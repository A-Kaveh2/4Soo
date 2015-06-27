package ir.rasen.charsoo.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileOutputStream;

import eu.janmuller.android.simplecropimage.CropImage;
import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.CustomeCamera;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.view.interface_m.ICropResult;
import ir.rasen.charsoo.view.widgets.charsoo_activity.CharsooActivity;


public class ActivityCamera extends CharsooActivity {

    private static String TAG = "CameraActivity";
    public static String FILE_PATH = "file_path";
    public static final int CAPTURE_PHOTO = 123;
    private String filePath;
    private static final int PIC_CROP = 4;

    private static String fileName="32543543.jpg";

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
        filePath= Environment.getExternalStorageDirectory()+"/"+ fileName;
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
                    resampleTakenPhoto(Environment.getExternalStorageDirectory()+"/",Environment.getExternalStorageDirectory()+"/sffs/");
                    Intent intent = new Intent(this, CropImage.class);
                    intent.putExtra(CropImage.IMAGE_PATH, Environment.getExternalStorageDirectory()+"/sffs/"+fileName);

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

    public void resampleTakenPhoto(String inputPhotoPath,String outputPhotoPath){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(inputPhotoPath+fileName,options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        String imageType = options.outMimeType;
        int outputImageWidth=0,outputImageHeight=0;
        if (imageHeight<imageWidth){
            if (imageHeight> (Params.IMAGE_MAX_WIDTH_HEIGHT_PIX*2)){
                outputImageHeight=Params.IMAGE_MAX_WIDTH_HEIGHT_PIX;
                outputImageWidth=(int) Math.floor((double) imageWidth *( (double) outputImageHeight / (double) imageHeight));
            }
        }
        else{
            if (imageWidth> (Params.IMAGE_MAX_WIDTH_HEIGHT_PIX*2)){
                outputImageWidth=Params.IMAGE_MAX_WIDTH_HEIGHT_PIX;
                outputImageHeight= (int) Math.floor((double) imageHeight *( (double) outputImageWidth / (double) imageWidth));
            }
        }

        int inSampleSize=calculateInSampleSize(options,outputImageWidth,outputImageHeight);

        options.inJustDecodeBounds = false;
        Bitmap outputImage = BitmapFactory.decodeFile(inputPhotoPath+fileName, options);

        FileOutputStream out = null;
        try {
            File saveDir=new File(outputPhotoPath);
                saveDir.mkdirs();
            File saveFile=new File(outputPhotoPath+fileName);
            if (saveFile.exists())
                saveFile.delete();
            out = new FileOutputStream(saveFile);
            outputImage.compress(Bitmap.CompressFormat.JPEG,100,out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}