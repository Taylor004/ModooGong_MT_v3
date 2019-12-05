package kr.co.bravecompany.modoogong.android.stdapp.pageLectureList;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.application.ModooGong;
import kr.co.bravecompany.api.android.stdapp.api.data.LectureItemVO;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.OnItemClickListener;

/**
 * Created by BraveCompany on 2016. 10. 21..
 */

public class LectureEndItemViewHolder extends RecyclerView.ViewHolder{
    private Context mContext;
    private LectureItemVO mLectureItemVO;

    private TextView txtTeacherName;
    private TextView txtSaleType;
    private TextView txtLectureName;
    private TextView txtLectureDetail;

    private OnItemClickListener mListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public LectureEndItemViewHolder(View itemView) {
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
                    //mListener.onItemClick(v, getLayoutPosition());
                }
            }
        });
    }

    public void setLectureEndItem(LectureItemVO lectureItem) {
        if(lectureItem != null) {
            mLectureItemVO = lectureItem;

            txtTeacherName.setText(lectureItem.getTeacherName());
            String cateName = lectureItem.getSubjectName();// lectureItem.getCateName();
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
            txtLectureDetail.setText(String.format(mContext.getString(R.string.lecture_end_item_detail)
                    , lectureItem.getStudyStartDay(), lectureItem.getStudyEndDay()));
        }
    }
}

