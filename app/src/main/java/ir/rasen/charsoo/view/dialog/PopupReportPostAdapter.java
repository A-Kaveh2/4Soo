package ir.rasen.charsoo.view.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.view.interface_m.IReportPost;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.widgets.TextViewFont;
import ir.rasen.charsoo.model.post.Report;


public class PopupReportPostAdapter extends MyPopup implements IWebserviceResponse {
    Context context;
    private IReportPost iReportPost;
    IWebserviceResponse iWebserviceResponse;
    int reportedItemPosition;
    ImageView reportedItemImageViewMore;

    @SuppressLint("NewApi")
    public PopupReportPostAdapter(final Context context, final int userId, final int postId,  int reportedItemPosition,  ImageView reportedItemImageViewMore, IReportPost iReportPost) {
        super(context);

        this.iWebserviceResponse = this;
        this.context = context;
        this.iReportPost = iReportPost;
        this.reportedItemPosition = reportedItemPosition;
        this.reportedItemImageViewMore = reportedItemImageViewMore;
        int height = getRowHeight();
        int width = getRowWidth();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        TextViewFont textViewUnfriend = new TextViewFont(context);
        textViewUnfriend.setGravity(Gravity.CENTER);
        textViewUnfriend.setLayoutParams(params);
        textViewUnfriend.setText(context.getResources().getString(R.string.report));
        textViewUnfriend.setBackgroundResource(R.drawable.selector_popup_one_item);
        textViewUnfriend.setTextSize(context.getResources().getDimension(R.dimen.popup_font));


        LinearLayout ll_body = getBody();
        ll_body.addView(textViewUnfriend);


        textViewUnfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Report(context,userId,postId,iWebserviceResponse).execute();
                dismiss();

            }
        });

    }


    @Override
    public void getResult(Object result) {
        iReportPost.notifyReportPost(reportedItemPosition,reportedItemImageViewMore);
    }

    @Override
    public void getError(Integer errorCode,String callerStringID) {

    }
}
