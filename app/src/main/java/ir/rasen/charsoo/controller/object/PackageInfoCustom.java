package ir.rasen.charsoo.controller.object;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Created by hossein-pc on 6/22/2015.
 */
public class PackageInfoCustom {
    public String appname = "";
    public String pname = "";
    public String versionName = "";
    public int versionCode = 0;
    public Bitmap icon;
    public void prettyPrint() {
        Log.v(appname + "\t" + pname + "\t" + versionName + "\t" + versionCode, "");
    }
}
