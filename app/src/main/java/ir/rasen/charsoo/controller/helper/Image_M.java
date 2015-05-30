package ir.rasen.charsoo.controller.helper;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.os.Environment;
import android.util.Base64;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import ir.rasen.charsoo.R;


/**
 * Created by android on 1/20/2015.
 */
public class Image_M {


    public static int LARGE = 1;
    public static int MEDIUM = 2;
    public static int SMALL = 3;
    public static int COVER = 4;

    public static enum ImageType {USER, BUSINESS, POST}

    ;

    public static int getDefaultImage(ImageType imageType) {
        int imageId = 0;
        switch (imageType) {
            case BUSINESS:
                imageId = R.drawable.ic_person_grey600_36dp;
                break;
            case POST:
                imageId = R.drawable.ic_person_grey600_36dp;
                break;
            case USER:
                imageId = R.drawable.ic_person_grey600_36dp;
                break;

        }
        return imageId;
    }

    public static String getBase64String(String imageFilePath) {
        Bitmap bm = BitmapFactory.decodeFile(imageFilePath);
        int si = Image_M.sizeOf(bm);

        BitmapFactory.Options ops = new BitmapFactory.Options();
        ops.inSampleSize = 1;

        //because of the webservice's bug, we have to decrease the volume of the image
//        while (Image_M.sizeOf(bm) > 100000) {
//            ops.inSampleSize++;
//            bm = BitmapFactory.decodeFile(imageFilePath, ops);
//            si = Image_M.sizeOf(bm);
//        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        String result = encodedImage.replace("\n", "");
        return result;
    }

    public static Bitmap getBitmapFromString(String codedImage) {
        byte[] decodedString = Base64.decode(codedImage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    public static void saveBitmap(String path, String imageName, Bitmap bitmap) {
        File checkDirectory = new File(path);
        if (!checkDirectory.exists())
            checkDirectory.mkdirs();

        File file = new File(path, imageName);
        file.mkdir();
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (Exception e) {
            String s = e.getMessage();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public static int sizeOf(Bitmap data) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
            return data.getRowBytes() * data.getHeight();
        } else {
            return data.getByteCount();
        }
    }

    static long getImageLength(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageInByte = stream.toByteArray();
        return imageInByte.length;
    }

    static int getBytesPerPixel(Config config) {
        if (config == Config.ARGB_8888) {
            return 4;
        } else if (config == Config.RGB_565) {
            return 2;
        } else if (config == Config.ARGB_4444) {
            return 2;
        } else if (config == Config.ALPHA_8) {
            return 1;
        }
        return 1;
    }

    public static Bitmap readBitmapFromStorate(String filePath) {
        return BitmapFactory.decodeFile(filePath);
    }

    public static void deletePictureById(Context context, int id) {
        String storagePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                context.getResources().getString(R.string.download_storage_path);

        File file = new File(storagePath + "/" + String.valueOf(id) + "_" + String.valueOf(1) + ".jpg");
        if (file.exists())
            file.delete();
        file = new File(storagePath + "/" + String.valueOf(id) + "_" + String.valueOf(2) + ".jpg");
        if (file.exists())
            file.delete();

        file = new File(storagePath + "/" + String.valueOf(id) + "_" + String.valueOf(3) + ".jpg");
        if (file.exists())
            file.delete();

    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }


}
