package kr.co.bravecompany.modoogong.android.stdapp.viewholder;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
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

public class FreeStudyHeaderViewHolder extends RecyclerView.ViewHolder {

    protected Context mContext;
    private FreeStudyData mFreeStudyData;

    private TextView txtTitle;
    private HtmlTextView txtContent;
    private ImageView imgThum;

    public interface OnHeaderItemClickListener {
        public void onItemClick(View view, FreeStudyData freeStudy);
    }
    private OnHeaderItemClickListener mListener;

    public void setOnHeaderItemClickListener(OnHeaderItemClickListener listener) {
        mListener = listener;
    }

    public FreeStudyHeaderViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
        txtContent = (HtmlTextView) itemView.findViewById(R.id.txtContent);

        imgThum = (ImageView) itemView.findViewById(R.id.imgThum);
        imgThum.setScaleType(ImageView.ScaleType.CENTER_CROP);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(v, mFreeStudyData);
                }
            }
        });
    }

    public void setFreeStudyHeaderItem(FreeStudyData freeStudyHeader) {
        if(freeStudyHeader != null) {
            mFreeStudyData = freeStudyHeader;

            FreeStudyItemVO study = freeStudyHeader.getFreeStudyItemVO();
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

               // content = "hi";
                txtContent.setHtml(BraveUtils.fromHTMLContent(content), new HtmlHttpImageGetter(txtContent, null, true));

                txtContent.setVisibility(View.VISIBLE);
            }

            //for test

            String thumb2 = study.getThumb2();
            if(thumb2 != null && thumb2.length() != 0){
                BraveUtils.setImage(imgThum, thumb2);
            }else{
                BraveUtils.setImage(imgThum, study.getThumb());
            }
        }
    }
}
