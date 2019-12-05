package kr.co.bravecompany.modoogong.android.stdapp.pageStudy;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import kr.co.bravecompany.api.android.stdapp.api.data.StudyItemVO;
import kr.co.bravecompany.modoogong.android.stdapp.data.StudyData;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.OnItemClickListener;

/**
 * Created by BraveCompany on 2016. 10. 18..
 */

public class StudyItemViewHolder extends RecyclerView.ViewHolder{
    private Context mContext;
    private StudyData mStudyData;

    private FrameLayout layoutItem;

    private TextView txtStudyOrder;
    private TextView txtStudyTime;
    private TextView txtStudyName;
    private LinearLayout layoutState;
    private TextView txtPlayed;
    private TextView txtDownState;

    private boolean isDownloadMode = false;

    private OnItemClickListener mListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    private OnItemClickListener mDownloadModeListener;
    public void setOnDownloadModeItemClickListener(OnItemClickListener listener) {
        mDownloadModeListener = listener;
    }

    public StudyItemViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        
        layoutItem = (FrameLayout)itemView.findViewById(R.id.layoutItem);

        txtStudyOrder = (TextView)itemView.findViewById(R.id.txtStudyOrder);
        txtStudyTime = (TextView)itemView.findViewById(R.id.txtStudyTime);
        txtStudyName = (TextView)itemView.findViewById(R.id.txtStudyName);
        layoutState = (LinearLayout)itemView.findViewById(R.id.layoutState);
        txtPlayed = (TextView)itemView.findViewById(R.id.txtPlayed);
        txtDownState = (TextView)itemView.findViewById(R.id.txtDownState);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isDownloadMode) {
                    if (mListener != null) {
                        mListener.onItemClick(v, getLayoutPosition() - 2);
                    }
                }else{
                    if (mDownloadModeListener != null) {
                        mDownloadModeListener.onItemClick(v, getLayoutPosition() - 2);
                    }
                }
            }
        });
    }

    public void setStudyItem(StudyData study) {
        if(study != null) {
            mStudyData = study;
            StudyItemVO studyItem = study.getStudyItemVO();

            txtStudyOrder.setText(BraveUtils.formatOrder(mContext,
                    studyItem.getStudyOrder(), studyItem.getStudySubOrder()));
            if (studyItem.getRecentStudyDate() != null && studyItem.getRecentStudyDate().length() != 0) {
                txtStudyOrder.setActivated(true);
            } else {
                txtStudyOrder.setActivated(false);
            }
            txtStudyTime.setText(String.format(mContext.getString(R.string.study_time),
                    studyItem.getStudyTime()));
            String studyName = BraveUtils.fromHTMLTitle(studyItem.getStudyName());
            txtStudyName.setText(studyName);

            updatePlayed(study.isPlayed());
            setItemSelected(study.isSelected());

            updateDownState(study.getDownState(), study.getDownPercents());
        }
    }

    public void updateDownloadingMode(boolean isDownloadMode){
        this.isDownloadMode = isDownloadMode;
    }

    private void setItemSelected(boolean selected){
        layoutItem.setSelected(selected);
    }

    public void updatePlayed(boolean isPlayed) {
        showPlayed(isPlayed);
    }

    public void updateDownState(int state, int percents){
        if(mStudyData != null){
            mStudyData.setDownState(state);
            mStudyData.setDownPercents(percents);
        }
        switch (state){
            case Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_NONE:
                showDownState(false);
                break;
            case Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_ING:
                showDownState(true);
                txtDownState.setText(String.format(mContext.getString(R.string.study_down_state_ing),
                        percents));
                break;
            case Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_API_COMPLETE:
            case Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_PENDING:
                showDownState(true);
                txtDownState.setText(mContext.getString(R.string.study_down_state_pending));
                break;
            case Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_COMPLETE:
                showDownState(true);
                txtDownState.setText(mContext.getString(R.string.study_down_state_complete));
                break;
            case Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_PAUSE:
                showDownState(true);
                txtDownState.setText(String.format(mContext.getString(R.string.study_down_state_pause),
                        percents));
                break;
            case Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_ERROR:
            case Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_API_ERROR:
                showDownState(true);
                txtDownState.setText(mContext.getString(R.string.study_down_state_error));
                break;
            case Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_API_ING:
                showDownState(true);
                txtDownState.setText(mContext.getString(R.string.study_down_state_prepare));
                break;
            /*
            case Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_API_COMPLETE:
                txtDownState.setVisibility(View.VISIBLE);
                txtDownState.setText(mContext.getString(R.string.download_state_prepare_complete));
                break;
                */
        }
    }

    private void showPlayed(boolean show){
        if(show){
            if(layoutState.getVisibility() == View.GONE){
                layoutState.setVisibility(View.VISIBLE);
            }
            txtPlayed.setVisibility(View.VISIBLE);
        }else{
            if(txtDownState.getVisibility() == View.INVISIBLE){
                layoutState.setVisibility(View.GONE);
            }
            txtPlayed.setVisibility(View.INVISIBLE);
        }
    }

    private void showDownState(boolean show){
        if(show){
            if(layoutState.getVisibility() == View.GONE){
                layoutState.setVisibility(View.VISIBLE);
            }
            txtDownState.setVisibility(View.VISIBLE);
        }else{
            if(txtPlayed.getVisibility() == View.INVISIBLE){
                layoutState.setVisibility(View.GONE);
            }
            txtDownState.setVisibility(View.INVISIBLE);
        }
    }

}
