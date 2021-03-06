package ir.rasen.charsoo.view.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.view.interface_m.IUpdateTimeLine;
import ir.rasen.charsoo.view.widgets.TextViewFont;
import ir.rasen.charsoo.model.post.CancelShare;


public class PopupCancelSharePost extends MyPopup {
    Context context;



    @SuppressLint("NewApi")
    public PopupCancelSharePost(final Context context, final int userId, final int postId,final IUpdateTimeLine iUpdateTimeLine) {
        super(context);

        this.context = context;

        int height = getRowHeight();
        int width = getRowWidth();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        TextViewFont textViewReport = new TextViewFont(context);
        textViewReport.setGravity(Gravity.CENTER);
        textViewReport.setLayoutParams(params);
        textViewReport.setText(context.getResources().getString(R.string.cancel_share));
        textViewReport.setBackgroundResource(R.drawable.selector_popup_one_item);

        LinearLayout ll_body = getBody();
        ll_body.addView(textViewReport);


        textViewReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CancelShare(context, userId, postId, iUpdateTimeLine).execute();
                dismiss();
            }
        });

    }




}
