package kr.co.bravecompany.modoogong.android.stdapp.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.NoticeItemViewHolder;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.OnItemClickListener;
import kr.co.bravecompany.modoogong.android.stdapp.data.NoticeData;

/**
 * Created by BraveCompany on 2016. 10. 25..
 */

public class NoticeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<NoticeData> items = new ArrayList<NoticeData>();

    public NoticeData getItem(int position){
        return items.get(position);
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void add(NoticeData NoticeData) {
        items.add(NoticeData);
        notifyDataSetChanged();
    }

    public void addAll(List<NoticeData> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    private OnItemClickListener mListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_notice_item, parent, false);
        return new NoticeItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NoticeItemViewHolder h = (NoticeItemViewHolder)holder;
        h.setNoticeItem(items.get(position));
        h.setOnItemClickListener(mListener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
