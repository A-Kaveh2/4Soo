package ir.rasen.charsoo.view.dialog;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.object.Comment;
import ir.rasen.charsoo.view.interface_m.ICommentChange;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.widget_customized.TextViewFont;
import ir.rasen.charsoo.model.comment.DeleteComment;


public class DialogDeleteCommentConfirmation extends MyDialogOkCancel {
    Context context;


    @SuppressLint("NewApi")
    public DialogDeleteCommentConfirmation(final Context context, final Comment comment, final IWebserviceResponse IWebserviceResponse, final ProgressDialog progressDialog, final ICommentChange iCommentChange) {
        super(context, context.getResources().getString(R.string.popup_warning),
                context.getResources().getString(R.string.cancel),
                context.getResources().getString(R.string.delete));

        //creating editText
        LinearLayout.LayoutParams paramsWarning = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextViewFont textViewWarning = new TextViewFont(context);
        textViewWarning.setGravity(Gravity.CENTER);
        paramsWarning.setMargins(5, getRowHeight(), 5, getRowHeight());
        textViewWarning.setLayoutParams(paramsWarning);
        textViewWarning.setText(context.getResources().getString(R.string.confirmation_delete_comment));


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
                progressDialog.show();
                //progressDialog will be closed in getResult or getError in calling class
                new DeleteComment(context,comment.businessID, comment.id, IWebserviceResponse, iCommentChange).execute();

                dismiss();
            }
        });

    }

}
