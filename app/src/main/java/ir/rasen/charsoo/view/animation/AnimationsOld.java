package ir.rasen.charsoo.view.animation;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;

import static ir.rasen.charsoo.view.animation.Anim.Duration;
import static ir.rasen.charsoo.view.animation.Anim.Interpolate;

/**
 * Created by kh.bakhtiari on 9/8/2014.
 */
public class AnimationsOld {

    public static Animation translateTopBottom(View view, long startDelay, Duration duration, Interpolate interpolator) {
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, -1,
                Animation.RELATIVE_TO_SELF, 0);

        return fillAnimationsData(animation, view, startDelay, duration, interpolator);
    }

    public static Animation translateBottomTop(View view, long startDelay, Duration duration, Interpolate interpolator) {
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1,
                Animation.RELATIVE_TO_SELF, 0);

        return fillAnimationsData(animation, view, startDelay, duration, interpolator);
    }

    public static Animation translateTopBottomParent(View view, long startDelay, Duration duration, Interpolate interpolator, int visibilityAfter) {
        Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_PARENT, 1);

        return fillAnimationsData(animation, view, startDelay, duration, interpolator, visibilityAfter);
    }

    public static Animation translateLeftRight(View view, long startDelay, Duration duration, Interpolate interpolator) {
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -0.6f,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0);

        return fillAnimationsData(translateAnimation, view, startDelay, duration, interpolator);
    }

    public static Animation translateRightLeft(View view, long startDelay, Duration duration, Interpolate interpolator) {
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.6f,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0);

        return fillAnimationsData(translateAnimation, view, startDelay, duration, interpolator);
    }

    public static Animation translateHideRight(View view, long startDelay, Duration duration, Interpolate interpolator) {
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_SELF, 0.8f,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0);

        return fillAnimationsData(translateAnimation, view, startDelay, duration, interpolator);
    }

    public static Animation scaleUp(View view, long startDelay, Duration duration, Interpolate interpolator) {
        return scaleUp(view, 1, 1, startDelay, duration, interpolator);
    }

    public static Animation scaleUp(View view, float toX, float toY, long startDelay, Duration duration, Interpolate interpolator) {
        ScaleAnimation animation = new ScaleAnimation(0, toX, 0, toY, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        return fillAnimationsData(animation, view, startDelay, duration, interpolator);
    }

    public static Animation scaleDown(View view, long startDelay, Duration duration, Interpolate interpolator, int visibilityAfter) {
        ScaleAnimation animation = new ScaleAnimation(1, 0, 1, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        return fillAnimationsData(animation, view, startDelay, duration, interpolator, visibilityAfter);
    }

    public static Animation fadeIn(View view, long startDelay, Duration duration, Interpolate interpolator) {
        AlphaAnimation animation = new AlphaAnimation(0, 1);

        return fillAnimationsData(animation, view, startDelay, duration, interpolator);
    }

    public static Animation fadeOut(View view, long startDelay, Duration duration, Interpolate interpolator, int visibilityAfter) {
        AlphaAnimation animation = new AlphaAnimation(1, 0);

        return fillAnimationsData(animation, view, startDelay, duration, interpolator, visibilityAfter);
    }

    public static Animation expand(View view, int height, long startDelay, Duration duration, Interpolate interpolator) {
        Animation animation = new ExpandAnimation(view, height);

        return fillAnimationsData(animation, view, startDelay, duration, interpolator);
    }

    public static Animation collapse(View view, long startDelay, Duration duration, Interpolate interpolator, int visibilityAfter) {
        Animation animation = new CollapseAnimation(view);

        return fillAnimationsData(animation, view, startDelay, duration, interpolator, visibilityAfter);
    }

    public static Animation topMarginShow(View view, int margin, long startDelay, Duration duration, Interpolate interpolator) {
        Animation animation = new MarginTopAnimationShow(view, margin);

        return fillAnimationsData(animation, view, startDelay, duration, interpolator);
    }

    public static Animation topMarginHide(View view, long startDelay, Duration duration, Interpolate interpolator) {
        Animation animation = new MarginTopAnimationHide(view);

        return fillAnimationsData(animation, view, startDelay, duration, interpolator);
    }

    public static Animation fillAnimationsData(Animation animation, View view, long startDelay, Duration duration, Interpolate interpolator) {
        return fillAnimationsData(animation, view, startDelay, duration, interpolator, View.VISIBLE);
    }

    public static Animation fillAnimationsData(Animation animation, View view, long startDelay, Duration duration, Interpolate interpolator, int visibilityAfter) {
        animation.setDuration(duration.duration);
        animation.setInterpolator(interpolator.interpolator);
        animation.setStartOffset(startDelay);
        animation.setAnimationListener(new AnimationListener(view, visibilityAfter));
        view.setAnimation(animation);
        return animation;
    }

    public static class AnimationListener implements Animation.AnimationListener {
        View view;
        int visibilityAfter;

        public AnimationListener(View view, int visibilityAfter) {
            this.view = view;
            this.visibilityAfter = visibilityAfter;
        }

        @Override
        public void onAnimationStart(Animation animation) {
            view.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            view.setVisibility(visibilityAfter);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    }

    public static class LeftToRightHider extends Animation implements Animation.AnimationListener {
        View view;
        ViewGroup.LayoutParams layoutParams;
        boolean toShow;
        int width;
        AnimationListener animationListener;
        String textToShow;

        public LeftToRightHider(View view, String textToShow, AnimationListener animationListener) {
            AnimationsOld.fillAnimationsData(this, view, 0, Duration.MEDIUM, Interpolate.ACCELERATE);
            setAnimationListener(this);
            this.view = view;
            this.animationListener = animationListener;
            this.textToShow = textToShow;
            this.layoutParams = view.getLayoutParams();
        }

        public void hide() {
            layoutParams.width = view.getWidth();
            toShow = false;
            view.startAnimation(this);
        }

        public void show() {
            view.setVisibility(View.VISIBLE);
            toShow = true;
            int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            view.measure(widthMeasureSpec, heightMeasureSpec);
            width = view.getMeasuredWidth();
            view.startAnimation(this);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            if (toShow) {
                layoutParams.width = ((int) (width * interpolatedTime));
                view.setLayoutParams(layoutParams);
            } else {
                layoutParams.width = layoutParams.width - ((int) (layoutParams.width * interpolatedTime));
                view.setLayoutParams(layoutParams);
            }
        }

        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            animationListener.onAnimationEnd(view, textToShow, animation);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        public interface AnimationListener {
            void onAnimationEnd(View view, String textToShow, Animation animation);
        }
    }
}
