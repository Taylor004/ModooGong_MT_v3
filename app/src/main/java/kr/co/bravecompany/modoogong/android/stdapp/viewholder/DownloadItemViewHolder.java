package kr.co.bravecompany.modoogong.android.stdapp.viewholder;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.db.model.Lecture;
import kr.co.bravecompany.modoogong.android.stdapp.db.model.Study;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import kr.co.bravecompany.modoogong.android.stdapp.view.CustomProgressBar;
import kr.co.bravecompany.modoogong.android.stdapp.data.DownloadData;

/**
 * Created by BraveCompany on 2016. 10. 20..
 */

public class DownloadItemViewHolder extends RecyclerView.ViewHolder{
    private Context mContext;
    private DownloadData mDownloadData;

    private FrameLayout layoutItem;

    private TextView txtLectureName;
    private TextView txtStudyName;
    private TextView txtDownState;
    private ImageView btnDownPause;

    private LinearLayout downStateDefault;
    private TextView txtPercent;
    private CustomProgressBar progressPercent;

    private boolean isDeleteMode = false;

    private OnItemClickListener mDeleteModeListener;
    public void setOnDeleteModeItemClickListener(OnItemClickListener listener) {
        mDeleteModeListener = listener;
    }

    private OnItemClickListener mDownErrorListener;
    public void setOnDownErrorItemClickListener(OnItemClickListener listener) {
        mDownErrorListener = listener;
    }

    private OnItemClickListener mDownPauseListener;
    public void setOnDownPauseClickListener(OnItemClickListener listener) {
        mDownPauseListener = listener;
    }

    public DownloadItemViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        layoutItem = (FrameLayout)itemView.findViewById(R.id.layoutItem);

        txtLectureName = (TextView)itemView.findViewById(R.id.txtLectureName);
        txtStudyName = (TextView)itemView.findViewById(R.id.txtStudyName);
        txtDownState = (TextView)itemView.findViewById(R.id.txtDownState);
        btnDownPause = (ImageView)itemView.findViewById(R.id.btnDownPause);

        downStateDefault = (LinearLayout)itemView.findViewById(R.id.downStateDefault);
        txtPercent = (TextView)itemView.findViewById(R.id.txtPercent);
        progressPercent = (CustomProgressBar)itemView.findViewById(R.id.progressPercent);

        downStateDefault.setVisibility(View.INVISIBLE);
        txtDownState.setVisibility(View.INVISIBLE);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDeleteMode) {
                    if (mDeleteModeListener != null) {
                        mDeleteModeListener.onItemClick(v, getLayoutPosition());
                    }
                }else{
                    if (mDownErrorListener != null) {
                        mDownErrorListener.onItemClick(v, getLayoutPosition());
                    }
                }
            }
        });

        btnDownPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDownPauseListener != null) {
                    if(!isDeleteMode) {
                        mDownPauseListener.onItemClick(v, getLayoutPosition());
                    }
                }
            }
        });
    }

    public void setDownloadItem(DownloadData download) {
        if(download != null) {
            mDownloadData = download;

            Lecture lecture = download.getLectureVO();
            Study study = download.getStudyVO();
            txtLectureName.setText(BraveUtils.fromHTMLTitle(lecture.getLectureName()));
            String studyName = String.format(mContext.getString(R.string.download_study_name),
                    BraveUtils.formatOrder(mContext, study.getStudyOrder(), study.getStudySubOrder()),
                    BraveUtils.fromHTMLTitle(study.getStudyName()));
            txtStudyName.setText(studyName);
            setDownImage(download.isPaused());
            setItemSelected(download.isSelected());

            updateDownState(download.getDownState(), download.getDownPercents(), download.getErrorCode());
        }
    }

    public void updateDeleteMode(boolean isDeleteMode){
        this.isDeleteMode = isDeleteMode;
    }

    private void setDownImage(boolean isPaused){
        btnDownPause.setActivated(isPaused);
    }

    private void setItemSelected(boolean selected){
        layoutItem.setSelected(selected);
    }


    public void updateDownState(int state, int percents, int errorCode){
        if(mDownloadData != null){
            mDownloadData.setDownState(state);
            mDownloadData.setErrorCode(errorCode);
            mDownloadData.setDownPercents(percents);
        }
        switch (state){
            case Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_NONE:
                downStateDefault.setVisibility(View.INVISIBLE);
                txtDownState.setVisibility(View.INVISIBLE);
                break;
            case Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_ING:
                downStateDefault.setVisibility(View.VISIBLE);
                txtDownState.setVisibility(View.INVISIBLE);
                txtPercent.setText(String.format(mContext.getString(R.string.download_state_ing),
                        percents));
                progressPercent.setProgress(percents);
                break;
            case Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_API_COMPLETE:
            case Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_PENDING:
                downStateDefault.setVisibility(View.INVISIBLE);
                txtDownState.setVisibility(View.VISIBLE);
                txtDownState.setText(mContext.getString(R.string.download_state_pending));
                txtDownState.setTextColor(mContext.getResources().getColor(R.color.black));
                break;
            case Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_COMPLETE:
                //remove
                break;
            case Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_PAUSE:
                downStateDefault.setVisibility(View.INVISIBLE);
                txtDownState.setVisibility(View.VISIBLE);
                txtDownState.setText(String.format(mContext.getString(R.string.download_state_pause),
                        percents));
                txtDownState.setTextColor(mContext.getResources().getColor(R.color.color_red));
                break;
            case Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_ERROR:
            case Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_API_ERROR:
                downStateDefault.setVisibility(View.INVISIBLE);
                txtDownState.setVisibility(View.VISIBLE);
                txtDownState.setText(mContext.getString(R.string.download_state_error));
                txtDownState.setTextColor(mContext.getResources().getColor(R.color.color_red));
                if(mDownloadData != null) {
                    //update pause button
                    mDownloadData.setPaused(true);
                    setDownImage(mDownloadData.isPaused());
                }
                break;
            case Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_API_ING:
                downStateDefault.setVisibility(View.INVISIBLE);
                txtDownState.setVisibility(View.VISIBLE);
                txtDownState.setText(mContext.getString(R.string.download_state_prepare));
                txtDownState.setTextColor(mContext.getResources().getColor(R.color.black));
                break;
            /*
            case Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_API_COMPLETE:
                downStateDefault.setVisibility(View.INVISIBLE);
                txtDownState.setVisibility(View.VISIBLE);
                txtDownState.setText(mContext.getString(R.string.download_state_prepare_complete));
                txtDownState.setTextColor(mContext.getResources().getColor(R.color.black));
                */
        }
    }

}
