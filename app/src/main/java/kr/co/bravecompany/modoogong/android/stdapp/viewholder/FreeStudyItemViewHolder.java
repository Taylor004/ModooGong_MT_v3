package kr.co.bravecompany.modoogong.android.stdapp.viewholder;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.sufficientlysecure.htmltextview.DrawTableLinkSpan;
import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import kr.co.bravecompany.api.android.stdapp.api.data.FreeStudyItemVO;
import kr.co.bravecompany.modoogong.android.stdapp.view.ClickableTableSpanImpl;
import kr.co.bravecompany.modoogong.android.stdapp.data.FreeStudyData;

/**
 * Created by BraveCompany on 2016. 10. 25..
 */

public class FreeStudyItemViewHolder extends RecyclerView.ViewHolder {

    protected Context mContext;
    private FreeStudyData mFreeStudyData;

    private TextView txtTitle;
    private HtmlTextView txtContent;

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public FreeStudyItemViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
        txtContent = (HtmlTextView) itemView.findViewById(R.id.txtContent);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(v, getLayoutPosition() - 2);
                }
            }
        });
    }

    public void setFreeStudyItem(FreeStudyData freeStudyItem) {
        if(freeStudyItem != null) {
            mFreeStudyData = freeStudyItem;

            FreeStudyItemVO study = freeStudyItem.getFreeStudyItemVO();
            txtTitle.setText(BraveUtils.fromHTMLTitle(study.getTitle()));

            String content = study.getContent();
            if(content == null){
                txtContent.setVisibility(View.GONE);
            }else{
                txtContent.setClickableTableSpan(new ClickableTableSpanImpl());
                DrawTableLinkSpan drawTableLinkSpan = new DrawTableLinkSpan();
                drawTableLinkSpan.setTableLinkText(mContext.getString(R.string.free_study_view_table));
                drawTableLinkSpan.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                txtContent.setDrawTableLinkSpan(drawTableLinkSpan);
                txtContent.setHtml(BraveUtils.fromHTMLContent(content), new HtmlHttpImageGetter(txtContent, null, true));

                txtContent.setVisibility(View.VISIBLE);
            }
        }
    }
}
