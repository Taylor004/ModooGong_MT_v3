package kr.co.bravecompany.modoogong.android.stdapp.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.config.AnalysisTags;
import kr.co.bravecompany.modoogong.android.stdapp.config.LocalAddress;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.manager.MediaPlayerManager;
import kr.co.bravecompany.modoogong.android.stdapp.manager.MediaRecorderManager;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import kr.co.bravecompany.modoogong.android.stdapp.utils.ProgressRunnable;
import kr.co.bravecompany.modoogong.android.stdapp.view.CustomCircularProgressBar;
import kr.co.bravecompany.modoogong.android.stdapp.view.OnProgressFinishListener;

public class VoiceRecordActivity extends BaseActivity {

    private ImageView btnClose;
    private TextView txtTime;

    private ImageView imgRecord;
    private ImageView btnDoRecord;
    private ImageView btnStop;
    private ImageView btnPlay;

    private TextView btnFinish;

    private MediaRecorderManager mRecorderManager;
    private MediaPlayerManager mPlayerManager;
    private String mPath = null;

    private CustomCircularProgressBar progressRecord;
    private Animation mAnim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.66f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.activity_voice_record);
        initLayout();
        initListener();
        initData();
    }

    private void initLayout() {

        btnClose = (ImageView)findViewById(R.id.btnClose);
        txtTime = (TextView)findViewById(R.id.txtTime);

        imgRecord = (ImageView)findViewById(R.id.imgRecord);
        btnDoRecord = (ImageView)findViewById(R.id.btnDoRecord);
        btnStop = (ImageView)findViewById(R.id.btnStop);
        btnPlay = (ImageView)findViewById(R.id.btnPlay);

        btnFinish = (TextView)findViewById(R.id.btnFinish);
        btnFinish.setEnabled(false);

        progressRecord = (CustomCircularProgressBar)findViewById(R.id.progressRecord);
        mAnim = AnimationUtils.loadAnimation(VoiceRecordActivity.this, R.anim.progress_anim);

        mRecorderManager = new MediaRecorderManager();
        mPlayerManager = new MediaPlayerManager();

    }

    private void initListener() {

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPath != null){
                    Intent intent = new Intent();
                    intent.putExtra(Tags.TAG_VOICE, mPath);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        btnDoRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecord(true);

                Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.VOICERECORD)
                        .putCustomAttribute(AnalysisTags.ACTION, "do_record_voice"));
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //stop record or play
                if(mRecorderManager.isRecording()){
                    onRecord(false);
                }else{
                    onPlay(false);
                }
            }
        });
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnFinish.isEnabled()) {
                    onPlay(true);
                }else{
                    setupPlayer(mPath, true);
                }
            }
        });

        progressRecord.setOnProgressFinish(new OnProgressFinishListener() {
            @Override
            public void onProgressFinish(View progressBar) {
                if(!mRecorderManager.isRecording()){
                    onPlay(false);
                }
            }
        });
    }

    private void initData(){
    }

    private void updatePlayerView(){
        setRecordBtnState(Tags.VOICE_RECORD_BTN_TYPE.VOICE_RECORD_PLAY);
        progressRecord.setProgress(0);
        txtTime.setText(BraveUtils.formatTime(mPlayerManager.getDuration()));

        Handler handler = new Handler();
        mPlayerManager.setupHandler(handler, new ProgressRunnable(
                handler, progressRecord, txtTime, mPlayerManager.getDuration()));
    }

    private void setupRecorder(){
        String child = LocalAddress.NAME + "_" + System.currentTimeMillis() + ".m4a";
        String path = BraveUtils.getDCIMFilePath(LocalAddress.FOLDER_RECORDER, child);

        if(mRecorderManager.setupRecording(path)){
            mPath = path;

            Handler handler = new Handler();
            mRecorderManager.setupHandler(handler, new ProgressRunnable(
                    handler, progressRecord, txtTime));

            mRecorderManager.setOnInfoListener(new MediaRecorder.OnInfoListener() {
                @Override
                public void onInfo(MediaRecorder mr, int what, int extra) {
                    if(what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED){
                        onRecord(false);
                        BraveUtils.showToast(VoiceRecordActivity.this, getString(R.string.toast_qa_upload_file_max_size));
                    }
                }
            });
        }else{
            Toast.makeText(VoiceRecordActivity.this, getString(R.string.toast_voice_setup_failed),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void setupPlayer(String path, final boolean play){
        startLoading();
        if(mPlayerManager.setupPlaying(path)) {
            mPlayerManager.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    stopLoading();
                    updatePlayerView();
                    if(play){
                        onPlay(true);
                    }
                }
            });
        }else{
            stopLoading();
            Toast.makeText(VoiceRecordActivity.this, getString(R.string.toast_player_setup_failed),
                    Toast.LENGTH_SHORT).show();

            setRecordBtnState(Tags.VOICE_RECORD_BTN_TYPE.VOICE_RECORD_PLAY_RESETUP);
            progressRecord.setProgress(0);
            txtTime.setText(BraveUtils.formatTime(0));
        }
    }

    private void onRecord(boolean start) {
        if (start) {
            setupRecorder();
            if(mPath != null) {
                mRecorderManager.startRecording();

                progressRecord.setProgressWithAnimation(100, 1000);
                progressRecord.startAnimation(mAnim);
                setRecordBtnState(Tags.VOICE_RECORD_BTN_TYPE.VOICE_RECORD_STOP);
            }
        } else {
            mRecorderManager.stopRecording();

            progressRecord.setProgressWithAnimation(0, 1000);
            progressRecord.clearAnimation();

            setupPlayer(mPath, false);
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            mPlayerManager.startPlaying(0);
            setRecordBtnState(Tags.VOICE_RECORD_BTN_TYPE.VOICE_RECORD_STOP);
        } else {
            mPlayerManager.pausePlaying();
            setRecordBtnState(Tags.VOICE_RECORD_BTN_TYPE.VOICE_RECORD_PLAY);
        }
    }

    private void setRecordBtnState(int state){
        switch (state){
            case Tags.VOICE_RECORD_BTN_TYPE.VOICE_RECORD_DO_RECORD:
                btnDoRecord.setVisibility(View.VISIBLE);
                btnStop.setVisibility(View.GONE);
                btnPlay.setVisibility(View.GONE);
                txtTime.setTextColor(getResources().getColor(R.color.color_gray));
                txtTime.setText(getString(R.string.voice_record_title));
                imgRecord.setActivated(false);
                btnFinish.setEnabled(false);
                break;
            case Tags.VOICE_RECORD_BTN_TYPE.VOICE_RECORD_STOP:
                btnDoRecord.setVisibility(View.GONE);
                btnStop.setVisibility(View.VISIBLE);
                btnPlay.setVisibility(View.GONE);
                txtTime.setTextColor(getResources().getColor(R.color.color_red));
                imgRecord.setActivated(true);
                btnFinish.setEnabled(false);
                break;
            case Tags.VOICE_RECORD_BTN_TYPE.VOICE_RECORD_PLAY:
                btnDoRecord.setVisibility(View.GONE);
                btnStop.setVisibility(View.GONE);
                btnPlay.setVisibility(View.VISIBLE);
                txtTime.setTextColor(getResources().getColor(R.color.color_gray));
                imgRecord.setActivated(false);
                btnFinish.setEnabled(true);
                break;
            case Tags.VOICE_RECORD_BTN_TYPE.VOICE_RECORD_PLAY_RESETUP:
                btnDoRecord.setVisibility(View.GONE);
                btnStop.setVisibility(View.GONE);
                btnPlay.setVisibility(View.VISIBLE);
                txtTime.setTextColor(getResources().getColor(R.color.color_gray));
                imgRecord.setActivated(false);
                btnFinish.setEnabled(false);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void finish(){
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mRecorderManager != null)
            mRecorderManager.releaseRecorder();
        if(mPlayerManager != null)
            mPlayerManager.releasePlayer();
    }
}
