package kr.co.bravecompany.modoogong.android.stdapp.viewholder;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.application.ModooGong;
import kr.co.bravecompany.modoogong.android.stdapp.data.MenuData;

/**
 * Created by BraveCompany on 2016. 10. 13..
 */

public class MenuIconViewHolder extends RecyclerView.ViewHolder implements Checkable {
    private Context mContext;
    private MenuData mMenuData;

    private FrameLayout layoutMenu;
    private ImageView imgMenu;
    private TextView txtMenu;

    private boolean isChecked = false;

    @Override
    public void setChecked(boolean checked) {
        if (isChecked != checked) {
            isChecked = checked;
            setMenuChecked();
        }
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked);
    }

    private void setMenuChecked(){
        layoutMenu.setSelected(isChecked());
        imgMenu.setSelected(isChecked());
        txtMenu.setSelected(isChecked());
    }

    private OnItemClickListener mListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public MenuIconViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        layoutMenu = (FrameLayout)itemView.findViewById(R.id.layoutMenu);
        imgMenu = (ImageView)itemView.findViewById(R.id.imgMenu);
        txtMenu = (TextView)itemView.findViewById(R.id.txtMenu);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(v, getLayoutPosition());
                }
            }
        });
    }

    public void setMenuItem(MenuData menuData) {
        if(menuData != null) {
            this.mMenuData = menuData;
            if(ModooGong.isShowMenuTextColorGray){
                txtMenu.setTextColor(mContext.getResources().getColorStateList(R.color.menu_item_text_color_gray));
            }else{
                txtMenu.setTextColor(mContext.getResources().getColorStateList(R.color.menu_item_text_color));
            }
            imgMenu.setBackgroundResource(menuData.getImage());
            txtMenu.setText(menuData.getName());
        }
    }
}
