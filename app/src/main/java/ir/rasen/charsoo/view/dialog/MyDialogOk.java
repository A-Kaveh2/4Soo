package ir.rasen.charsoo.view.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.view.widgets.TextViewFont;


public class MyDialogOk extends MyDialog {
    Context context;
    private TextViewFont  textViewOk;


    @SuppressLint("NewApi")
    public MyDialogOk(final Context context, String title, String okButtonText) {
        super(context, title);

        this.context = context;


        //footer upper divider height is getDividerChildLength dp then buttonHeight should be popup_item_height - getDividerChildLength
        int buttonHeight = getRowHeight() - getDividerChildLength();
        //footer middle divider width is getDividerChildLength dp then buttonWidth should be popup_item_width/getDividerChildLength - (divider child width /2)
        int buttonWidth = getRowWidth();

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


        //creating ok button
        LinearLayout.LayoutParams paramsOk = new LinearLayout.LayoutParams(buttonWidth, buttonHeight);
        textViewOk = new TextViewFont(context);
        textViewOk.setGravity(Gravity.CENTER);
        textViewOk.setLayoutParams(paramsOk);
        textViewOk.setText(okButtonText);
        textViewOk.setBackgroundResource(R.drawable.selector_popup_bottom_button_right);

        //add cancel button, vertical divider and ok button to the buttons section layout
        ll_buttons.addView(textViewOk);


        //add the buttons section layout to the footer layout
        LinearLayout ll_footer = getFooter();
        ll_footer.addView(textViewFooterDivider);
        ll_footer.addView(ll_buttons);
    }

    public TextViewFont getOkButtonTextView() {
        //enable subclasses to handle ok button onClickListener
        return textViewOk;
    }


}
