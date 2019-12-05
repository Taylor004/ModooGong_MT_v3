package kr.co.bravecompany.modoogong.android.stdapp.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.gun0912.tedpermission.PermissionListener;

import java.util.ArrayList;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.adapter.QADetailAdapter;
import kr.co.bravecompany.api.android.stdapp.api.OnResultListener;
import kr.co.bravecompany.api.android.stdapp.api.requests.QARequests;
import kr.co.bravecompany.modoogong.android.stdapp.config.AnalysisTags;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.manager.MediaPlayerManager;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.QADetailAnswerItemViewHolder;
import kr.co.bravecompany.api.android.stdapp.api.data.OneToOneQADetailVO;
import kr.co.bravecompany.modoogong.android.stdapp.data.QADetailAddData;
import kr.co.bravecompany.api.android.stdapp.api.data.QADetailVO;
import kr.co.bravecompany.api.android.stdapp.api.data.StudyQADetailVO;
import okhttp3.Request;

public class QADetailActivity extends BaseActivity {

    private int index = Tags.QA_TYPE.QA_ONE_TO_ONE;
    private int qaNo = -1;
    private FrameLayout contentCall;

    private Toolbar mToolbar;
    private TextView btnEdit;

    private RecyclerView mListView;
    private QADetailAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private TextView btnCall;

    //player
    private ArrayList<MediaPlayerManager> mPlayerManagers;

    private QADetailAddData mSelectedAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemBar(true);

        setContentView(R.layout.activity_qa_detail);

        index = getIntent().getIntExtra(Tags.TAG_QA_TYPE, Tags.QA_TYPE.QA_ONE_TO_ONE);
        qaNo = getIntent().getIntExtra(Tags.TAG_QA_NO, -1);

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

        contentCall = (FrameLayout)findViewById(R.id.contentCall);
        if(index == Tags.QA_TYPE.QA_ONE_TO_ONE){
            title.setText(getString(R.string.one_to_one_qa_detail_toolbar_title));
            contentCall.setVisibility(View.VISIBLE);
        }else{
            title.setText(getString(R.string.study_qa_detail_toolbar_title));
            contentCall.setVisibility(View.GONE);
        }

        btnEdit = (TextView)mToolbar.findViewById(R.id.btnEdit);

        //player
        mPlayerManagers = new ArrayList<MediaPlayerManager>();

        mAdapter = new QADetailAdapter(mPlayerManagers);
        mListView = (RecyclerView)findViewById(R.id.recyclerQADetail);
        mListView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mListView.setLayoutManager(mLayoutManager);

        btnCall = (TextView)findViewById(R.id.btnCall);

    }

    private void initListener() {

        mAdapter.setOnImagePreviewClickListener(new QADetailAnswerItemViewHolder.OnImagePreviewClickListener() {
            @Override
            public void onImagePreviewClickListener(View view, QADetailAddData item) {
                if(item != null) {
                    String path = item.getPath();
                    if (path != null && path.length() != 0) {
                        BraveUtils.goPhotoView(QADetailActivity.this, path);

                        Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.QADETAIL)
                                .putCustomAttribute(AnalysisTags.ACTION, "go_photo_view"));
                    }
                }
            }
        });

        mAdapter.setOnFilePreviewClickListener(new QADetailAnswerItemViewHolder.OnFilePreviewClickListener() {
            @Override
            public void onFilePreviewClickListener(View view, QADetailAddData item) {
                if(item != null) {
                    mSelectedAdd = item;
                    BraveUtils.checkPermission(QADetailActivity.this, mDownloadPermissionListener,
                            getString(R.string.check_permission_external_storage),
                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final QADetailVO questionItem = mAdapter.getQuestionItem();
                if(questionItem != null) {
                    boolean isEditer = true;
                    if(questionItem instanceof StudyQADetailVO){
                        isEditer = (((StudyQADetailVO)questionItem).isEditer() == 1);
                    }else{
                        isEditer = (((OneToOneQADetailVO)questionItem).isEditer() == 1);
                    }
                    if(isEditer) {
                        BraveUtils.showAlertDialogOkCancel(QADetailActivity.this, getString(R.string.dialog_qa_edit),
                                new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        goEdit(questionItem);
                                    }
                                }, null);
                    }else{
                        goEdit(questionItem);
                    }
                }
            }
        });

        mListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                /*
                if(index == Tags.QA_TYPE.QA_ONE_TO_ONE && recyclerView.getChildAt(0) != null) {
                    int firstComplete = mLayoutManager.findFirstCompletelyVisibleItemPosition();
                    if(firstComplete == 0 && newState == RecyclerView.SCROLL_STATE_SETTLING){
                        BraveUtils.setVisibilityBottomView(contentCall, View.VISIBLE);
                    }else if(newState == RecyclerView.SCROLL_STATE_DRAGGING){
                        BraveUtils.setVisibilityBottomView(contentCall, View.GONE);
                    }
                }
                */
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //BraveUtils.goCall(QADetailActivity.this, getString(R.string.ono_to_ono_qa_call_uri));
                BraveUtils.checkPermission(QADetailActivity.this, mCallPermissionListener,
                        getString(R.string.check_permission_call_phone),
                        Manifest.permission.CALL_PHONE);
            }
        });
    }

    private void initData() {
        mAdapter.clear();
        loadData(qaNo);
    }

    private void loadData(int qaNo){
        if(index == Tags.QA_TYPE.QA_ONE_TO_ONE){
            startLoading();
            QARequests.getInstance().requestOneToOneQADetail(qaNo, new OnResultListener<OneToOneQADetailVO>(){

                @Override
                public void onSuccess(Request request, OneToOneQADetailVO result) {
                    stopLoading();
                    setData(result);
                }

                @Override
                public void onFail(Request request, Exception exception) {
                    stopLoading();
                    BraveUtils.showToast(QADetailActivity.this, getString(R.string.toast_common_network_fail));
                }
            });
        }else{
            startLoading();
            QARequests.getInstance().requestStudyQADetail(qaNo, new OnResultListener<StudyQADetailVO>(){

                @Override
                public void onSuccess(Request request, StudyQADetailVO result) {
                    stopLoading();
                    setData(result);
                }

                @Override
                public void onFail(Request request, Exception exception) {
                    stopLoading();
                    BraveUtils.showToast(QADetailActivity.this, getString(R.string.toast_common_network_fail));
                }
            });
        }
    }

    private void setData(QADetailVO result) {
        if(result == null){
            BraveUtils.showToast(QADetailActivity.this, getString(R.string.toast_common_network_response_null));
            return;
        }
        ArrayList<QADetailVO> items = new ArrayList<>();
        items.add(result);

        boolean isReply = false;
        if(result instanceof StudyQADetailVO){
            isReply = ((StudyQADetailVO)result).isHasReply();
        }else{
            isReply = ((OneToOneQADetailVO)result).isHasReply();
        }

        if(isReply){
            items.add(result);
        }
        mAdapter.addAll(items);
        setVisibilityEditButton(!isReply);
    }

    private void setVisibilityEditButton(boolean show){
        if(show){
            btnEdit.setVisibility(View.VISIBLE);
        }else{
            btnEdit.setVisibility(View.GONE);
        }
    }

    private void goEdit(QADetailVO questionItem){
        Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.QADETAIL)
                .putCustomAttribute(AnalysisTags.ACTION, "go_edit"));

        Intent intent = new Intent();
        intent.putExtra(Tags.TAG_QA_DATA, BraveUtils.toJson(questionItem));
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPlayerManagers != null) {
            for (MediaPlayerManager playerManager : mPlayerManagers) {
                playerManager.releasePlayer();
            }
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

    private PermissionListener mCallPermissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            if(BraveUtils.checkTelephony(QADetailActivity.this)) {
                BraveUtils.goCall(QADetailActivity.this, getString(R.string.one_to_one_qa_call_uri));

                Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.QADETAIL)
                        .putCustomAttribute(AnalysisTags.ACTION, "go_call"));
            }
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            //do nothing
        }
    };

    private PermissionListener mDownloadPermissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            if(BraveUtils.checkExternalStorageMounted(QADetailActivity.this)){
                if(mSelectedAdd != null){
                    String path = mSelectedAdd.getPath();
                    String name = mSelectedAdd.getName();
                    if(path != null && path.length() != 0){
                        BraveUtils.doAttachDownload(QADetailActivity.this, path, name);

                        Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.QADETAIL)
                                .putCustomAttribute(AnalysisTags.ACTION, "download_attach"));
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
