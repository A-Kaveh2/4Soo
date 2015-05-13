package ir.rasen.charsoo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.internal.in;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.ui.TextViewFont;


public class MyDialog extends MyPopup {
    Context context;
    Dialog dialog;

    private View view;
    private LinearLayout ll_super_body, ll_body;
    //only subclasses can access to the ll_footer
    protected LinearLayout ll_footer;
    TextViewFont tv_title;



    public MyDialog(Context context, String title) {
        super(context);

        //get body layout from MyPopup class
        ll_super_body = super.getBody();



        //creating title textView
        LinearLayout.LayoutParams paramsTitle = new LinearLayout.LayoutParams(getRowWidth(), getRowHeight());
        TextViewFont textViewTitle = new TextViewFont(context);
        textViewTitle.setGravity(Gravity.CENTER);
        textViewTitle.setLayoutParams(paramsTitle);
        textViewTitle.setTextColor(context.getResources().getColor(R.color.app_color));
        textViewTitle.setTextSize(context.getResources().getInteger(R.integer.font_size_dialog_header));
        textViewTitle.setText(title);

        //creating header divider
        LinearLayout.LayoutParams paramsTitleDivider = new LinearLayout.LayoutParams(getRowWidth(),getDividerSuperLength());
        TextViewFont textViewTitleDivider = new TextViewFont(context);
        textViewTitleDivider.setLayoutParams(paramsTitleDivider);
        textViewTitleDivider.setBackgroundColor(context.getResources().getColor(R.color.app_color));

        //creating body layout
        LinearLayout.LayoutParams paramsBody = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        ll_body = new LinearLayout(context);
        ll_body.setLayoutParams(paramsBody);
        ll_body.setGravity(Gravity.CENTER);
        ll_body.setOrientation(LinearLayout.VERTICAL);

        //creating footer section layout
        LinearLayout.LayoutParams paramsFooter = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, getRowHeight());
        ll_footer = new LinearLayout(context);
        ll_footer.setLayoutParams(paramsFooter);
        ll_footer.setOrientation(LinearLayout.VERTICAL);


        //add title, header divider, body section and footer section to the super body layout
        ll_super_body.addView(textViewTitle);
        ll_super_body.addView(textViewTitleDivider);
        ll_super_body.addView(ll_body);
        ll_super_body.addView(ll_footer);

    }


    @Override
    public LinearLayout getBody() {
        //enable subclasses to add views to the ll_body
        return ll_body;
    }

    public LinearLayout getFooter() {
        //enable subclasses to add view (buttons) to the ll_footer
        return ll_footer;
    }
}
