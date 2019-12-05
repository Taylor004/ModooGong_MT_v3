package kr.co.bravecompany.modoogong.android.stdapp.pageStudy;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.api.android.stdapp.api.data.LectureItemVO;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import kr.co.bravecompany.modoogong.android.stdapp.data.StudyHeaderData;

/**
 * Created by BraveCompany on 2016. 10. 18..
 */

public class StudyHeaderViewHolder extends RecyclerView.ViewHolder{

    private StudyHeaderData mStudyHeader;

    private TextView txtTeacherName;
    private TextView txtSaleType;
    private TextView txtLectureName;
    private TextView txtLectureDetail;

    public StudyHeaderViewHolder(View itemView) {
        super(itemView);

        txtTeacherName = (TextView)itemView.findViewById(R.id.txtTeacherName);
        txtSaleType = (TextView)itemView.findViewById(R.id.txtSaleType);
        txtLectureName = (TextView)itemView.findViewById(R.id.txtLectureName);
        txtLectureDetail = (TextView)itemView.findViewById(R.id.txtLectureDetail);
    }

    public void setStudyHeaderItem(StudyHeaderData studyHeader) {
        if(studyHeader != null) {
            mStudyHeader = studyHeader;
            LectureItemVO lectureItem = studyHeader.getLectureItemVO();

            BraveUtils.updateStudyHeaderView(txtTeacherName, txtSaleType,
                    txtLectureName, txtLectureDetail, lectureItem);
        }
    }

}
