package ir.rasen.charsoo.view.widget_customized.imageviews;

import android.content.Context;
import android.util.AttributeSet;

public class RoundedSquareImageView extends RoundedImageView {

    public RoundedSquareImageView(Context context) {
        super(context);
        init(context);
    }

    public RoundedSquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RoundedSquareImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}