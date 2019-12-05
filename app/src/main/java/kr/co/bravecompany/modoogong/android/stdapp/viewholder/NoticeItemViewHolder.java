package kr.co.bravecompany.modoogong.android.stdapp.viewholder;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import kr.co.bravecompany.api.android.stdapp.api.data.NoticeItemVO;
import kr.co.bravecompany.modoogong.android.stdapp.data.NoticeData;

/**
 * Created by BraveCompany on 2016. 10. 25..
 */

public class NoticeItemViewHolder extends RecyclerView.ViewHolder {

    private NoticeData mNoticeData;
    private Context mContext;

    private TextView txtTypeNone;
    private TextView txtTypeFirst;
    private TextView txtTypeEvent;
    private TextView txtTitle;

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public NoticeItemViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        txtTypeNone = (TextView) itemView.findViewById(R.id.txtTypeNone);
        txtTypeFirst = (TextView) itemView.findViewById(R.id.txtTypeFirst);
        txtTypeEvent = (TextView) itemView.findViewById(R.id.txtTypeEvent);
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

    public void setNoticeItem(NoticeData notice) {
        if(notice != null) {
            mNoticeData = notice;
            NoticeItemVO noticeItemVO = notice.getNoticeItemVO();

            BraveUtils.updateNoticeTypeView(txtTypeNone, txtTypeFirst, txtTypeEvent, noticeItemVO.getType());
            String title = BraveUtils.fromHTMLTitle(noticeItemVO.getTitle());
            if(notice.isNew()){
                txtTitle.setText(getSpannableTitle(title));
            }else{
                txtTitle.setText(title);
            }
        }
    }

    private SpannableStringBuilder getSpannableTitle(String title){
        SpannableStringBuilder s = new SpannableStringBuilder(title);
        s.append("\u0020X");
        int end = s.length();
        s.setSpan(new ImageSpan(mContext, R.drawable.ic_notice, ImageSpan.ALIGN_BASELINE),
                end-1, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return s;
    }
}
