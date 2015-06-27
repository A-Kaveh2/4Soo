package ir.rasen.charsoo.view.widgets.material_library.widgets;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.view.widgets.TextViewFont;
import ir.rasen.charsoo.view.widgets.buttons.FlatButton;

public class Dialog extends android.app.Dialog{
	
	Context context;
	View view;
	View backView;
	String message;
	TextViewFont messageTextView;
	String title;
	TextViewFont titleTextView;
	
	FlatButton buttonAccept;
	FlatButton buttonCancel;

	String buttonAcceptText;
	String buttonCancelText;
	
	View.OnClickListener onAcceptButtonClickListener;
	View.OnClickListener onCancelButtonClickListener;
	
	
	public Dialog(Context context,String title, String message) {
		super(context, android.R.style.Theme_Translucent);
		this.context = context;// init Context
		this.message = message;
		this.title = title;
	}

	public void setAcceptText(String acceptText) {
		this.buttonAcceptText = acceptText;
		if(buttonAccept!=null)
			this.buttonAccept.setText(acceptText);
	}

	public void addCancelButton(String buttonCancelText){
		this.buttonCancelText = buttonCancelText;
	}
	
	public void addCancelButton(String buttonCancelText, View.OnClickListener onCancelButtonClickListener){
		this.buttonCancelText = buttonCancelText;
		this.onCancelButtonClickListener = onCancelButtonClickListener;
	}

	@Override
	  protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.dialog);
	    
		view = findViewById(R.id.contentDialog);
		backView = findViewById(R.id.dialog_rootView);
		backView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getX() < view.getLeft() 
						|| event.getX() >view.getRight()
						|| event.getY() > view.getBottom() 
						|| event.getY() < view.getTop()) {
					dismiss();
				}
				return false;
			}
		});
		
	    this.titleTextView = (TextViewFont) findViewById(R.id.title);
	    setTitle(title);

	    this.messageTextView = (TextViewFont) findViewById(R.id.message);
	    setMessage(message);

	    this.buttonAccept = (FlatButton) findViewById(R.id.button_accept);
		if(buttonAcceptText!=null)
			buttonAccept.setText(buttonAcceptText);
		buttonAccept.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				if (onAcceptButtonClickListener != null)
					onAcceptButtonClickListener.onClick(v);
			}
		});
	    
	    if(buttonCancelText != null){
		    this.buttonCancel = (FlatButton) findViewById(R.id.button_cancel);
		    this.buttonCancel.setVisibility(View.VISIBLE);
		    this.buttonCancel.setText(buttonCancelText);
	    	buttonCancel.setOnClickListener(new View.OnClickListener() {
	    		
				@Override
				public void onClick(View v) {
					dismiss();	
					if(onCancelButtonClickListener != null)
				    	onCancelButtonClickListener.onClick(v);
				}
			});
	    }
	}
	
	@Override
	public void show() {
		// TODO 自动生成的方法存根
		super.show();
		// set dialog enter animations
		view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.dialog_main_show_amination));
		backView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.dialog_root_show_amin));
	}
	
	// GETERS & SETTERS

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
		messageTextView.setText(message);
	}

	public TextViewFont getMessageTextView() {
		return messageTextView;
	}

	public void setMessageTextView(TextViewFont messageTextView) {
		this.messageTextView = messageTextView;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		if(title == null)
			titleTextView.setVisibility(View.GONE);
		else{
			titleTextView.setVisibility(View.VISIBLE);
			titleTextView.setText(title);
		}
	}

	public TextView getTitleTextView() {
		return titleTextView;
	}

	public void setTitleTextView(TextViewFont titleTextView) {
		this.titleTextView = titleTextView;
	}

	public FlatButton getButtonAccept() {
		return buttonAccept;
	}

	public void setButtonAccept(FlatButton buttonAccept) {
		this.buttonAccept = buttonAccept;
	}

	public FlatButton getButtonCancel() {
		return buttonCancel;
	}

	public void setButtonCancel(FlatButton buttonCancel) {
		this.buttonCancel = buttonCancel;
	}

	public void setOnAcceptButtonClickListener(
			View.OnClickListener onAcceptButtonClickListener) {
		this.onAcceptButtonClickListener = onAcceptButtonClickListener;
		if(buttonAccept != null)
			buttonAccept.setOnClickListener(onAcceptButtonClickListener);
	}

	public void setOnCancelButtonClickListener(
			View.OnClickListener onCancelButtonClickListener) {
		this.onCancelButtonClickListener = onCancelButtonClickListener;
		if(buttonCancel != null)
			buttonCancel.setOnClickListener(onCancelButtonClickListener);
	}
	
	@Override
	public void dismiss() {
		Animation anim = AnimationUtils.loadAnimation(context, R.anim.dialog_main_hide_amination);
		anim.setAnimationListener(new AnimationListener() {
			
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
			        	Dialog.super.dismiss();
			        }
			    });
				
			}
		});
		Animation backAnim = AnimationUtils.loadAnimation(context, R.anim.dialog_root_hide_amin);
		
		view.startAnimation(anim);
		backView.startAnimation(backAnim);
	}
	
	

}
