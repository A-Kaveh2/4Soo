package ir.rasen.charsoo.view.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.view.interface_m.IUnfollowBusiness;
import ir.rasen.charsoo.view.widgets.TextViewFont;
import ir.rasen.charsoo.view.widgets.WaitDialog;


public class PopupUnfollowBusiness extends MyPopup {
    Context context;



    @SuppressLint("NewApi")
    public PopupUnfollowBusiness(final Context context, final int businessId, final WaitDialog progressDialog, final IUnfollowBusiness iUnfollowBusiness) {
        super(context);

        this.context = context;


        int height = getRowHeight();
        int width = getRowWidth();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        TextViewFont textViewUnfriend = new TextViewFont(context);
        textViewUnfriend.setGravity(Gravity.CENTER);
        textViewUnfriend.setLayoutParams(params);
        textViewUnfriend.setText(context.getResources().getString(R.string.unfollow));
        textViewUnfriend.setBackgroundResource(R.drawable.selector_popup_one_item);


        LinearLayout ll_body = getBody();
        ll_body.addView(textViewUnfriend);

        textViewUnfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUnfollowBusinessConfirmation d = new DialogUnfollowBusinessConfirmation(context,businessId,progressDialog,iUnfollowBusiness);
                d.show();
                dismiss();
            }
        });

    }




}
