package ir.rasen.charsoo.view.animation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;

/**
 * Created by kh.bakhtiari on 9/16/2014.
 */
public class MarginTopAnimationShow extends Animation {
    View view;
    int margin;
    RelativeLayout.LayoutParams layoutParams;

    public MarginTopAnimationShow(View view, int margin) {
        this.view = view;
        this.margin = margin;
        this.layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        layoutParams.topMargin = (int) (margin * interpolatedTime);
        view.setLayoutParams(layoutParams);
    }
}
