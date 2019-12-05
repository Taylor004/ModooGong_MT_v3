package kr.co.bravecompany.modoogong.android.stdapp.activity;

import android.Manifest;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.CustomEvent;
import com.gun0912.tedpermission.PermissionListener;

import java.util.ArrayList;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.api.android.stdapp.api.OnResultListener;
import kr.co.bravecompany.api.android.stdapp.api.requests.NoticeRequests;
import kr.co.bravecompany.api.android.stdapp.api.utils.APIManager;
import kr.co.bravecompany.api.android.stdapp.api.data.NoticeDetailResult;
import kr.co.bravecompany.modoogong.android.stdapp.config.AnalysisTags;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import okhttp3.Request;

public class NoticeDetailActivity extends BaseActivity {

    private Toolbar mToolbar;
    private TextView txtTypeNone;
    private TextView txtTypeFirst;
    private TextView txtTypeEvent;
    private TextView txtTitle;
    private TextView txtDate;
    private TextView txtFileName;
    private WebView webNotice;

    private String mFilePath = null;
    private String mFileName = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemBar(true);
        setContentView(R.layout.activity_notice_detail);
        initLayout();
        initListener();
        initData();
    }

    private void initLayout() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        TextView title = (TextView)mToolbar.findViewById(R.id.toolbar_title);
        title.setText(getString(R.string.notice_detail_toolbar_title));

        txtTypeNone = (TextView)findViewById(R.id.txtTypeNone);
        txtTypeFirst = (TextView)findViewById(R.id.txtTypeFirst);
        txtTypeEvent = (TextView)findViewById(R.id.txtTypeEvent);
        txtTitle = (TextView)findViewById(R.id.txtTitle);
        txtDate = (TextView)findViewById(R.id.txtDate);
        txtFileName = (TextView)findViewById(R.id.txtFileName);
        txtFileName.setVisibility(View.GONE);
        //WebView
        webNotice = (WebView)findViewById(R.id.webNotice);
        webNotice.getSettings().setJavaScriptEnabled(true);
        webNotice.getSettings().setLoadWithOverviewMode(true);
        webNotice.getSettings().setUseWideViewPort(true);
        int size = BraveUtils.convertPxToDp(getApplicationContext(),
                getResources().getDimension(R.dimen.text_small));
        webNotice.getSettings().setDefaultFontSize(size);
        webNotice.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
    }

    private void initListener() {
        txtFileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //BraveUtils.showToast(NoticeDetailActivity.this, getString(R.string.toast_notice_detail_attach));
                if(mFilePath != null){
                    BraveUtils.checkPermission(NoticeDetailActivity.this, mDownloadPermissionListener,
                            getString(R.string.check_permission_external_storage),
                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
            }
        });
    }

    private void initData() {
        int noticeNo = getIntent().getIntExtra(Tags.TAG_NOTICE_NO, -1);
        if(noticeNo != -1){
            loadData(noticeNo);
        }
    }

    private void loadData(int noticeNo){
        startLoading();
        NoticeRequests.getInstance().requestNoticeDetail(noticeNo, new OnResultListener<NoticeDetailResult>(){

            @Override
            public void onSuccess(Request request, NoticeDetailResult result) {
                stopLoading();
                setData(result);
            }

            @Override
            public void onFail(Request request, Exception exception) {
                stopLoading();
                BraveUtils.showToast(NoticeDetailActivity.this, getString(R.string.toast_common_network_fail));
            }
        });
    }

    private void setData(NoticeDetailResult result) {
        if(result == null){
            BraveUtils.showToast(NoticeDetailActivity.this, getString(R.string.toast_common_network_response_null));
            return;
        }
        String title = BraveUtils.fromHTMLTitle(result.getTitle());
        txtTitle.setText(title);
        txtDate.setText(result.getWriteDate());
        BraveUtils.updateNoticeTypeView(txtTypeNone, txtTypeFirst, txtTypeEvent, result.getType());

        String filePath = result.getFilePath();
        if(filePath != null){
            String fileName = result.getFilePathName();
            if(fileName == null) {
                fileName = BraveUtils.getFileName(filePath);
            }
            txtFileName.setVisibility(View.VISIBLE);
            txtFileName.setText(fileName);
            mFilePath = filePath;
            mFileName = fileName;
        }

        int padding = BraveUtils.convertPxToDp(getApplicationContext(), getResources().getDimension(R.dimen.common_margin_medium));
        String content = BraveUtils.makeHTML(result.getContent(), padding, 1.5f);
        if(content != null) {
            webNotice.loadDataWithBaseURL("", content, "text/html", "UTF-8", null);
        }

        ContentViewEvent event = new ContentViewEvent()
                .putCustomAttribute(AnalysisTags.VIEW, "notice")
                .putContentId(String.valueOf(result.getNoticeNo()));
        if(title != null){
            event.putContentName(title);
        }
        Answers.getInstance().logContentView(event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    // =============================================================================
    // Check Permission
    // =============================================================================

    private PermissionListener mDownloadPermissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            if(BraveUtils.checkExternalStorageMounted(NoticeDetailActivity.this)){
                if(mFilePath != null) {
                    String url = APIManager.getFileUrl(getApplicationContext(), mFilePath);
                    if (url != null) {
                        BraveUtils.doAttachDownload(NoticeDetailActivity.this, url, mFileName);

                        CustomEvent event = new CustomEvent(AnalysisTags.NOTICEDETAIL)
                                .putCustomAttribute(AnalysisTags.ACTION, "download_attach");
                        String fileName = mFileName;
                        if(fileName == null){
                            fileName = BraveUtils.getFileName(url);
                        }
                        event.putCustomAttribute(AnalysisTags.VALUE, fileName);
                        Answers.getInstance().logCustom(event);
                    }
                }
            }
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            //do nothing
        }
    };
}
