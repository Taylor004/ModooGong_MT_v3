package kr.co.bravecompany.modoogong.android.stdapp.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.kollus.sdk.media.util.ErrorCodes;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.config.AnalysisTags;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.download.DownloadManager;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import kr.co.bravecompany.modoogong.android.stdapp.utils.log;

/**
 * Created by BraveCompany on 2016. 12. 5..
 */

public class DownBaseActivity extends BaseActivity{

    protected DownloadManager mDownloadManager;
    private FrameLayout mFab;

    protected boolean isResumed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDownloadManager = new DownloadManager(this);
        mDownloadManager.bindDownloadService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mStartReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mStateReceiver);

        if(mDownloadManager != null) {
            mDownloadManager.unBindDownloadService();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mStartReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mStateReceiver);
        LocalBroadcastManager.getInstance(this).registerReceiver(mStartReceiver, mStartFilter);
        LocalBroadcastManager.getInstance(this).registerReceiver(mStateReceiver, mStateFilter);
        updateDownloadViews();
        isResumed = true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        //LocalBroadcastManager.getInstance(this).unregisterReceiver(mStartReceiver);
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(mStateReceiver);
        isResumed = false;
    }

    private IntentFilter mStartFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_START);

    private BroadcastReceiver mStartReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateFabVisibility();
        }
    };

    private IntentFilter mStateFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_STATE);

    private BroadcastReceiver mStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            log.d("DownBaseActivity mStateReceiver - onReceive isResumed: " + isResumed + " context: " + context);
            String studyKey = intent.getStringExtra(Tags.TAG_STUDY_KEY);
            int state = intent.getIntExtra(Tags.TAG_DOWN_STATE, -1);
            int percent = intent.getIntExtra(Tags.TAG_DOWN_PERCENT, 0);
            int errorCode = intent.getIntExtra(Tags.TAG_ERROR_CODE, -1);
            updateDownloadViews(studyKey, state, percent, errorCode);
        }
    };

    protected void updateDownloadViews(){
        updateFabVisibility();
        updateFabState();
    }

    protected void updateDownloadViews(String studyKey, final int state, int percent, final int errorCode){
        if(studyKey == null){
            return;
        }
        if(isResumed) {
            log.d(String.format("DownBaseActivity updateDownloadViews - studyKey: %s, state: %d, percent: %d, errorCode: %d", studyKey, state, percent, errorCode));
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(state == Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_COMPLETE){
                    updateFabVisibility();
                }
                if(state != Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_ING) {
                    updateFabState();
                }

                if(isResumed && (errorCode == ErrorCodes.ERROR_STORAGE_FULL ||
                        errorCode == ErrorCodes.ERROR_NOT_VAILD_CONTENTS_INFO)){
                    BraveUtils.showToast(DownBaseActivity.this, getString(R.string.toast_download_error_full_storage));
                }
            }
        });
    }

    protected void initLayout(){
        mFab = (FrameLayout)findViewById(R.id.fab);
        if(mFab != null) {
            ImageView imgFabArrow = (ImageView) mFab.findViewById(R.id.imgFabArrow);
            Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_anim);
            imgFabArrow.startAnimation(anim);
        }
    }

    protected void initListener(){
        if(mFab != null) {
            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BraveUtils.goDownload(DownBaseActivity.this);
                    Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.FAB)
                            .putCustomAttribute(AnalysisTags.ACTION, "go_download"));
                }
            });
        }
    }

    protected void updateFabVisibility(){
        if(mDownloadManager != null){
            int count = mDownloadManager.getDownloadingStudyCount();
            if(count != -1){
                showFab(count);
            }else{
                hideFab();
            }
        }
    }

    protected void updateFabState(){
        if(mDownloadManager != null){
            int count = mDownloadManager.getDownloadPauseStudyCount();
            if(count != -1){
                updateFabState(Tags.STUDY_DOWN_FAB_TYPE.STUDY_DOWN_PAUSE);
            }else{
                updateFabState(Tags.STUDY_DOWN_FAB_TYPE.STUDY_DOWN_DOWN);
            }
        }
    }

    public void showFab(int count){
        if(mFab != null) {
            updateFabDownCnt(count);
            if(mFab.getVisibility() != View.VISIBLE) {
                mFab.setVisibility(View.VISIBLE);
            }
        }
    }

    public void hideFab(){
        if(mFab != null && mFab.getVisibility() != View.GONE) {
            mFab.setVisibility(View.GONE);
        }
    }

    public void updateFabDownCnt(int count){
        if(mFab != null){
            TextView txtDownCnt = (TextView)mFab.findViewById(R.id.txtDownCnt);
            txtDownCnt.setText(String.valueOf(count));
        }
    }

    public void updateFabState(int state){
        if(mFab != null) {
            LinearLayout fabDefault = (LinearLayout) mFab.findViewById(R.id.fabDefault);
            ImageView fabPause = (ImageView) mFab.findViewById(R.id.fabPause);
            switch (state) {
                case Tags.STUDY_DOWN_FAB_TYPE.STUDY_DOWN_DOWN:
                    fabDefault.setVisibility(View.VISIBLE);
                    fabPause.setVisibility(View.GONE);
                    break;
                case Tags.STUDY_DOWN_FAB_TYPE.STUDY_DOWN_PAUSE:
                    fabDefault.setVisibility(View.GONE);
                    fabPause.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }
}
