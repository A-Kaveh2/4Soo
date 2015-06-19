package ir.rasen.charsoo.view.widgets;

import android.content.Context;
import android.util.AttributeSet;

import ir.rasen.charsoo.R;

public class RippleView extends com.andexert.library.RippleView {

    public RippleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public RippleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RippleView(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        if (!isInEditMode()) {

        }
    }

}