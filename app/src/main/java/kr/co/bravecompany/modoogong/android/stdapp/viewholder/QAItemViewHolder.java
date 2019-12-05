package kr.co.bravecompany.modoogong.android.stdapp.viewholder;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.api.android.stdapp.api.data.OneToOneQAItemVO;
import kr.co.bravecompany.api.android.stdapp.api.data.QAItemVO;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import kr.co.bravecompany.modoogong.android.stdapp.data.QAData;
import kr.co.bravecompany.api.android.stdapp.api.data.StudyQAItemVO;

/**
 * Created by BraveCompany on 2016. 11. 1..
 */

public class QAItemViewHolder extends RecyclerView.ViewHolder {
    private Context mContext;
    private QAData mQAData;

    private TextView txtType;
    private TextView txtDate;
    private TextView txtTitle;
    private TextView txtState;

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public QAItemViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        txtType = (TextView) itemView.findViewById(R.id.txtType);
        txtDate = (TextView) itemView.findViewById(R.id.txtDate);
        txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
        txtState = (TextView) itemView.findViewById(R.id.txtState);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(v, getLayoutPosition());
                }
            }
        });
    }

    public void setQAItem(QAData QAData) {
        if(QAData != null) {
            mQAData = QAData;
            QAItemVO qaItemVO = QAData.getQaItemVO();
            String type = null;
            String writeDate = null;
            String title = null;
            boolean hasReply = false;

            if (qaItemVO instanceof StudyQAItemVO) {
                type = ((StudyQAItemVO) qaItemVO).getCategoryName();
                writeDate = ((StudyQAItemVO) qaItemVO).getWriteDate();
                title = ((StudyQAItemVO) qaItemVO).getTitle();
                hasReply = ((StudyQAItemVO) qaItemVO).isHasReply();
            } else {
                type = ((OneToOneQAItemVO) qaItemVO).getTypeName();
                writeDate = ((OneToOneQAItemVO) qaItemVO).getWriteDate();
                title = ((OneToOneQAItemVO) qaItemVO).getTitle();
                hasReply = ((OneToOneQAItemVO) qaItemVO).isHasReply();
            }

            if(type == null){
                type = mContext.getString(R.string.qa_type_default);
            }
            txtType.setText(type);
            txtDate.setText(writeDate);
            txtTitle.setText(BraveUtils.fromHTMLTitle(title));

            List<String> states = Arrays.asList(
                    mContext.getResources().getStringArray(R.array.qa_state_type));
            if (!hasReply) {
                txtState.setText(states.get(Tags.QA_STATE_TYPE.QA_STATE_ING));
            } else {
                txtState.setText(states.get(Tags.QA_STATE_TYPE.QA_STATE_END));
            }
        }
    }
}
