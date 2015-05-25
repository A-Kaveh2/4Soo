package ir.rasen.charsoo.view.widget_customized.buttons;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class ButtonFont extends RectangleButton {

  public ButtonFont(Context context, AttributeSet attrs){
    super(context, attrs);
    init();
  }

  private void init(){
    if(!isInEditMode()){
        try {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/font.ttf");
            setTypeface(tf);
        } catch (Exception e) {
        }
    }
  }
}