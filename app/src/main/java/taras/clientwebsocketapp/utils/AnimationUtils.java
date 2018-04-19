package taras.clientwebsocketapp.utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

public class AnimationUtils {

    public static void rotation(View view){
        RotateAnimation rotate = new RotateAnimation(0,360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(1000);
        rotate.setRepeatCount(Animation.INFINITE);
        rotate.setRepeatMode(Animation.INFINITE);
        rotate.setInterpolator(new LinearInterpolator());
        view.startAnimation(rotate);
        //imageButton.startAnimation(rotate);
    }
}
