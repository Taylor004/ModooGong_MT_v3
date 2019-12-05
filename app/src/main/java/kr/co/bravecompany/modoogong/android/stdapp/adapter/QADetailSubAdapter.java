package kr.co.bravecompany.modoogong.android.stdapp.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.QADetailSubViewHolder;
import kr.co.bravecompany.modoogong.android.stdapp.data.QADetailSubData;

/**
 * Created by BraveCompany on 2017. 4. 17..
 */

public class QADetailSubAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected List<QADetailSubData> items = new ArrayList<QADetailSubData>();

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void add(QADetailSubData qaDetailSubData) {
        items.add(qaDetailSubData);
        notifyDataSetChanged();
    }

    public void addAll(List<QADetailSubData> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_qa_detail_sub, parent, false);
        return new QADetailSubViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        QADetailSubViewHolder h = (QADetailSubViewHolder)holder;
        h.setQADetailSub(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}