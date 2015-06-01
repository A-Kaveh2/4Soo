/*
 ===========================================================================
 Copyright (c) 2012 Three Pillar Global Inc. http://threepillarglobal.com

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sub-license, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 ===========================================================================
 */
/*J was here*/

package ir.rasen.charsoo.controller.helper.downloadImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.ImageHelper;
import ir.rasen.charsoo.controller.helper.Image_M;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.URLs;
import ir.rasen.charsoo.model.WebserviceGET;

/**
 * Class for Downloading image
 *
 * @author Vineet Aggarwal & J
 */

public class ImageDownloader {
    private ImageView imageView;

    MemoryCache memoryCache = new MemoryCache();
    FileCache fileCache;
    private static final String TAG = "ImageDownloader";
    private ServerAnswer serverAnswer;
    private Context context;
    private int pictureId,imageSize;
    Image_M.ImageType imageType;

    public ImageDownloader(Context context,Integer pictureId, int imageSize, Image_M.ImageType imageType, ImageView imageView) {
        fileCache = new FileCache(context);
        this.context = context;
        this.pictureId = pictureId;
        this.imageSize = imageSize;
        this.imageType = imageType;
        this.imageView = imageView;
    }

    public void DownloadImage() {

        if (imageView == null)
            return;
        Bitmap bitmap = memoryCache.get(String.valueOf(pictureId));
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            queuePhoto(pictureId, imageSize, imageType);
        }
    }

    private void queuePhoto(Integer pictureId, final int imageSize, final Image_M.ImageType imageType) {
        final PhotoToLoad p = new PhotoToLoad(pictureId, imageSize, imageType);

        AsyncTask<PhotoToLoad, Integer, Bitmap> asyncTask = new AsyncTask<PhotoToLoad, Integer, Bitmap>() {
            @Override
            protected Bitmap doInBackground(PhotoToLoad... params) {
                PhotoToLoad pl = params[0];
                String imageString;
                Bitmap bitmap;

                if (pl == null)
                    return null;

                WebserviceGET webserviceGET = new WebserviceGET(URLs.DOWNLOAD_IMAGE, new ArrayList<>(Arrays.asList(
                        String.valueOf(pl.pictureId),
                        String.valueOf(pl.imageSize))));
                try {
                    serverAnswer = webserviceGET.execute(context);
                    if (!serverAnswer.getSuccessStatus())
                        return null;
                    JSONObject jsonObject = serverAnswer.getResult();
                    if (jsonObject == null)
                        return null;

                    imageString = jsonObject.getString(Params.IMAGE);
                    if (imageString == null)
                        return null;

                    bitmap = Image_M.getBitmapFromString(imageString);
                    if (bitmap == null)
                        return null;
                    if (imageType == Image_M.ImageType.BUSINESS)
                        bitmap = ImageHelper.getRoundedCornerBitmap(bitmap, context.getResources().getInteger(R.integer.squar_image_corner));
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    return null;
                }

                memoryCache.put(String.valueOf(pl.pictureId), bitmap);
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                initialImage(bitmap,p.imageType);
            }
        };
        asyncTask.execute(p);
    }

    private void initialImage(Bitmap bitmap,Image_M.ImageType imageType){
        /*if (bitmap == null)
            imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), Image_M.getDefaultImage(imageType)));
        else
            imageView.setImageBitmap(bitmap);*/
        if (bitmap != null)
            imageView.setImageBitmap(bitmap);
    }
    // Task for the queue
    private class PhotoToLoad {
        public int pictureId, imageSize;
        Image_M.ImageType imageType;


        public PhotoToLoad(Integer pictureId, int imageSize, Image_M.ImageType imageType) {
            this.pictureId = pictureId;
            this.imageSize = imageSize;
            this.imageType = imageType;

        }
    }

    public void clearCache() {
        memoryCache.clear();
        fileCache.clear();
    }
}
