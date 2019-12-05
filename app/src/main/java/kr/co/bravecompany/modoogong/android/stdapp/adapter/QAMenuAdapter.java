package kr.co.bravecompany.modoogong.android.stdapp.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.api.android.stdapp.api.data.BoardCateVO;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.OnItemClickListener;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.QAMenuViewHolder;
import kr.co.bravecompany.modoogong.android.stdapp.data.QAMenuData;

/**
 * Created by BraveCompany on 2016. 10. 26..
 */

public class QAMenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected List<QAMenuData> items = new ArrayList<QAMenuData>();
    protected ArrayList<BoardCateVO> mQAType;

    public void setQAType(ArrayList<BoardCateVO> qaType){
        mQAType = qaType;
    }

    public int checkQAMenuIndexEmpty(){
        for(int i=0; i<getItemCount(); i++){
            if(getQAMenuIndex(i) == -1){
                QAMenuData m = items.get(i);
                if(m.isEnable() && m.isRequire()) {
                    return i;
                }
            }
        }
        return -1;
    }

    public boolean checkQAMenuPrevIndexIsEmpty(int position){
        QAMenuData menu = items.get(position);
        for(int i=0; i<position; i++){
            if(getQAMenuIndex(i) == -1){
                QAMenuData prev = items.get(i);
                if(menu.getTag() == prev.getTag()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void resetQAMenuIndexValue(){
        for(QAMenuData menu : items){
            menu.setIndex(-1);
            menu.setValue("");
        }
        notifyDataSetChanged();
    }

    public void resetQAMenuIndexValue(int position){
        updateQAMenuIndexValue(position, -1, "");
    }

    public int getQAMenuIndex(int position){
        return items.get(position).getIndex();
    }

    public String getQAMenuValue(int position){
        return items.get(position).getValue();
    }

    public void updateQAMenuIndexValue(int position, int index, String value){
        QAMenuData menu = items.get(position);
        menu.setIndex(index);
        menu.setValue(value);

        for(int i=position+1; i<items.size(); i++){
            QAMenuData m = items.get(i);
            if(menu.getTag() == m.getTag()) {
                m.setIndex(-1);
                m.setValue("");
            }
        }

        notifyDataSetChanged();
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void add(QAMenuData QAMenuData) {
        items.add(QAMenuData);
        notifyDataSetChanged();
    }

    public void addAll(List<QAMenuData> items) {
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
        View view = inflater.inflate(R.layout.view_qa_menu, parent, false);
        return new QAMenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        QAMenuViewHolder h = (QAMenuViewHolder)holder;
        h.setQAMenuItem(items.get(position));
        h.setOnItemClickListener(mListener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}