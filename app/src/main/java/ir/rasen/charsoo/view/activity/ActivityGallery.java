package ir.rasen.charsoo.view.activity;

import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import eu.janmuller.android.simplecropimage.CropImage;
import ir.rasen.charsoo.R;
import ir.rasen.charsoo.view.widget_customized.charsoo_activity.CharsooActivity;

public class ActivityGallery extends CharsooActivity {

    private static String TAG = "GalleryActivity";
    final int GALLERY_CAPTURE = 3;
    final int PIC_CROP = 4;


    private int size;
    private int quality;

    public static String FILE_PATH = "file_path";
    public static Integer CAPTURE_GALLERY =2;
    public static String SIZE ="size";
    public static String QUALITY ="quality";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        size = getResources().getInteger(R.integer.image_size);
        quality = getResources().getInteger(R.integer.image_quality);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        try {
            intent.putExtra("return-data", true);
            startActivityForResult(intent, GALLERY_CAPTURE);
        } catch (ActivityNotFoundException e) {
            Log.d(TAG, e.getMessage());

        }

/*        Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_CAPTURE) {
                performCrop(data.getData());
            } else if (requestCode == PIC_CROP) {
                String path = data.getStringExtra(CropImage.IMAGE_PATH);

                // if nothing received
                if (path == null) {
                    Intent i = getIntent();
                    i.putExtra(ActivityGallery.FILE_PATH, path);
                    setResult(RESULT_CANCELED, i);
                    finish();
                    return;
                }

                Intent i = getIntent();
                i.putExtra(ActivityGallery.FILE_PATH, path);
                setResult(RESULT_OK, i);
                finish();

            }
        } else finish();

    }

    private void performCrop(Uri uri) {
        try {
            // create explicit intent
            Intent intent = new Intent(this, CropImage.class);

            // tell CropImage activity to look for image to crop
//            String filePath = getRealPathFromURI(uri);
            String filePath=getPath(this,uri);
            intent.putExtra(CropImage.IMAGE_PATH, filePath);

            // allow CropImage activity to rescale image
            intent.putExtra(CropImage.SCALE, true);

            // if the aspect ratio is fixed to ratio 1/1
            intent.putExtra(CropImage.ASPECT_X, 1);
            intent.putExtra(CropImage.ASPECT_Y, 1);

            // start activity CropImage with certain request code and listen
            // for result
            startActivityForResult(intent, PIC_CROP);

        } catch (ActivityNotFoundException anfe) {
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }


    }

    private static File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "SocialBusiness Temp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }

    /*private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            if (idx>=0)
                result = cursor.getString(idx);
            else{
                String[] filePathColumn = { MediaStore.Images.Media.DATA};
                cursor = getContentResolver().query(contentURI,filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                result = cursor.getString(columnIndex);
                cursor.close();
            }
            cursor.close();
        }
        return result;
        *//*String selectedImagePath;
        //1:MEDIA GALLERY --- query from MediaStore.Images.Media.DATA
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentURI, projection, null, null, null);
        if(cursor != null){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            selectedImagePath = cursor.getString(column_index);
        }else{
            selectedImagePath = null;
        }

        if(selectedImagePath == null){
            //2:OI FILE Manager --- call method: uri.getPath()
            selectedImagePath = contentURI.getPath();
        }
        return selectedImagePath;*//*
    }
*/
    public static String getPath(final Context context, final Uri uri) {

        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            if (DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if ("com.android.externalstorage.documents".equals(uri.getAuthority())) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                    // TODO handle non-primary volumes
                }
                // DownloadsProvider
                else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    }/* else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }*/

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{
                            split[1]
                    };

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }


    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
}