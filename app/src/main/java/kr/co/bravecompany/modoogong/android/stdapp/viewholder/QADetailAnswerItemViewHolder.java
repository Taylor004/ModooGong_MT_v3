package kr.co.bravecompany.modoogong.android.stdapp.viewholder;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;

import java.util.ArrayList;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.adapter.QADetailAddAdapter;
import kr.co.bravecompany.api.android.stdapp.api.utils.APIManager;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.manager.MediaPlayerManager;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import kr.co.bravecompany.api.android.stdapp.api.data.OneToOneQADetailVO;
import kr.co.bravecompany.modoogong.android.stdapp.data.QADetailAddData;
import kr.co.bravecompany.api.android.stdapp.api.data.QADetailVO;
import kr.co.bravecompany.api.android.stdapp.api.data.StudyQADetailVO;

/**
 * Created by BraveCompany on 2016. 11. 2..
 */

public class QADetailAnswerItemViewHolder extends RecyclerView.ViewHolder {

    protected Context mContext;
    protected QADetailVO mQADetailVO;

    protected WebView webContent;

    private RecyclerView mListView;
    private QADetailAddAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;


    public interface OnImagePreviewClickListener {
        public void onImagePreviewClickListener(View view, QADetailAddData item);
    }
    public interface OnFilePreviewClickListener {
        public void onFilePreviewClickListener(View view, QADetailAddData item);
    }
    private OnImagePreviewClickListener mImagePreviewClickListener;
    public void setOnImagePreviewClickListener(OnImagePreviewClickListener listener) {
        mImagePreviewClickListener = listener;
    }
    private OnFilePreviewClickListener mFilePreviewClickListener;
    public void setOnFilePreviewClickListener(OnFilePreviewClickListener listener) {
        mFilePreviewClickListener = listener;
    }

    public QADetailAnswerItemViewHolder(View itemView, ArrayList<MediaPlayerManager> playerManagers) {
        super(itemView);
        mContext = itemView.getContext();

        mAdapter = new QADetailAddAdapter(playerManagers);
        mListView = (RecyclerView)itemView.findViewById(R.id.recyclerQADetailAdd);
        mListView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(mContext){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mListView.setLayoutManager(mLayoutManager);

        //WebView
        webContent = (WebView) itemView.findViewById(R.id.webContent);
        if(webContent != null) {
            webContent.getSettings().setJavaScriptEnabled(true);
            webContent.getSettings().setLoadWithOverviewMode(true);
            webContent.getSettings().setUseWideViewPort(true);
            int size = BraveUtils.convertPxToDp(mContext,
                    mContext.getResources().getDimension(R.dimen.text_medium));
            webContent.getSettings().setDefaultFontSize(size);
            webContent.setBackgroundColor(Color.TRANSPARENT);
            webContent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return true;
                }
            });
        }

        initListener();
    }

    private void initListener(){
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                QADetailAddData item = mAdapter.getItem(position);
                if(item != null){
                    int type = item.getType();
                    switch (type){
                        case Tags.QA_UPLOAD_TYPE.QA_UPLOAD_IMAGE:
                            mImagePreviewClickListener.onImagePreviewClickListener(view, item);
                            break;
                        case Tags.QA_UPLOAD_TYPE.QA_UPLOAD_FILE:
                            mFilePreviewClickListener.onFilePreviewClickListener(view, item);
                            break;
                    }
                }
            }
        });
    }

    public void setQADetailItem(QADetailVO qaDetailVO) {
        if(qaDetailVO != null) {
            mQADetailVO = qaDetailVO;
            String filePath = null;
            String fileName = null;
            String mp3Path = null;
            String web_content = null;

            if (qaDetailVO instanceof StudyQADetailVO) {
                web_content = BraveUtils.makeHTMLTextGray(mContext, ((StudyQADetailVO) qaDetailVO).getReplyContent());
                filePath = ((StudyQADetailVO) qaDetailVO).getReplyFilePath();
                fileName= ((StudyQADetailVO) qaDetailVO).getReplyFilePathName();
                mp3Path = ((StudyQADetailVO) qaDetailVO).getReplyMp3Path();
            } else {
                web_content = BraveUtils.makeHTMLTextGray(mContext, ((OneToOneQADetailVO) qaDetailVO).getReplyContent());
                filePath = ((OneToOneQADetailVO) qaDetailVO).getReplyFilePath();
                fileName= ((OneToOneQADetailVO) qaDetailVO).getReplyFilePathName();
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
                    mp3Name = mContext.getString(R.string.qa_detail_attach_reply_voice) + extension;
                }
                checkAdds(mp3Path, mp3Name);
            }
        }
    }

    protected void checkAdds(String path, String name){
        path = APIManager.getFileUrl(mContext, path);
        int type = Tags.QA_UPLOAD_TYPE.QA_UPLOAD_NONE;

        if(path != null) {
            if (BraveUtils.checkImageFormat(path)) {
                type = Tags.QA_UPLOAD_TYPE.QA_UPLOAD_IMAGE;
            } else if (BraveUtils.checkAudioFormat(path)) {
                type = Tags.QA_UPLOAD_TYPE.QA_UPLOAD_VOICE;
            } else {
                type = Tags.QA_UPLOAD_TYPE.QA_UPLOAD_FILE;
            }
        }
        ArrayList<QADetailAddData> adds = new ArrayList<>();
        if(type != Tags.QA_UPLOAD_TYPE.QA_UPLOAD_NONE){
            QADetailAddData add = new QADetailAddData();
            add.setType(type);
            add.setPath(path);
            if(name == null){
                name = BraveUtils.getFileName(path);
            }
            add.setName(name);
            adds.add(add);
        }
        mAdapter.addAll(adds);
    }
}
