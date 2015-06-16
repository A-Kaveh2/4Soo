package ir.rasen.charsoo.view.widgets.imageviews;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;

import ir.rasen.charsoo.R;

/**
 * Created by 'Sina KH' on 5/18/2015.
 */
public class ExpandableImageView extends ImageView {

    // TODO:: IT's FOR SQUARE IMAGES ONLY -> mContentHeight should be measured correctly

    private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;

    private boolean mExpanded = false;
    private int mCollapsedHeight = 0;
    private int mContentHeight = 0;
    private int mAnimationDuration = 0;
    private int screenWidth = 0;
    private int screenHeight = 0;

    private OnExpandListener mListener;

    public ExpandableImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandableImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        super.setScaleType(SCALE_TYPE);

        mListener = new DefaultOnExpandListener();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExpandableImageView, 0, 0);

        measureWindowSize(context);
        // How high the content should be in "collapsed" state
        mCollapsedHeight = (int) a.getDimension(R.styleable.ExpandableImageView_collapsedHeight, (float) (screenWidth * 0.6));
        Animation anim;
        anim = new ExpandAnimation(mContentHeight, mCollapsedHeight);
        anim.setDuration(0);
        startAnimation(anim);
        mContentHeight = screenWidth;

        // How long the animation should take
        mAnimationDuration = a.getInteger(R.styleable.ExpandableImageView_animationDuration, 500);
        setOnClickListener(new PanelToggle());

        a.recycle();
    }

    public void setOnExpandListener(OnExpandListener listener) {
        mListener = listener;
    }

    public void setCollapsedHeight(int collapsedHeight) {
        mCollapsedHeight = collapsedHeight;
    }

    public void setAnimationDuration(int animationDuration) {
        mAnimationDuration = animationDuration;
    }

    private class PanelToggle implements OnClickListener {
        public void onClick(View v) {
            Animation a;
            if (mExpanded) {
                a = new ExpandAnimation(mContentHeight, mCollapsedHeight);
                mListener.onCollapse();
            } else {
                a = new ExpandAnimation(mCollapsedHeight, mContentHeight);
                mListener.onExpand();
            }
            a.setDuration(mAnimationDuration);
            if (getLayoutParams().height == 0) { // Need to do this or
                // else the
                // animation
                // will not play if
                // the height is 0
                android.view.ViewGroup.LayoutParams lp = getLayoutParams();
                lp.height = 1;
                setLayoutParams(lp);
                requestLayout();
            }
            startAnimation(a);
            mExpanded = !mExpanded;
        }
    }

    private class ExpandAnimation extends Animation {
        private final int mStartHeight;
        private final int mDeltaHeight;

        public ExpandAnimation(int startHeight, int endHeight) {
            mStartHeight = startHeight;
            mDeltaHeight = endHeight - startHeight;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            android.view.ViewGroup.LayoutParams lp = getLayoutParams();
            lp.height = (int) (mStartHeight + mDeltaHeight * interpolatedTime);
            setLayoutParams(lp);
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }

    private void measureWindowSize(Context context) {

            WindowManager w = ((Activity) context).getWindowManager();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                Point size = new Point();
                w.getDefaultDisplay().getSize(size);
                screenWidth = size.x;
                screenHeight = size.y;
            } else {
                Display d = w.getDefaultDisplay();
                screenWidth = d.getWidth();
                screenHeight = d.getHeight();
            }
    }

    public interface OnExpandListener {
        public void onExpand();

        public void onCollapse();
    }

    private class DefaultOnExpandListener implements OnExpandListener {
        public void onCollapse() {
        }

        public void onExpand() {
        }
    }

}