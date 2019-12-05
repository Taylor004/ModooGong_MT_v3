package kr.co.bravecompany.modoogong.android.stdapp.activity;

import android.Manifest;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.gun0912.tedpermission.PermissionListener;

import java.util.ArrayList;

import kr.co.bravecompany.api.android.stdapp.api.OnResultListener;
import kr.co.bravecompany.api.android.stdapp.api.data.StudyFileDetailResult;
import kr.co.bravecompany.api.android.stdapp.api.requests.StudyFileRequests;
import kr.co.bravecompany.api.android.stdapp.api.utils.APIManager;
import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.pageStudy.StudyFileDetailAdapter;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.data.StudyFileAddData;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.OnItemClickListener;
import okhttp3.Request;

public class StudyFileDetailActivity extends BaseActivity {

    private Toolbar mToolbar;
    private TextView txtTitle;
    private WebView webStudyFile;

    private RecyclerView mListView;
    private StudyFileDetailAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private StudyFileAddData mDownStudyFileAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_file_detail);
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
        title.setText(getString(R.string.study_file_detail_title));

        txtTitle = (TextView)findViewById(R.id.txtTitle);

        //WebView
        webStudyFile = (WebView)findViewById(R.id.webStudyFile);
        webStudyFile.getSettings().setJavaScriptEnabled(true);
        webStudyFile.getSettings().setLoadWithOverviewMode(true);
        webStudyFile.getSettings().setUseWideViewPort(true);
        int size = BraveUtils.convertPxToDp(getApplicationContext(),
                getResources().getDimension(R.dimen.text_medium_s));
        webStudyFile.getSettings().setDefaultFontSize(size);
        webStudyFile.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        mAdapter = new StudyFileDetailAdapter();
        mListView = (RecyclerView)findViewById(R.id.recyclerStudyFileDetail);
        mListView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(getApplicationContext()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mListView.setLayoutManager(mLayoutManager);
    }

    private void initListener() {
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                StudyFileAddData add = mAdapter.getItem(position);
                if(add.getPath() != null){
                    BraveUtils.checkPermission(StudyFileDetailActivity.this, mDownloadPermissionListener,
                            getString(R.string.check_permission_external_storage),
                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    mDownStudyFileAdd = add;
                }
            }
        });
    }

    private void initData() {
        int studyFileNo = getIntent().getIntExtra(Tags.TAG_STUDY_FILE_NO, -1);
        if(studyFileNo != -1){
            loadData(studyFileNo);
        }
    }

    private void loadData(int studyFileNo) {
        startLoading();
        StudyFileRequests.getInstance().requestStudyFileDetail(studyFileNo, new OnResultListener<StudyFileDetailResult>() {
            @Override
            public void onSuccess(Request request, StudyFileDetailResult result) {
                stopLoading();
                setData(result);
            }

            @Override
            public void onFail(Request request, Exception exception) {
                stopLoading();
                BraveUtils.showToast(StudyFileDetailActivity.this, getString(R.string.toast_common_network_fail));
            }
        });
    }

    private void setData(StudyFileDetailResult result) {
        if (result == null) {
            BraveUtils.showToast(StudyFileDetailActivity.this, getString(R.string.toast_common_network_response_null));
            return;
        }
        StudyFileDetailResult.ViewInfoVO viewInfo = result.getView_info();
        if(viewInfo != null){
            txtTitle.setText(BraveUtils.fromHTMLTitle(viewInfo.getTitle()));
            String content = BraveUtils.makeHTML(viewInfo.getContent(), 0, 1.5f);
            if(content != null) {
                webStudyFile.loadDataWithBaseURL("", content, "text/html", "UTF-8", null);
            }
        }
        ArrayList<StudyFileDetailResult.FileInfoVO> fileInfos = result.getFile_info();
        if(fileInfos != null && fileInfos.size() != 0){
            ArrayList<StudyFileAddData> items = new ArrayList<>();
            for(int i=0; i<fileInfos.size(); i++){
                StudyFileDetailResult.FileInfoVO fileInfo = fileInfos.get(i);
                items.add(new StudyFileAddData(i+1, fileInfo.getUpload_file(), fileInfo.getFilename()));
            }
            mAdapter.addAll(items);
        }
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
            if(BraveUtils.checkExternalStorageMounted(StudyFileDetailActivity.this)){
                if(mDownStudyFileAdd != null && mDownStudyFileAdd.getPath()!= null) {
                    String url = APIManager.getFileUrl(getApplicationContext(), mDownStudyFileAdd.getPath());
                    if (url != null) {
                        BraveUtils.doAttachDownload(StudyFileDetailActivity.this, url, mDownStudyFileAdd.getName());
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
