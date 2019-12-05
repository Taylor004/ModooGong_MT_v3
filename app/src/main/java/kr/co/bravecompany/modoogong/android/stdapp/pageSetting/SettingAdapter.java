package kr.co.bravecompany.modoogong.android.stdapp.pageSetting;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.data.SettingData;

/**
 * Created by BraveCompany on 2016. 10. 26..
 */

public class SettingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SettingData> items = new ArrayList<SettingData>();

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void add(SettingData SettingData) {
        items.add(SettingData);
        notifyDataSetChanged();
    }

    public void addAll(List<SettingData> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    private SettingSwitchItemViewHolder.OnSwitchCheckedChangeListener mSwitchListener;
    public void setOnSwitchCheckedChangeListener(SettingSwitchItemViewHolder.OnSwitchCheckedChangeListener listener) {
        mSwitchListener = listener;
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
            case Tags.SETTING_TYPE.SETTING_SWITCH:
                view = inflater.inflate(R.layout.view_setting_switch_item, parent, false);
                return new SettingSwitchItemViewHolder(view);
            case Tags.SETTING_TYPE.SETTING_TEXT:
                view = inflater.inflate(R.layout.view_setting_item, parent, false);
                return new SettingItemViewHolder(view);
            case Tags.SETTING_TYPE.SETTING_DIVIDER:
                view = inflater.inflate(R.layout.view_setting_divider, parent, false);
                return new SettingDividerViewHolder(view);
        }
        throw new IllegalArgumentException("invalid position");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = items.get(position).getType();
        switch (type){
            case Tags.SETTING_TYPE.SETTING_SWITCH:
                SettingSwitchItemViewHolder holder1 = (SettingSwitchItemViewHolder)holder;
                holder1.setSettingSwitchItem(items.get(position));
                holder1.setOnSwitchCheckedChangeListener(mSwitchListener);
                break;
            case Tags.SETTING_TYPE.SETTING_TEXT:
                SettingItemViewHolder holder2 = (SettingItemViewHolder)holder;
                holder2.setSettingItem(items.get(position));
                break;
            case Tags.SETTING_TYPE.SETTING_DIVIDER:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
