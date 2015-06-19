package ir.rasen.charsoo.view.widgets;

import android.content.Context;
import android.util.AttributeSet;

import ir.rasen.charsoo.R;

public class RippleViewMenu extends com.andexert.library.RippleView {

    public RippleViewMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public RippleViewMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RippleViewMenu(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        setRippleColor(R.color.material_gray); // not true here!
        if (!isInEditMode()) {

        }
    }

}