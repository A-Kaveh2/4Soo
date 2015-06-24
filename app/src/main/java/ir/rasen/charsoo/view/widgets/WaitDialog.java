package ir.rasen.charsoo.view.widgets;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import ir.rasen.charsoo.R;

public class WaitDialog extends android.app.Dialog {

    private View view;
    private Context context;
    private String message;

    private TextViewFont messageTextView;

    public WaitDialog(Context context) {
        super(context, android.R.style.Theme_Translucent);
        this.context = context;
        this.message = context.getString(R.string.please_wait);
    }

    public WaitDialog(Context context, int messageId) {
        super(context, android.R.style.Theme_Translucent);
        this.context = context;
        this.message = context.getString(messageId);
    }

    public WaitDialog(Context context, String message) {
        super(context, android.R.style.Theme_Translucent);
        this.context = context;
        this.message = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wait);

        view = findViewById(R.id.waitView);
        messageTextView = (TextViewFont) view.findViewById(R.id.waitView_status);
        messageTextView.setText(message);
        view.setOnClickListener(null);
    }

    @Override
    public void show() {
        // TODO 自动生成的方法存根
        super.show();
        // set dialog enter animations
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.dialog_main_show_amination));
    }

    // GETERS & SETTERS

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        if(messageTextView!=null)
            messageTextView.setText(message);
    }

    public TextViewFont getMessageTextView() {
        return messageTextView;
    }

    public void setMessageTextView(TextViewFont messageTextView) {
        this.messageTextView = messageTextView;
    }

    @Override
    public void dismiss() {
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.dialog_main_hide_amination);
        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        WaitDialog.super.dismiss();
                    }
                });

            }
        });

        if(view!=null)
            view.startAnimation(anim);
    }

}
