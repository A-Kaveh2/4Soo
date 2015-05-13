package ir.rasen.charsoo.helper;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.view.widget_customized.TextViewFontActionBarTitle;

/**
 * Created by android on 5/3/2015.
 */
public class ActionBar_M {

    public static void setActionBar(android.support.v7.app.ActionBar actionBar,Context context,String title){
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.DeepSkyBlue)));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflator = (LayoutInflater) context .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.layout_action_bar_home, null);
        ((TextViewFontActionBarTitle)v.findViewById(R.id.textView_title)).setText(title);
        actionBar.setCustomView(v);
    }

    public static void setActionBarNoBack(android.support.v7.app.ActionBar actionBar, Context context, String title) {
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.DeepSkyBlue)));
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.layout_action_bar_home, null);
        ((TextViewFontActionBarTitle) v.findViewById(R.id.textView_title)).setText(title);
        actionBar.setCustomView(v);
    }


}
