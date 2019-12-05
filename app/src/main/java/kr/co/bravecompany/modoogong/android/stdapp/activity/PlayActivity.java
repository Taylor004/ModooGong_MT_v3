package kr.co.bravecompany.modoogong.android.stdapp.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.config.AnalysisTags;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import kr.co.bravecompany.player.android.stdapp.video.VideoPlayer;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BackPressedHandler;
import kr.co.bravecompany.modoogong.android.stdapp.utils.log;

/**
 * Kollus Player Activity
 */

public class PlayActivity extends BaseActivity {
    private static final String TAG = PlayActivity.class.getSimpleName();

    private View rootView;
    private ImageView btnClose;

    private VideoPlayer mVideoPlayer;

    private static final int SCROLL_MODE_N = 0;
    private static final int SCROLL_MODE_V = 1;
    private static final int SCROLL_MODE_H = 2;

    private static final int SCROLL_SEEK_MOUNT = 90;
    private int mScrollMode = SCROLL_MODE_N;
    private int mSeekControlDistance = 0;

    private int mVolumeControlDistance = 0;
    private int mBrightnessControlDistance = 0;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(mVideoPlayer != null){
            mVideoPlayer.setVisibilityTimeView(newConfig.orientation);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);

        initLayout();
        initListener();
        handleIntent(getIntent());
    }

    private void initLayout() {
        rootView = findViewById(R.id.rootView);
        btnClose = (ImageView)findViewById(R.id.btnClose);
    }

    private void initListener() {
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mGestureDetector = new GestureDetector(this, new SimpleGestureListener());
    }

    @Override
    protected void onResume() {
        if(mVideoPlayer != null){
            mVideoPlayer.onResume();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if(mVideoPlayer != null) {
            mVideoPlayer.onPause();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        log.w("onDestroy");
        if(mVideoPlayer != null) {
            mVideoPlayer.onDestroy();
        }
        super.onDestroy();
    }
/*
    @Override
    protected void onUserLeaveHint() {
        log.w("onUserLeaveHint");
        if(mVideoPlayer != null) {
            mVideoPlayer.onUserLeaveHint();
        }
        super.onUserLeaveHint();
    }
*/
    private void handleIntent(Intent intent) {
        String title = intent.getStringExtra(Tags.TAG_PLAY);
        Uri uri = intent.getData();
        String mediaContentKey = intent.getStringExtra(Tags.TAG_MEDIA_CONTENT_KEY);


        //String testURL= "http://v.kr.kollus.com/si?jwt=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJtYyI6W3sibWNrZXkiOiI0cEk5eW9VTCIsIm1jcGYiOm51bGx9XSwiY3VpZCI6IjEwOjA4OjQzNzY3OTMyIiwiZXhwdCI6MTU2NzY1NTY5MX0.erVrC66XqoX8bfW6UnFkePDwcG82ElTHdzxDHvSQoAY;custom_key=7a2d7d2e1ec35645a5411b863df1466a01796d12efe607c54eb30bd514446589;autoplay;uservalue0=1701;uservalue1=10228;uservalue2=10:08:437679;uservalue3=CHRSTD;uservalue4=281447;uservalue5=988025;uservalue6=1994411;uservalue7=A;uservalue8=14.36.251.74;uservalue9=bs_mdgong”;
        //uri = Uri.parse("http://hello.com");

       // uri = Uri.parse("http://v.kr.kollus.com/i/Go7BwZcJ");
       // mediaContentKey = "VAkxI6EE";

        mVideoPlayer = new VideoPlayer(PlayActivity.this, rootView, title, uri, mediaContentKey);

        //for test!!
        //mVideoPlayer = new VideoPlayer(PlayActivity.this, rootView, title, "http://hello", "");

        onConfigurationChanged(getResources().getConfiguration());


        ContentViewEvent event = new ContentViewEvent()
                .putCustomAttribute(AnalysisTags.ACTION, "play");
        if(title != null){
            event.putContentName(title);
        }
        if(uri != null){
            event.putContentType("streaming");
        }else if(mediaContentKey != null){
            event.putContentType("downloaded");
        }
        Answers.getInstance().logContentView(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(mVideoPlayer == null){
            return super.onKeyDown(keyCode, event);
        }else{
            return mVideoPlayer.onKeyDown(keyCode, event)
                    || super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getPointerCount() > 1){
            //TODO: Scale Gesture
            return super.onTouchEvent(event);
        }else{
            if(event.getAction() == MotionEvent.ACTION_UP) {
                if(mScrollMode == SCROLL_MODE_H){
                    if(mVideoPlayer != null) {
                        mVideoPlayer.setSeekLabel(mSeekControlDistance * 1000, false);
                    }
                    mSeekControlDistance = 0;
                }
                mScrollMode = SCROLL_MODE_N;
            }
            return mGestureDetector.onTouchEvent(event);
        }
    }

    private GestureDetector mGestureDetector;
    private class SimpleGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if(mVideoPlayer != null) {
                mVideoPlayer.toggleControlView();
            }
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            if(mVideoPlayer != null) {
                if(mVideoPlayer.canMoveVideoScreen()) {
                    mVideoPlayer.moveVideoFrame(-distanceX, -distanceY);
                }else {
                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                    int volumeChangeX  = displayMetrics.widthPixels/2;
                    float moveX = Math.abs(e1.getX() - e2.getX());
                    float moveY = Math.abs(e1.getY() - e2.getY());

                    if(mScrollMode == SCROLL_MODE_N) {
                        if(moveX > moveY) {
                            mScrollMode = SCROLL_MODE_H;
                        }else {
                            mScrollMode = SCROLL_MODE_V;
                        }
                    }

                    if(mScrollMode == SCROLL_MODE_V) {
                        //Volume Control
                        if(e1.getX() > volumeChangeX) {
                            setVolumeControl(distanceY);
                        }
                        //Brightness Control
                        else{
                            setBrightnessControl(distanceY);
                        }
                    } else if(mScrollMode == SCROLL_MODE_H){
                        if(e2.getAction() == MotionEvent.ACTION_MOVE) {
                            mSeekControlDistance = (int)((e2.getX() - e1.getX()) * SCROLL_SEEK_MOUNT / displayMetrics.widthPixels);
                            mVideoPlayer.setSeekLabel(mSeekControlDistance * 1000, true);
                        }
                    }
                }
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return super.onDoubleTap(e);
        }
    }

    private void setVolumeControl(float distanceY) {
        if((mVolumeControlDistance > 0 && distanceY < 0) ||
                (mVolumeControlDistance < 0 && distanceY > 0)) {
            mVolumeControlDistance = 0;
        }

        mVolumeControlDistance += distanceY;
        if(Math.abs(mVolumeControlDistance) > 30) {
            mVideoPlayer.setVolumeControl((distanceY >= 0));
            mVolumeControlDistance = 0;
        }
    }

    private void setBrightnessControl(float distanceY) {
        if((mBrightnessControlDistance > 0 && distanceY < 0) ||
                (mBrightnessControlDistance < 0 && distanceY > 0))
            mBrightnessControlDistance = 0;

        mBrightnessControlDistance += distanceY;
        if(Math.abs(mBrightnessControlDistance) > 45) {
            mVideoPlayer.setBrightnessControl((distanceY >= 0));
            mBrightnessControlDistance = 0;
        }
    }

    @Override
    public void onBackPressed() {
        if(!(mVideoPlayer != null && mVideoPlayer.isLoading())) {
            BackPressedHandler.onBackPressed(PlayActivity.this, getString(R.string.toast_kollus_guide_finish));
        }
    }
}
