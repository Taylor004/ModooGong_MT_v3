package kr.co.bravecompany.modoogong.android.stdapp.viewholder;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.api.android.stdapp.api.data.FreeExplainStudyItemVO;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import kr.co.bravecompany.modoogong.android.stdapp.data.FreeExplainStudyData;

/**
 * Created by BraveCompany on 2016. 12. 16..
 */

public class FreeExplainStudyItemViewHolder extends RecyclerView.ViewHolder{

    private FreeExplainStudyData mFreeExplainStudyData;
    private Context mContext;

    private TextView txtTitle;

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public FreeExplainStudyItemViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(v, getLayoutPosition());
                }
            }
        });
    }

    public void setFreeExplainStudyItem(FreeExplainStudyData freeStudyItem) {
        if(freeStudyItem != null) {
            mFreeExplainStudyData = freeStudyItem;

            FreeExplainStudyItemVO study = freeStudyItem.getFreeExplainStudyItemVO();
            String name = BraveUtils.fromHTMLTitle(study.getExamName());
            txtTitle.setText(name);
        }
    }
}

