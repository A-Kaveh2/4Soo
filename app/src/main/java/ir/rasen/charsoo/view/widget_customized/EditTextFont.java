package ir.rasen.charsoo.view.widget_customized;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class EditTextFont extends EditText {

    private Drawable drawableRight;
    private Drawable drawableLeft;
    private Drawable drawableTop;
    private Drawable drawableBottom;

    int actionX, actionY;

    boolean errorSet=false;

    public EditTextFont(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public EditTextFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditTextFont(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/yekan.ttf");
            setTypeface(tf);
        }
       /* setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });*/

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getError() != null)
            setError(null);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        if (getError() != null)
            setError(null);
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
    }

    @Override
    public void setError(CharSequence error) {
        super.setError(error);
        if(errorSet) {
            errorSet=false;
            return;
        }
        errorSet=true;
        if(error!=null)
            YoYo.with(Techniques.Shake)
                .duration(700)
                .playOn(this);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD) {
            ForegroundColorSpan fgcspan = new ForegroundColorSpan(getResources().getColor(android.R.color.black));
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder(error);
            ssbuilder.setSpan(fgcspan, 0, error.length(), 0);
            setError(ssbuilder);
        } else {
            setError(error);
        }
        requestFocus();
    }

}