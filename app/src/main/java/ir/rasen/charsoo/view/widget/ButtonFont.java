package ir.rasen.charsoo.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class ButtonFont extends Button {
  public ButtonFont(Context context, AttributeSet attrs, int defStyle){
    super(context,attrs,defStyle);
    init();
  }
  public ButtonFont(Context context, AttributeSet attrs){
    super(context,attrs);
    init();
  }
  public ButtonFont(Context context){
    super(context);
    init();
  }

  private void init(){
    if(!isInEditMode()){
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/font.ttf");
        setTypeface(tf);
    }
  }
}