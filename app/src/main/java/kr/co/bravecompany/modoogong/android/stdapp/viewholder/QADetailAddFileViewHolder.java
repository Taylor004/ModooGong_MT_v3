package kr.co.bravecompany.modoogong.android.stdapp.viewholder;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.data.QADetailAddData;

/**
 * Created by BraveCompany on 2017. 2. 1..
 */

public class QADetailAddFileViewHolder extends RecyclerView.ViewHolder{

    protected Context mContext;
    private QADetailAddData mQADetailAddData;

    private TextView txtName;

    private OnItemClickListener mListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public QADetailAddFileViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        txtName = (TextView)itemView.findViewById(R.id.txtName);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(v, getLayoutPosition());
                }
            }
        });
    }

    public void setQADetailAddFile(QADetailAddData qaDetailAddFile) {
        if(qaDetailAddFile != null) {
            mQADetailAddData = qaDetailAddFile;

            txtName.setText(qaDetailAddFile.getName());
        }
    }
}
