package ir.rasen.charsoo.view.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.helper.LoginInfo;
import ir.rasen.charsoo.view.interface_m.IReportPost;
import ir.rasen.charsoo.view.interface_m.IUpdateTimeLine;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.widget_customized.TextViewFont;
import ir.rasen.charsoo.webservices.post.CancelShare;
import ir.rasen.charsoo.webservices.post.Report;


public class PopupReportCancelSharePost extends MyPopup implements IWebserviceResponse {

    IWebserviceResponse iWebserviceResponse;
    IReportPost iReportPost;
    int position;
    ImageView imageViewMore;


    @SuppressLint("NewApi")
    public PopupReportCancelSharePost(final Context context, final int userId,final int postId, int position, ImageView imageViewMore,final  IUpdateTimeLine iUpdateTimeLine,final IReportPost iReportPost) {
        super(context);

        iWebserviceResponse = this;
        this.iReportPost = iReportPost;
        this.position = position;
        this.imageViewMore = imageViewMore;

        int screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;

        int height = getRowHeight();
        int width = getRowWidth();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        TextViewFont textViewCancelShare = new TextViewFont(context);
        textViewCancelShare.setGravity(Gravity.CENTER);
        textViewCancelShare.setLayoutParams(params);
        textViewCancelShare.setText(context.getResources().getString(R.string.cancel_share));
        textViewCancelShare.setTextSize(18);

        textViewCancelShare.setBackgroundResource(R.drawable.selector_popup_top_item);

        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(width, 1);
        TextViewFont textViewDevider = new TextViewFont(context);
        textViewDevider.setLayoutParams(params2);
        textViewDevider.setBackgroundColor(Color.GRAY);


        TextViewFont textViewReport = new TextViewFont(context);
        textViewReport.setGravity(Gravity.CENTER);
        textViewReport.setLayoutParams(params);
        textViewReport.setText(context.getResources().getString(R.string.report));
        textViewReport.setTextSize(context.getResources().getDimension(R.dimen.popup_font));
        textViewReport.setBackgroundResource(R.drawable.selector_popup_bottom_item);

        LinearLayout ll_body = getBody();
        ll_body.addView(textViewCancelShare);
        ll_body.addView(textViewDevider);
        ll_body.addView(textViewReport);

        textViewCancelShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CancelShare(context, LoginInfo.getUserId(context), postId, iUpdateTimeLine).execute();
                dismiss();
            }
        });
        textViewReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Report(context,userId,postId,iWebserviceResponse).execute();
                dismiss();

            }
        });
    }


    @Override
    public void getResult(Object result) {
        iReportPost.notifyReportPost(position,imageViewMore);
    }

    @Override
    public void getError(Integer errorCode) {

    }
}
