package ir.rasen.charsoo.view.dialog;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.object.Comment;
import ir.rasen.charsoo.view.interface_m.ICommentChange;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.widget_customized.TextViewFont;


public class PopupDeleteCommentBlockUser extends MyPopup {
    Context context;



    @SuppressLint("NewApi")
    public PopupDeleteCommentBlockUser(final Context context,final int postOwnerBusinessId, final Comment comment, final IWebserviceResponse IWebserviceResponse,final ProgressDialog progressDialog,final ICommentChange iCommentChange ) {
        super(context);

        this.context = context;


        int height = getRowHeight();
        int width = getRowWidth();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        TextViewFont textViewEdit = new TextViewFont(context);
        textViewEdit.setGravity(Gravity.CENTER);
        textViewEdit.setLayoutParams(params);
        textViewEdit.setText(context.getResources().getString(R.string.delete));
        textViewEdit.setBackgroundResource(R.drawable.selector_popup_top_item);
        textViewEdit.setTextSize(context.getResources().getDimension(R.dimen.popup_font));

        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(width,1);
        TextViewFont textViewDevider = new TextViewFont(context);
        textViewDevider.setLayoutParams(params2);
        textViewDevider.setBackgroundColor(Color.GRAY);


        TextViewFont textViewDelete = new TextViewFont(context);
        textViewDelete.setGravity(Gravity.CENTER);
        textViewDelete.setLayoutParams(params);
        textViewDelete.setText(context.getResources().getString(R.string.block));
        textViewDelete.setBackgroundResource(R.drawable.selector_popup_bottom_item);
        textViewDelete.setTextSize(context.getResources().getDimension(R.dimen.popup_font));

        LinearLayout ll_body = getBody();
        ll_body.addView(textViewEdit);
        ll_body.addView(textViewDevider);
        ll_body.addView(textViewDelete);

        textViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogDeleteCommentConfirmation d = new DialogDeleteCommentConfirmation(context,comment,iCommentChange);
                d.show();
                dismiss();
            }
        });
        textViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogBlockUserConfirmation d = new DialogBlockUserConfirmation(context,postOwnerBusinessId,comment, IWebserviceResponse,progressDialog);
                d.show();
                dismiss();
            }
        });
    }




}
