package kr.co.bravecompany.modoogong.android.stdapp.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.MenuIconViewHolder;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.MenuViewHolder;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.OnItemClickListener;
import kr.co.bravecompany.modoogong.android.stdapp.data.MenuData;

/**
 * Created by BraveCompany on 2016. 10. 13..
 */

public class MenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<MenuData> items = new ArrayList<MenuData>();
    private int selectedItem = -1;

    public MenuData getItem(int postion){
        return items.get(postion);
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void add(MenuData MenuData) {
        items.add(MenuData);
        notifyDataSetChanged();
    }

    public void addAll(List<MenuData> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void addAll(int index, List<MenuData> items) {
        if(index == -1){
            return;
        }
        this.items.addAll(index, items);
        notifyDataSetChanged();
    }


    public void setSelect(int position){
        selectedItem = position;
        notifyDataSetChanged();
    }

    public int getSelect(){
        return selectedItem;
    }

    public void updateNew(int index, boolean isNew){
        if(index == -1){
            return;
        }
        items.get(index).setNew(isNew);
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
            case Tags.MENU_VIEW_TYPE.MENU_ICON:
                view = inflater.inflate(R.layout.view_menu_icon_item, parent, false);
                return new MenuIconViewHolder(view);
            case Tags.MENU_VIEW_TYPE.MENU_TEXT:
                view = inflater.inflate(R.layout.view_menu_item, parent, false);
                return new MenuViewHolder(view);
        }
        throw new IllegalArgumentException("invalid position");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = items.get(position).getType();
        boolean isSelected = (selectedItem == position);
        switch (type){
            case Tags.MENU_VIEW_TYPE.MENU_ICON:
                MenuIconViewHolder holder1 = (MenuIconViewHolder)holder;
                holder1.setMenuItem(items.get(position));
                holder1.setOnItemClickListener(mListener);
                holder1.setChecked(isSelected);
                break;
            case Tags.MENU_VIEW_TYPE.MENU_TEXT:
                MenuViewHolder holder2 = (MenuViewHolder)holder;
                holder2.setMenuItem(items.get(position));
                holder2.setOnItemClickListener(mListener);
                holder2.setChecked(isSelected);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
