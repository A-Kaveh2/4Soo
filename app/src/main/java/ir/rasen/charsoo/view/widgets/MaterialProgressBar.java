package ir.rasen.charsoo.view.widgets;

import android.content.Context;
import android.util.AttributeSet;

import ir.rasen.charsoo.view.widgets.material_library.views.ProgressBarCircularIndeterminate;

public class MaterialProgressBar extends ProgressBarCircularIndeterminate {

    public MaterialProgressBar(Context context) {
        super(context, null);
        init();
    }

    public MaterialProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
    }

}