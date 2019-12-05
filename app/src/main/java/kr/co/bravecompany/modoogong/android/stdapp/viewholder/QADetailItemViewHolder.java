package kr.co.bravecompany.modoogong.android.stdapp.viewholder;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.adapter.QADetailSubAdapter;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.manager.MediaPlayerManager;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import kr.co.bravecompany.api.android.stdapp.api.data.OneToOneQADetailVO;
import kr.co.bravecompany.api.android.stdapp.api.data.QADetailVO;
import kr.co.bravecompany.api.android.stdapp.api.data.StudyQADetailVO;
import kr.co.bravecompany.modoogong.android.stdapp.data.QADetailSubData;

/**
 * Created by BraveCompany on 2016. 11. 2..
 */

public class QADetailItemViewHolder extends QADetailAnswerItemViewHolder {
    
    private TextView txtType;
    private TextView txtDate;
    private TextView txtTitle;
    private TextView txtState;

    private View layoutSubList;
    private RecyclerView mSubListView;
    private QADetailSubAdapter mSubAdapter;
    private LinearLayoutManager mSubLayoutManager;


    public QADetailItemViewHolder(View itemView, ArrayList<MediaPlayerManager> playerManagers) {
        super(itemView, playerManagers);

        txtType = (TextView) itemView.findViewById(R.id.txtType);
        txtDate = (TextView) itemView.findViewById(R.id.txtDate);
        txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
        txtState = (TextView) itemView.findViewById(R.id.txtState);

        layoutSubList = itemView.findViewById(R.id.layoutSubList);
        mSubAdapter = new QADetailSubAdapter();
        mSubListView = (RecyclerView)itemView.findViewById(R.id.recyclerQADetailSub);
        mSubListView.setAdapter(mSubAdapter);
        mSubLayoutManager = new LinearLayoutManager(mContext){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mSubListView.setLayoutManager(mSubLayoutManager);
    }

    @Override
    public void setQADetailItem(QADetailVO qaDetailVO) {
        //super.setQADetailItem(qaDetailVO);
        if(qaDetailVO != null) {
            mQADetailVO = qaDetailVO;
            String type = null;
            String writeDate = null;
            String title = null;
            boolean hasReply = false;

            String lectureName = null;
            String bookName = null;
            String bookPage = null;

            String web_content = null;
            String filePath = null;
            String fileName = null;
            String mp3Path = null;

            if (qaDetailVO instanceof StudyQADetailVO) {
                type = ((StudyQADetailVO) qaDetailVO).getCategoryName();
                writeDate = ((StudyQADetailVO) qaDetailVO).getWriteDate();
                title = ((StudyQADetailVO) qaDetailVO).getTitle();
                hasReply = ((StudyQADetailVO) qaDetailVO).isHasReply();

                lectureName = ((StudyQADetailVO) qaDetailVO).getLectureName();
                bookName = ((StudyQADetailVO) qaDetailVO).getBookName();
                bookPage = ((StudyQADetailVO) qaDetailVO).getBookPage();

                web_content = BraveUtils.makeHTMLTextGray(mContext, ((StudyQADetailVO) qaDetailVO).getContent());
                filePath = ((StudyQADetailVO) qaDetailVO).getFilePath();
                fileName= ((StudyQADetailVO) qaDetailVO).getFilePathName();
                mp3Path = ((StudyQADetailVO) qaDetailVO).getMp3Path();
            } else {
                type = ((OneToOneQADetailVO) qaDetailVO).getTypeName();
                writeDate = ((OneToOneQADetailVO) qaDetailVO).getWriteDate();
                title = ((OneToOneQADetailVO) qaDetailVO).getTitle();
                hasReply = ((OneToOneQADetailVO) qaDetailVO).isHasReply();

                web_content = BraveUtils.makeHTMLTextGray(mContext, ((OneToOneQADetailVO) qaDetailVO).getContent());
                filePath = ((OneToOneQADetailVO) qaDetailVO).getFilePath();
                fileName= ((OneToOneQADetailVO) qaDetailVO).getFilePathName();
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

            ArrayList<QADetailSubData> subs = new ArrayList<>();
            if(lectureName != null){
                QADetailSubData sub = new QADetailSubData();
                sub.setTitle(mContext.getString(R.string.study_qa_menu_lecture_name));
                sub.setValue(lectureName);
                subs.add(sub);
            }
            if(bookName != null){
                QADetailSubData sub = new QADetailSubData();
                sub.setTitle(mContext.getString(R.string.study_qa_menu_book_name));
                sub.setValue(bookName);
                subs.add(sub);
            }
            if(bookPage != null){
                QADetailSubData sub = new QADetailSubData();
                sub.setTitle(mContext.getString(R.string.study_qa_menu_book_page));
                sub.setValue(bookPage);
                subs.add(sub);
            }
            if(subs != null && subs.size() != 0) {
                mSubAdapter.addAll(subs);
                layoutSubList.setVisibility(View.VISIBLE);
            }else{
                layoutSubList.setVisibility(View.GONE);
            }

            if (web_content != null) {
                webContent.loadDataWithBaseURL("", web_content, "text/html", "UTF-8", null);
            }

            if(filePath != null){
                checkAdds(filePath, fileName);
            }
            if(mp3Path != null){
                String mp3Name = null;
                String extension = BraveUtils.getExtension(mp3Path);
                if(!extension.equals("")){
                    mp3Name = mContext.getString(R.string.qa_detail_attach_voice) + extension;
                }
                checkAdds(mp3Path, mp3Name);
            }
        }
    }
}
