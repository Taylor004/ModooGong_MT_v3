package kr.co.bravecompany.modoogong.android.stdapp.pageStudy;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.data.StudyFileData;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.CommonFooterViewHolder;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.OnItemClickListener;
import kr.co.bravecompany.modoogong.android.stdapp.pageStudy.StudyFileHeaderViewHolder;
import kr.co.bravecompany.modoogong.android.stdapp.pageStudy.StudyFileItemViewHolder;

/**
 * Created by BraveCompany on 2017. 10. 20..
 */

public class StudyFileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<StudyFileData> items = new ArrayList<StudyFileData>();
    private boolean isShowFooter = false;

    public StudyFileData getItem(int position){
        return items.get(position);
    }

    public boolean isShowFooter() {
        return isShowFooter;
    }

    public void setShowFooter(boolean showFooter) {
        isShowFooter = showFooter;
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void add(StudyFileData item) {
        items.add(item);
        notifyDataSetChanged();
    }

    public void addAll(List<StudyFileData> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    private OnItemClickListener mListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == items.size() && isShowFooter){
            return Tags.STUDY_FILE_VIEW_TYPE.STUDY_FILE_FOOTER;
        }else {
            return items.get(position).getType();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = null;

        switch (viewType){
            case Tags.STUDY_FILE_VIEW_TYPE.STUDY_FILE_HEADER:
                view = inflater.inflate(R.layout.view_study_file_header, parent, false);
                return new StudyFileHeaderViewHolder(view);
            case Tags.STUDY_FILE_VIEW_TYPE.STUDY_FILE_ITEM:
                view = inflater.inflate(R.layout.view_study_file_item, parent, false);
                return new StudyFileItemViewHolder(view);
            case Tags.STUDY_FILE_VIEW_TYPE.STUDY_FILE_FOOTER:
                view = inflater.inflate(R.layout.view_common_footer_view, parent, false);
                return new CommonFooterViewHolder(view);
        }
        throw new IllegalArgumentException("invalid position");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        switch (type){
            case Tags.STUDY_FILE_VIEW_TYPE.STUDY_FILE_HEADER:
                StudyFileHeaderViewHolder header = (StudyFileHeaderViewHolder)holder;
                header.setStudyFileHeader(items.get(position));
                header.setOnItemClickListener(mListener);
                break;
            case Tags.STUDY_FILE_VIEW_TYPE.STUDY_FILE_ITEM:
                StudyFileItemViewHolder item = (StudyFileItemViewHolder)holder;
                item.setStudyFileItem(items.get(position));
                item.setOnItemClickListener(mListener);
                break;
            case Tags.FREE_LECTURE_VIEW_TYPE.FREE_LECTURE_FOOTER:
                break;
        }
    }

    @Override
    public int getItemCount() {
        if(getRealItemCount() > 0 && isShowFooter){
            return items.size() + 1;
        }else{
            return items.size();
        }
    }

    public int getRealItemCount(){
        return items.size();
    }
}
