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
import ir.rasen.charsoo.view.widget_customized.TextViewFont;
import ir.rasen.charsoo.webservices.post.Report;


public class PopupReportPostActivity extends MyPopup implements  IWebserviceResponse {
    Context context;
    private IReportPost iReportPost;
    IWebserviceResponse iWebserviceResponse;
    int position;
    ImageView imageViewMore;

    @SuppressLint("NewApi")
    public PopupReportPostActivity(final Context context, final int userId, final int postId,int position,ImageView imageViewMore,  final IReportPost iReportPost) {
        super(context);

        iWebserviceResponse = this;
        this.iReportPost = iReportPost;
        this.position = position;
        this.imageViewMore = imageViewMore;

        this.context = context;
        this.iReportPost = iReportPost;
        int height = getRowHeight();
        int width = getRowWidth();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        TextViewFont textViewReport = new TextViewFont(context);
        textViewReport.setGravity(Gravity.CENTER);
        textViewReport.setLayoutParams(params);
        textViewReport.setText(context.getResources().getString(R.string.report));
        textViewReport.setBackgroundResource(R.drawable.selector_popup_one_item);
        textViewReport.setTextSize(context.getResources().getDimension(R.dimen.popup_font));


        LinearLayout ll_body = getBody();
        ll_body.addView(textViewReport);


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
