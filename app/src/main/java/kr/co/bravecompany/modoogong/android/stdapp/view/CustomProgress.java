package kr.co.bravecompany.modoogong.android.stdapp.view;

/**
 * Created by BraveCompany on 2016. 11. 4..
 */

public interface CustomProgress {
    public void setProgressWithAnimation(int progress, int duration);
    public void setOnProgressFinish(OnProgressFinishListener listener);
}
