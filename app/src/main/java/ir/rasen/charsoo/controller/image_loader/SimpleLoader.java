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

package ir.rasen.charsoo.controller.image_loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.Image_M;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.URLs;
import ir.rasen.charsoo.controller.image_loader.core.DisplayImageOptions;
import ir.rasen.charsoo.controller.image_loader.core.ImageLoader;
import ir.rasen.charsoo.controller.image_loader.core.ImageLoaderConfiguration;
import ir.rasen.charsoo.controller.image_loader.core.assist.FailReason;
import ir.rasen.charsoo.controller.image_loader.core.listener.ImageLoadingProgressListener;
import ir.rasen.charsoo.controller.image_loader.core.listener.SimpleImageLoadingListener;
import ir.rasen.charsoo.view.widget_customized.MaterialProgressBar;

/**
 * Class for Downloading image
 *
 * @author Vineet Aggarwal & J
 */

public class SimpleLoader {
    private ImageView imageView;

    private static final String TAG = "ImageDownloader";

    private ServerAnswer serverAnswer;
    private Context context;
    private int pictureId, imageSize;
    Image_M.ImageType imageType;
    DisplayImageOptions options;

    public SimpleLoader(Context context) {
        this.context = context;

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).build();
        ImageLoader.getInstance().init(config);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_empty)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_empty)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        //DownloadImages downloadImages = new DownloadImages(context);
        //downloadImages.download(pictureId,imageSize,imageType,imageView,false);
    }
    public void loadImage(Integer pictureId, int imageSize, Image_M.ImageType imageType, ImageView imageView) {
        loadImage(pictureId, imageSize, imageType, imageView, null);
    }

    public void loadImage(Integer pictureId, int imageSize, Image_M.ImageType imageType, ImageView imageView, final MaterialProgressBar progressBar) {

        this.pictureId = pictureId;
        this.imageSize = imageSize;
        this.imageType = imageType;
        this.imageView = imageView;
        ImageLoader.getInstance().displayImage(URLs.DOWNLOAD_IMAGE + "/" + pictureId + "/" + imageSize, imageView, options,
                progressBar!=null ? new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                progressBar.setVisibility(View.GONE);
            }
        }: null, progressBar!=null ? new ImageLoadingProgressListener() {
            @Override
            public void onProgressUpdate(String imageUri, View view, int current, int total) {
                //progressBar.setProgress(Math.round(100.0f * current / total));
            }
        }: null);

        /*
        if (imageView == null)
            return;
        Bitmap bitmap = memoryCache.get(String.valueOf(pictureId));
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            //queuePhoto(pictureId, imageSize, imageType);
        }*/
    }
}
/*
    private void queuePhoto(Integer pictureId, final int imageSize, final Image_M.ImageType imageType) {
        final PhotoToLoad p = new PhotoToLoad(pictureId, imageSize, imageType);

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
/*    }

        private void queuePhoto(Integer pictureId, int imageSize, Image_M.ImageType imageType, ImageView imageView, boolean isRounded) {
            PhotoToLoad p = new PhotoToLoad(pictureId, imageSize, imageType, imageView, isRounded);
        }
            private void queuePhoto(Integer pictureId, final int imageSize, Image_M.ImageType imageType, ImageView imageView, boolean isRounded) {
        PhotoToLoad p = new PhotoToLoad(pictureId, imageSize, imageType, imageView, isRounded);
    }

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

                            String.valueOf(pl.imageSize)));

                            String.valueOf(pl.imageSize)));

                            try {
                                serverAnswer = webserviceGET.execute(context);
                                if (serverAnswer.getSuccessStatus()) {
                                    JSONObject jsonObject = serverAnswer.getResult();

                                    if (jsonObject != null) {
                                        return jsonObject.getString(Params.IMAGE);
                                    }

                                    if (jsonObject == null)
                                        return null;

                                    imageString = jsonObject.getString(Params.IMAGE);
                                    if (imageString == null)
                                        bitmap = BitmapFactory.decodeResource(context.getResources(), Image_M.getDefaultImage(pl.imageType));
                                    else {
                                        bitmap = Image_M.getBitmapFromString(imageString);
                                        if (bitmap == null)
                                            return null;


                                        imageString = jsonObject.getString(Params.IMAGE);
                                        if (imageString == null)
                                            return null;


                                        bitmap = Image_M.getBitmapFromString(imageString);
                                        if (bitmap == null)

                                    }else

                                    return null;
                                    if (imageType == Image_M.ImageType.BUSINESS)
                                        bitmap = ImageHelper.getRoundedCornerBitmap(bitmap, context.getResources().getInteger(R.integer.squar_image_corner));
                                }
                            }catch (Exception e) {
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
                }

                    private void initialImage(Bitmap bitmap,Image_M.ImageType imageType){
        /*if (bitmap == null)
            imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), Image_M.getDefaultImage(imageType)));
        else
            imageView.setImageBitmap(bitmap);*/
      /*                  if (bitmap != null)
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
*/