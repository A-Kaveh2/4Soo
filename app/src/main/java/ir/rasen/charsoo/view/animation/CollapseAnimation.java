package ir.rasen.charsoo.view.animation;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by kh.bakhtiari on 9/15/2014.
 */
public class CollapseAnimation extends Animation {
    View view;
    ViewGroup.LayoutParams layoutParams;

    public CollapseAnimation(View view) {
        this.view = view;
        layoutParams = view.getLayoutParams();
        this.view.setLayoutParams(layoutParams);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        layoutParams.height = layoutParams.height - (int) (layoutParams.height * interpolatedTime);
        view.setLayoutParams(layoutParams);
    }
}