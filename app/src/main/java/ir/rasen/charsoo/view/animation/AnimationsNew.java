package ir.rasen.charsoo.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;

import static ir.rasen.charsoo.animation.Anim.Duration;
import static ir.rasen.charsoo.animation.Anim.Interpolate;

/**
 * Created by kh.bakhtiari on 8/31/2014.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class AnimationsNew {

    public static ObjectAnimator fadeIn(View view, long startDelay, Duration duration, Interpolate interpolator) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);

        return fillAnimationsData(animator, startDelay, duration, interpolator);
    }

    public static ObjectAnimator fadeOut(View view, long startDelay, Duration duration, Interpolate interpolator, int visibilityAfter) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);

        return fillAnimationsData(animator, startDelay, duration, interpolator, visibilityAfter);
    }

    public static ObjectAnimator translateTopBottom(View view, long startDelay, Duration duration, Interpolate interpolator) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", (int) (view.getHeight() * 0.75 * -1), 0);

        return fillAnimationsData(animator, startDelay, duration, interpolator);
    }


    public static ObjectAnimator translateTopBottomHalf(View view, long startDelay, Duration duration, Interpolate interpolator) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", view.getHeight() * -1, 0);

        return fillAnimationsData(animator, startDelay, duration, interpolator);
    }

    public static ObjectAnimator translateBottomTop(View view, long startDelay, Duration duration, Interpolate interpolator) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", view.getHeight(), 0);

        return fillAnimationsData(animator, startDelay, duration, interpolator);
    }

    public static ObjectAnimator translateBottomTopVisibility(View view, long startDelay, Duration duration, Interpolate interpolator,int visibilityAfter) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", 0, -view.getHeight());

        return fillAnimationsData(animator, startDelay, duration, interpolator,visibilityAfter);
    }

    public static ObjectAnimator translateTopBottomParent(View parent, View view, long startDelay, Duration duration, Interpolate interpolator, int visibilityAfter) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", 0, parent.getHeight());

        return fillAnimationsData(animator, startDelay, duration, interpolator, visibilityAfter);
    }

    public static ObjectAnimator translateBottomTopParent(View parent, View view, long startDelay, Duration duration, Interpolate interpolator, int visibilityAfter) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", parent.getHeight(), 0);

        return fillAnimationsData(animator, startDelay, duration, interpolator, visibilityAfter);
    }

    public static ObjectAnimator translateLeftRight(View view, long startDelay, Duration duration, Interpolate interpolator) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", view.getWidth() * -1, 0);

        return fillAnimationsData(animator, startDelay, duration, interpolator);
    }

    public static ObjectAnimator translateRightLeft(View view, long startDelay, Duration duration, Interpolate interpolator) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", view.getWidth(), 0);

        return fillAnimationsData(animator, startDelay, duration, interpolator);
    }

    public static ObjectAnimator translateHideRight(View view, long startDelay, Duration duration, Interpolate interpolator) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", 0, view.getWidth());

        return fillAnimationsData(animator, startDelay, duration, interpolator);
    }

    public static ObjectAnimator scaleUp(View view, long startDelay, Duration duration, Interpolate interpolator) {
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(view,
                PropertyValuesHolder.ofFloat("scaleX", 0f, 1f),
                PropertyValuesHolder.ofFloat("scaleY", 0f, 1f));

        return fillAnimationsData(animator, startDelay, duration, interpolator);
    }

    public static ObjectAnimator scaleUp(View view, float toX, float toY, long startDelay, Duration duration, Interpolate interpolator) {
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(view,
                PropertyValuesHolder.ofFloat("scaleX", 0f, toX, 1f),
                PropertyValuesHolder.ofFloat("scaleY", 0f, toY, 1f));

        return fillAnimationsData(animator, startDelay, duration, interpolator);
    }

    public static ObjectAnimator scaleDown(View view, long startDelay, Duration duration, Interpolate interpolator, int visibilityAfter) {
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(view,
                PropertyValuesHolder.ofFloat("scaleX", 1, 0),
                PropertyValuesHolder.ofFloat("scaleY", 1, 0));

        return fillAnimationsData(animator, startDelay, duration, interpolator, visibilityAfter);
    }

    public static ObjectAnimator animateColor(View view, long startDelay, Duration duration, Interpolate interpolator, int visibilityAfter, int colorStart, int colorEnd) {
        ObjectAnimator animator = ObjectAnimator.ofInt(view, "backgroundColor", colorStart, colorEnd);

        return fillAnimationsData(animator, startDelay, duration, interpolator, visibilityAfter);
    }

    private static ObjectAnimator fillAnimationsData(ObjectAnimator animator, long startDelay, Duration duration, Interpolate interpolator) {
        return fillAnimationsData(animator, startDelay, duration, interpolator, View.VISIBLE);
    }

    private static ObjectAnimator fillAnimationsData(ObjectAnimator animator, long startDelay, Duration duration, Interpolate interpolator, int visibilityAfter) {
        View view = (View) animator.getTarget();
        animator.setDuration(duration.duration);
        animator.setInterpolator(interpolator.interpolator);
        animator.setStartDelay(startDelay);
        animator.addListener(new AnimationListener(view, visibilityAfter));
        return animator;
    }

    public static class AnimationListener implements Animator.AnimatorListener {
        View view;
        int visibilityAfter;

        public AnimationListener(View view, int visibilityAfter) {
            this.view = view;
            this.visibilityAfter = visibilityAfter;
        }

        @Override
        public void onAnimationStart(Animator animation) {
            view.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            view.setVisibility(visibilityAfter);
        }

        @Override
        public void onAnimationCancel(Animator animation) {
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    }
}
