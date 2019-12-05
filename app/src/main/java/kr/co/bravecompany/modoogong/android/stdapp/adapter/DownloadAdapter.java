package kr.co.bravecompany.modoogong.android.stdapp.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.data.DownloadData;
import kr.co.bravecompany.modoogong.android.stdapp.db.model.Study;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.DownloadItemViewHolder;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.OnItemClickListener;

/**
 * Created by BraveCompany on 2016. 10. 20..
 */

public class DownloadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<DownloadData> items = new ArrayList<DownloadData>();

    private boolean isDeleteMode = false;

    public DownloadData getItem(int postion){
        return items.get(postion);
    }

    public int getItemWithStudyKey(String studyKey){
        synchronized(items) {
            for (int i = 0; i < items.size(); i++) {
                Study item = items.get(i).getStudyVO();
                try {
                    if (studyKey.equals(item.getStudyKey())) {
                        return i;
                    }
                }catch (IllegalStateException e){
                }
            }
            return -1;
        }
    }

    public void removeItem(String studyKey){
        synchronized(items) {
            int position = getItemWithStudyKey(studyKey);
            if (position != -1) {
                items.remove(position);
                notifyDataSetChanged();
            }
        }
    }

    public ArrayList<DownloadData> removeSelected() {
        synchronized(items) {
            ArrayList<DownloadData> removes = new ArrayList<>();
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).isSelected()) {
                    removes.add(items.get(i));
                }
            }
            items.removeAll(removes);
            notifyDataSetChanged();

            return removes;
        }
    }

    public void removeAll(ArrayList<DownloadData> removes){
        synchronized(items) {
            items.removeAll(removes);
            notifyDataSetChanged();
        }
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void add(DownloadData StudyItemVO) {
        items.add(StudyItemVO);
        notifyDataSetChanged();
    }

    public void addAll(List<DownloadData> items) {
        this.items.addAll(items);
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
                int state = items.get(i).getDownState();
                if (state != Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_PENDING
                        && state != Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_API_ING
                        && state != Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_API_COMPLETE){
                    setItemSelected(i, selected);
                }
            }else{
                setItemSelected(i, selected);
            }
        }
    }

    public void toggle(int position) {
        int state = items.get(position).getDownState();
        if (state != Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_PENDING
                && state != Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_API_ING
                && state != Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_API_COMPLETE){
            setItemSelected(position, !items.get(position).isSelected());
        }
    }


    public int getSelectedCnt(){
        int result = 0;
        for(DownloadData item : items){
            if(item.isSelected()){
                result = result + 1;
            }
        }
        return result;
    }

    public ArrayList<DownloadData> getSelectedItems(){
        ArrayList<DownloadData> result = new ArrayList<>();
        for(DownloadData item : items){
            if(item.isSelected()){
                result.add(item);
            }
        }
        return result;
    }

    public void setItemPaused(int position, boolean paused) {
        if (items.get(position).isPaused() != paused) {
            items.get(position).setPaused(paused);
            notifyDataSetChanged();
        }
    }

    public void togglePaused(int position) {
        setItemPaused(position, !items.get(position).isPaused());
    }

    public int getPauseCnt(){
        int result = 0;
        for(DownloadData item : items){
            if(item.isPaused()){
                result = result + 1;
            }
        }
        return result;
    }

    private OnItemClickListener mDeleteModeListener;
    public void setOnDeleteModeItemClickListener(OnItemClickListener listener) {
        mDeleteModeListener = listener;
    }
    private OnItemClickListener mDownErrorListener;
    public void setOnDownErrorItemClickListener(OnItemClickListener listener) {
        mDownErrorListener = listener;
    }
    private OnItemClickListener mDownPauseListener;
    public void setOnDownPauseClickListener(OnItemClickListener listener) {
        mDownPauseListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_download_item, parent, false);
        RecyclerView.ViewHolder holder = new DownloadItemViewHolder(view);
        view.setTag(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DownloadItemViewHolder h = (DownloadItemViewHolder)holder;
        h.updateDeleteMode(isDeleteMode);
        h.setDownloadItem(items.get(position));
        h.setOnDeleteModeItemClickListener(mDeleteModeListener);
        h.setOnDownErrorItemClickListener(mDownErrorListener);
        h.setOnDownPauseClickListener(mDownPauseListener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateDeleteMode(boolean isDeleteMode){
        this.isDeleteMode = isDeleteMode;
        if(!isDeleteMode){
            setAllItemSelected(false);
        }
        notifyDataSetChanged();
    }

    public boolean isDeleteMode() {
        return isDeleteMode;
    }

    public void updateDownState(View childView, int state, int percents, int errorCode){
        Object childAt = childView.getTag();
        if(childAt instanceof DownloadItemViewHolder) {
            ((DownloadItemViewHolder) childAt).updateDownState(state, percents, errorCode);
        }
    }

    public void updateDownState(String studyKey, int state, int percents, int errorCode){
        int position = getItemWithStudyKey(studyKey);
        if(position != -1){
            DownloadData download = getItem(position);
            download.setDownState(state);
            download.setErrorCode(errorCode);
            download.setDownPercents(percents);
            if(state == Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_API_ERROR
                    || state == Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_ERROR){
                download.setPaused(true);
            }
        }
    }

}