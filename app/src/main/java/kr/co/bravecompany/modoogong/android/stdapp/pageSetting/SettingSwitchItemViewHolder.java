package kr.co.bravecompany.modoogong.android.stdapp.pageSetting;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.data.SettingData;

/**
 * Created by BraveCompany on 2016. 10. 26..
 */

public class SettingSwitchItemViewHolder extends RecyclerView.ViewHolder{

    private SettingData mSettingData;

    private TextView txtTitle;
    private SwitchButton switchDefault;

    public interface OnSwitchCheckedChangeListener {
        public void onCheckedChanged(View view, int position, boolean isChecked);
    }

    private OnSwitchCheckedChangeListener mSwitchListener;
    public void setOnSwitchCheckedChangeListener(OnSwitchCheckedChangeListener listener) {
        mSwitchListener = listener;
    }

    public SettingSwitchItemViewHolder(View itemView) {
        super(itemView);

        txtTitle = (TextView)itemView.findViewById(R.id.txtTitle);
        switchDefault = (SwitchButton)itemView.findViewById(R.id.switchDefault);

        switchDefault.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(mSwitchListener != null) {
                    mSwitchListener.onCheckedChanged(buttonView, getLayoutPosition(), isChecked);
                }
            }
        });
    }

    public void setSettingSwitchItem(SettingData setting) {
        if(setting != null) {
            mSettingData = setting;

            txtTitle.setText(setting.getTitle());
            switchDefault.setChecked(setting.isChecked());
        }
    }
}

