package ir.rasen.charsoo.animation;

import android.view.animation.Animation;
import android.view.animation.AnimationSet;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by kh.bakhtiari on 9/20/2014.
 */
public class AnimSet {
    private Set<Anim> animations;

    public AnimSet() {
        animations = new HashSet<Anim>();
    }

    public void addAnimation(Anim animation) {
        animations.add(animation);
    }

    public void startAnimation() {
        if (Anim.isAnimationNew()) {
            for (Anim animation : animations)
                animation.start();
        } else {
            AnimationSet animationSet = new AnimationSet(false);
            for (Anim animation : animations)
                animationSet.addAnimation((Animation) animation.animation);
            animationSet.start();
        }
    }
}