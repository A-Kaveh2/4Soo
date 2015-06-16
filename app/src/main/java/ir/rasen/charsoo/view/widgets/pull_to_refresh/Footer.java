package ir.rasen.charsoo.view.widgets.pull_to_refresh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import ir.rasen.charsoo.R;


/**
 * Created by A.Kaveh on 5/6/2015.
 */
public class Footer {

    View footerView;

    public  Footer(Context context) {
        footerView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_loading_more_footer, null, false);
        footerView.setVisibility(View.GONE);

        Interpolator ANIMATION_INTERPOLATOR = new LinearInterpolator();
        int ROTATION_ANIMATION_DURATION = 1200;

        RotateAnimation anim = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        anim.setInterpolator(ANIMATION_INTERPOLATOR);
        anim.setDuration(ROTATION_ANIMATION_DURATION);
        anim.setRepeatCount(Animation.INFINITE);
        anim.setRepeatMode(Animation.RESTART);

        // Start animating the image
        ImageView mHeaderImage = (ImageView) footerView.findViewById(R.id.pull_to_refresh_image);
        mHeaderImage.startAnimation(anim);
    }

    public View getFooterView(){
        return footerView;
    }

    public void setVisibility(int visibility){
        footerView.setVisibility(visibility);
    }


}
