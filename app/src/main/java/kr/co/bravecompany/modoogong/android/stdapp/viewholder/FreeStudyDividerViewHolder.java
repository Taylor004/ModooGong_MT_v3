package kr.co.bravecompany.modoogong.android.stdapp.viewholder;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import kr.co.bravecompany.modoogong.android.stdapp.R;

/**
 * Created by BraveCompany on 2016. 12. 29..
 */

public class FreeStudyDividerViewHolder extends RecyclerView.ViewHolder {
    private Context mContext;
    private TextView txtTitle;

    public FreeStudyDividerViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
    }

    public void setFreeStudyDivider(String dividerTitle) {
        if (dividerTitle != null) {
            txtTitle.setText(String.format(mContext.getString(R.string.free_study_divider), dividerTitle));
        }
    }
}
