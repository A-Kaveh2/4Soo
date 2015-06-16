package ir.rasen.charsoo.view.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.object.Comment;
import ir.rasen.charsoo.model.comment.UpdateComment;
import ir.rasen.charsoo.view.interface_m.ICommentChange;
import ir.rasen.charsoo.view.widgets.EditTextFont;
import ir.rasen.charsoo.view.widgets.TextViewFont;


public class DialogEditComment extends MyDialogOkCancel {
    Context context;


    @SuppressLint("NewApi")
    public DialogEditComment(final Context context, final Comment comment,final ICommentChange iCommentChange) {
        super(context, context.getResources().getString(R.string.edit_comment),
                context.getResources().getString(R.string.cancel),
                context.getResources().getString(R.string.edit));

        //creating editText
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final EditTextFont et_comment = new EditTextFont(context);
        params.setMargins(10, getRowHeight(), 10, getRowHeight());
        et_comment.setLayoutParams(params);
        et_comment.setText(comment.text);
        et_comment.setBackgroundResource(R.drawable.shape_edit_text_back);
        et_comment.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        int padding = context.getResources().getInteger(R.integer.edit_text_padding);
        et_comment.setPadding(padding, padding, padding, padding);
        //put cursor in the end of the text
        et_comment.setSelection(et_comment.getText().length());
        et_comment.setSingleLine();
        //add editText to the body
        LinearLayout ll_body = getBody();
        ll_body.addView(et_comment);

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
                if (et_comment.length() < Params.COMMENT_TEXT_MIN_LENGTH) {
                    et_comment.setError(context.getString(R.string.comment_is_too_short));
                    return;
                }
                if (et_comment.length() > Params.COMMENT_TEXT_MAX_LENGTH) {
                    et_comment.setError(context.getString(R.string.enter_is_too_long));
                    return;
                }
                comment.text = et_comment.getText().toString();

                //update the comment
                new UpdateComment(context,comment,iCommentChange).execute();
                dismiss();
            }
        });

    }

}
