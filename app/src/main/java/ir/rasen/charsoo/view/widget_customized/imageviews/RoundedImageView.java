package ir.rasen.charsoo.view.widget_customized.imageviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class RoundedImageView extends ImageView {

    public RoundedImageView(Context context) {
        super(context);
        init(context);
    }

    public RoundedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RoundedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        RelativeLayout.LayoutParams params = new RelativeLayout
                .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                context.getResources().getDisplayMetrics().widthPixels);
        setLayoutParams(params);
    }
}