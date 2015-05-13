package ir.rasen.charsoo.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import ir.rasen.charsoo.view.activity.ActivityLogin;
import ir.rasen.charsoo.R;
import ir.rasen.charsoo.helper.LoginInfo;
import ir.rasen.charsoo.ui.TextViewFont;


public class DialogExit extends MyDialogOkCancel {


    Activity activity;

    @SuppressLint("NewApi")
    public DialogExit(final Activity activity) {
        super(activity, activity.getResources().getString(R.string.popup_warning),
                activity.getResources().getString(R.string.cancel),
                activity.getResources().getString(R.string.exit));

        //creating editText

        LinearLayout.LayoutParams paramsWarning = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextViewFont textViewWarning = new TextViewFont(activity);
        textViewWarning.setGravity(Gravity.CENTER);
        paramsWarning.setMargins(5, getRowHeight(), 5, getRowHeight());
        textViewWarning.setLayoutParams(paramsWarning);
        textViewWarning.setText(activity.getResources().getString(R.string.confirmation_exit));


        //add editText to the body
        LinearLayout ll_body = getBody();
        ll_body.addView(textViewWarning);

        //set onClickListener for the cancel button
        TextViewFont textViewOk = getOkButtonTextView();
        TextViewFont textViewCancel = getCancelButtonTextView();

        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        //set onClickListener for the ok button
        textViewOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginInfo.logout(activity);
                activity.startActivity(new Intent(activity, ActivityLogin.class));
                dismiss();
            }
        });

    }

}
