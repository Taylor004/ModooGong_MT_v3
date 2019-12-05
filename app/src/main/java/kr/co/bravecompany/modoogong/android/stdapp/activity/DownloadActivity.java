package kr.co.bravecompany.modoogong.android.stdapp.activity;

import androidx.annotation.NonNull;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.kollus.sdk.media.util.ErrorCodes;

import java.util.ArrayList;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.adapter.DownloadAdapter;
import kr.co.bravecompany.modoogong.android.stdapp.config.AnalysisTags;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.data.DownloadData;
import kr.co.bravecompany.modoogong.android.stdapp.db.model.Lecture;
import kr.co.bravecompany.modoogong.android.stdapp.db.model.Study;
import kr.co.bravecompany.modoogong.android.stdapp.download.OnDownloadDataListener;
import kr.co.bravecompany.modoogong.android.stdapp.download.OnDownloadBindListener;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import kr.co.bravecompany.modoogong.android.stdapp.utils.log;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.OnItemClickListener;

public class DownloadActivity extends DownBaseActivity {
    private Toolbar mToolbar;
    private TextView toolbarTitle;
    private ImageView btnClose;

    private TextView btnCancel;
    private ImageView btnDelete;
    private TextView btnSelect;
    private TextView btnDoDelete;

    private RecyclerView mListView;
    private DownloadAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    //default
    private LinearLayout layoutDefault;

    //header
    private TextView txtDown;
    private TextView txtLectureSelectCnt;

    private LinearLayout headerDefault;
    private RelativeLayout headerSelect;
    private LinearLayout headerPause;
    private TextView allSelect;
    private TextView allDeselect;

    private boolean isDeleteMode = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.66f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.activity_download);
        initLayout();
        initListener();
        //initData();
    }

    @Override
    protected void initLayout() {

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        toolbarTitle = (TextView)mToolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(getString(R.string.download_toolbar_title));
        btnClose = (ImageView)mToolbar.findViewById(R.id.btnClose);

        btnCancel = (TextView)findViewById(R.id.btnCancel);
        btnDelete = (ImageView)findViewById(R.id.btnDelete);
        btnSelect = (TextView)findViewById(R.id.btnSelect);
        btnDoDelete = (TextView)findViewById(R.id.btnDoDelete);

        mAdapter = new DownloadAdapter();
        mListView = (RecyclerView)findViewById(R.id.recyclerDown);
        mListView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mListView.setLayoutManager(mLayoutManager);

        //default
        layoutDefault = (LinearLayout) findViewById(R.id.layoutDefault);
        TextView txtDefault = (TextView)layoutDefault.findViewById(R.id.txtDefault);
        txtDefault.setText(getString(R.string.no_download));

        //header
        txtDown = (TextView)findViewById(R.id.txtDown);
        txtLectureSelectCnt = (TextView)findViewById(R.id.txtLectureSelectCnt);

        headerDefault = (LinearLayout)findViewById(R.id.headerDefault);
        headerSelect = (RelativeLayout)findViewById(R.id.selectHeader);
        headerPause = (LinearLayout)findViewById(R.id.headerPause);

        allSelect = (TextView)findViewById(R.id.allSelect);
        allDeselect = (TextView)findViewById(R.id.allDeselect);
    }

    @Override
    protected void initListener() {

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDeleteMode(true);
            }
        });

        btnDoDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do down
                String msg = String.format(getString(R.string.dialog_download_delete_guide),
                        mAdapter.getSelectedCnt());
                BraveUtils.showAlertDialogOkCancel(DownloadActivity.this, msg,
                        mRemoveDialogListener, null);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDeleteMode(false);
            }
        });

        mAdapter.setOnDeleteModeItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //select
                DownloadData item = mAdapter.getItem(position);
                showToastWithDownState(item.getDownState());
                mAdapter.toggle(position);

                updateHeader();
            }
        });

        mAdapter.setOnDownErrorItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(mDownloadManager != null) {
                    DownloadData download = mAdapter.getItem(position);
                    int state = download.getDownState();
                    int errorCode = download.getErrorCode();
                    if(state == Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_API_ERROR
                            || state == Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_ERROR){
                        String title = getString(R.string.kollus_error_dialog_title);
                        title += " ("+errorCode+")";
                        String message;
                        if (errorCode == ErrorCodes.ERROR_ABNORMAL_DRM_INFO){
                            message = getString(R.string.dialog_error_impl_err_8655);
                        }else{
                            if(errorCode == -1){
                                errorCode = ErrorCodes.ERROR_UNDEFINED_CODE;
                            }
                            message = ErrorCodes.getInstance(getApplicationContext()).getErrorString(errorCode);
                        }
                        BraveUtils.showAlertDialog(DownloadActivity.this, title, message,
                                getString(R.string.common_ok), null, null, null);

                        Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.DOWNLOAD)
                                .putCustomAttribute(AnalysisTags.ACTION, "check_error"));
                    }
                }
            }
        });

        mAdapter.setOnDownPauseClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(mDownloadManager != null){
                    DownloadData download = mAdapter.getItem(position);
                    if(!download.isPaused()){
                        int state = download.getDownState();
                        if (state != Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_PENDING
                                && state != Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_API_ING
                                && state != Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_API_COMPLETE) {
                            mDownloadManager.pauseDownloadingStudy(download.getStudyVO().getMediaContentKey());

                            mAdapter.togglePaused(position);
                            updateHeader();
                        }
                        showToastWithDownState(state);

                        Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.DOWNLOAD)
                                .putCustomAttribute(AnalysisTags.ACTION, "pause_download"));
                    }else{
                        Study downloadStudy = download.getStudyVO();
                        mDownloadManager.reStartDownload(BraveUtils.fromHTML(downloadStudy.getVodInfo()),
                                downloadStudy.getStudyKey());

                        mAdapter.togglePaused(position);
                        updateHeader();

                        Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.DOWNLOAD)
                                .putCustomAttribute(AnalysisTags.ACTION, "restart_download"));
                    }
                }
            }
        });

        allSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.setAllItemSelected(true);
                updateHeader();

                Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.DOWNLOAD)
                        .putCustomAttribute(AnalysisTags.ACTION, "select_all"));
            }
        });

        allDeselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.setAllItemSelected(false);
                updateHeader();

                Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.DOWNLOAD)
                        .putCustomAttribute(AnalysisTags.ACTION, "deselect_all"));
            }
        });

        if(mDownloadManager != null){
            mDownloadManager.setOnDownloadBindListener(new OnDownloadBindListener() {
                @Override
                public void onBindComplete() {
                    initData();
                }
            });
        }
    }

    private void initData() {
        mAdapter.clear();
        if(mDownloadManager != null) {
            //ArrayList<DownloadData> items = mDownloadManager.getDownloadingList();
            ArrayList<DownloadData> items = new ArrayList<>();
            ArrayList<Study> downloadStudies = mDownloadManager.getDownloadingStudyList();
            if(downloadStudies != null && downloadStudies.size() != 0){
                for(int i=0; i<downloadStudies.size(); i++){
                    DownloadData download = null;
                    Study downloadStudy = downloadStudies.get(i);
                    Lecture lecture = mDownloadManager.getDownloadLecture(downloadStudy.getStudyLectureNo());
                    if(lecture != null){
                        download = mDownloadManager.getDownloadingDownloadVO(downloadStudy.getStudyKey());
                        if(download != null){
                            download.setLectureVO(lecture);
                            download.setStudyVO(downloadStudy);
                            int state = download.getDownState();
                            if(state == Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_PAUSE
                                    || state == Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_API_ERROR
                                    || state == Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_ERROR){
                                download.setPaused(true);
                            }else{
                                download.setPaused(false);
                            }
                            items.add(download);
                        }
                    }
                }
                mAdapter.addAll(items);
            }

            updateDeleteMode(false);
        }else{
            log.d("mDownloadManager == NULL");
        }
    }

    private void setDownBtnState(int state){
        switch (state){
            case Tags.DOWNLOAD_BTN_TYPE.DOWNLOAD_DELETE:
                toolbarTitle.setVisibility(View.VISIBLE);
                btnDelete.setVisibility(View.VISIBLE);
                btnSelect.setVisibility(View.GONE);
                btnDoDelete.setVisibility(View.GONE);
                btnCancel.setVisibility(View.GONE);
                btnClose.setVisibility(View.VISIBLE);
                break;
            case Tags.DOWNLOAD_BTN_TYPE.DOWNLOAD_SELECT:
                toolbarTitle.setVisibility(View.GONE);
                btnDelete.setVisibility(View.GONE);
                btnSelect.setVisibility(View.VISIBLE);
                btnDoDelete.setVisibility(View.GONE);
                btnCancel.setVisibility(View.VISIBLE);
                btnClose.setVisibility(View.GONE);
                break;
            case Tags.DOWNLOAD_BTN_TYPE.DOWNLOAD_DO_DELETE:
                toolbarTitle.setVisibility(View.GONE);
                btnDelete.setVisibility(View.GONE);
                btnSelect.setVisibility(View.GONE);
                btnDoDelete.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.VISIBLE);
                btnClose.setVisibility(View.GONE);
                break;
        }
    }

    public void updateDeleteMode(boolean isDeleteMode){
        if(!isDeleteMode){
            setDownBtnState(Tags.DOWNLOAD_BTN_TYPE.DOWNLOAD_DELETE);
        }
        mAdapter.updateDeleteMode(isDeleteMode);
        this.isDeleteMode = isDeleteMode;
        updateHeader();
    }

    private void updateHeader(){
        int totalCnt = mAdapter.getItemCount();
        int downloadCnt = mAdapter.getItemCount()-mAdapter.getPauseCnt();
        int selectCnt = mAdapter.getSelectedCnt();
        boolean isShowSelectAll = (selectCnt == 0);

        if(isDeleteMode) {
            if (isShowSelectAll) {
                setDownBtnState(Tags.DOWNLOAD_BTN_TYPE.DOWNLOAD_SELECT);
            } else {
                setDownBtnState(Tags.DOWNLOAD_BTN_TYPE.DOWNLOAD_DO_DELETE);
            }
        }

        updateSelectAllView(isShowSelectAll);

        int pauseCnt = mAdapter.getPauseCnt();
        int type;
        if(pauseCnt == 0){
            type = Tags.DOWNLOAD_HEADER_TYPE.DOWNLOAD_HEADER_NONE;
        }else if(isDeleteMode){
            type = Tags.DOWNLOAD_HEADER_TYPE.DOWNLOAD_HEADER_SELECT;
        }else {
            type = Tags.DOWNLOAD_HEADER_TYPE.DOWNLOAD_HEADER_PAUSE;
        }

        updateHeaderView(totalCnt, downloadCnt, selectCnt, type);
    }

    private void updateHeaderView(int totalCnt, int downloadCnt, int selectCnt, int type){
        if(!isDeleteMode){
            switch (type){
                case Tags.DOWNLOAD_HEADER_TYPE.DOWNLOAD_HEADER_NONE:
                    headerDefault.setVisibility(View.VISIBLE);
                    headerSelect.setVisibility(View.GONE);
                    headerPause.setVisibility(View.GONE);

                    txtDown.setText(String.format(getString(R.string.download_state_guide),
                            downloadCnt));

                    break;
                case Tags.DOWNLOAD_HEADER_TYPE.DOWNLOAD_HEADER_PAUSE:
                    headerDefault.setVisibility(View.GONE);
                    headerSelect.setVisibility(View.GONE);
                    headerPause.setVisibility(View.VISIBLE);
                    break;
            }

        }else{
            headerDefault.setVisibility(View.GONE);
            headerSelect.setVisibility(View.VISIBLE);
            headerPause.setVisibility(View.GONE);

            txtLectureSelectCnt.setText(Html.fromHtml(String.format(getString(R.string.study_select_state),
                    totalCnt, selectCnt)));
        }
    }

    private void updateSelectAllView(boolean isShow){
        if(isShow){
            allSelect.setVisibility(View.VISIBLE);
            allDeselect.setVisibility(View.GONE);
        }else{
            allSelect.setVisibility(View.GONE);
            allDeselect.setVisibility(View.VISIBLE);
        }
    }

    // =============================================================================
    // Download
    // =============================================================================

    @Override
    protected void updateDownloadViews(final String studyKey, final int state, final int percent, final int errorCode){
        if(studyKey == null){
            return;
        }
        if(isResumed) {
            log.d(String.format("DownloadActivity updateDownloadViews - studyKey: %s, state: %d, percent: %d, errorCode: %d", studyKey, state, percent, errorCode));
        }
        super.updateDownloadViews(studyKey, state, percent, errorCode);
        if(state == Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_COMPLETE){
            mAdapter.removeItem(studyKey);
            updateHeader();
            showDefault();

            if(mAdapter.getItemCount() == 0 && !mAdapter.isDeleteMode()){
                finish();
            }
        }else {
            runOnUiThread(new Runnable() {
                public void run() {
                    int position = mAdapter.getItemWithStudyKey(studyKey);
                    if (position != -1) {
                        int idx = position;
                        idx = idx - mLayoutManager.findFirstVisibleItemPosition();
                        View view = mListView.getChildAt(idx);
                        if (view != null) {
                            mAdapter.updateDownState(view, state, percent, errorCode);
                        } else {
                            mAdapter.updateDownState(studyKey, state, percent, errorCode);
                        }
                        updateHeader();
                    }
                }
            });
        }
    }

    // =============================================================================

    private MaterialDialog.SingleButtonCallback mRemoveDialogListener = new MaterialDialog.SingleButtonCallback() {
        @Override
        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            if (mDownloadManager != null) {
                final ArrayList<DownloadData> removes = mAdapter.getSelectedItems();
                if (removes != null && removes.size() != 0) {
                    startLoading();
                    ArrayList<Study> studies = new ArrayList<>();
                    for(int i=0; i<removes.size(); i++) {
                        studies.add(removes.get(i).getStudyVO());
                    }
                    mDownloadManager.removeDownloadStudyList(studies,
                            new OnDownloadDataListener<String>() {
                                @Override
                                public void onDataProgress(int progress) {
                                }

                                @Override
                                public void onDataComplete(String result) {
                                    stopLoading();
                                    mAdapter.removeAll(removes);

                                    showDefault();
                                    updateDeleteMode(false);

                                    Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.DOWNLOAD)
                                            .putCustomAttribute(AnalysisTags.ACTION, "do_delete"));
                                }

                                @Override
                                public void onDataError(int error) {
                                    stopLoading();
                                    BraveUtils.showLoadOnFailToast(DownloadActivity.this);
                                }
                            });
                }
            }
        }
    };

    private void showToastWithDownState(int state){
        switch (state){
            case Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_PENDING:
            case Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_API_ING:
            case Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_API_COMPLETE:
                BraveUtils.showToast(DownloadActivity.this, getString(R.string.toast_study_down_pending));
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
    }

    private void showDefault(){
        if(mAdapter != null){
            showDefault(mAdapter.getItemCount() == 0);
        }
    }

    private void showDefault(boolean show){
        if(show){
            layoutDefault.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        }else{
            layoutDefault.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
        }
    }
}
