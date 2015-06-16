package ir.rasen.charsoo.controller.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Handler;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.widget.ImageView;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.model.post.Like;
import ir.rasen.charsoo.view.widgets.animations.ResizeAnimation;

/**
 * Created by android on 5/11/2015.
 */
public class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
    int position;
    ImageView imageViewLike;
    ImageView imageViewPostLike;
    Context context;
    int postId;
    boolean postLikeStatus;


    public MyGestureDetector(Context context, int postId, boolean postLikeStatus, ImageView imageViewLike, ImageView imageViewPostLike) {
        this.context = context;
        this.postId = postId;
        this.postLikeStatus = postLikeStatus;
        this.imageViewLike = imageViewLike;
        this.imageViewPostLike = imageViewPostLike;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {


        //if (postLikeStatus) {
            //unlike the post
            /*imageViewPostLike.setImageResource(R.drawable.ic_like);
            imageViewPostLike.setVisibility(View.VISIBLE);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    imageViewPostLike.setVisibility(View.INVISIBLE);
                }
            }, 700);

            new Unlike(context, LoginInfo.getUserId(context), position).execute();
            postLikeStatus = false;
            imageViewLike.setImageResource(R.drawable.ic_like);*/
        //} else {

            int TIME_ANIM_MOVING = 500;
            // like animation

            int width;//imageViewPostLike.getLayoutParams().width;

            WindowManager w = ((Activity) context).getWindowManager();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                Point size = new Point();
                w.getDefaultDisplay().getSize(size);
                width = size.x;
            } else {
                Display d = w.getDefaultDisplay();
                width = d.getWidth();
            }

            Animation anim_zoom = new ResizeAnimation(imageViewPostLike, 0, 0, width*3/4, width*3/4, TIME_ANIM_MOVING);
            // logo animations
            //animationSet.addAnimation(anim_toTop);
            anim_zoom.setInterpolator(new AccelerateInterpolator());

            //like the post
            imageViewPostLike.startAnimation(anim_zoom);
            imageViewPostLike.setVisibility(View.VISIBLE);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    imageViewPostLike.setVisibility(View.INVISIBLE);
                    Animation anim_zoom = new ResizeAnimation(imageViewPostLike, 0, 0, 0, 0, 1);
                    imageViewPostLike.startAnimation(anim_zoom);
                }
            }, 1000);
            //Anim.fadeOut(imageViewPostLike,0,Anim.Duration.MEDIUM,Anim.Interpolate.ACCELERATE_DECELERATE,View.INVISIBLE);
            if(!postLikeStatus) new Like(context, LoginInfo.getUserId(context), postId).execute();
            postLikeStatus = true;
            imageViewLike.setImageResource(R.drawable.ic_favorite_blue);
        //}
        return true;
    }
}
