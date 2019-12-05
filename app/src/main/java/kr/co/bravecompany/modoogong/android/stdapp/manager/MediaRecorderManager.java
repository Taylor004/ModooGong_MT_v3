package kr.co.bravecompany.modoogong.android.stdapp.manager;

import android.media.MediaRecorder;
import android.os.Handler;

import java.io.IOException;

import kr.co.bravecompany.modoogong.android.stdapp.utils.ProgressRunnable;
import kr.co.bravecompany.modoogong.android.stdapp.utils.log;

/**
 * Created by BraveCompany on 2016. 11. 3..
 */

public class MediaRecorderManager {
    private static final long MAX_SIZE = 3000000;

    private MediaRecorder mRecorder = null;

    private final int RECORDER_START = 0;
    private final int RECORDER_STOP = 1;

    private int mState = RECORDER_STOP;

    private String mPath = null;
    private ProgressRunnable mProgressRunnable;
    private Handler mHandler;

    public void createMediaRecorder(){
        mRecorder = new MediaRecorder();
    }

    public void setupHandler(Handler handler, ProgressRunnable runnable){
        mProgressRunnable = runnable;
        mHandler = handler;
    }

    public boolean setupRecording(String path){
        if(mRecorder == null){
            createMediaRecorder();
        }
        try {
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mRecorder.setOutputFile(path);
            mRecorder.setMaxFileSize(MAX_SIZE);
            mRecorder.setOnInfoListener(mInfoListener);
            mRecorder.prepare();
        } catch (IOException e) {
            log.e("MediaRecorder prepare() failed");
            return false;
        } catch (IllegalStateException ie){
            log.e("IllegalStateException - MediaRecorder prepare() failed");
            return false;
        }
        mPath = path;
        return true;
    }

    public void startRecording() {
        if(mRecorder == null)
            return;

        mRecorder.start();

        if (mHandler != null && mProgressRunnable != null) {
            mHandler.post(mProgressRunnable);
        }
        mState = RECORDER_START;
    }

    public void stopRecording() {
        if(mRecorder == null)
            return;

        mRecorder.stop();
        mRecorder.reset();

        if (mHandler != null && mProgressRunnable != null) {
            mHandler.removeCallbacks(mProgressRunnable);
            mProgressRunnable = null;
            mHandler = null;
        }
        mState = RECORDER_STOP;

    }

    public boolean isRecording(){
        return mState == RECORDER_START;
    }

    public void releaseRecorder(){
        if(mRecorder == null)
            return;

        if (isRecording()) {
            mRecorder.stop();
        }
        mRecorder.release();
        mRecorder = null;

        if (mHandler != null && mProgressRunnable != null) {
            mHandler.removeCallbacks(mProgressRunnable);
            mProgressRunnable = null;
            mHandler = null;
        }
    }

    private MediaRecorder.OnInfoListener mOnInfoListener;
    public void setOnInfoListener(MediaRecorder.OnInfoListener listener) {
        mOnInfoListener = listener;
    }

    MediaRecorder.OnInfoListener mInfoListener = new MediaRecorder.OnInfoListener() {
        @Override
        public void onInfo(MediaRecorder mr, int what, int extra) {
            if(what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED){
                if(mOnInfoListener != null) {
                    mOnInfoListener.onInfo(mr, what, extra);
                }
            }
        }
    };
}
