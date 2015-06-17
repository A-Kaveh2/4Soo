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
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.model.post.Like;

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

        int TIME_ANIM_MOVING = 300;
        int LIKE_SIZE = 20; // (% of picture)
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

        //Animation anim_zoom = new ResizeAnimation(imageViewPostLike, 0, 0, width * LIKE_SIZE / 100, width * LIKE_SIZE / 100, TIME_ANIM_MOVING);
        // logo animations
        //animationSet.addAnimation(anim_toTop);
        //anim_zoom.setInterpolator(new AccelerateInterpolator());

        //like the post
        //imageViewPostLike.startAnimation(anim_zoom);
        YoYo.with(Techniques.ZoomIn)
                .duration(700)
                .playOn(imageViewPostLike);
        imageViewPostLike.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                imageViewPostLike.setVisibility(View.INVISIBLE);
                //Animation anim_zoom = new ResizeAnimation(imageViewPostLike, 0, 0, 0, 0, 1);
                //imageViewPostLike.startAnimation(anim_zoom);
            }
        }, 700);
        //Anim.fadeOut(imageViewPostLike,0,Anim.Duration.MEDIUM,Anim.Interpolate.ACCELERATE_DECELERATE,View.INVISIBLE);
        if (!postLikeStatus) new Like(context, LoginInfo.getUserId(context), postId).execute();
        postLikeStatus = true;
        imageViewLike.setImageResource(R.drawable.ic_favorite_blue);
        YoYo.with(Techniques.Shake)
                .duration(700)
                .playOn(imageViewLike);
        //}
        return true;
    }
}
