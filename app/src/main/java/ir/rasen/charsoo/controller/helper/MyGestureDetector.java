package ir.rasen.charsoo.controller.helper;

import android.content.Context;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.model.post.Like;
import ir.rasen.charsoo.model.post.Unlike;

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


        if (postLikeStatus) {
            //unlike the post
            imageViewPostLike.setImageResource(R.drawable.ic_favorite_grey);
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
            imageViewLike.setImageResource(R.drawable.ic_favorite_grey);
        } else {
            //like the post

            imageViewPostLike.setImageResource(R.drawable.ic_favorite_blue);
            imageViewPostLike.setVisibility(View.VISIBLE);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    imageViewPostLike.setVisibility(View.INVISIBLE);
                }
            }, 700);
            //Anim.fadeOut(imageViewPostLike,0,Anim.Duration.MEDIUM,Anim.Interpolate.ACCELERATE_DECELERATE,View.INVISIBLE);
            new Like(context, LoginInfo.getUserId(context), postId).execute();
            postLikeStatus = true;
            imageViewLike.setImageResource(R.drawable.ic_favorite_blue);
        }
        return true;
    }
}
