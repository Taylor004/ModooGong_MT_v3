package kr.co.bravecompany.modoogong.android.stdapp.viewholder;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.config.AnalysisTags;
import kr.co.bravecompany.modoogong.android.stdapp.manager.MediaPlayerManager;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import kr.co.bravecompany.modoogong.android.stdapp.utils.ProgressRunnable;
import kr.co.bravecompany.modoogong.android.stdapp.view.CustomProgressBar;
import kr.co.bravecompany.modoogong.android.stdapp.view.OnProgressFinishListener;
import kr.co.bravecompany.modoogong.android.stdapp.data.QADetailAddData;

/**
 * Created by BraveCompany on 2017. 2. 1..
 */

public class QADetailAddVoiceViewHolder extends RecyclerView.ViewHolder{

    protected Context mContext;
    private QADetailAddData mQADetailAddData;
    private String mMp3Path;

    private ImageView btnPlay;
    private CustomProgressBar progressPlay;
    private TextView txtPlay;
    private TextView txtName;

    //player
    private MediaPlayerManager mPlayerManager;

    private OnItemClickListener mListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public QADetailAddVoiceViewHolder(View itemView, MediaPlayerManager playerManager) {
        super(itemView);
        mContext = itemView.getContext();
        mPlayerManager = playerManager;

        btnPlay = (ImageView)itemView.findViewById(R.id.btnPlay);
        progressPlay = (CustomProgressBar)itemView.findViewById(R.id.progressPlay);
        txtPlay = (TextView)itemView.findViewById(R.id.txtPlay);
        txtPlay.setText(BraveUtils.formatTime(0));
        txtName = (TextView)itemView.findViewById(R.id.txtName);

        initListener();
    }

    private void initListener(){
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMp3Path != null) {
                    if (mPlayerManager.checkNowPlay(mMp3Path)) {
                        boolean play = !btnPlay.isActivated();
                        onPlay(play);

                        Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.QADETAIL)
                                .putCustomAttribute(AnalysisTags.ACTION, "do_play_voice_record")
                                .putCustomAttribute(AnalysisTags.VALUE, String.valueOf(play)));
                    } else {
                        if (!mPlayerManager.isStopped()) {
                            mPlayerManager.stopPlaying();
                        }
                        setupPlayer(mMp3Path, true);
                    }
                }
            }
        });

        progressPlay.setOnProgressFinish(new OnProgressFinishListener() {
            @Override
            public void onProgressFinish(View progressBar) {
                resetPlay();
            }
        });
    }

    public void setQADetailAddVoice(QADetailAddData qaDetailAddVoice) {
        if(qaDetailAddVoice != null) {
            mQADetailAddData = qaDetailAddVoice;
            mMp3Path = qaDetailAddVoice.getPath();
            txtName.setText(qaDetailAddVoice.getName());
            if (mMp3Path != null && !mPlayerManager.checkNowPlay(mMp3Path)) {
                if (!mPlayerManager.isStopped()) {
                    mPlayerManager.stopPlaying();
                }
                setupPlayer(mMp3Path, false);
            }
        }
    }

    //// Voice //////////////////////////////////////////////////////////////////////

    private void setupPlayer(String path, final boolean play){
        btnPlay.setEnabled(false);
        txtPlay.setText(mContext.getString(R.string.common_loading));
        if(mPlayerManager.setupPlaying(path)) {
            mPlayerManager.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    updatePlayerView();
                    if(play){
                        onPlay(true);
                    }
                }
            });
        }else{
            BraveUtils.showToast(mContext, String.format(mContext.getString(R.string.toast_player_file_setup_failed),
                    txtName.getText()));

            btnPlay.setEnabled(true);
            progressPlay.setProgress(0);
            txtPlay.setText(BraveUtils.formatTime(0));
        }
    }

    private void updatePlayerView(){
        btnPlay.setEnabled(true);
        btnPlay.setActivated(false);
        progressPlay.setProgress(0);
        progressPlay.setMax(mPlayerManager.getDuration());
        txtPlay.setText(BraveUtils.formatTime(mPlayerManager.getDuration()));

        Handler handler = new Handler();
        mPlayerManager.setupHandler(handler, new ProgressRunnable(
                handler, progressPlay, txtPlay, mPlayerManager.getDuration()));

        mPlayerManager.setupPlayButton(btnPlay);
    }

    private void onPlay(boolean start) {
        if (start) {
            mPlayerManager.startPlaying(progressPlay.getProgress());
        } else {
            mPlayerManager.pausePlaying();
        }
    }

    private void resetPlay(){
        onPlay(false);
        progressPlay.setProgressWithAnimation(0, 10);
        txtPlay.setText(BraveUtils.formatTime(mPlayerManager.getDuration()));
    }
}

