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
    private OnImageDownloadListener mDownloadListener;

    MemoryCache memoryCache = new MemoryCache();
    FileCache fileCache;
    private static final String TAG = "ImageDownloader";
    private ServerAnswer serverAnswer;
    private Context context;

    public ImageDownloader(Context context) {
        fileCache = new FileCache(context);
        this.context = context;
    }

    public void DownloadImage(String url, int size, OnImageDownloadListener downloadListener) {
        mDownloadListener = downloadListener;
        if (mDownloadListener == null)
            return;
        Bitmap bitmap = memoryCache.get(url);
        if (bitmap != null) {
            mDownloadListener.DownloadComplete(bitmap);
        } else {
            queuePhoto(url, size);
        }
    }

    /*    public void CreateBitmap(String url, Bitmap bitmap, int size) {
            imageViews.put(bitmap, url);
            Bitmap bitmap = memoryCache.get(url);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                queuePhoto(url, imageView, size);
                imageView.setImageResource(R.drawable.ic_setting);
            }
        }
    */
    private void queuePhoto(Integer pictureId, final int imageSize, Image_M.ImageType imageType, ImageView imageView, boolean isRounded) {
        PhotoToLoad p = new PhotoToLoad(pictureId, imageSize, imageType, imageView, isRounded);

        AsyncTask<PhotoToLoad, Integer, Bitmap> asyncTask = new AsyncTask<PhotoToLoad, Integer, Bitmap>() {
            @Override
            protected Bitmap doInBackground(PhotoToLoad... params) {
                PhotoToLoad pl = params[0];
                String imageString;
                Bitmap bitmap;

                if (pl == null)
                    return null;

                WebserviceGET webserviceGET = new WebserviceGET(URLs.DOWNLOAD_IMAGE, new ArrayList<>(
                        String.valueOf(pl.pictureId),
                        String.valueOf(pl.imageSize)));
                try {
                    serverAnswer = webserviceGET.execute(context);
                    if (serverAnswer.getSuccessStatus()) {
                        JSONObject jsonObject = serverAnswer.getResult();
                        if (jsonObject == null)
                            return null;

                        imageString = jsonObject.getString(Params.IMAGE);
                        if (imageString == null)
                            bitmap = BitmapFactory.decodeResource(context.getResources(), Image_M.getDefaultImage(pl.imageType));
                        else {
                            bitmap = Image_M.getBitmapFromString(imageString);
                            if(bitmap == null)
                                return null;

                            if (bitmap != null) {
                                images.put(key, bitmap);
                                if (isRounded)
                                    downloadQueue.get(0).imageView.setImageBitmap(ImageHelper.getRoundedCornerBitmap(bitmap, context.getResources().getInteger(R.integer.squar_image_corner)));
                                else
                                    downloadQueue.get(0).imageView.setImageBitmap(bitmap);
                                Image_M.saveBitmap(storagePath, downloadQueue.get(0).imageID + "_" + downloadQueue.get(0).imageSize + ".jpg", bitmap);
                            }
                        }

                    } else
                        return null;
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }

                Bitmap bmp = getBitmap(pl.url, pl.size);

                return bmp;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                memoryCache.put(pl.url, bitmap);
                mDownloadListener.DownloadComplete(bitmap);
            }
        };
        asyncTask.execute(p);
        //executorService.submit(new PhotosLoader(p, size));

    }

   /* private Bitmap getBitmap(String url, int size) {
        File f = fileCache.getFile(url);

        // from SD cache
        Bitmap b = decodeFile(f, size);
        if (b != null)
            return b;

        // from web
        try {
            Bitmap bitmap = null;
            //Download with your code
            AsyncTask<Integer, Void, Bitmap> asyncTask = new AsyncTask<Integer, Void, Bitmap>() {
                @Override
                protected Bitmap doInBackground(Integer... params) {
                    int pictureId = params[0];
                    if (pictureId <= 0)
                        return null;


                    return bmp;
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    super.onPostExecute(bitmap);
                    mDownloadListener.DownloadComplete(bitmap);
                }
            };
            asyncTask.execute(p);

            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            InputStream is = conn.getInputStream();
            OutputStream os = new FileOutputStream(f);
            copyStream(is, os);
            os.close();
            //Download with your code
            bitmap = decodeFile(f, size);
            return bitmap;
        } catch (Throwable ex) {
            ex.printStackTrace();
            if (ex instanceof OutOfMemoryError)
                memoryCache.clear();
            return null;
        }
    }*/

    // decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f, int size) {
        try {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            FileInputStream stream1 = new FileInputStream(f);
            BitmapFactory.decodeStream(stream1, null, o);
            stream1.close();

            int width_tmp = o.outWidth, height_tmp = o.outHeight;

            int scale = 1;

            // Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = size;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inJustDecodeBounds = false;
            o2.inSampleSize = scale;
            o2.inDither = true;
            o2.inScaled = false;
            o2.inPreferredConfig = Bitmap.Config.ARGB_8888;
            o2.inPurgeable = true;
            FileInputStream stream2 = new FileInputStream(f);
            Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
            Log.d("Bitmap Width", String.valueOf(o2.outWidth));
            Log.d("Bitmap Height", String.valueOf(o2.outHeight));

            stream2.close();

            return bitmap;

        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    // Task for the queue
    private class PhotoToLoad {
        public int pictureId, imageSize;
        Image_M.ImageType imageType;
        ImageView imageView;
        boolean isRounded;

        public PhotoToLoad(Integer pictureId, int imageSize, Image_M.ImageType imageType, ImageView imageView, boolean isRounded) {
            this.pictureId = pictureId;
            this.imageSize = imageSize;
            this.imageType = imageType;
            this.imageView = imageView;
            this.isRounded = isRounded;
        }
    }


    public void clearCache() {
        memoryCache.clear();
        fileCache.clear();
    }

    public void copyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }

}
