package kr.co.bravecompany.modoogong.android.stdapp.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

import com.h6ah4i.android.widget.verticalseekbar.VerticalSeekBar;

/**
 * Created by BraveCompany on 2017. 5. 15..
 */

public class CustomVerticalSeekBar extends VerticalSeekBar implements CustomProgress{

    public CustomVerticalSeekBar(Context context) {
        super(context);
        setPadding(0,0,0,0);
    }

    public CustomVerticalSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPadding(0,0,0,0);
    }

    public CustomVerticalSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setPadding(0,0,0,0);
    }

    @Override
    public void setProgressWithAnimation(int progress, int duration) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(this, "progress", progress);
        objectAnimator.setDuration(duration);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.start();

        if(progress == getMax()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(mListener != null) {
                        mListener.onProgressFinish(CustomVerticalSeekBar.this);
                    }
                }
            }, duration);
        }
    }

    private OnProgressFinishListener mListener;
    @Override
    public void setOnProgressFinish(OnProgressFinishListener listener) {
        mListener = listener;
    }
}
