package ir.rasen.charsoo.view.animation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;

/**
 * Created by kh.bakhtiari on 9/16/2014.
 */
public class MarginTopAnimationHide extends Animation {
    View view;
    RelativeLayout.LayoutParams layoutParams;

    public MarginTopAnimationHide(View view) {
        this.view = view;
        this.layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        layoutParams.topMargin = layoutParams.topMargin - (int) (layoutParams.topMargin * interpolatedTime);
        view.setLayoutParams(layoutParams);
    }
}
