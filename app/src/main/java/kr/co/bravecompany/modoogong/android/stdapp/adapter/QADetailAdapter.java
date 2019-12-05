package kr.co.bravecompany.modoogong.android.stdapp.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.manager.MediaPlayerManager;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.QADetailAnswerItemViewHolder;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.QADetailItemViewHolder;
import kr.co.bravecompany.api.android.stdapp.api.data.QADetailVO;

/**
 * Created by BraveCompany on 2016. 11. 2..
 */

public class QADetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<QADetailVO> items = new ArrayList<QADetailVO>();

    //player
    private ArrayList<MediaPlayerManager> mPlayerManagers;

    public QADetailAdapter(ArrayList<MediaPlayerManager> playerManagers) {
        this.mPlayerManagers = playerManagers;
    }

    public QADetailVO getQuestionItem(){
        return items.get(0);
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void add(QADetailVO QADetailVO) {
        items.add(QADetailVO);
        notifyDataSetChanged();
    }

    public void addAll(List<QADetailVO> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    private QADetailAnswerItemViewHolder.OnImagePreviewClickListener mImagePreviewClickListener;
    public void setOnImagePreviewClickListener(QADetailAnswerItemViewHolder.OnImagePreviewClickListener listener) {
        mImagePreviewClickListener = listener;
    }

    private QADetailAnswerItemViewHolder.OnFilePreviewClickListener mFilePreviewClickListener;
    public void setOnFilePreviewClickListener(QADetailAnswerItemViewHolder.OnFilePreviewClickListener listener) {
        mFilePreviewClickListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return Tags.QA_DETAIL_VIEW_TYPE.QA_DETAIL_ITEM;
        }else{
            return Tags.QA_DETAIL_VIEW_TYPE.QA_DETAIL_ANSWER;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = null;

        switch (viewType){
            case Tags.QA_DETAIL_VIEW_TYPE.QA_DETAIL_ITEM:
                view = inflater.inflate(R.layout.view_qa_detail_item, parent, false);
                QADetailItemViewHolder item = new QADetailItemViewHolder(view, mPlayerManagers);
                return item;
            case Tags.QA_DETAIL_VIEW_TYPE.QA_DETAIL_ANSWER:
                view = inflater.inflate(R.layout.view_qa_detail_answer_item, parent, false);
                QADetailAnswerItemViewHolder answer = new QADetailAnswerItemViewHolder(view, mPlayerManagers);
                return answer;
        }
        throw new IllegalArgumentException("invalid position");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position == 0) {
            QADetailItemViewHolder h = (QADetailItemViewHolder)holder;
            h.setQADetailItem(items.get(position));
            h.setOnImagePreviewClickListener(mImagePreviewClickListener);
            h.setOnFilePreviewClickListener(mFilePreviewClickListener);
        }else{
            QADetailAnswerItemViewHolder h = (QADetailAnswerItemViewHolder)holder;
            h.setQADetailItem(items.get(position));
            h.setOnImagePreviewClickListener(mImagePreviewClickListener);
            h.setOnFilePreviewClickListener(mFilePreviewClickListener);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}