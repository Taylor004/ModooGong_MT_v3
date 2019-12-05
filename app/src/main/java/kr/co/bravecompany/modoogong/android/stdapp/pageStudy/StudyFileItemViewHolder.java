package kr.co.bravecompany.modoogong.android.stdapp.pageStudy;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.data.StudyFileData;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.OnItemClickListener;

/**
 * Created by BraveCompany on 2017. 10. 20..
 */

public class StudyFileItemViewHolder extends RecyclerView.ViewHolder {

    private StudyFileData mStudyFileData;
    private Context mContext;

    private TextView txtNo;
    private TextView txtTitle;

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public StudyFileItemViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        txtNo = (TextView) itemView.findViewById(R.id.txtNo);
        txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(v, getLayoutPosition());
                }
            }
        });
    }

    public void setStudyFileItem(StudyFileData studyFile) {
        if(studyFile != null) {
            mStudyFileData = studyFile;

            txtNo.setText(String.valueOf(studyFile.getNumber()));
            txtTitle.setText(BraveUtils.fromHTMLTitle(studyFile.getStudyFileItemVO().getTitle()));
        }
    }
}