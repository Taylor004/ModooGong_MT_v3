package kr.co.bravecompany.modoogong.android.stdapp.pageStudy;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.data.StudyHeaderData;

/**
 * Created by BraveCompany on 2016. 11. 4..
 */

public class StudySelectHeaderViewHolder extends RecyclerView.ViewHolder{
    private Context mContext;
    private StudyHeaderData mStudyHeader;

    private TextView txtDefaultGuide;
    private TextView txtLectureCnt;
    private TextView txtLectureSelectCnt;

    private LinearLayout headerDefault;
    private RelativeLayout headerSelect;
    private TextView allSelect;
    private TextView allDeselect;

    private boolean isDownloadMode = false;

    private View.OnClickListener mAllSelectListener;
    public void setOnAllSelectClickListener(View.OnClickListener listener) {
        mAllSelectListener = listener;
    }
    private View.OnClickListener mAllDeselectListener;
    public void setOnAllDeselectClickListener(View.OnClickListener listener) {
        mAllDeselectListener = listener;
    }

    public StudySelectHeaderViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        txtDefaultGuide = (TextView)itemView.findViewById(R.id.txtDefaultGuide);
        txtDefaultGuide.setText(mContext.getString(R.string.study_play_guide));
        txtLectureCnt = (TextView)itemView.findViewById(R.id.txtLectureCnt);
        txtLectureSelectCnt = (TextView)itemView.findViewById(R.id.txtLectureSelectCnt);

        headerDefault = (LinearLayout)itemView.findViewById(R.id.headerDefault);
        headerSelect = (RelativeLayout)itemView.findViewById(R.id.selectHeader);

        allSelect = (TextView)itemView.findViewById(R.id.allSelect);
        allDeselect = (TextView)itemView.findViewById(R.id.allDeselect);
        allSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAllSelectListener != null){
                    mAllSelectListener.onClick(v);
                }
            }
        });
        allDeselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAllDeselectListener != null){
                    mAllDeselectListener.onClick(v);
                }
            }
        });
    }

    public void setStudySelectHeaderItem(StudyHeaderData studyHeader) {
        if(studyHeader != null) {
            mStudyHeader = studyHeader;
            updateSelectAllView(studyHeader.isShowSelectAll());
            updateHeader(studyHeader.getLectureCnt(), studyHeader.getSelectCnt());
        }
    }

    public void updateDownloadingMode(boolean isDownloadMode){
        this.isDownloadMode = isDownloadMode;
    }

    private void updateHeader(int lectureCnt, int selectCnt){
        if(!isDownloadMode){
            headerDefault.setVisibility(View.VISIBLE);
            headerSelect.setVisibility(View.GONE);

            txtLectureCnt.setText(String.format(mContext.getString(R.string.study_play_count),
                    lectureCnt));

        }else{
            headerDefault.setVisibility(View.GONE);
            headerSelect.setVisibility(View.VISIBLE);

            txtLectureSelectCnt.setText(Html.fromHtml(String.format(mContext.getString(R.string.study_select_state),
                    lectureCnt, selectCnt)));
        }
    }

    private void updateSelectAllView(boolean isShow){
        if(isShow){
            allSelect.setVisibility(View.VISIBLE);
            allDeselect.setVisibility(View.GONE);
        }else{
            allSelect.setVisibility(View.GONE);
            allDeselect.setVisibility(View.VISIBLE);
        }
    }
}
