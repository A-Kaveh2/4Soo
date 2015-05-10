package ir.rasen.charsoo.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Build;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

/**
 * Created by kh.bakhtiari on 9/10/2014.
 */
public class Anim<T> {
    public T animation;
    private AnimationListener animationListener;

    private Anim(T animation) {
        this.animation = animation;
    }

    public static <T> Anim newAnimation(T animation) {
        if (animation instanceof Animation)
            return new Anim<Animation>((Animation) animation);

        return new Anim<ObjectAnimator>((ObjectAnimator) animation);
    }



    public static Anim fadeIn(View view, long startDelay, Duration duration, Interpolate interpolator) {
        Anim animation;

        if (isAnimationNew())
            animation = newAnimation(AnimationsNew.fadeIn(view, startDelay, duration, interpolator));
        else
            animation = newAnimation(AnimationsOld.fadeIn(view, startDelay, duration, interpolator));

        return animation;
    }

    public static Anim fadeOut(View view, long startDelay, Duration duration, Interpolate interpolator, int visibilityAfter) {
        Anim animation;

        if (isAnimationNew())
            animation = newAnimation(AnimationsNew.fadeOut(view, startDelay, duration, interpolator, visibilityAfter));
        else
            animation = newAnimation(AnimationsOld.fadeOut(view, startDelay, duration, interpolator, visibilityAfter));

        return animation;
    }

    public static Anim scaleUp(View view, long startDelay, Duration duration, Interpolate interpolator) {
        Anim animation;

        if (isAnimationNew())
            animation = newAnimation(AnimationsNew.scaleUp(view, startDelay, duration, interpolator));
        else
            animation = newAnimation(AnimationsOld.scaleUp(view, startDelay, duration, interpolator));

        return animation;
    }

    public static Anim scaleUp(View view, float toX, float toY, long startDelay, Duration duration, Interpolate interpolator) {
        Anim animation;

        if (isAnimationNew())
            animation = newAnimation(AnimationsNew.scaleUp(view, toX, toY, startDelay, duration, interpolator));
        else
            animation = newAnimation(AnimationsOld.scaleUp(view, toX, toY, startDelay, duration, interpolator));

        return animation;
    }

    public static void startTranslateTopBottom(View view, long startDelay, Duration duration, Interpolate interpolator) {
        if (isAnimationNew())
            AnimationsNew.translateTopBottom(view, startDelay, duration, interpolator).start();
        else
            view.startAnimation(AnimationsOld.translateTopBottom(view, startDelay, duration, interpolator));
    }
    public static void startTranslateTopBottomHalf(View view, long startDelay, Duration duration, Interpolate interpolator) {
        if (isAnimationNew())
            AnimationsNew.translateTopBottomHalf(view, startDelay, duration, interpolator).start();
        else
            view.startAnimation(AnimationsOld.translateTopBottom(view, startDelay, duration, interpolator));
    }

    public static void startTranslateBottomTop(View view, long startDelay, Duration duration, Interpolate interpolator) {
        if (isAnimationNew())
            AnimationsNew.translateBottomTop(view, startDelay, duration, interpolator).start();
        else
            view.startAnimation(AnimationsOld.translateBottomTop(view, startDelay, duration, interpolator));
    }

    public static void startTranslateBottomTopVisibility(View view, long startDelay, Duration duration, Interpolate interpolator,int visibilityAfter) {
        if (isAnimationNew())
            AnimationsNew.translateBottomTopVisibility(view, startDelay, duration, interpolator,visibilityAfter).start();
        else
            view.startAnimation(AnimationsOld.translateBottomTop(view, startDelay, duration, interpolator));
    }

    public static void translateTopBottomParent(View parent, View view, long startDelay, Duration duration, Interpolate interpolator, int visibilityAfter) {
        if (isAnimationNew())
            AnimationsNew.translateTopBottomParent(parent, view, startDelay, duration, interpolator, visibilityAfter).start();
        else
            view.startAnimation(AnimationsOld.translateTopBottomParent(view, startDelay, duration, interpolator, visibilityAfter));
    }

    public static void translateBottomTopParent(View parent, View view, long startDelay, Duration duration, Interpolate interpolator, int visibilityAfter) {
        if (isAnimationNew())
            AnimationsNew.translateBottomTopParent(parent, view, startDelay, duration, interpolator, visibilityAfter).start();
        else
            view.startAnimation(AnimationsOld.translateBottomTop(view, startDelay, duration, interpolator));
    }

    public static void startTranslateLeftRight(View view, long startDelay, Duration duration, Interpolate interpolator) {
        if (isAnimationNew())
            AnimationsNew.translateLeftRight(view, startDelay, duration, interpolator).start();
        else
            view.startAnimation(AnimationsOld.translateLeftRight(view, startDelay, duration, interpolator));
    }

    public static void startTranslateRightLeft(View view, long startDelay, Duration duration, Interpolate interpolator) {
        if (isAnimationNew())
            AnimationsNew.translateRightLeft(view, startDelay, duration, interpolator).start();
        else
            view.startAnimation(AnimationsOld.translateRightLeft(view, startDelay, duration, interpolator));
    }

    public static void startTranslateHideRight(View view, long startDelay, Duration duration, Interpolate interpolator) {
        if (isAnimationNew())
            AnimationsNew.translateHideRight(view, startDelay, duration, interpolator).start();
        else
            view.startAnimation(AnimationsOld.translateHideRight(view, startDelay, duration, interpolator));
    }

    public static void startScaleDown(View view, long startDelay, Duration duration, Interpolate interpolator, int visibilityAfter) {
        if (isAnimationNew())
            AnimationsNew.scaleDown(view, startDelay, duration, interpolator, visibilityAfter).start();
        else
            view.startAnimation(AnimationsOld.scaleDown(view, startDelay, duration, interpolator, visibilityAfter));
    }

    public static void startExpand(View view, long startDelay, Duration duration, Interpolate interpolator) {
        startExpand(view, -1, startDelay, duration, interpolator);
    }

    public static void startExpand(View view, int height, long startDelay, Duration duration, Interpolate interpolator) {
        view.startAnimation(AnimationsOld.expand(view, height, startDelay, duration, interpolator));
    }

    public static void startCollapse(View view, long startDelay, Duration duration, Interpolate interpolator) {
        view.startAnimation(AnimationsOld.collapse(view, startDelay, duration, interpolator, View.GONE));
        fadeOut(view, 0, duration, interpolator, View.GONE).start();
    }

    public static void startShowMarginTop(View view, int topMargin, long startDelay, Duration duration, Interpolate interpolator) {
        view.startAnimation(AnimationsOld.topMarginShow(view, topMargin, startDelay, duration, interpolator));
    }

    public static void startHideMarginTop(View view, long startDelay, Duration duration, Interpolate interpolator) {
        view.startAnimation(AnimationsOld.topMarginHide(view, startDelay, duration, interpolator));
    }

    public static void startAnimateColor(View view, long startDelay, Duration duration, Interpolate interpolator, int visibilityAfter, int colorStart, int colorEnd) {
        if (isAnimationNew())
            AnimationsNew.animateColor(view, startDelay, duration, interpolator, visibilityAfter, colorStart, colorEnd).start();
//        else
//            view.startAnimation(AnimationsOld.translateLeftRight(view, startDelay, duration, interpolator));
    }

    public static boolean isAnimationNew() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public void start() {
        if (animation instanceof Animation)
            ((Animation) animation).start();
        else
            ((ObjectAnimator) animation).start();
    }

    public void setAnimationListener(AnimationListener animationListener) {
        this.animationListener = animationListener;

        if (animation instanceof Animation) {
            ((Animation) animation).setAnimationListener(new ListenerOld());
        } else {
            ((ObjectAnimator) animation).addListener(new ListenerNew());
        }
    }

    public enum Duration {
        SHORTEST_FAST(100),
        SHORTEST(150),
        SHORTER(200),
        SHORT(300),
        MEDIUM_SHORT(350),
        MEDIUM(400),
        MEDIUM_LONG(450),
        LONG(500),
        LONGER(600),
        LONGEST(750),
        TEMP(5000);

        public long duration;

        Duration(long duration) {
            this.duration = duration;
        }
    }

    public enum Interpolate {
        LINEAR(new LinearInterpolator()),
        ACCELERATE(new AccelerateInterpolator()),
        DECELERATE(new DecelerateInterpolator()),
        ACCELERATE_DECELERATE(new AccelerateDecelerateInterpolator()),
        BOUNCE(new BounceInterpolator());

        Interpolator interpolator;

        Interpolate(Interpolator interpolator) {
            this.interpolator = interpolator;
        }
    }

    public interface AnimationListener {
//        public void onAnimationStart();

        public void onAnimationEnd();
    }

    private class ListenerNew implements ObjectAnimator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            animationListener.onAnimationEnd();
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }

    private class ListenerOld implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            animationListener.onAnimationEnd();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
