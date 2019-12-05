package kr.co.bravecompany.modoogong.android.stdapp.pageStudy;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.OnItemClickListener;
import kr.co.bravecompany.api.android.stdapp.api.data.LectureItemVO;
import kr.co.bravecompany.modoogong.android.stdapp.data.StudyHeaderData;
import kr.co.bravecompany.api.android.stdapp.api.data.StudyItemVO;
import kr.co.bravecompany.modoogong.android.stdapp.data.StudyData;

/**
 * Created by BraveCompany on 2016. 10. 18..
 */

public class StudyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<StudyData> items = new ArrayList<StudyData>();
    private StudyHeaderData studyHeader;

    public boolean isDownloadMode = false;

    public void setStudyHeader(StudyHeaderData studyHeader) {
        this.studyHeader = studyHeader;
        notifyDataSetChanged();
    }

    public void refreshStudyHeader(){
        int studyLessonCount = getStudyLessonCnt();
        LectureItemVO oldLecture = studyHeader.getLectureItemVO();
        if(oldLecture.getStudyLessonCount() != studyLessonCount){
            oldLecture.setStudyLessonCount(studyLessonCount);
            notifyDataSetChanged();
        }
    }

    public int getStudyLessonCnt(){
        int result = 0;
        for(StudyData item : items){
            String recentStudyDate = item.getStudyItemVO().getRecentStudyDate();
            if(recentStudyDate != null && recentStudyDate.length() != 0){
                result = result + 1;
            }
        }
        return result;
    }

    public StudyData getItem(int position){
        return items.get(position);
    }

    public int getItemWithStudyKey(String studyKey){
        for(int i=0; i<items.size(); i++){
            StudyData item = items.get(i);
            if(studyKey.equals(item.getStudyKey())){
                return i;
            }
        }
        return -1;
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void add(StudyData StudyItemVO) {
        items.add(StudyItemVO);
        notifyDataSetChanged();
    }

    public void addAll(List<StudyData> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void refreshPlayed(String studyKey){
        int oldItem = getItemWithStudyKey(studyKey);
        if(oldItem != -1) {
            for (int i = 0; i < items.size(); i++) {
                StudyData item = items.get(i);
                StudyItemVO studyItem = item.getStudyItemVO();
                if (i == oldItem) {
                    studyItem.setRecentStudyDate("played");
                    item.setPlayed(true);
                } else {
                    item.setPlayed(false);
                }
            }
            notifyDataSetChanged();

            refreshStudyHeader();
        }
    }

    public void refreshDownloaded(String studyKey){
        int oldItem = getItemWithStudyKey(studyKey);
        if(oldItem != -1) {
            StudyItemVO studyItem = items.get(oldItem).getStudyItemVO();
            studyItem.setRecentStudyDate("downloaded");

            notifyDataSetChanged();

            refreshStudyHeader();
        }
    }

    public void setPlayedItem(int position){
        for(int i=0; i<items.size(); i++){
            items.get(i).setPlayed(i == position);
        }
        notifyDataSetChanged();
    }

    public void setItemSelected(int position, boolean selected) {
        if (items.get(position).isSelected() != selected) {
            items.get(position).setSelected(selected);
            notifyDataSetChanged();
        }
    }

    public void setAllItemSelected(boolean selected) {
        for(int i=0; i<items.size(); i++){
            if(selected){
                if (items.get(i).getDownState() == Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_NONE) {
                    setItemSelected(i, selected);
                }
            }else{
                setItemSelected(i, selected);
            }
        }
    }

    public void toggle(int position) {
        int state = items.get(position).getDownState();
        if (state == Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_NONE) {
            setItemSelected(position, !items.get(position).isSelected());
        }
    }

    public int getSelectedCnt(){
        int result = 0;
        for(StudyData item : items){
            if(item.isSelected()){
                result = result + 1;
            }
        }
        return result;
    }

    public ArrayList<StudyData> getSelectedItems(){
        ArrayList<StudyData> result = new ArrayList<>();
        for(StudyData item : items){
            if(item.isSelected()){
                result.add(item);
            }
        }
        return result;
    }

    private OnItemClickListener mListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    private OnItemClickListener mDownloadModeListener;
    public void setOnDownloadModeItemClickListener(OnItemClickListener listener) {
        mDownloadModeListener = listener;
    }

    private View.OnClickListener mAllSelectListener;
    public void setOnAllSelectClickListener(View.OnClickListener listener) {
        mAllSelectListener = listener;
    }
    private View.OnClickListener mAllDeselectListener;
    public void setOnAllDeselectClickListener(View.OnClickListener listener) {
        mAllDeselectListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return Tags.STUDY_VIEW_TYPE.STUDY_HEADER;
        }else if(position == 1){
            return Tags.STUDY_VIEW_TYPE.STUDY_SELECT_HEADER;
        }else{
            return Tags.STUDY_VIEW_TYPE.STUDY_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = null;
        RecyclerView.ViewHolder holder = null;

        switch (viewType){
            case Tags.STUDY_VIEW_TYPE.STUDY_HEADER:
                view = inflater.inflate(R.layout.view_study_header, parent, false);
                holder = new StudyHeaderViewHolder(view);
                break;
            case Tags.STUDY_VIEW_TYPE.STUDY_SELECT_HEADER:
                view = inflater.inflate(R.layout.view_default_header, parent, false);
                holder = new StudySelectHeaderViewHolder(view);
                break;
            case Tags.STUDY_VIEW_TYPE.STUDY_ITEM:
                view = inflater.inflate(R.layout.view_study_item, parent, false);
                holder = new StudyItemViewHolder(view);
                break;
        }
        if(holder != null){
            view.setTag(holder);
            return holder;
        }else{
            throw new IllegalArgumentException("invalid position");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position == 0) {
            StudyHeaderViewHolder h = (StudyHeaderViewHolder) holder;
            h.setStudyHeaderItem(studyHeader);
        }else if(position == 1) {
            StudySelectHeaderViewHolder h = (StudySelectHeaderViewHolder) holder;
            h.updateDownloadingMode(isDownloadMode);
            h.setStudySelectHeaderItem(studyHeader);
            h.setOnAllSelectClickListener(mAllSelectListener);
            h.setOnAllDeselectClickListener(mAllDeselectListener);
        }else if(position > 1){
            StudyItemViewHolder h = (StudyItemViewHolder)holder;
            h.updateDownloadingMode(isDownloadMode);
            h.setStudyItem(items.get(position-2));
            h.setOnItemClickListener(mListener);
            h.setOnDownloadModeItemClickListener(mDownloadModeListener);
        }
    }

    @Override
    public int getItemCount() {
        return items.size() + 2;
    }

    public int getRealItemCount(){
        return items.size();
    }

    public void updateDownloadMode(boolean isDownloadMode){
        this.isDownloadMode = isDownloadMode;
        if(!isDownloadMode){
            setAllItemSelected(false);
        }
        notifyDataSetChanged();
    }

    public void updateHeader(boolean isShowSelectAll){
        studyHeader.setShowSelectAll(isShowSelectAll);
        studyHeader.setLectureCnt(getRealItemCount());
        studyHeader.setSelectCnt(getSelectedCnt());
        notifyDataSetChanged();
    }

    public void updateDownState(View childView, int state, int percents){
        Object childAt = childView.getTag();
        if(childAt instanceof StudyItemViewHolder) {
            ((StudyItemViewHolder) childAt).updateDownState(state, percents);
        }
    }

    public void updateDownState(String studyKey, int state, int percents){
        int position = getItemWithStudyKey(studyKey);
        if(position != -1){
            StudyData study = getItem(position);
            study.setDownState(state);
            study.setDownPercents(percents);
        }
    }

}
