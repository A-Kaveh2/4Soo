package ir.rasen.charsoo.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.view.widget_customized.TextViewFont;


public class MyPopup   {
    private Context context;
    Dialog dialog;

    private View view;
    private LinearLayout ll_body;
    TextViewFont tv_title;
    private int rowHeight;
    private int rowWidth;
    private int dividerSuperLength;
    private int dividerChildLength;

    public MyPopup(Context context) {
        this.context = context;

        int screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;

        rowWidth = (screenWidth/10) * context.getResources().getInteger(R.integer.dialog_row_width_weight);
        rowHeight = screenHeight / context.getResources().getInteger(R.integer.dialog_row_width_height);
        dividerSuperLength =  rowHeight/20;
        dividerChildLength = dividerSuperLength/2;

        view = LayoutInflater.from(context).inflate(
                R.layout.layout_popup, null);

        //creating super body layout
        ll_body = (LinearLayout) view.findViewById(R.id.rl_dialog_body);
        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) ll_body.getLayoutParams();
        layoutParams2.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        layoutParams2.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        layoutParams2.setMargins(5, 2, 5, 5);
        ll_body.setLayoutParams(layoutParams2);
    }


    public LinearLayout getBody() {
        //enable subclasses to add view to the super body layout
        return this.ll_body;
    }

    public void dismiss() {
        dialog.dismiss();
    }

    public void show() {
        //show dialog
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

    }

    public int getRowHeight() {
        return rowHeight;
    }

    public int getRowWidth() {
        return rowWidth;
    }

    public int getDividerSuperLength() {
        return dividerSuperLength;
    }

    public int getDividerChildLength() {
        return dividerChildLength;
    }


}
