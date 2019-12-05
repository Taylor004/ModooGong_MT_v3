package kr.co.bravecompany.modoogong.android.stdapp.manager;

import android.media.MediaPlayer;
import android.os.Handler;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import kr.co.bravecompany.modoogong.android.stdapp.utils.ProgressRunnable;
import kr.co.bravecompany.modoogong.android.stdapp.utils.log;

/**
 * Created by BraveCompany on 2016. 11. 3..
 */

public class MediaPlayerManager {

    private MediaPlayer mPlayer = null;

    public static final int PLAYER_STATE_STOP = 0;
    public static final int PLAYER_STATE_START = 1;
    public static final int PLAYER_STATE_PAUSE = 2;

    private int mState = PLAYER_STATE_STOP;

    private String mPath = null;
    private ProgressRunnable mProgressRunnable;
    private Handler mHandler;

    private ImageView btnPlay;


    public void createMediaPlayer(){
        mPlayer = new MediaPlayer();
    }

    public void setupHandler(Handler handler, ProgressRunnable runnable){
        mProgressRunnable = runnable;
        mHandler = handler;
    }

    public void setupPlayButton(ImageView btnPlay){
        this.btnPlay = btnPlay;
    }

    public void setOnPreparedListener(MediaPlayer.OnPreparedListener listener){
        mPlayer.setOnPreparedListener(listener);
    }

    public boolean setupPlaying(String path){
        if(mPlayer == null){
            createMediaPlayer();
        }
        if(!BraveUtils.checkURLFormat(path)){
            File file = new File(path);
            if(!file.exists()){
                return false;
            }
        }
        try {
            mPlayer.setDataSource(path);
            mPlayer.prepareAsync();
        } catch (IOException e) {
            log.e("MediaPlayer prepare() failed");
            return false;
        } catch (IllegalStateException ie) {
            log.e("IllegalStateException - MediaPlayer prepare() failed");
            return false;
        } catch (Exception e){
            log.e("Error: " + e.getMessage());
            return false;
        }
        mPath = path;
        return true;
    }

    public void startPlaying(int start) {
        if(mPlayer == null)
            return;

        mPlayer.seekTo(start);
        mPlayer.start();

        if (mHandler != null && mProgressRunnable != null) {
            mProgressRunnable.seekTo(start);
            mHandler.post(mProgressRunnable);
        }

        if(btnPlay != null){
            btnPlay.setActivated(true);
        }

        mState = PLAYER_STATE_START;
    }

    public void pausePlaying(){
        if(mPlayer == null)
            return;

        mPlayer.pause();

        if (mHandler != null && mProgressRunnable != null) {
            mHandler.removeCallbacks(mProgressRunnable);
        }

        if(btnPlay != null){
            btnPlay.setActivated(false);
        }

        mState = PLAYER_STATE_PAUSE;
    }

    public void stopPlaying() {
        if(mPlayer == null)
            return;

        mPlayer.stop();
        mPlayer.reset();

        if (mHandler != null && mProgressRunnable != null) {
            mHandler.removeCallbacks(mProgressRunnable);
            mProgressRunnable = null;
            mHandler = null;
        }

        if(btnPlay != null){
            btnPlay.setActivated(false);
        }

        mState = PLAYER_STATE_STOP;
    }

    public void setSeekTo(int start){
        if(mPlayer == null)
            return;

        mPlayer.seekTo(start);
    }

    public int getDuration(){
        if(mPlayer == null)
            return -1;

        return mPlayer.getDuration();
    }

    public boolean isPlaying(){
        if(mPlayer == null)
            return false;

        return mPlayer.isPlaying();
    }

    public boolean isStopped(){
        return mState == PLAYER_STATE_STOP;
    }

    public boolean checkNowPlay(String play){
        return (mPath != null && mPath.equals(play));
    }

    public void releasePlayer(){
        if(mPlayer == null)
            return;

        if (mPlayer.isPlaying()) {
            mPlayer.stop();
        }
        mPlayer.release();
        mPlayer = null;

        if (mHandler != null && mProgressRunnable != null) {
            mHandler.removeCallbacks(mProgressRunnable);
            mProgressRunnable = null;
            mHandler = null;
        }

        if(btnPlay != null){
            btnPlay = null;
        }
    }

    public interface OnStateChangeListener {
        public void onStateChange(int state);
    }

    private OnStateChangeListener mStateChangeListener;
    public void setOnStateChangeListener(OnStateChangeListener listener) {
        mStateChangeListener = listener;
    }

}
