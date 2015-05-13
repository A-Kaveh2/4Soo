package ir.rasen.charsoo.view.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.view.widget_customized.TextViewFont;


public class DialogMessage extends MyDialogOk {
    Context context;


    @SuppressLint("NewApi")
    public DialogMessage(final Context context, String message) {
        super(context, context.getResources().getString(R.string.popup_warning),
                context.getResources().getString(R.string.ok));


        LinearLayout.LayoutParams paramsWarning = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextViewFont textViewWarning = new TextViewFont(context);
        textViewWarning.setGravity(Gravity.CENTER);
        paramsWarning.setMargins(5, getRowHeight(), 5, getRowHeight());
        textViewWarning.setLayoutParams(paramsWarning);
        textViewWarning.setText(message);


        LinearLayout ll_body = getBody();
        ll_body.addView(textViewWarning);

        //set onClickListener for the cancel button
        TextViewFont textViewOk = getOkButtonTextView();

        //set onClickListener for the ok button
        textViewOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

}
