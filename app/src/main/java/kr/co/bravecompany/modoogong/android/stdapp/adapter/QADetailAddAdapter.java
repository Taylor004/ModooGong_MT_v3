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
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.OnItemClickListener;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.QADetailAddFileViewHolder;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.QADetailAddImageViewHolder;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.QADetailAddVoiceViewHolder;
import kr.co.bravecompany.modoogong.android.stdapp.data.QADetailAddData;

/**
 * Created by BraveCompany on 2017. 2. 1..
 */

public class QADetailAddAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<QADetailAddData> items = new ArrayList<QADetailAddData>();

    //player
    private ArrayList<MediaPlayerManager> mPlayerManagers;

    public QADetailAddAdapter(ArrayList<MediaPlayerManager> playerManagers) {
        this.mPlayerManagers = playerManagers;
    }

    public QADetailAddData getItem(int position){
        return items.get(position);
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void add(QADetailAddData qADetailAddData) {
        items.add(qADetailAddData);
        notifyDataSetChanged();
    }

    public void addAll(List<QADetailAddData> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    private OnItemClickListener mListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
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
            case Tags.QA_UPLOAD_TYPE.QA_UPLOAD_IMAGE:
                view = inflater.inflate(R.layout.view_qa_detail_add_image, parent, false);
                return new QADetailAddImageViewHolder(view);
            case Tags.QA_UPLOAD_TYPE.QA_UPLOAD_VOICE:
                view = inflater.inflate(R.layout.view_qa_detail_add_voice, parent, false);
                MediaPlayerManager playerManager = new MediaPlayerManager();
                QADetailAddVoiceViewHolder voice = new QADetailAddVoiceViewHolder(view, playerManager);
                mPlayerManagers.add(playerManager);
                return voice;
            case Tags.QA_UPLOAD_TYPE.QA_UPLOAD_FILE:
                view = inflater.inflate(R.layout.view_qa_detail_add_file, parent, false);
                return new QADetailAddFileViewHolder(view);
        }
        throw new IllegalArgumentException("invalid position");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = items.get(position).getType();
        switch (type){
            case Tags.QA_UPLOAD_TYPE.QA_UPLOAD_IMAGE:
                QADetailAddImageViewHolder image = (QADetailAddImageViewHolder)holder;
                image.setQADetailAddImage(items.get(position));
                image.setOnItemClickListener(mListener);
                break;
            case Tags.QA_UPLOAD_TYPE.QA_UPLOAD_VOICE:
                QADetailAddVoiceViewHolder voice = (QADetailAddVoiceViewHolder)holder;
                voice.setQADetailAddVoice(items.get(position));
                break;
            case Tags.QA_UPLOAD_TYPE.QA_UPLOAD_FILE:
                QADetailAddFileViewHolder file = (QADetailAddFileViewHolder)holder;
                file.setQADetailAddFile(items.get(position));
                file.setOnItemClickListener(mListener);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
