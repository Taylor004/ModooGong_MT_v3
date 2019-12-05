package kr.co.bravecompany.modoogong.android.stdapp.pageSetting;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.data.SettingData;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.OnItemClickListener;

/**
 * Created by BraveCompany on 2016. 10. 26..
 */

public class SettingItemViewHolder extends RecyclerView.ViewHolder{
    private Context mContext;
    private SettingData mSettingData;

    private TextView txtTitle;
    private TextView txtContent;

    private OnItemClickListener mListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public SettingItemViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        txtTitle = (TextView)itemView.findViewById(R.id.txtTitle);
        txtContent = (TextView)itemView.findViewById(R.id.txtContent);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(v, getLayoutPosition());
                }
            }
        });
    }

    public void setSettingItem(SettingData setting) {
        if(setting != null) {
            mSettingData = setting;

            txtTitle.setText(setting.getTitle());
            String content = setting.getContent();
            if(content == null){
                content = mContext.getString(R.string.setting_memory_default);
            }
            txtContent.setText(content);
        }
    }
}
