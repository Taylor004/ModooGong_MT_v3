package kr.co.bravecompany.modoogong.android.stdapp.pageStudy;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.data.StudyFileAddData;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.OnItemClickListener;
import kr.co.bravecompany.modoogong.android.stdapp.pageStudy.StudyFileDetailItemViewHolder;

/**
 * Created by BraveCompany on 2017. 10. 20..
 */

public class StudyFileDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<StudyFileAddData> items = new ArrayList<StudyFileAddData>();

    public StudyFileAddData getItem(int position){
        return items.get(position);
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void add(StudyFileAddData item) {
        items.add(item);
        notifyDataSetChanged();
    }

    public void addAll(List<StudyFileAddData> items) {
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
        View view = inflater.inflate(R.layout.view_study_file_detail_item, parent, false);
        return new StudyFileDetailItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        StudyFileDetailItemViewHolder h = (StudyFileDetailItemViewHolder)holder;
        h.setStudyFileAddItem(items.get(position));
        h.setOnItemClickListener(mListener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
