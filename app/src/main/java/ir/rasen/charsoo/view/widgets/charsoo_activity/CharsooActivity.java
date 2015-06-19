package ir.rasen.charsoo.view.widgets.charsoo_activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.view.widgets.TextViewFontActionBarTitle;

/**
 * Created by Sina on 5/21/15.
 */
public class CharsooActivity extends AppCompatActivity {

    private Toolbar toolbar;
    // moving variables::
    //private View movingObject;
    private boolean actionBarSet = false;
    //private static int MOVING_TIME = 200;
    //private View content;
    //private boolean scrollMovement = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customizeActionbar();
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflator.inflate(R.layout.layout_action_bar_home, null);
            ((TextViewFontActionBarTitle) v.findViewById(R.id.textView_title)).setText(title);
            actionBar.setCustomView(v);
        }

        /*final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                content = findViewById(R.id.content);
                if(content!=null) {
                    ViewGroup.LayoutParams params = content.getLayoutParams();
                    if(content.getHeight()>0) {
                        params.height = content.getHeight() + movingObject.getHeight();
                        ViewPropertyAnimator.animate(movingObject).translationY(0).setDuration(MOVING_TIME);
                        ViewPropertyAnimator.animate(content).translationY(0).setDuration(MOVING_TIME);
                        content.setLayoutParams(params);
                        scrollMovement = true;
                    } else {
                        handler.postDelayed(this, 50);
                    }
                } else scrollMovement = true;
            }
        }, 50);*/
    }

    @Override
    public void setTitle(int titleId) {
        super.setTitle(titleId);

        setTitle(getString(titleId));
    }

    @Override
    public void setSupportActionBar(Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
        if (actionBarSet) {
            actionBarSet = false;
            return;
        }
        actionBarSet = true;
        this.toolbar = toolbar;
        //this.movingObject = toolbar;
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setSupportActionBar(toolbar);
        customizeActionbar();
    }

    private void customizeActionbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null && toolbar == null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.DeepSkyBlue)));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowCustomEnabled(true);
            setTitle(getTitle());
        } else {
            setTitle(null);
        }
    }

    //public void onScrollStateChanged(AbsListView absListView, int scrollState) {
/*        switch (scrollState) {
            case AbsListView.SCROLL_AXIS_NONE:
                floatHiding = false;
                floatShowing = false;
                ViewPropertyAnimator.animate(movingObject).translationY(0).setDuration(MOVING_TIME);
                try {
                    ViewPropertyAnimator.animate(content).translationY(0).setDuration(MOVING_TIME);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }*/
    //}

    //private boolean floatHiding = false, floatShowing = false;
    //private int mLastFirstVisibleItem;
    //public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
/*
        if(!scrollMovement)
            return;

        if (mLastFirstVisibleItem < firstVisibleItem) {

            if (floatShowing) floatShowing = false;
            if (!floatHiding) {
                ViewPropertyAnimator.animate(movingObject).translationY(-movingObject.getHeight()).setDuration(MOVING_TIME);
                if(content!=null)
                    ViewPropertyAnimator.animate(content).translationY(-movingObject.getHeight()).setDuration(MOVING_TIME);
                floatHiding = true;
            }
        }
        if (mLastFirstVisibleItem > firstVisibleItem) {
            if (floatHiding) {
                floatHiding = false;
            }
            if (!floatShowing) {
                ViewPropertyAnimator.animate(movingObject).translationY(0).setDuration(MOVING_TIME);
                if(content!=null)
                    ViewPropertyAnimator.animate(content).translationY(0).setDuration(MOVING_TIME);
                floatShowing = true;
            }
        }
        mLastFirstVisibleItem = firstVisibleItem;*/
    //}

//    public void setMovingObject(View view) {
//        movingObject = view;
//    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        //overridePendingTransition(R.anim.anim_right_to_0, R.anim.anim_0_to_left);
    }

    @Override
    public void finish() {
        super.finish();
        //overridePendingTransition(R.anim.anim_0_to_right, R.anim.anim_left_to_0);
    }
}
