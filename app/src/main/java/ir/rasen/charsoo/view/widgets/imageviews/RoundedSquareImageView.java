package ir.rasen.charsoo.view.widgets.imageviews;

import android.content.Context;
import android.util.AttributeSet;

public class RoundedSquareImageView extends RoundedImageView {

    public RoundedSquareImageView(Context context) {
        super(context);
    }

    public RoundedSquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundedSquareImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

}