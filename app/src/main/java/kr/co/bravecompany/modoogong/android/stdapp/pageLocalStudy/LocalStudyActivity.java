package kr.co.bravecompany.modoogong.android.stdapp.pageLocalStudy;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.CustomEvent;

import java.util.ArrayList;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.activity.DownBaseActivity;
import kr.co.bravecompany.modoogong.android.stdapp.config.AnalysisTags;
import kr.co.bravecompany.modoogong.android.stdapp.config.RequestCode;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.db.model.Lecture;
import kr.co.bravecompany.modoogong.android.stdapp.db.model.Study;
import kr.co.bravecompany.modoogong.android.stdapp.download.OnDownloadDataListener;
import kr.co.bravecompany.modoogong.android.stdapp.download.OnDownloadBindListener;
import kr.co.bravecompany.modoogong.android.stdapp.manager.PropertyManager;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import kr.co.bravecompany.modoogong.android.stdapp.utils.log;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.OnItemClickListener;
import kr.co.bravecompany.modoogong.android.stdapp.data.LocalStudyData;

public class LocalStudyActivity extends DownBaseActivity {

    private Toolbar mToolbar;
    private TextView btnCancel;
    private ImageView btnDelete;
    private TextView btnSelect;
    private TextView btnDoDelete;

    private RecyclerView mListView;
    private LocalStudyAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private SwipeRefreshLayout refreshView;

    //default
    private LinearLayout layoutDefault;
    
    //header
    private TextView txtDefaultGuide;
    private TextView txtLectureCnt;
    private TextView txtLectureSelectCnt;

    private LinearLayout headerDefault;
    private RelativeLayout headerSelect;
    private TextView allSelect;
    private TextView allDeselect;

    //end
    private TextView txtMemory;

    private boolean isDeleteMode = false;
    private Lecture mLecture;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RequestCode.REQUEST_PLAY){
            refreshData();
        }else if(requestCode == RequestCode.REQUEST_DOWN){
            initData();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_study);
        setSystemBar(true);

        initLayout();
        initListener();
        //initData();
    }

    @Override
    protected void initLayout() {
        if(BraveUtils.checkUserInfo()) {
            super.initLayout();
        }
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        TextView title = (TextView)mToolbar.findViewById(R.id.toolbar_title);
        title.setText(getString(R.string.local_study_toolbar_title));

        btnCancel = (TextView)findViewById(R.id.btnCancel);
        btnDelete = (ImageView)findViewById(R.id.btnDelete);
        btnSelect = (TextView)findViewById(R.id.btnSelect);
        btnDoDelete = (TextView)findViewById(R.id.btnDoDelete);

        mAdapter = new LocalStudyAdapter();
        mListView = (RecyclerView)findViewById(R.id.recyclerStudyDown);
        mListView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mListView.setLayoutManager(mLayoutManager);

        refreshView = (SwipeRefreshLayout)findViewById(R.id.refreshView);
        int color = getResources().getColor(R.color.colorPrimary);
        refreshView.setColorSchemeColors(color, color, color, color);

        //default
        layoutDefault = (LinearLayout) findViewById(R.id.layoutDefault);
        TextView txtDefault = (TextView)layoutDefault.findViewById(R.id.txtDefault);
        txtDefault.setText(getString(R.string.no_local_study));

        //header
        txtDefaultGuide = (TextView)findViewById(R.id.txtDefaultGuide);
        txtDefaultGuide.setTextSize(BraveUtils.convertPxToDp(getApplicationContext(),
                getResources().getDimension(R.dimen.text_medium)));
        txtDefaultGuide.setTextColor(getResources().getColor(R.color.black));
        txtDefaultGuide.setTypeface(Typeface.DEFAULT_BOLD);
        txtLectureCnt = (TextView)findViewById(R.id.txtLectureCnt);
        txtLectureSelectCnt = (TextView)findViewById(R.id.txtLectureSelectCnt);

        headerDefault = (LinearLayout)findViewById(R.id.headerDefault);
        headerSelect = (RelativeLayout)findViewById(R.id.selectHeader);

        allSelect = (TextView)findViewById(R.id.allSelect);
        allDeselect = (TextView)findViewById(R.id.allDeselect);

        //end
        txtMemory = (TextView)findViewById(R.id.txtMemory);
    }

    @Override
    protected void initListener() {
        if(BraveUtils.checkUserInfo()) {
            super.initListener();
        }
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
                BraveUtils.showAlertDialogOkCancel(LocalStudyActivity.this, msg,
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
                mAdapter.toggle(position);

                updateHeader();
            }
        });

        allSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.setAllItemSelected(true);
                updateHeader();

                Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.LOCALSTUDY)
                        .putCustomAttribute(AnalysisTags.ACTION, "select_all"));
            }
        });

        allDeselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.setAllItemSelected(false);
                updateHeader();

                Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.LOCALSTUDY)
                        .putCustomAttribute(AnalysisTags.ACTION, "deselect_all"));
            }
        });

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //play
                Study item = mAdapter.getItem(position).getStudyVO();
                if(item != null){
                    playLocalStudy(item);
                }
            }
        });

        refreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();

                Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.LOCALSTUDY)
                        .putCustomAttribute(AnalysisTags.ACTION, "refresh"));
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
            int studyLectureNo = getIntent().getIntExtra(Tags.TAG_LECTURE, -1);
            if(studyLectureNo != -1) {
                mLecture = mDownloadManager.getDownloadLecture(studyLectureNo);
                if(mLecture != null) {
                    String lectureName = BraveUtils.fromHTMLTitle(mLecture.getLectureName());
                    txtDefaultGuide.setText(lectureName);

                    ArrayList<LocalStudyData> items = new ArrayList<>();
                    ArrayList<Study> downloadStudies = mDownloadManager.getDownloadCompleteStudyList(studyLectureNo);
                    if (downloadStudies != null && downloadStudies.size() != 0) {
                        for (int i = 0; i < downloadStudies.size(); i++) {
                            LocalStudyData study = new LocalStudyData();
                            Study downloadStudy = downloadStudies.get(i);
                            study.setStudyVO(downloadStudy);
                            study.setPlayed(PropertyManager.getInstance().getLastPlay().equals(downloadStudy.getStudyKey()));
                            items.add(study);
                        }
                        mAdapter.addAll(items);
                    }
                    showDefault();
                    updateDeleteMode(false);

                    txtMemory.setText(BraveUtils.getAvailableMemorySize(getApplicationContext()));

                    ContentViewEvent event = new ContentViewEvent()
                            .putCustomAttribute(AnalysisTags.VIEW, "local_study")
                            .putContentId(String.valueOf(studyLectureNo));
                    if(lectureName != null){
                        event.putContentName(lectureName);
                    }
                    Answers.getInstance().logContentView(event);
                }
            }
        }else{
            log.d("mDownloadManager == NULL");
        }
        if(refreshView.isRefreshing()){
            refreshView.setRefreshing(false);
        }
    }

    private void refreshData(){
        String lastStudy = PropertyManager.getInstance().getLastPlay();
        if(lastStudy != null) {
            mAdapter.refresh(lastStudy);
        }
    }

    private void playLocalStudy(Study study){
        if(mLecture != null && study != null){
            PropertyManager.getInstance().setLastPlay(study.getStudyKey());

            String title = BraveUtils.fromHTMLTitle(study.getStudyName());
            BraveUtils.goPlayerWithMediaContentKey(LocalStudyActivity.this, title, study.getMediaContentKey());

            Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.LOCALSTUDY)
                    .putCustomAttribute(AnalysisTags.ACTION, "play_local_study"));
        }
    }

    public void updateDeleteMode(boolean isDeleteMode){
        if(!isDeleteMode){
            setDownBtnState(Tags.LOCAL_STUDY_BTN_TYPE.LOCAL_STUDY_DELETE);
        }
        mAdapter.updateDeleteMode(isDeleteMode);
        this.isDeleteMode = isDeleteMode;
        updateHeader();
    }

    public void updateHeader(){
        int lectureCnt = mAdapter.getItemCount();
        int selectCnt = mAdapter.getSelectedCnt();
        boolean isShowSelectAll = (selectCnt == 0);

        if(isDeleteMode) {
            if(isShowSelectAll){
                setDownBtnState(Tags.LOCAL_STUDY_BTN_TYPE.LOCAL_STUDY_SELECT);
            }else{
                setDownBtnState(Tags.LOCAL_STUDY_BTN_TYPE.LOCAL_STUDY_DO_DELETE);
            }
        }

        updateSelectAllView(isShowSelectAll);
        updateHeaderView(lectureCnt, selectCnt);
    }

    private void updateHeaderView(int lectureCnt, int selectCnt){
        if(!isDeleteMode){
            headerDefault.setVisibility(View.VISIBLE);
            headerSelect.setVisibility(View.GONE);

            txtLectureCnt.setText(String.format(getString(R.string.study_play_count), lectureCnt));

        }else{
            headerDefault.setVisibility(View.GONE);
            headerSelect.setVisibility(View.VISIBLE);

            txtLectureSelectCnt.setText(Html.fromHtml(String.format(getString(R.string.study_select_state),
                    lectureCnt, selectCnt)));
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
    
    private void setDownBtnState(int state){
        switch (state){
            case Tags.LOCAL_STUDY_BTN_TYPE.LOCAL_STUDY_DELETE:
                btnDelete.setVisibility(View.VISIBLE);
                btnSelect.setVisibility(View.GONE);
                btnDoDelete.setVisibility(View.GONE);
                btnCancel.setVisibility(View.GONE);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                break;
            case Tags.LOCAL_STUDY_BTN_TYPE.LOCAL_STUDY_SELECT:
                btnDelete.setVisibility(View.GONE);
                btnSelect.setVisibility(View.VISIBLE);
                btnDoDelete.setVisibility(View.GONE);
                btnCancel.setVisibility(View.VISIBLE);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                break;
            case Tags.LOCAL_STUDY_BTN_TYPE.LOCAL_STUDY_DO_DELETE:
                btnDelete.setVisibility(View.GONE);
                btnSelect.setVisibility(View.GONE);
                btnDoDelete.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.VISIBLE);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                break;
        }
    }

    // =============================================================================
    // Download
    // =============================================================================

    @Override
    protected void updateDownloadViews(String studyKey, final int state, final int percent, int errorCode){
        if(studyKey == null){
            return;
        }
        if(isResumed) {
            log.d(String.format("LocalStudyActivity updateDownloadViews - studyKey: %s, state: %d, percent: %d, errorCode: %d", studyKey, state, percent, errorCode));
        }
        super.updateDownloadViews(studyKey, state, percent, errorCode);
        if(isResumed && state == Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_COMPLETE){
            if(mDownloadManager != null){
                Study study = mDownloadManager.getDownloadStudy(studyKey);
                if(mLecture != null && study != null) {
                    if (mLecture.getStudyLectureNo() == study.getStudyLectureNo()) {
                        BraveUtils.showToast(LocalStudyActivity.this, getString(R.string.toast_local_download_complete));
                    }
                }
            }
        }
    }

    // =============================================================================

    private MaterialDialog.SingleButtonCallback mRemoveDialogListener = new MaterialDialog.SingleButtonCallback() {
        @Override
        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            if (mDownloadManager != null) {
                final ArrayList<LocalStudyData> removes = mAdapter.getSelectedItems();
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
                                    txtMemory.setText(BraveUtils.getAvailableMemorySize(getApplicationContext()));

                                    Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.LOCALSTUDY)
                                            .putCustomAttribute(AnalysisTags.ACTION, "do_delete"));
                                }

                                @Override
                                public void onDataError(int error) {
                                    stopLoading();
                                    BraveUtils.showLoadOnFailToast(LocalStudyActivity.this);
                                }
                            });
                }
            }
        }
    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
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
