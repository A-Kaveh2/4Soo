package ir.rasen.charsoo.view.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.Validation;
import ir.rasen.charsoo.view.interface_m.IChangePassword;
import ir.rasen.charsoo.view.widget_customized.EditTextFont;
import ir.rasen.charsoo.view.widget_customized.TextViewFont;


public class DialogChangePassword extends MyDialogOkCancel {
    Context context;


    @SuppressLint("NewApi")
    public DialogChangePassword(final Context context,final  IChangePassword iChangePassword) {
        super(context, context.getResources().getString(R.string.change_password),
                context.getResources().getString(R.string.cancel),
                context.getResources().getString(R.string.update));

        //creating editText
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final EditTextFont etOldPassword = new EditTextFont(context);
        params.setMargins(10,0 , 10, 30);
        etOldPassword.setLayoutParams(params);
        etOldPassword.setBackgroundResource(R.drawable.shape_edit_text_back);
        etOldPassword.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        int padding = context.getResources().getInteger(R.integer.edit_text_padding);
        etOldPassword.setPadding(padding, padding, padding, padding);
        etOldPassword.setHint(context.getResources().getString(R.string.password));
        //etOldPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        etOldPassword.setSingleLine();

        final EditTextFont etNewPassword = new EditTextFont(context);
        params.setMargins(10, 30, 10,30 );
        etNewPassword.setLayoutParams(params);
        etNewPassword.setBackgroundResource(R.drawable.shape_edit_text_back);
        etNewPassword.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        etNewPassword.setHint(context.getResources().getString(R.string.new_password));
        etNewPassword.setPadding(padding, padding, padding, padding);
        etNewPassword.setSingleLine();
        //etNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        final EditTextFont etNewPasswordRepeat = new EditTextFont(context);
        params.setMargins(10, 30, 10,30 );
        etNewPasswordRepeat.setLayoutParams(params);
        etNewPasswordRepeat.setBackgroundResource(R.drawable.shape_edit_text_back);
        etNewPasswordRepeat.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        etNewPasswordRepeat.setHint(context.getResources().getString(R.string.password_repeat));
        etNewPasswordRepeat.setPadding(padding, padding, padding, padding);
        //etNewPasswordRepeat.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        etNewPasswordRepeat.setSingleLine();


        //add editText to the body
        LinearLayout ll_body = getBody();
        ll_body.addView(etOldPassword);
        ll_body.addView(etNewPassword);
        ll_body.addView(etNewPasswordRepeat);

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
                if(!Validation.validatePassword(context,etOldPassword.getText().toString()).isValid()) {
                    etOldPassword.setError(Validation.getErrorMessage());
                    return;
                }
                if(!Validation.validatePassword(context,etNewPassword.getText().toString()).isValid()){
                    etNewPassword.setError(Validation.getErrorMessage());
                    return;
                }
                if (!Validation.validateRepeatPassword(context,etNewPassword.getText().toString(),etNewPasswordRepeat.getText().toString()).isValid()) {
                    etNewPassword.setError(Validation.getErrorMessage());
                    return;
                }
                iChangePassword.notifyNewPassword(etNewPassword.getText().toString());
                dismiss();
            }
        });

    }

}
