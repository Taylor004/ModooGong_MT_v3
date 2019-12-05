package kr.co.bravecompany.modoogong.android.stdapp.viewholder;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.data.QAMenuData;

/**
 * Created by BraveCompany on 2016. 10. 26..
 */

public class QAMenuViewHolder extends RecyclerView.ViewHolder{

    private QAMenuData mQAMenuData;
    private Context mContext;

    private TextView txtTitle;
    private TextView txtTitleSub;
    private TextView txtValue;

    private OnItemClickListener mListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public QAMenuViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        txtTitle = (TextView)itemView.findViewById(R.id.txtTitle);
        txtTitleSub = (TextView)itemView.findViewById(R.id.txtTitleSub);
        txtValue = (TextView)itemView.findViewById(R.id.txtValue);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    if(mQAMenuData.isEnable()) {
                        mListener.onItemClick(v, getLayoutPosition());
                    }
                }
            }
        });
    }

    public void setQAMenuItem(QAMenuData menu) {
        if(menu != null) {
            mQAMenuData = menu;
            txtTitle.setText(menu.getTitle());
            txtTitle.setEnabled(menu.isEnable());
            String subTitle = "";
            if(!menu.isRequire()){
                subTitle = mContext.getString(R.string.qa_menu_sub_title_not_required);
            }
            txtTitleSub.setText(subTitle);
            txtTitleSub.setEnabled(menu.isEnable());
            txtValue.setText(menu.getValue());
        }
    }
}

