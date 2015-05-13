package ir.rasen.charsoo.view.widget_customized;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.TextView;

public class TextViewFontActionBarTitle extends TextView {
    public TextViewFontActionBarTitle(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TextViewFontActionBarTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewFontActionBarTitle(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/font.ttf");
            setTypeface(tf);

            int density = getResources().getDisplayMetrics().densityDpi;

            int baseFontSize = 0;
            int screenSize = getResources().getConfiguration().screenLayout &
                    Configuration.SCREENLAYOUT_SIZE_MASK;

            switch (screenSize) {
                case Configuration.SCREENLAYOUT_SIZE_SMALL:
                    baseFontSize = 18;
                    break;
                case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                    if (density < DisplayMetrics.DENSITY_XHIGH)
                        baseFontSize = 18;
                    else
                        baseFontSize = 20;
                    break;
                case Configuration.SCREENLAYOUT_SIZE_LARGE:
                    baseFontSize = 21;
                    break;
                case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                    baseFontSize = 22;
                    break;
                case Configuration.SCREENLAYOUT_SIZE_UNDEFINED:
                    baseFontSize = 18;
                    break;

            }
            setTextSize(baseFontSize);
            //setTextSize(getResources().getDimension(R.dimen.font_size_base));
        }
    }
}