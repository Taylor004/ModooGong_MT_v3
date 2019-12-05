package kr.co.bravecompany.modoogong.android.stdapp.pageLectureList;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.application.ModooGong;
import kr.co.bravecompany.api.android.stdapp.api.data.LectureItemVO;
import kr.co.bravecompany.modoogong.android.stdapp.data.LectureData;
import kr.co.bravecompany.modoogong.android.stdapp.db.DataManager;
import kr.co.bravecompany.modoogong.android.stdapp.db.model.Lecture;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import kr.co.bravecompany.modoogong.android.stdapp.utils.UIUtils;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.OnItemClickListener;

/**
 * Created by BraveCompany on 2016. 10. 18..
 */

public class LectureItemViewHolder extends RecyclerView.ViewHolder{
    private Context mContext;

    private LectureData mLectureData;
    private LectureItemVO mLectureItemVO;

    private TextView txtTeacherName;
    private TextView txtSaleType;
    private TextView txtLectureName;
    private TextView txtLectureDetail;
    private ImageView newMark;

    ProgressBar progLectureBar;
    TextView progLectureText;

    private CheckBox checkFavoriteSelected; // 즐겨찾기 버튼으로 사용되어질 CheckBox 선언.[2019.11.06 테일러]
   // private boolean isSelected;

    private OnItemClickListener mClickListener;
    private OnItemClickListener mUpdateListener;

    private boolean isInit=true;



    public LectureItemViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        txtTeacherName = (TextView)itemView.findViewById(R.id.txtTeacherName);
        txtSaleType = (TextView)itemView.findViewById(R.id.txtSaleType);
        txtLectureName = (TextView)itemView.findViewById(R.id.txtLectureName);
        txtLectureDetail = (TextView)itemView.findViewById(R.id.txtLectureDetail);
        newMark = (ImageView) itemView.findViewById(R.id.lecture_item_newmark);

        progLectureBar = (ProgressBar) itemView.findViewById(R.id.progLectureBar);
        progLectureText =(TextView) itemView.findViewById(R.id.progLectureText);
        checkFavoriteSelected = (CheckBox) itemView.findViewById(R.id.checkFavoriteSelected); // 즐겨찾기 CheckBox 객체화.[2019.11.06 테일러]
        //isSelected = false;


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onItemClick(v, getLayoutPosition());
                }
            }
        });

        checkFavoriteSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                mLectureData.markFab = isChecked;

                if(mUpdateListener!=null)
                    mUpdateListener.onItemClick(buttonView, getLayoutPosition());

//                if(!isInit) {
//                    // Lecture lecture = DataManager.getInstance().getLectureOrInsert(mLectureItemVO);
//                   // DataManager.getInstance().updateLectureFavFlag(mLectureItemVO, isChecked);
//                    //mLectureItemVO.favMark = isChecked;
//
//                    mLectureData.markFab = isChecked;
//                    mUpdateListener.onItemClick(buttonView, getLayoutPosition());
//                }
//
//                isInit = false;
            }
        });

        /*
        // 즐겨찾기 버튼을 눌렀을 때 [2019.10.28 테일러]
        checkFavoriteSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isSelected)
                {
                    checkFavoriteSelected.setButtonDrawable(R.drawable.btn_selected_checkbox);
                    isSelected = true;
                }
                else
                {
                    checkFavoriteSelected.setButtonDrawable(R.drawable.btn_checkbox);
                    isSelected = false;
                }

            }
        });
        */
    }



    public void setLectureItem(LectureItemVO lectureItem, LectureData lectureData)
    {
        mClickListener = null;
        mUpdateListener = null;

        if(lectureItem != null)
        {
            isInit= true;
            mLectureItemVO = lectureItem;
            mLectureData = lectureData;

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

//            txtLectureDetail.setText(String.format(mContext.getString(R.string.lecture_item_detail2),
//                    lectureItem.getStudyStartDay(), lectureItem.getStudyEndDay(),
//                    lectureItem.getLectureingDays(),
//                    lectureItem.getStudyLessonCount(), lectureItem.getLectureCnt()));

            txtLectureDetail.setText(String.format(mContext.getString(R.string.lecture_item_detail2),
                    lectureItem.getStudyStartDay(), lectureItem.getStudyEndDay(),
                    lectureItem.getLectureingDays()));

            progLectureText.setText(String.format("진도 %1$d강/%2$d강",lectureItem.getStudyLessonCount(), lectureItem.getLectureCnt() ));

            float prog= (lectureItem.getStudyLessonCount()* 100)/ (float) lectureItem.getLectureCnt();

            UIUtils.startAnimation(progLectureBar, 0,  (int) prog, 500);

            //Lecture lecture = DataManager.getInstance().getLectureOrInsert(lectureItem);
            checkFavoriteSelected.setChecked(lectureData.markFab);

            newMark.setVisibility(lectureData.markNew ? View.VISIBLE : View.INVISIBLE);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener, OnItemClickListener updateListener){
        mClickListener = listener;
        mUpdateListener = updateListener;
    }
}
