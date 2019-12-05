package kr.co.bravecompany.modoogong.android.stdapp.viewholder;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import kr.co.bravecompany.modoogong.android.stdapp.data.QADetailSubData;

/**
 * Created by BraveCompany on 2017. 4. 17..
 */

public class QADetailSubViewHolder extends RecyclerView.ViewHolder{

    private QADetailSubData mQADetailSubData;
    private Context mContext;

    private TextView txtTitle;
    private TextView txtValue;


    public QADetailSubViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        txtTitle = (TextView)itemView.findViewById(R.id.txtTitle);
        txtValue = (TextView)itemView.findViewById(R.id.txtValue);
    }

    public void setQADetailSub(QADetailSubData menu) {
        if(menu != null) {
            mQADetailSubData = menu;
            txtTitle.setText(String.format(mContext.getString(R.string.study_qa_detail_sub), menu.getTitle()));
            txtValue.setText(BraveUtils.fromHTMLTitle(menu.getValue()));
        }
    }
}
