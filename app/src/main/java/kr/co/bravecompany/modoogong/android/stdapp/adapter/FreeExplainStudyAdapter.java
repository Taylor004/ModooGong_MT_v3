package kr.co.bravecompany.modoogong.android.stdapp.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.api.android.stdapp.api.data.FreeExplainStudyItemVO;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.CommonFooterViewHolder;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.FreeExplainStudyItemViewHolder;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.OnItemClickListener;
import kr.co.bravecompany.modoogong.android.stdapp.data.FreeExplainStudyData;

/**
 * Created by BraveCompany on 2016. 12. 16..
 */

public class FreeExplainStudyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<FreeExplainStudyData> items = new ArrayList<FreeExplainStudyData>();
    private boolean isShowFooter = false;

    public FreeExplainStudyData getItem(int position){
        return items.get(position);
    }

    public boolean isShowFooter() {
        return isShowFooter;
    }

    public void setShowFooter(boolean showFooter) {
        isShowFooter = showFooter;
    }

    public int getItemWithExamNo(int examNo){
        for(int i=0; i<items.size(); i++){
            FreeExplainStudyItemVO item = items.get(i).getFreeExplainStudyItemVO();
            if(examNo == item.getExamNo()){
                return i;
            }
        }
        return -1;
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void add(FreeExplainStudyData freeExplainStudyData) {
        items.add(freeExplainStudyData);
        notifyDataSetChanged();
    }

    public void addAll(List<FreeExplainStudyData> items) {
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
            return Tags.FREE_EXPLAIN_VIEW_TYPE.FREE_EXPLAIN_FOOTER;
        }else{
            return Tags.FREE_EXPLAIN_VIEW_TYPE.FREE_EXPLAIN_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = null;

        switch (viewType){
            case Tags.FREE_EXPLAIN_VIEW_TYPE.FREE_EXPLAIN_FOOTER:
                view = inflater.inflate(R.layout.view_common_footer_view, parent, false);
                return new CommonFooterViewHolder(view);
            case Tags.FREE_EXPLAIN_VIEW_TYPE.FREE_EXPLAIN_ITEM:
                view = inflater.inflate(R.layout.view_free_explain_study_item, parent, false);
                return new FreeExplainStudyItemViewHolder(view);
        }
        throw new IllegalArgumentException("invalid position");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case Tags.FREE_EXPLAIN_VIEW_TYPE.FREE_EXPLAIN_FOOTER:
                break;
            case Tags.FREE_EXPLAIN_VIEW_TYPE.FREE_EXPLAIN_ITEM:
                FreeExplainStudyItemViewHolder h = (FreeExplainStudyItemViewHolder)holder;
                h.setFreeExplainStudyItem(items.get(position));
                h.setOnItemClickListener(mListener);
                break;
        }
    }
    @Override
    public int getItemCount() {
        if(getRealItemCount() > 0 && isShowFooter) {
            return items.size() + 1;
        }else{
            return items.size();
        }
    }

    public int getRealItemCount(){
        return items.size();
    }
}
