package ir.rasen.charsoo.view.widget_customized.charsoo_activity;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.view.widget_customized.TextViewFontActionBarTitle;

/**
 * Created by Sina on 5/21/15.
 */
public class CharsooActivity extends ActionBarActivity {

    Toolbar toolbar;
    View movingObject;
    boolean actionBarSet = false;

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
        this.movingObject = toolbar;
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

    public void hideToolbar() {
        if(movingObject==null)
            return;
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        //    movingObject.animate().translationY(-movingObject.getBottom()).setInterpolator(new AccelerateInterpolator());
    }

    public void showToolbar() {
        if(movingObject==null)
            return;
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        //    movingObject.animate().translationY(0).setInterpolator(new DecelerateInterpolator());
    }

    public void setMovingObject(View view) {
        movingObject = view;
    }

}
