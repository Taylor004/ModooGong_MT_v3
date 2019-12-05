package kr.co.bravecompany.modoogong.android.stdapp.utils;

import android.os.Handler;
import android.widget.TextView;

import kr.co.bravecompany.modoogong.android.stdapp.view.CustomCircularProgressBar;
import kr.co.bravecompany.modoogong.android.stdapp.view.CustomProgress;

/**
 * Created by BraveCompany on 2016. 11. 4..
 */

public class ProgressRunnable implements Runnable{
    public static final int TIME_SEC = 1000;

    private Handler handler;
    private CustomProgress progressBar;
    private TextView textView;
    private int max = -1;
    int milliseconds = 0;

    public ProgressRunnable(Handler handler, CustomProgress progressbar, TextView textView) {
        super();
        this.handler = handler;
        this.progressBar = progressbar;
        this.textView = textView;
    }

    public ProgressRunnable(Handler handler, CustomProgress progressbar, TextView textView, int max) {
        super();
        this.handler = handler;
        this.progressBar = progressbar;
        this.textView = textView;
        this.max = max;
    }

    @Override
    public void run() {
        if(max != -1){
            if(milliseconds < max){
                updateProgress(milliseconds, TIME_SEC);
                milliseconds = milliseconds + TIME_SEC;
                handler.postDelayed(this, TIME_SEC);
            }else{
                updateProgress(max, milliseconds-max);
                return;
            }
        }else{
            updateProgress(milliseconds, TIME_SEC);
            milliseconds = milliseconds + TIME_SEC;
            handler.postDelayed(this, TIME_SEC);
        }
    }

    private void updateProgress(int milliseconds, int duration){
        textView.setText(BraveUtils.formatTime(milliseconds));
        if(max != -1){
            if(progressBar instanceof CustomCircularProgressBar){
                float ratio = (float)milliseconds / (float)max * 100f;
                ((CustomCircularProgressBar)progressBar).setProgressWithAnimation(ratio, duration);
            }else{
                progressBar.setProgressWithAnimation(milliseconds, duration);
            }
        }
    }

    public void seekTo(int start){
        this.milliseconds = start;
    }
}
