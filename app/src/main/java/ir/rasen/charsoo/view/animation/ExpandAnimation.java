package ir.rasen.charsoo.animation;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

/**
 * Created by kh.bakhtiari on 9/15/2014.
 */
public class ExpandAnimation extends Animation {
    View view;
    int targetHeight;
    ViewGroup.LayoutParams layoutParams;

    public ExpandAnimation(View view) {
        this(view, -1);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ExpandAnimation(View view, int height) {
        this.view = view;
        this.view.setVisibility(View.VISIBLE);
        targetHeight = height;
        if (height == -1)
            targetHeight = getHeightGroup();
        layoutParams = view.getLayoutParams();
        layoutParams.height = 0;

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB)
            this.view.setAlpha(1);
    }

    private int getHeightGroup() {
        LinearLayout linearLayout = (LinearLayout) view;
        View viewChild = linearLayout.getChildAt(0);
        return linearLayout.getChildCount() * getHeightView(viewChild);
    }

    private int getHeightView(View view) {
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthMeasureSpec, heightMeasureSpec);
        return view.getMeasuredHeight();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        layoutParams.height = (int) (targetHeight * interpolatedTime);
        view.setLayoutParams(layoutParams);
    }
}
