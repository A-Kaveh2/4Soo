package ir.rasen.charsoo.helper;

import android.content.res.Resources;

import ir.rasen.charsoo.R;

/**
 * Created by android on 3/7/2015.
 */
public class ExceptionMessage {
    public static String set(Resources resources, String exceptionMessage, String exceptionCode) {
        return resources.getString(R.string.exception_code) + exceptionCode + "\n" + exceptionMessage;
    }
}
