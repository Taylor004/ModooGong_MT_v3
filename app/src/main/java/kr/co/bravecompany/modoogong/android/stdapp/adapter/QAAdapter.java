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
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.OnItemClickListener;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.QAItemViewHolder;
import kr.co.bravecompany.modoogong.android.stdapp.data.QAData;

/**
 * Created by BraveCompany on 2016. 11. 1..
 */

public class QAAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<QAData> items = new ArrayList<QAData>();
    private boolean isShowFooter = false;

    public QAData getItem(int position){
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

    public void add(QAData QAData) {
        items.add(QAData);
        notifyDataSetChanged();
    }

    public void addAll(List<QAData> items) {
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
            return Tags.QA_VIEW_TYPE.QA_FOOTER;
        }else{
            return Tags.QA_VIEW_TYPE.QA_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = null;

        switch (viewType){
            case Tags.QA_VIEW_TYPE.QA_FOOTER:
                view = inflater.inflate(R.layout.view_common_footer_view, parent, false);
                return new CommonFooterViewHolder(view);
            case Tags.QA_VIEW_TYPE.QA_ITEM:
                view = inflater.inflate(R.layout.view_qa_item, parent, false);
                return new QAItemViewHolder(view);
        }
        throw new IllegalArgumentException("invalid position");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case Tags.QA_VIEW_TYPE.QA_FOOTER:
                break;
            case Tags.QA_VIEW_TYPE.QA_ITEM:
                QAItemViewHolder h = (QAItemViewHolder)holder;
                h.setQAItem(items.get(position));
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
