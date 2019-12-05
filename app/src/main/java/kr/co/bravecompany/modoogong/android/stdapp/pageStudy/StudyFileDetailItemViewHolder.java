package kr.co.bravecompany.modoogong.android.stdapp.pageStudy;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.data.StudyFileAddData;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.OnItemClickListener;

/**
 * Created by BraveCompany on 2017. 10. 20..
 */

public class StudyFileDetailItemViewHolder extends RecyclerView.ViewHolder {

    private StudyFileAddData mStudyFileAddData;
    private Context mContext;

    private TextView txtNo;
    private TextView txtName;

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public StudyFileDetailItemViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        txtNo = (TextView) itemView.findViewById(R.id.txtNo);
        txtName = (TextView) itemView.findViewById(R.id.txtName);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(v, getLayoutPosition());
                }
            }
        });
    }

    public void setStudyFileAddItem(StudyFileAddData studyFileAdd) {
        if(studyFileAdd != null) {
            mStudyFileAddData = studyFileAdd;

            txtNo.setText(String.format(mContext.getString(R.string.study_file_detail_add), studyFileAdd.getNo()));
            txtName.setText(String.format(mContext.getString(R.string.study_file_detail_add_name), studyFileAdd.getName()));
        }
    }
}