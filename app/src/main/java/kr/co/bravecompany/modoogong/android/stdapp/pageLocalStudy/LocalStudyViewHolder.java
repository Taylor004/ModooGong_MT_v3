package kr.co.bravecompany.modoogong.android.stdapp.pageLocalStudy;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.db.model.Study;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import kr.co.bravecompany.modoogong.android.stdapp.data.LocalStudyData;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.OnItemClickListener;

/**
 * Created by BraveCompany on 2016. 10. 24..
 */

public class LocalStudyViewHolder extends RecyclerView.ViewHolder{
    private Context mContext;
    private LocalStudyData mLocalStudyData;

    private FrameLayout layoutItem;

    private TextView txtStudyOrder;
    private TextView txtStudyTime;
    private TextView txtStudyName;
    private LinearLayout layoutState;
    private TextView txtPlayed;
    private TextView txtDownState;

    private boolean isDeleteMode = false;

    private OnItemClickListener mListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    private OnItemClickListener mDeleteModeListener;
    public void setOnDeleteModeItemClickListener(OnItemClickListener listener) {
        mDeleteModeListener = listener;
    }

    public LocalStudyViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        layoutItem = (FrameLayout)itemView.findViewById(R.id.layoutItem);

        txtStudyOrder = (TextView)itemView.findViewById(R.id.txtStudyOrder);
        txtStudyTime = (TextView)itemView.findViewById(R.id.txtStudyTime);
        txtStudyName = (TextView)itemView.findViewById(R.id.txtStudyName);
        layoutState = (LinearLayout)itemView.findViewById(R.id.layoutState);
        txtPlayed = (TextView)itemView.findViewById(R.id.txtPlayed);
        txtDownState = (TextView)itemView.findViewById(R.id.txtDownState);
        txtDownState.setVisibility(View.INVISIBLE);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isDeleteMode) {
                    if (mListener != null) {
                        mListener.onItemClick(v, getLayoutPosition());
                    }
                }else{
                    if (mDeleteModeListener != null) {
                        mDeleteModeListener.onItemClick(v, getLayoutPosition());
                    }
                }
            }
        });
    }

    public void setLocalStudyItem(LocalStudyData study) {
        if(study != null) {
            mLocalStudyData = study;

            Study studyItem = study.getStudyVO();
            txtStudyOrder.setText(BraveUtils.formatOrder(mContext,
                    studyItem.getStudyOrder(), studyItem.getStudySubOrder()));
            if(!txtStudyOrder.isActivated()){
                txtStudyOrder.setActivated(true);
            }
            txtStudyTime.setText(String.format(mContext.getString(R.string.study_time),
                    studyItem.getStudyTime()));
            String studyName = BraveUtils.fromHTMLTitle(studyItem.getStudyName());
            txtStudyName.setText(studyName);

            updatePlayed(study.isPlayed());
            setItemSelected(study.isSelected());
        }
    }

    public void updateDeleteMode(boolean isDeleteMode){
        this.isDeleteMode = isDeleteMode;
    }

    private void setItemSelected(boolean selected){
        layoutItem.setSelected(selected);
    }

    public void updatePlayed(boolean isPlayed) {
        showPlayed(isPlayed);
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

}
