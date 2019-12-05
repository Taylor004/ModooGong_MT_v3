package kr.co.bravecompany.modoogong.android.stdapp.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.CommonFooterViewHolder;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.FreeStudyDividerViewHolder;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.FreeStudyHeaderViewHolder;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.FreeStudyItemViewHolder;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.OnItemClickListener;
import kr.co.bravecompany.api.android.stdapp.api.data.FreeStudyItemVO;
import kr.co.bravecompany.modoogong.android.stdapp.data.FreeStudyData;

/**
 * Created by BraveCompany on 2016. 10. 25..
 */

public class FreeStudyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<FreeStudyData> items = new ArrayList<FreeStudyData>();
    private boolean isShowFooter = false;
    private FreeStudyData studyHeader;
    private String dividerTitle;

    public void setFreeStudyHeader(FreeStudyData studyHeader) {
        this.studyHeader = studyHeader;
        notifyDataSetChanged();
    }

    public FreeStudyData getFreeStudyHeader() {
        return studyHeader;
    }

    public void setFreeStudyDivider(String dividerTitle) {
        this.dividerTitle = dividerTitle;
        notifyDataSetChanged();
    }

    public FreeStudyData getItem(int position){
        return items.get(position);
    }

    public boolean isShowFooter() {
        return isShowFooter;
    }

    public void setShowFooter(boolean showFooter) {
        isShowFooter = showFooter;
    }

    public int getItemWithCastNo(int castNo){
        for(int i=0; i<items.size(); i++){
            FreeStudyItemVO item = items.get(i).getFreeStudyItemVO();
            if(castNo == item.getCastNo()){
                return i;
            }
        }
        return -1;
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void add(FreeStudyData FreeStudyData) {
        items.add(FreeStudyData);
        notifyDataSetChanged();
    }

    public void addAll(List<FreeStudyData> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    private OnItemClickListener mListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    private FreeStudyHeaderViewHolder.OnHeaderItemClickListener mHeaderListener;
    public void setOnItemClickListener(FreeStudyHeaderViewHolder.OnHeaderItemClickListener listener) {
        mHeaderListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return Tags.FREE_LECTURE_VIEW_TYPE.FREE_LECTURE_HEADER;
        }else if(position == 1){
            return Tags.FREE_LECTURE_VIEW_TYPE.FREE_LECTURE_DIVIDER;
        }else if(position == items.size() + 2 && isShowFooter){
            return Tags.FREE_LECTURE_VIEW_TYPE.FREE_LECTURE_FOOTER;
        }else{
            return Tags.FREE_LECTURE_VIEW_TYPE.FREE_LECTURE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = null;

        switch (viewType){
            case Tags.FREE_LECTURE_VIEW_TYPE.FREE_LECTURE_DIVIDER:
                view = inflater.inflate(R.layout.view_free_study_divider, parent, false);
                return new FreeStudyDividerViewHolder(view);
            case Tags.FREE_LECTURE_VIEW_TYPE.FREE_LECTURE_HEADER:
                view = inflater.inflate(R.layout.view_free_study_header, parent, false);
                return new FreeStudyHeaderViewHolder(view);
            case Tags.FREE_LECTURE_VIEW_TYPE.FREE_LECTURE_FOOTER:
                view = inflater.inflate(R.layout.view_common_footer_view, parent, false);
                return new CommonFooterViewHolder(view);
            case Tags.FREE_LECTURE_VIEW_TYPE.FREE_LECTURE_ITEM:
                view = inflater.inflate(R.layout.view_free_study_item, parent, false);
                return new FreeStudyItemViewHolder(view);
        }
        throw new IllegalArgumentException("invalid position");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case Tags.FREE_LECTURE_VIEW_TYPE.FREE_LECTURE_DIVIDER:
                FreeStudyDividerViewHolder divider = (FreeStudyDividerViewHolder) holder;
                divider.setFreeStudyDivider(dividerTitle);
                break;
            case Tags.FREE_LECTURE_VIEW_TYPE.FREE_LECTURE_HEADER:
                FreeStudyHeaderViewHolder header = (FreeStudyHeaderViewHolder) holder;
                header.setFreeStudyHeaderItem(studyHeader);
                header.setOnHeaderItemClickListener(mHeaderListener);
                break;
            case Tags.FREE_LECTURE_VIEW_TYPE.FREE_LECTURE_FOOTER:
                break;
            case Tags.FREE_LECTURE_VIEW_TYPE.FREE_LECTURE_ITEM:
                FreeStudyItemViewHolder item = (FreeStudyItemViewHolder)holder;
                item.setFreeStudyItem(items.get(position-2));
                item.setOnItemClickListener(mListener);
                break;
        }
    }

    @Override
    public int getItemCount() {
        if(getRealItemCount() > 0 && isShowFooter){
            return items.size() + 3;
        }else{
            return items.size() + 2;
        }
    }

    public int getRealItemCount(){
        return items.size();
    }
}
