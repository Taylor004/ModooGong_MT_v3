package kr.co.bravecompany.modoogong.android.stdapp.viewholder;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.TextView;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.application.ModooGong;
import kr.co.bravecompany.modoogong.android.stdapp.data.MenuData;

/**
 * Created by BraveCompany on 2016. 10. 13..
 */

public class MenuViewHolder extends RecyclerView.ViewHolder implements Checkable{
    private Context mContext;
    private MenuData mMenuData;

    private FrameLayout layoutMenu;
    private TextView txtNewMenu;
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
        txtMenu.setSelected(isChecked());
    }

    private OnItemClickListener mListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public MenuViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        layoutMenu = (FrameLayout)itemView.findViewById(R.id.layoutMenu);
        txtNewMenu = (TextView) itemView.findViewById(R.id.txtNewMenu);
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
            updateMenu(menuData.isNew(), menuData.getName());
        }
    }

    private void updateMenu(boolean isNew, String title) {
        if(ModooGong.isShowMenuTextColorGray){
            txtNewMenu.setTextColor(mContext.getResources().getColorStateList(R.color.menu_item_text_color_gray));
            txtMenu.setTextColor(mContext.getResources().getColorStateList(R.color.menu_item_text_color_gray));
        }else{
            txtNewMenu.setTextColor(mContext.getResources().getColorStateList(R.color.menu_item_text_color));
            txtMenu.setTextColor(mContext.getResources().getColorStateList(R.color.menu_item_text_color));
        }
        if (isNew) {
            txtNewMenu.setVisibility(View.VISIBLE);
            txtMenu.setVisibility(View.GONE);
            txtNewMenu.setText(title);
        } else {
            txtNewMenu.setVisibility(View.GONE);
            txtMenu.setVisibility(View.VISIBLE);
            txtMenu.setText(title);
        }
    }
}
