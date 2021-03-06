package ir.rasen.charsoo.view.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import ir.rasen.charsoo.view.activity.ActivityBusinessRegisterEdit;
import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.view.interface_m.IChangeBusiness;
import ir.rasen.charsoo.view.widgets.TextViewFont;


public class PopupEditDeleteBusiness extends MyPopup {



    @SuppressLint("NewApi")
    public PopupEditDeleteBusiness(final Activity activity, final int businessId,final String businessIdentifier, final IChangeBusiness iChangeBusiness) {
        super(activity);


        int screenHeight = activity.getResources().getDisplayMetrics().heightPixels;
        int screenWidth = activity.getResources().getDisplayMetrics().widthPixels;

        int height = getRowHeight();
        int width = getRowWidth();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        TextViewFont textViewEdit = new TextViewFont(activity);
        textViewEdit.setGravity(Gravity.CENTER);
        textViewEdit.setLayoutParams(params);
        textViewEdit.setText(activity.getResources().getString(R.string.edit_profile));
        textViewEdit.setBackgroundResource(R.drawable.selector_popup_top_item);

        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(width, 1);
        TextViewFont textViewDevider = new TextViewFont(activity);
        textViewDevider.setLayoutParams(params2);
        textViewDevider.setBackgroundColor(Color.GRAY);


        TextViewFont textViewDelete = new TextViewFont(activity);
        textViewDelete.setGravity(Gravity.CENTER);
        textViewDelete.setLayoutParams(params);
        textViewDelete.setText(activity.getResources().getString(R.string.delete));
        textViewDelete.setBackgroundResource(R.drawable.selector_popup_bottom_item);

        LinearLayout ll_body = getBody();
        ll_body.addView(textViewEdit);
        ll_body.addView(textViewDevider);
        ll_body.addView(textViewDelete);

        textViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ActivityBusinessRegisterEdit.class);
                intent.putExtra(Params.BUSINESS_ID_STRING,businessId);
                intent.putExtra(Params.BUSINESS_IDENTIFIER,businessIdentifier);
                activity.startActivityForResult(intent,Params.ACTION_EDIT_BUSINESS);
                dismiss();
            }
        });
        textViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogDeleteBusinessConfirmation d =  new DialogDeleteBusinessConfirmation(activity,businessId, iChangeBusiness);
                d.show();
                dismiss();
            }
        });
    }


}
