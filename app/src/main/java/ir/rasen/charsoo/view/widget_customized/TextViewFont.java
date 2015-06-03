package ir.rasen.charsoo.view.widget_customized;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.TextView;

public class TextViewFont extends TextView {
    final static String ANDROIDXML = "http://schemas.android.com/apk/res/android";

    boolean textSet = false;

    public TextViewFont(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public TextViewFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TextViewFont(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/font.ttf");
            setTypeface(tf);

            int density = getResources().getDisplayMetrics().densityDpi;

            int textSize = attrs!=null ? attrs.getAttributeResourceValue(ANDROIDXML,"textSize",-1) : -1;

            if(textSize==-1) {
                int baseFontSize = 0;
                int screenSize = getResources().getConfiguration().screenLayout &
                        Configuration.SCREENLAYOUT_SIZE_MASK;

                switch (screenSize) {
                    case Configuration.SCREENLAYOUT_SIZE_SMALL:
                        baseFontSize = 8;
                        break;
                    case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                        if (density < DisplayMetrics.DENSITY_XHIGH)
                            baseFontSize = 11;
                        else
                            baseFontSize = 14;
                        break;
                    case Configuration.SCREENLAYOUT_SIZE_LARGE:
                        baseFontSize = 15;
                        break;
                    case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                        baseFontSize = 18;
                        break;
                    case Configuration.SCREENLAYOUT_SIZE_UNDEFINED:
                        baseFontSize = 11;
                        break;

                }
                setTextSize(baseFontSize);
            }
        }
    }

}