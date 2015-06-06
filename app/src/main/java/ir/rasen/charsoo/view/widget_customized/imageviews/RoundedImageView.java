package ir.rasen.charsoo.view.widget_customized.imageviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RoundedImageView extends ImageView {

    Context context;
    boolean isRounded = true;

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
        this.context = context;
        setRounded(isRounded);
    }

    private void setRounded(boolean isRounded) {
        this.isRounded = isRounded;
        /*if(isRounded) {
            LinearLayout.LayoutParams params = new LinearLayout
                    .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    context.getResources().getDisplayMetrics().widthPixels);
            setLayoutParams(params);
        } else {
            LinearLayout.LayoutParams params = new LinearLayout
                    .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            setLayoutParams(params);
        }*/
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        setRounded(isRounded);
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        setRounded(isRounded);
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        setRounded(isRounded);
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        setRounded(isRounded);
    }

}