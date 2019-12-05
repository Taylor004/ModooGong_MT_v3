package kr.co.bravecompany.modoogong.android.stdapp.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.view.ProgressBarDialog;

/**
 * Created by BraveCompany on 2016. 10. 12..
 */

public abstract class BaseActivity extends AppCompatActivity {

    private ProgressBarDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mProgressDialog = ProgressBarDialog.getInstance(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLoading();
    }

    public void startLoading(){
        if(mProgressDialog != null && !mProgressDialog.isShowing() && !isFinishing()) {
            mProgressDialog.show();
        }
    }

    public void stopLoading(){
        if(mProgressDialog != null && mProgressDialog.isShowing() && !isFinishing()) {
            mProgressDialog.dismiss();
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Abstract Method In Activity
    ///////////////////////////////////////////////////////////////////////////

    protected void setSystemBar(boolean visible) {

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    protected void setDialogWindow()
    {
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.66f;
        getWindow().setAttributes(lpWindow);
    }
}
