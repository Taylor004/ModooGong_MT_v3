package kr.co.bravecompany.modoogong.android.stdapp.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;


/**
 * Created by BraveCompany on 2016. 11. 1..
 */

public class CustomCircularProgressBar extends CircularProgressBar implements CustomProgress{
    public CustomCircularProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setProgressWithAnimation(float progress, int duration) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "progress", progress);
        objectAnimator.setDuration(duration);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.start();

        if(progress >= 100){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(mListener != null) {
                        mListener.onProgressFinish(CustomCircularProgressBar.this);
                    }
                }
            }, duration);
        }
    }

    @Override
    public void setProgressWithAnimation(int progress, int duration) {
        setProgressWithAnimation((float)progress, duration);
    }

    private OnProgressFinishListener mListener;
    @Override
    public void setOnProgressFinish(OnProgressFinishListener listener) {
        mListener = listener;
    }
}
