package ir.rasen.charsoo.dialog;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.interfaces.ICancelFriendship;
import ir.rasen.charsoo.interfaces.IWebserviceResponse;
import ir.rasen.charsoo.ui.TextViewFont;


public class PopupCancelFriendship extends MyPopup {
    Context context;



    @SuppressLint("NewApi")
    public PopupCancelFriendship(final Context context, final int requestUserId, final ICancelFriendship iCancelFriendship) {
        super(context);

        this.context = context;


        int height = getRowHeight();
        int width = getRowWidth();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        TextViewFont textViewUnfriend = new TextViewFont(context);
        textViewUnfriend.setGravity(Gravity.CENTER);
        textViewUnfriend.setLayoutParams(params);
        textViewUnfriend.setText(context.getResources().getString(R.string.unfriend));
        textViewUnfriend.setBackgroundResource(R.drawable.selector_popup_one_item);



        LinearLayout ll_body = getBody();
        ll_body.addView(textViewUnfriend);


        textViewUnfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogCancelFriendshipConfirmation d = new DialogCancelFriendshipConfirmation(context,requestUserId,iCancelFriendship);
                d.show();
                dismiss();
            }
        });

    }




}
