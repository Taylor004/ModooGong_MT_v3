package kr.co.bravecompany.modoogong.android.stdapp.pageLocalStudy;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.db.model.Study;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.OnItemClickListener;
import kr.co.bravecompany.modoogong.android.stdapp.data.LocalStudyData;

/**
 * Created by BraveCompany on 2016. 10. 24..
 */

public class LocalStudyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<LocalStudyData> items = new ArrayList<LocalStudyData>();

    public boolean isDeleteMode = false;

    public LocalStudyData getItem(int postion){
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

    public ArrayList<LocalStudyData> removeSelected() {
        ArrayList<LocalStudyData> removes = new ArrayList<>();
        for(int i=0; i<items.size(); i++){
            if(items.get(i).isSelected()){
                removes.add(items.get(i));
            }
        }
        items.removeAll(removes);
        notifyDataSetChanged();

        return removes;
    }

    public void removeAll(ArrayList<LocalStudyData> removes){
        synchronized(items) {
            items.removeAll(removes);
            notifyDataSetChanged();
        }
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void add(LocalStudyData StudyItemVO) {
        items.add(StudyItemVO);
        notifyDataSetChanged();
    }

    public void addAll(List<LocalStudyData> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void refresh(String lastStudy){
        int oldItem = getItemWithStudyKey(lastStudy);
        if(oldItem != -1) {
            for (int i = 0; i < items.size(); i++) {
                items.get(i).setPlayed(i == oldItem);
            }
            notifyDataSetChanged();
        }
    }

    public void setItemSelected(int position, boolean selected) {
        if (items.get(position).isSelected() != selected) {
            items.get(position).setSelected(selected);
            notifyDataSetChanged();
        }
    }

    public void setAllItemSelected(boolean selected) {
        for (int i = 0; i < items.size(); i++) {
            setItemSelected(i, selected);
        }
    }

    public void toggle(int position) {
        setItemSelected(position, !items.get(position).isSelected());
    }

    public int getSelectedCnt() {
        int result = 0;
        for (LocalStudyData item : items) {
            if (item.isSelected()) {
                result = result + 1;
            }
        }
        return result;
    }

    public ArrayList<LocalStudyData> getSelectedItems(){
        ArrayList<LocalStudyData> result = new ArrayList<>();
        for(LocalStudyData item : items){
            if(item.isSelected()){
                result.add(item);
            }
        }
        return result;
    }

    private OnItemClickListener mListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    private OnItemClickListener mDeleteModeListener;
    public void setOnDeleteModeItemClickListener(OnItemClickListener listener) {
        mDeleteModeListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_study_item, parent, false);
        return new LocalStudyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LocalStudyViewHolder h = (LocalStudyViewHolder)holder;
        h.updateDeleteMode(isDeleteMode);
        h.setLocalStudyItem(items.get(position));
        h.setOnItemClickListener(mListener);
        h.setOnDeleteModeItemClickListener(mDeleteModeListener);
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
}
