package ir.rasen.charsoo.view.dialog;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.Validation;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.widgets.EditTextFont;
import ir.rasen.charsoo.view.widgets.TextViewFont;
import ir.rasen.charsoo.model.user.ForgetPassword;


public class DialogForgetPassword extends MyDialogOkCancel {
    Context context;


    @SuppressLint("NewApi")
    public DialogForgetPassword(final Context context, final IWebserviceResponse IWebserviceResponse, final ProgressDialog progressDialog) {
        super(context, context.getResources().getString(R.string.dialog_forget_password),
                context.getResources().getString(R.string.cancel),
                context.getResources().getString(R.string.retrieval));

        //creating editText
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final EditTextFont et_retrieval = new EditTextFont(context);
        params.setMargins(10, getRowHeight(), 10, getRowHeight());
        et_retrieval.setLayoutParams(params);
        et_retrieval.setHint(context.getResources().getString(R.string.email));
        et_retrieval.setBackgroundResource(R.drawable.shape_edit_text_back);
        et_retrieval.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        int padding = context.getResources().getInteger(R.integer.edit_text_padding);
        et_retrieval.setPadding(padding, padding, padding, padding);
        //put cursor in the end of the text
        et_retrieval.setSelection(et_retrieval.getText().length());
        et_retrieval.setSingleLine();
        //add editText to the body
        LinearLayout ll_body = getBody();
        ll_body.addView(et_retrieval);

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
                if (!Validation.validateEmail(context,et_retrieval.getText().toString()).isValid()) {
                    et_retrieval.setError(Validation.getErrorMessage());
                    return;
                }
                progressDialog.show();
                new ForgetPassword(context, et_retrieval.getText().toString(), IWebserviceResponse).execute();
                dismiss();
            }
        });

    }

}
