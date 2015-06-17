package ir.rasen.charsoo.view.widgets.buttons;

import android.content.Context;
import android.util.AttributeSet;

import com.gc.materialdesign.views.ButtonFloat;

public class NoShadowFloatButton extends ButtonFloat {

    public NoShadowFloatButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundShadow(false);
    }

}