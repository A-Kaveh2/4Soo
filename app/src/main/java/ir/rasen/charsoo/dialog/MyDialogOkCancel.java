package ir.rasen.charsoo.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.classes.Comment;
import ir.rasen.charsoo.ui.TextViewFont;


public class MyDialogOkCancel extends MyDialog {
    Context context;
    private TextViewFont textViewCancel, textViewOk;


    @SuppressLint("NewApi")
    public MyDialogOkCancel(final Context context, String title, String cancelButtonText, String okButtonText) {
        super(context, title);

        this.context = context;


        //footer upper divider height is getDividerChildLength dp then buttonHeight should be popup_item_height - getDividerChildLength
        int buttonHeight = getRowHeight() - getDividerChildLength();
        //footer middle divider width is getDividerChildLength dp then buttonWidth should be popup_item_width/getDividerChildLength - (divider child width /2)
        int buttonWidth = getRowWidth() / 2;
        buttonWidth -= getDividerChildLength()/2;

        //creating footer upper divider
        LinearLayout.LayoutParams paramsFooterDivider = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getDividerChildLength());
        TextViewFont textViewFooterDivider = new TextViewFont(context);
        textViewFooterDivider.setLayoutParams(paramsFooterDivider);
        textViewFooterDivider.setBackgroundColor(Color.LTGRAY);


        //creating footer buttons section layout
        LinearLayout.LayoutParams paramsLLButtons = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, buttonHeight);
        LinearLayout ll_buttons = new LinearLayout(context);
        ll_buttons.setLayoutParams(paramsLLButtons);
        ll_buttons.setOrientation(LinearLayout.HORIZONTAL);


        //creating cancel button
        LinearLayout.LayoutParams paramsCancel = new LinearLayout.LayoutParams(buttonWidth, buttonHeight);
        textViewCancel = new TextViewFont(context);
        textViewCancel.setGravity(Gravity.CENTER);
        textViewCancel.setLayoutParams(paramsCancel);
        textViewCancel.setText(cancelButtonText);
        textViewCancel.setBackgroundResource(R.drawable.selector_popup_bottom_button_left);

        //creating footer middle vertical divider
        LinearLayout.LayoutParams paramsVerticalDivider = new LinearLayout.LayoutParams(getDividerChildLength(), ViewGroup.LayoutParams.MATCH_PARENT);
        TextViewFont textViewDivider = new TextViewFont(context);
        textViewDivider.setLayoutParams(paramsVerticalDivider);
        textViewDivider.setBackgroundColor(Color.LTGRAY);

        //creating ok button
        textViewOk = new TextViewFont(context);
        textViewOk.setGravity(Gravity.CENTER);
        textViewOk.setLayoutParams(paramsCancel);
        textViewOk.setText(okButtonText);
        textViewOk.setBackgroundResource(R.drawable.selector_popup_bottom_button_right);

        //add cancel button, vertical divider and ok button to the buttons section layout
        ll_buttons.addView(textViewCancel);
        ll_buttons.addView(textViewDivider);
        ll_buttons.addView(textViewOk);


        //add the buttons section layout to the footer layout
        LinearLayout ll_footer = getFooter();
        ll_footer.addView(textViewFooterDivider);
        ll_footer.addView(ll_buttons);
    }


    public TextViewFont getCancelButtonTextView() {
        //enable subclasses to handle cancel button onClickListener
        return textViewCancel;
    }

    public TextViewFont getOkButtonTextView() {
        //enable subclasses to handle ok button onClickListener
        return textViewOk;
    }


}
