package ir.rasen.charsoo.webservices;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.helper.ImageHelper;
import ir.rasen.charsoo.helper.Image_M;
import ir.rasen.charsoo.helper.Params;
import ir.rasen.charsoo.helper.ServerAnswer;
import ir.rasen.charsoo.helper.URLs;


/**
 * Created by android on 1/31/2015.
 */
public class DownloadImages {

    //this class download images with thread pool and cache them after download.


    //key: image name
    //value: image bitmap
    Hashtable<Integer, Bitmap> images;

    private boolean isDownloadStarted;
    private String storagePath;
    private Context context;
    private ArrayList<DownloadImage> downloadQueue;
    private Image_M.ImageType imageType;

    public DownloadImages(Context context) {
        isDownloadStarted = false;
        this.context = context;
        storagePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                context.getResources().getString(R.string.download_storage_path);
        downloadQueue = new ArrayList<>();
        images = new Hashtable<>();
    }

    public void download(int imageID, int imageSize, Image_M.ImageType imageType, ImageView imageView, boolean isRounded) {
        //imageSize: 1=large, 2=medium, 3= small

        this.imageType = imageType;
        if (imageView == null)
            return;
        int key = Integer.valueOf(String.valueOf(imageID) + String.valueOf(imageSize));

        //if image is already used
        if (images.containsKey(key)) {
            if (isRounded)
                imageView.setImageBitmap(ImageHelper.getRoundedCornerBitmap(images.get(key), context.getResources().getInteger(R.integer.squar_image_corner)));
            else
                imageView.setImageBitmap(images.get(key));
            //imageView.setImageBitmap(Image_M.getRoundedCornerBitmap(images.get(imageID),80));
            return;
        }

        if (isImageInStorage(imageID, imageSize)) {
            Bitmap bitmap = BitmapFactory.decodeFile(storagePath + "/" + String.valueOf(imageID) + "_" + String.valueOf(imageSize) + ".jpg");
            if (bitmap == null)
                bitmap = BitmapFactory.decodeResource(context.getResources(),Image_M.getDefaultImage(imageType));


            images.put(key, bitmap);
            try {
                if (isRounded)
                    imageView.setImageBitmap(ImageHelper.getRoundedCornerBitmap(bitmap, context.getResources().getInteger(R.integer.squar_image_corner)));
                else
                    imageView.setImageBitmap(bitmap);
                //imageView.setImageBitmap(Image_M.getRoundedCornerBitmap(bitmap,(int)context.getResources().getDimension(R.dimen.base_adapter_item_height)));
            } catch (Exception e) {
                String s = e.getMessage();
            }
            return;
        }

        if (!isDownloadStarted) {
            isDownloadStarted = true;
            downloadQueue.add(new DownloadImage(imageID, imageSize, imageView));
            new DownloadImageThread(context, isRounded).execute();
        } else {
            downloadQueue.add(new DownloadImage(imageID, imageSize, imageView));
        }


    }

    private boolean isImageInStorage(int imageID, int imageSize) {
        File file = new File(storagePath, String.valueOf(imageID) + "_" + String.valueOf(imageSize) + ".jpg");
        if (file.exists())
            return true;
        return false;
    }

    private class DownloadImage {
        int imageID;
        int imageSize;
        ImageView imageView;

        public DownloadImage(int imageID, int imageSize, ImageView imageView) {
            this.imageID = imageID;
            this.imageSize = imageSize;
            this.imageView = imageView;
        }
    }

    private class DownloadImageThread extends AsyncTask<Void, Void, String> {
        private static final String TAG = "DownloadImage";
        private ServerAnswer serverAnswer;
        private Context context;
        private boolean isRounded;

        public DownloadImageThread(Context context, boolean isRounded) {
            this.context = context;
            this.isRounded = isRounded;
        }

        @Override
        protected String doInBackground(Void... voids) {
            if (downloadQueue.size() == 0) {
                isDownloadStarted = false;
                return null;
            }

            WebserviceGET webserviceGET = new WebserviceGET(URLs.DOWNLOAD_IMAGE, new ArrayList<>(
                    Arrays.asList(String.valueOf(downloadQueue.get(0).imageID),
                            String.valueOf(downloadQueue.get(0).imageSize))));
            try {
                serverAnswer = webserviceGET.execute(context);
                if (serverAnswer.getSuccessStatus()) {
                    JSONObject jsonObject = serverAnswer.getResult();
                    if (jsonObject != null) {
                        return jsonObject.getString(Params.IMAGE);
                    }
                } else
                    return null;
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (!isDownloadStarted)
                return;

            int key = Integer.valueOf(String.valueOf(downloadQueue.get(0).imageID) + String.valueOf(downloadQueue.get(0).imageSize));

            if (result == null) {
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), Image_M.getDefaultImage(imageType));
                images.put(key, bitmap);
                downloadQueue.get(0).imageView.setImageBitmap(bitmap);
            } else {
                Bitmap bitmap = Image_M.getBitmapFromString(result);
                if (bitmap != null) {
                    images.put(key, bitmap);
                    if (isRounded)
                        downloadQueue.get(0).imageView.setImageBitmap(ImageHelper.getRoundedCornerBitmap(bitmap, context.getResources().getInteger(R.integer.squar_image_corner)));
                    else
                        downloadQueue.get(0).imageView.setImageBitmap(bitmap);
                    Image_M.saveBitmap(storagePath, downloadQueue.get(0).imageID + "_" + downloadQueue.get(0).imageSize + ".jpg", bitmap);
                }
            }

            downloadQueue.remove(0);
            new DownloadImageThread(context, isRounded).execute();

        }
    }
}
