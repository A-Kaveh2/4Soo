package ir.rasen.charsoo.view.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.view.interface_m.IReportPost;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.widget_customized.TextViewFont;
import ir.rasen.charsoo.model.business.BlockUser;


public class PopupBlockUser extends MyPopup {
    Context context;
    private IReportPost iReportPost;


    @SuppressLint("NewApi")
    public PopupBlockUser(final Context context,final int businessId, final int blockingUserId,final IWebserviceResponse iWebserviceResponse) {
        super(context);

        this.context = context;
        this.iReportPost = iReportPost;
        int height = getRowHeight();
        int width = getRowWidth();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        TextViewFont textViewUnfriend = new TextViewFont(context);
        textViewUnfriend.setGravity(Gravity.CENTER);
        textViewUnfriend.setLayoutParams(params);
        textViewUnfriend.setText(context.getResources().getString(R.string.block_follower));
        textViewUnfriend.setBackgroundResource(R.drawable.selector_popup_one_item);
        textViewUnfriend.setTextSize(context.getResources().getDimension(R.dimen.popup_font));


        LinearLayout ll_body = getBody();
        ll_body.addView(textViewUnfriend);


        textViewUnfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BlockUser(context,businessId,blockingUserId,iWebserviceResponse).execute();
                dismiss();
            }
        });

    }




}
