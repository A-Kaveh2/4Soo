package ir.rasen.charsoo.view.dialog;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.LoginInfo;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.view.interface_m.IAddReview;
import ir.rasen.charsoo.view.widgets.EditTextFont;
import ir.rasen.charsoo.view.widgets.TextViewFont;
import ir.rasen.charsoo.model.review.ReviewBusiness;


public class DialogAddReview extends MyDialogOkCancel {
    Context context;


    @SuppressLint("NewApi")
    public DialogAddReview(final Context context,final int businessId, final IAddReview iAddReview, final ProgressDialog progressDialog) {
        super(context, context.getResources().getString(R.string.edit_review),
                context.getResources().getString(R.string.cancel),
                context.getResources().getString(R.string.edit));

        /*View viewRatingBar = LayoutInflater.from(context).inflate(
                R.layout.layout_rating_bar, null);*/

        //creating ratingBar
        LinearLayout.LayoutParams paramsRatingBar = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final RatingBar ratingBar =
                new RatingBar(context, null, android.R.attr.ratingBarStyle);
        paramsRatingBar.setMargins(10, 20, 10, 20);
        ratingBar.setLayoutParams(paramsRatingBar);
        ratingBar.setStepSize(1.0f);
        ratingBar.setNumStars(5);


        //creating editText
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final EditTextFont et_review = new EditTextFont(context);
        params.setMargins(10, 20, 10, 20);
        et_review.setLayoutParams(params);
        et_review.setBackgroundResource(R.drawable.shape_edit_text_back);
        et_review.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        et_review.setSingleLine();
        int padding = context.getResources().getInteger(R.integer.edit_text_padding);
        et_review.setPadding(padding, padding, padding, padding);
        //put cursor in the end of the text
        et_review.setSelection(et_review.getText().length());
        et_review.setMaxLines(10);

        //add editText to the body
        LinearLayout ll_body = getBody();
        ll_body.addView(ratingBar);
        ll_body.addView(et_review);

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
                if (et_review.length() < Params.COMMENT_TEXT_MIN_LENGTH) {
                    et_review.setError(context.getString(R.string.comment_is_too_short));
                    return;
                }
                if (et_review.length() > Params.COMMENT_TEXT_MAX_LENGTH) {
                    et_review.setError(context.getString(R.string.enter_is_too_long));
                    return;
                }


                //update the review
                progressDialog.show();
                new ReviewBusiness(context, LoginInfo.getUserId(context),businessId,et_review.getText().toString(),Math.round(ratingBar.getRating()),iAddReview).execute();
                dismiss();
            }
        });

    }

}
