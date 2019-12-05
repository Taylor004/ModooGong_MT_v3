package kr.co.bravecompany.modoogong.android.stdapp.pageLocalLecture;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.application.ModooGong;
import kr.co.bravecompany.modoogong.android.stdapp.db.DataManager;
import kr.co.bravecompany.modoogong.android.stdapp.db.BcDateUtils;
import kr.co.bravecompany.modoogong.android.stdapp.db.model.Lecture;
//import kr.co.bravecompany.modoogong.android.stdapp.pageLocalLecture.LocalLectureItemViewHolder;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.OnItemClickListener;
import kr.co.bravecompany.modoogong.android.stdapp.data.LocalLectureData;

/**
 * Created by BraveCompany on 2016. 10. 24..
 */

public class LocalLectureAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<LocalLectureData> items = new ArrayList<LocalLectureData>();
    ProgressBar progressBar; // view_lecture_item.xml에 있는 프로그래스 바 정보를 담을 프로그래스 바 선언.
    LinearLayout linearLayout; // 프로그레스 바가 존재 했던 리니어 레이아웃을 제거 하기 위해 선언.
    public LocalLectureData getItem(int position){
        return items.get(position);
    }

    public int getItemWithStudyLectureNo(int studyLectureNo){
        for(int i=0; i<items.size(); i++){
            Lecture item = items.get(i).getLectureVO();
            if(studyLectureNo == item.getStudyLectureNo()){
                return i;
            }
        }
        return -1;
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void add(LocalLectureData LocalLectureData) {
        items.add(LocalLectureData);
        notifyDataSetChanged();
    }

    public void addAll(List<LocalLectureData> items) {
        this.items.addAll(items);
        sort();
        notifyDataSetChanged();
    }

    void sort()
    {
        final Date max = new Date();
        final Date min = BcDateUtils.GetStartOfDay(-7);
        final long maxTime = max.getTime() - min.getTime();

        Collections.sort(items, new Comparator<LocalLectureData>() {
            @Override
            public int compare(LocalLectureData item1, LocalLectureData item2)
            {
                long item1Time = item1.getLectureVO().updatedTime;
                long item2Time = item2.getLectureVO().updatedTime;

                if(item1Time < min.getTime())
                    item1Time =  min.getTime();

                if(item2Time < min.getTime())
                    item2Time =  min.getTime();

                float time1 =(float) ((item1Time- min.getTime()) / (float)maxTime);
                float time2 =(float) ((item2Time - min.getTime()) / (float)maxTime);

                float value1 = (item1.getLectureVO().newMark? 100 : 0) + (item1.getLectureVO().favMark ? 10 :0)  + time1;
                float value2 = (item2.getLectureVO().newMark? 100 : 0) + (item2.getLectureVO().favMark ? 10 :0) + time2;

                return value2 > value1 ? 1 : -1;
            };
        });
    }

    private OnItemClickListener mListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        Log.d("onCreateviewholder", "GO TO DOWLOAD LOCKER !!!"); // 다운로드 보관함 클릭 했다는 로그.
        View view = inflater.inflate(R.layout.view_lecture_item, parent, false);

        progressBar = view.findViewById(R.id.progLectureBar); // 프로그래스 바 id값 저장.
        progressBar.setVisibility(View.GONE); // 다운로드 보관함를 클릭하였으면, view_lecture_item.xml 내에 프로그래스 바를 없애도록 설정.

        linearLayout = view.findViewById(R.id.linearLayoutInLecture); // 리니어 레이아웃 id값 저장.
        linearLayout.setVisibility(View.GONE); // 다운로드 보관함를 클릭하였으면, view_lecture_item.xml 내에 리니어 레이아웃를 없애도록 설정.
        return new LocalLectureItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LocalLectureItemViewHolder h = (LocalLectureItemViewHolder)holder;

        //Note. 아래 호출 순서 주의
        h.setLocalLectureItem(items.get(position));
        h.setOnItemClickListener(mListener,mUpdateListener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private OnItemClickListener mUpdateListener = new OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {



           sort();
           notifyDataSetChanged();
        }
    };

    /*******************************************************************
     *
     *  LocalLectureItemViewHolder
     *
     *******************************************************************/

    public class LocalLectureItemViewHolder extends RecyclerView.ViewHolder{
        private Context mContext;
        private LocalLectureData mLocalLectureData;

        private TextView txtTeacherName;
        private TextView txtSaleType;
        private TextView txtLectureName;
        private TextView txtLectureDetail;
        private CheckBox checkFabMark;
        private ImageView imgNewMark;

        private OnItemClickListener mListener;
        private OnItemClickListener mUpdateListener;

        public void setOnItemClickListener(OnItemClickListener listener, OnItemClickListener updateListener) {
            mListener = listener;
            mUpdateListener = updateListener;
        }

        public LocalLectureItemViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();

            txtTeacherName = (TextView)itemView.findViewById(R.id.txtTeacherName);
            txtSaleType = (TextView)itemView.findViewById(R.id.txtSaleType);
            txtLectureName = (TextView)itemView.findViewById(R.id.txtLectureName);
            txtLectureDetail = (TextView)itemView.findViewById(R.id.txtLectureDetail);

            checkFabMark = (CheckBox)itemView.findViewById(R.id.checkFavoriteSelected);
            imgNewMark = (ImageView)itemView.findViewById(R.id.lecture_item_newmark);

            //checkFabMark.setVisibility(View.GONE);
           // imgNewMark.setVisibility(View.GONE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemClick(v, getLayoutPosition());
                    }
                }
            });


            checkFabMark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (mUpdateListener != null) {

                        DataManager.getInstance().updateLectureFavFlag(mLocalLectureData.getLectureVO(), isChecked);
                        //mLocalLectureData.getLectureVO().favMark = isChecked;
                        mUpdateListener.onItemClick(buttonView, getLayoutPosition());
                    }
                }});

        }

        public void setLocalLectureItem(LocalLectureData lectureDown) {

            mUpdateListener =null;
            mListener =null;

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

                checkFabMark.setChecked(mLocalLectureData.getLectureVO().favMark);
                imgNewMark.setVisibility(View.GONE);
            }

        }
    }
}
