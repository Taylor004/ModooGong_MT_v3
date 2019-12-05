package kr.co.bravecompany.modoogong.android.stdapp.pageLectureList;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.db.DataManager;
import kr.co.bravecompany.modoogong.android.stdapp.db.BcDateUtils;
import kr.co.bravecompany.modoogong.android.stdapp.db.StudyDataManager;
import kr.co.bravecompany.modoogong.android.stdapp.db.model.Lecture;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.OnItemClickListener;
import kr.co.bravecompany.api.android.stdapp.api.data.LectureItemVO;
import kr.co.bravecompany.modoogong.android.stdapp.data.LectureData;

/**
 * Created by BraveCompany on 2016. 10. 14..
 */

public class LectureAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<LectureData> items = new ArrayList<LectureData>();

    private OnItemClickListener mClickListener;

    private OnItemClickListener mUpdateListener = new OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {

            updateFavMark(position);
        }
    };

    public void setOnItemClickListener(OnItemClickListener clickListener )
    {
        mClickListener = clickListener;
    }

    public LectureData getItem(int position){
        return items.get(position);
    }


    public int getItemWithStudyLectureNo(int studyLectureNo){
        for(int i=0; i<items.size(); i++){
            LectureItemVO item = items.get(i).getLectureItemVO();
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

    public void add(LectureData LectureData) {
        items.add(LectureData);
        notifyDataSetChanged();
    }

    public void addAll(List<LectureData> items) {

        this.items.addAll(items);

        for ( LectureData data: items)
        {
            Lecture l =  DataManager.getInstance().getLectureOrInsert(data.getLectureItemVO());

            data.markFab = l.favMark;
            data.markNew = l.newMark;
            data.updatedTime = l.updatedTime;

            boolean isNew = StudyDataManager.getInstance().isNewSubscribeLecture(String.valueOf(l.getLectureCode()));
            if(isNew)
            {
                data.markNew = true;
                DataManager.getInstance().updateLectureNewFlag(data.getLectureItemVO(), true);
            }

            if(data.markNew)
            {
                Date now = new Date();

            }
        }

        StudyDataManager.getInstance().clearTempLectureSubscribeList();

        sort();
        notifyDataSetChanged();
    }

    public void resetNewmark(int pos)
    {
        LectureData lectureData = getItem(pos);

        if (lectureData.markNew) {
            lectureData.updatedTime = new Date().getTime();
            DataManager.getInstance().updateLectureNewFlag(lectureData.getLectureItemVO(), false);
        }
    }

    public void updateFavMark(int pos)
    {
        LectureData lecture = getItem(pos);
        lecture.updatedTime = new Date().getTime();
        DataManager.getInstance().updateLectureFavFlag(lecture.getLectureItemVO(), lecture.markFab);

        refreshSort();
    }


    public void refresh(LectureItemVO lecture)
    {
        int oldItem = getItemWithStudyLectureNo(lecture.getStudyLectureNo());
        if(oldItem != -1){
            items.get(oldItem).setLectureItemVO(lecture);
        }
        sort();
        notifyDataSetChanged();
    }

    public void remove(LectureData lectureData, int position)
    {

        items.remove(lectureData);
       // notifyDataSetChanged();

        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    void sort()
    {
        final Date max = new Date();
        final Date min = BcDateUtils.GetStartOfDay(-7);
        final long maxTime = max.getTime() - min.getTime();

        Collections.sort(items, new Comparator<LectureData>() {
            @Override
            public int compare(LectureData item1, LectureData item2)
            {
                if(item1.updatedTime < min.getTime())
                    item1.updatedTime =  min.getTime();

                if(item2.updatedTime < min.getTime())
                    item2.updatedTime =  min.getTime();

                float time1 =(float) ((item1.updatedTime - min.getTime()) / (float)maxTime);
                float time2 =(float) ((item2.updatedTime - min.getTime()) / (float)maxTime);

                float value1 = (item1.markNew? 100 : 0) + (item1.markFab ? 10 :0)  + time1;
                float value2 = (item2.markNew? 100 : 0) + (item2.markFab ? 10 :0) + time2;

                return value2 > value1 ? 1 : -1;
            };
        });
    }

    public void refreshSort()
    {
        sort();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = null;

        switch (viewType){
            case Tags.LECTURE_VIEW_TYPE.LECTURE_ING_ITEM:
                view = inflater.inflate(R.layout.view_lecture_item, parent, false);
                return new LectureItemViewHolder(view);
            case Tags.LECTURE_VIEW_TYPE.LECTURE_END_ITEM:
                view = inflater.inflate(R.layout.view_lecture_end_item, parent, false);
                return new LectureEndItemViewHolder(view);
        }

        throw new IllegalArgumentException("invalid position");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = items.get(position).getType();
        switch (type){
            case Tags.LECTURE_VIEW_TYPE.LECTURE_ING_ITEM:
                LectureItemViewHolder holder1 = (LectureItemViewHolder)holder;

                //Note. 내부에서 이벤트가 발생하는 순서 때문에, 콜 순서가 매주 중요함!!
                holder1.setLectureItem(items.get(position).getLectureItemVO(),items.get(position));
                holder1.setOnItemClickListener(mClickListener, mUpdateListener);
                break;
            case Tags.LECTURE_VIEW_TYPE.LECTURE_END_ITEM:
                LectureEndItemViewHolder holder2 = (LectureEndItemViewHolder)holder;
                holder2.setLectureEndItem(items.get(position).getLectureItemVO());
                //holder2.setOnItemClickListener(mListener);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
