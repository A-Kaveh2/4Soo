package ir.rasen.charsoo.view.widgets.buttons;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class ButtonFontStandard extends Button {

  public ButtonFontStandard(Context context, AttributeSet attrs){
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