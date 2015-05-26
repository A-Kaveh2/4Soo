package ir.rasen.charsoo.view.widget_customized.charsoo_activity;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.view.widget_customized.TextViewFontActionBarTitle;

/**
 * Created by Sina on 5/21/15.
 */
public class NoBackActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.DeepSkyBlue)));
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
        }
    }

    public void init(String title) {
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null) {
            LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflator.inflate(R.layout.layout_action_bar_home, null);
            ((TextViewFontActionBarTitle) v.findViewById(R.id.textView_title)).setText(title);
            actionBar.setCustomView(v);
        }
    }

}
