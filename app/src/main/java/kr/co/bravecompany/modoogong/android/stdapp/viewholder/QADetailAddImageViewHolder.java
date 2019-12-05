package kr.co.bravecompany.modoogong.android.stdapp.viewholder;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import kr.co.bravecompany.modoogong.android.stdapp.data.QADetailAddData;

/**
 * Created by BraveCompany on 2017. 2. 1..
 */

public class QADetailAddImageViewHolder extends RecyclerView.ViewHolder{

    protected Context mContext;
    private QADetailAddData mQADetailAddData;

    private ImageView imgThum;
    private TextView txtName;

    private OnItemClickListener mListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public QADetailAddImageViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        imgThum = (ImageView)itemView.findViewById(R.id.imgThum);
        imgThum.setScaleType(ImageView.ScaleType.CENTER_CROP);
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

    public void setQADetailAddImage(QADetailAddData qaDetailAddImage) {
        if(qaDetailAddImage != null) {
            mQADetailAddData = qaDetailAddImage;

            int size = (int)mContext.getResources().getDimension(R.dimen.common_row_height_s);
            BraveUtils.setImage(imgThum, qaDetailAddImage.getPath(), size, size);
            txtName.setText(qaDetailAddImage.getName());
        }
    }
}
