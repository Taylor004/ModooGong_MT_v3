package kr.co.bravecompany.modoogong.android.stdapp.Zremoved;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.application.ModooGong;
import kr.co.bravecompany.modoogong.android.stdapp.db.model.Lecture;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import kr.co.bravecompany.modoogong.android.stdapp.data.LocalLectureData;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.OnItemClickListener;

/**
 * Created by BraveCompany on 2016. 10. 24..
 */
/*
public class LocalLectureItemViewHolder extends RecyclerView.ViewHolder{
    private Context mContext;
    private LocalLectureData mLocalLectureData;

    private TextView txtTeacherName;
    private TextView txtSaleType;
    private TextView txtLectureName;
    private TextView txtLectureDetail;

    private OnItemClickListener mListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public LocalLectureItemViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        txtTeacherName = (TextView)itemView.findViewById(R.id.txtTeacherName);
        txtSaleType = (TextView)itemView.findViewById(R.id.txtSaleType);
        txtLectureName = (TextView)itemView.findViewById(R.id.txtLectureName);
        txtLectureDetail = (TextView)itemView.findViewById(R.id.txtLectureDetail);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(v, getLayoutPosition());
                }
            }
        });
    }

    public void setLocalLectureItem(LocalLectureData lectureDown) {
        if(lectureDown != null) {
            mLocalLectureData = lectureDown;

            Lecture lectureItem = lectureDown.getLectureVO();
            txtTeacherName.setText(lectureItem.getTeacherName());
            String cateName = lectureItem.getCateName();
            if(!ModooGong.isShowCateName){
                cateName = null;
            }
            if(cateName != null && cateName.length() != 0){
                txtSaleType.setText(cateName);
                txtSaleType.setVisibility(View.VISIBLE);
            }else{
                txtSaleType.setVisibility(View.GONE);
            }
            txtLectureName.setText(BraveUtils.fromHTMLTitle(lectureItem.getLectureName()));
            txtLectureDetail.setText(String.format(mContext.getString(R.string.local_lecture_item_detail),
                    lectureItem.getStudyStartDay(), lectureItem.getStudyEndDay(),
                    BraveUtils.getLectureingDays(lectureItem.getStudyEndDay())));
        }

    }
}
*/