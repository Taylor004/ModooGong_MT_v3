package kr.co.bravecompany.modoogong.android.stdapp.pageStudy;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
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
import kr.co.bravecompany.api.android.stdapp.api.OnResultListener;
import kr.co.bravecompany.api.android.stdapp.api.requests.StudyRequests;
import kr.co.bravecompany.api.android.stdapp.api.data.StudyResult;
import kr.co.bravecompany.api.android.stdapp.api.data.StudyVodResult;
import kr.co.bravecompany.modoogong.android.stdapp.activity.DownBaseActivity;
import kr.co.bravecompany.modoogong.android.stdapp.config.AnalysisTags;
import kr.co.bravecompany.modoogong.android.stdapp.config.RequestCode;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.db.model.Study;
import kr.co.bravecompany.modoogong.android.stdapp.download.OnDownloadDataListener;
import kr.co.bravecompany.modoogong.android.stdapp.download.OnDownloadBindListener;
import kr.co.bravecompany.modoogong.android.stdapp.manager.PropertyManager;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import kr.co.bravecompany.modoogong.android.stdapp.utils.SystemUtils;
import kr.co.bravecompany.modoogong.android.stdapp.utils.log;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.OnItemClickListener;
import kr.co.bravecompany.api.android.stdapp.api.data.LectureItemVO;
import kr.co.bravecompany.modoogong.android.stdapp.data.StudyHeaderData;
import kr.co.bravecompany.api.android.stdapp.api.data.StudyItemVO;
import kr.co.bravecompany.modoogong.android.stdapp.data.StudyData;
import okhttp3.Request;

public class StudyActivity extends DownBaseActivity {

    private Toolbar mToolbar;
    private TextView btnCancel;
    private TextView btnDown;
    private TextView btnSelect;
    private TextView btnDoDown;

    private RecyclerView mListView;
    private StudyAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private int mCurrentVisibleItem;

    //select header
    private FrameLayout defaultHeader;
    private TextView txtDefaultGuide;
    private TextView txtLectureCnt;
    private TextView txtLectureSelectCnt;

    private LinearLayout headerDefault;
    private RelativeLayout headerSelect;
    private TextView allSelect;
    private TextView allDeselect;

    private FrameLayout layoutList;
    private LinearLayout layoutDefault;
    private TextView txtTeacherName;
    private TextView txtSaleType;
    private TextView txtLectureName;
    private TextView txtLectureDetail;

    private boolean isDownloadMode = false;

    private LectureItemVO mLecture;
    private StudyData mLastStudy = null;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RequestCode.REQUEST_PLAY){
            refreshPlayedData();
        }else if(requestCode == RequestCode.REQUEST_DOWN){
            refreshData();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemBar(true);

        setContentView(R.layout.activity_study);
        initLayout();
        initListener();

        //initData();
    }

    @Override
    protected void initLayout() {
        super.initLayout();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        TextView title = (TextView)mToolbar.findViewById(R.id.toolbar_title);
        title.setText(getString(R.string.study_toolbar_title));

        btnCancel = (TextView)findViewById(R.id.btnCancel);
        btnDown = (TextView)findViewById(R.id.btnDown);
        btnSelect = (TextView)findViewById(R.id.btnSelect);
        btnDoDown = (TextView)findViewById(R.id.btnDoDown);

        mAdapter = new StudyAdapter();
        mListView = (RecyclerView)findViewById(R.id.recyclerStudy);
        mListView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mListView.setLayoutManager(mLayoutManager);

        //select header
        defaultHeader = (FrameLayout)findViewById(R.id.defaultHeader);
        txtDefaultGuide = (TextView)findViewById(R.id.txtDefaultGuide);
        txtDefaultGuide.setText(getString(R.string.study_play_guide));
        txtLectureCnt = (TextView)findViewById(R.id.txtLectureCnt);
        txtLectureSelectCnt = (TextView)findViewById(R.id.txtLectureSelectCnt);

        headerDefault = (LinearLayout)findViewById(R.id.headerDefault);
        headerSelect = (RelativeLayout)findViewById(R.id.selectHeader);
        allSelect = (TextView)findViewById(R.id.allSelect);
        allDeselect = (TextView)findViewById(R.id.allDeselect);

        layoutList = (FrameLayout)findViewById(R.id.layoutList);
        layoutDefault = (LinearLayout)findViewById(R.id.layoutDefault);
        txtTeacherName = (TextView)layoutDefault.findViewById(R.id.txtTeacherName);
        txtSaleType = (TextView)layoutDefault.findViewById(R.id.txtSaleType);
        txtLectureName = (TextView)layoutDefault.findViewById(R.id.txtLectureName);
        txtLectureDetail = (TextView)layoutDefault.findViewById(R.id.txtLectureDetail);
        TextView txtDefault = (TextView)layoutDefault.findViewById(R.id.txtDefault);
        txtDefault.setText(getString(R.string.study_not_open_lecture));
    }

    @Override
    protected void initListener() {
        super.initListener();

        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDownloadMode(true);
            }
        });

        btnDoDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do down
                doDownloadWithDialog();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDownloadMode(false);
            }
        });

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //play
                StudyData study = mAdapter.getItem(position);
                StudyItemVO item = study.getStudyItemVO();
                if(item != null && mLecture != null){
                    mLastStudy = study;
                    if(study.getDownState() == Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_COMPLETE){
                        playLocalStudy(study);
                    }else{
                        if(item.getStudyVodKey() != null) {
                            loadVodDataWithDialog(mLecture.getStudyLectureNo(), item.getLctCode());
                        }else{
                            BraveUtils.showToast(StudyActivity.this, getString(R.string.toast_study_not_upload));
                        }
                    }
                }
            }
        });
        mAdapter.setOnDownloadModeItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mDownloadManager != null) {
                    StudyData study = mAdapter.getItem(position);
                    StudyItemVO item = study.getStudyItemVO();
                    if(item != null) {
                        if (study.getDownState() == Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_NONE){
                            if(item.getStudyVodKey() == null) {
                                BraveUtils.showToast(StudyActivity.this, getString(R.string.toast_study_not_upload));
                                return;
                            }else if(mDownloadManager.checkDownloadedStudy(item.getStudyVodKey())){
                                BraveUtils.showToast(StudyActivity.this, getString(R.string.toast_study_down_duplicate));
                                return;
                            }
                        }

                        //select
                        showToastWithDownState(study.getDownState());
                        mAdapter.toggle(position);

                        updateHeader();
                    }
                }
            }
        });

        mAdapter.setOnAllSelectClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAllItems();
            }
        });

        mAdapter.setOnAllDeselectClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deselectAllItems();
            }
        });

        allSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAllItems();
            }
        });

        allDeselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deselectAllItems();
            }
        });

        mListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();
                if (mCurrentVisibleItem == firstVisibleItem) {
                    return;
                } else {
                    mCurrentVisibleItem = firstVisibleItem;
                    if (mAdapter.getItemViewType(firstVisibleItem) == Tags.STUDY_VIEW_TYPE.STUDY_HEADER) {
                        defaultHeader.setVisibility(View.GONE);
                    } else {
                        if (defaultHeader.getVisibility() == View.GONE)
                            defaultHeader.setVisibility(View.VISIBLE);
                    }
                }
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

        String lecture = getIntent().getStringExtra(Tags.TAG_LECTURE);
        mLecture = (LectureItemVO)BraveUtils.toJsonString(lecture, LectureItemVO.class);
        if(mLecture != null) {

            if(BraveUtils.checkNotOpenLecture(mLecture.getLectureStartDay())){
                BraveUtils.updateStudyHeaderView(txtTeacherName, txtSaleType,
                        txtLectureName, txtLectureDetail, mLecture);

                showDefault(true);
            }else{
                StudyHeaderData studyHeader = new StudyHeaderData();
                studyHeader.setLectureItemVO(mLecture);
                mAdapter.setStudyHeader(studyHeader);

                loadData(mLecture.getStudyLectureNo(), mLecture.getLectureCode());

                showDefault(false);
            }

            String lectureName = mLecture.getLectureName();
            ContentViewEvent event = new ContentViewEvent()
                    .putCustomAttribute(AnalysisTags.VIEW, "study")
                    .putContentId(String.valueOf(mLecture.getStudyLectureNo()));
            if(lectureName != null){
                event.putContentName(lectureName);
            }
            Answers.getInstance().logContentView(event);
        }
    }

    private void loadData(int studyLectureNo, int lectureCode){
        startLoading();
        StudyRequests.getInstance().requestStudyList(studyLectureNo, lectureCode, new OnResultListener<StudyResult>(){

            @Override
            public void onSuccess(Request request, StudyResult result) {
                stopLoading();
                setData(result);
            }

            @Override
            public void onFail(Request request, Exception exception) {
                stopLoading();
                BraveUtils.showRequestOnFailToast(StudyActivity.this, exception);

                if(exception != null && exception.getMessage() != null) {
                    Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.REQUEST)
                            .putCustomAttribute(AnalysisTags.ERROR, AnalysisTags.STUDY + ":: " + exception.getMessage()));
                }
            }
        });
    }

    private void refreshPlayedData(){
        String lastStudy = PropertyManager.getInstance().getLastPlay();
        if(lastStudy != null) {
            mAdapter.refreshPlayed(lastStudy);
        }
    }

    private void refreshDownloadedData(String studyKey){
        mAdapter.refreshDownloaded(studyKey);
    }

    private void refreshData(){
        if(mDownloadManager != null) {
            for (int i = 0; i < mAdapter.getRealItemCount(); i++) {
                String studyKey = mAdapter.getItem(i).getStudyKey();
                StudyData downloadStudy = mDownloadManager.getDownloadStudyVO(studyKey);
                if (downloadStudy != null) {
                    int state = downloadStudy.getDownState();
                    int percent = downloadStudy.getDownPercents();
                    updateDownloadViews(studyKey, state, percent, -1);
                } else {
                    int state = Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_NONE;
                    int percent = 0;
                    updateDownloadViews(studyKey, state, percent, -1);
                }
            }
        }
    }

    private void loadVodDataWithDialog(final int studyLectureNo, final int lctCode){
        if(SystemUtils.getConnectivityStatus(getApplicationContext()) == Tags.NETWORK_TYPE.NETWORK_MOBILE
                && PropertyManager.getInstance().isNoticeData()){
            String message = getString(R.string.dialog_no_wifi_play);
            BraveUtils.showAlertDialogOkCancel(StudyActivity.this, message,
                    new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            loadVodData(studyLectureNo, lctCode);
                        }
                    }, null);
        }else{
            loadVodData(studyLectureNo, lctCode);
        }
    }

    private void loadVodData(int studyLectureNo, int lctCode){
        startLoading();
        StudyRequests.getInstance().requestStudyVod(studyLectureNo, lctCode, true,
                new OnResultListener<StudyVodResult>(){

            @Override
            public void onSuccess(Request request, StudyVodResult result) {
                stopLoading();
                playStudy(result);
            }

            @Override
            public void onFail(Request request, Exception exception) {
                stopLoading();
                BraveUtils.showRequestOnFailToast(StudyActivity.this, exception);

                if(exception != null && exception.getMessage() != null) {
                    Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.REQUEST)
                            .putCustomAttribute(AnalysisTags.ERROR, AnalysisTags.STUDY + ":: " + exception.getMessage()));
                }
            }
        });
    }

    private void setData(StudyResult result){
        if(result == null){
            return;
        }
        ArrayList<StudyData> items = new ArrayList<>();
        ArrayList<StudyItemVO> studies = result.getLesson();

        if(studies != null && studies.size() != 0){
            for(int i=0; i<studies.size(); i++){
                StudyItemVO item = studies.get(i);
                String studyKey = Study.makeStudyKey(mLecture.getStudyLectureNo(), item.getLctCode());

                StudyData study = null;
                StudyData downloadStudy = null;
                if(mDownloadManager != null){
                    downloadStudy = mDownloadManager.getDownloadStudyVO(studyKey);
                }
                if(downloadStudy != null){
                    study = downloadStudy;
                }else{
                    study = new StudyData();
                }
                study.setStudyItemVO(item);
                study.setPlayed(PropertyManager.getInstance().getLastPlay().equals(studyKey));
                study.setStudyKey(studyKey);
                items.add(study);
            }
            mAdapter.addAll(items);
        }

        updateDownloadMode(false);
    }

    private void playStudy(StudyVodResult result){
        if(result == null){
            return;
        }
        String vodInfo = result.getVodInfo();
        if(vodInfo != null){
            PropertyManager.getInstance().setLastPlay(mLastStudy.getStudyKey());

            String title = BraveUtils.fromHTMLTitle(mLastStudy.getStudyItemVO().getStudyName());
            vodInfo = BraveUtils.fromHTML(vodInfo);
            BraveUtils.goPlayerWithUrl(StudyActivity.this, title, vodInfo);

            Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.STUDY)
                    .putCustomAttribute(AnalysisTags.ACTION, "play_study"));
        }
    }

    private void playLocalStudy(StudyData study){
        if(mLecture != null && study != null){
            if(mDownloadManager != null) {
                Study downloadStudy = mDownloadManager.getDownloadStudy(study.getStudyKey());
                if(downloadStudy != null) {
                    PropertyManager.getInstance().setLastPlay(study.getStudyKey());

                    String title = BraveUtils.fromHTMLTitle(study.getStudyItemVO().getStudyName());
                    BraveUtils.goPlayerWithMediaContentKey(StudyActivity.this, title, downloadStudy.getMediaContentKey());

                    Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.STUDY)
                            .putCustomAttribute(AnalysisTags.ACTION, "play_local_study"));
                }
            }else{
                loadVodDataWithDialog(mLecture.getStudyLectureNo(), study.getStudyItemVO().getLctCode());
            }
        }
    }

    private void setDownBtnState(int state){
        switch (state){
            case Tags.STUDY_BTN_TYPE.STUDY_DOWN:
                btnDown.setVisibility(View.VISIBLE);
                btnSelect.setVisibility(View.GONE);
                btnDoDown.setVisibility(View.GONE);
                btnCancel.setVisibility(View.GONE);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                break;
            case Tags.STUDY_BTN_TYPE.STUDY_SELECT:
                btnDown.setVisibility(View.GONE);
                btnSelect.setVisibility(View.VISIBLE);
                btnDoDown.setVisibility(View.GONE);
                btnCancel.setVisibility(View.VISIBLE);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                break;
            case Tags.STUDY_BTN_TYPE.STUDY_DO_DOWN:
                btnDown.setVisibility(View.GONE);
                btnSelect.setVisibility(View.GONE);
                btnDoDown.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.VISIBLE);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                break;
        }
    }

    private void updateDownloadMode(boolean isDownloadMode){
        if(!isDownloadMode){
            setDownBtnState(Tags.STUDY_BTN_TYPE.STUDY_DOWN);
        }
        mAdapter.updateDownloadMode(isDownloadMode);
        this.isDownloadMode = isDownloadMode;
        updateHeader();
    }

    private void updateHeader(){
        int lectureCnt = mAdapter.getRealItemCount();
        int selectCnt = mAdapter.getSelectedCnt();
        boolean isShowSelectAll = (selectCnt == 0);

        if(isDownloadMode) {
            if (isShowSelectAll) {
                setDownBtnState(Tags.STUDY_BTN_TYPE.STUDY_SELECT);
            } else {
                setDownBtnState(Tags.STUDY_BTN_TYPE.STUDY_DO_DOWN);
            }
        }

        updateSelectAllView(isShowSelectAll);
        mAdapter.updateHeader(isShowSelectAll);
        updateHeaderView(lectureCnt, selectCnt);
    }

    private void updateHeaderView(int lectureCnt, int selectCnt){
        if(!isDownloadMode){
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

    private void selectAllItems(){
        if(mDownloadManager != null){
            for (int i = 0; i < mAdapter.getRealItemCount(); i++) {
                StudyData study = mAdapter.getItem(i);
                StudyItemVO studyItem = study.getStudyItemVO();
                if(studyItem != null) {
                    //check duplication
                    if (study.getDownState() == Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_NONE){
                        if(studyItem.getStudyVodKey() == null) {
                            continue;
                        }else if(mDownloadManager.checkDownloadedStudy(studyItem.getStudyVodKey())) {
                            continue;
                        }else{
                            mAdapter.setItemSelected(i, true);
                        }
                    }
                }
            }

            //mAdapter.setAllItemSelected(true);
            updateHeader();

            Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.STUDY)
                    .putCustomAttribute(AnalysisTags.ACTION, "select_all"));
        }
    }

    private void deselectAllItems(){
        mAdapter.setAllItemSelected(false);
        updateHeader();

        Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.STUDY)
                .putCustomAttribute(AnalysisTags.ACTION, "deselect_all"));
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

    private void doDownloadWithDialog(){
        if(SystemUtils.getConnectivityStatus(getApplicationContext()) == Tags.NETWORK_TYPE.NETWORK_MOBILE
                && PropertyManager.getInstance().isNoticeData()){
            String message = getString(R.string.dialog_no_wifi_download);
            BraveUtils.showAlertDialogOkCancel(StudyActivity.this, message,
                    new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            doDownload();
                        }
                    }, new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            updateDownloadMode(false);
                        }
                    });
        }else{
            doDownload();
        }
    }

    private void doDownload(){
        startDownload();
        updateDownloadMode(false);
        Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.STUDY)
                .putCustomAttribute(AnalysisTags.ACTION, "do_download"));
    }

    // =============================================================================
    // Download
    // =============================================================================

    private void startDownload(){
        final ArrayList<StudyData> selectedItems = mAdapter.getSelectedItems();
        if(selectedItems != null && selectedItems.size() != 0 && mDownloadManager != null){
            startLoading();
            mDownloadManager.startDownload(mLecture, selectedItems,
                    new OnDownloadDataListener<Integer>() {
                        @Override
                        public void onDataProgress(int progress) {
                        }

                        @Override
                        public void onDataComplete(Integer result) {
                            stopLoading();
                            mDownloadManager.sendStartDownload();
                            BraveUtils.showToast(StudyActivity.this,
                                    String.format(getString(R.string.toast_study_down_guide), result));
                        }

                        @Override
                        public void onDataError(int error) {
                            stopLoading();
                            BraveUtils.showLoadOnFailToast(StudyActivity.this);
                        }
                    });
        }
    }

    @Override
    protected void updateDownloadViews(final String studyKey, final int state, final int percent, int errorCode){
        if(studyKey == null){
            return;
        }
        if(isResumed) {
            log.d(String.format("StudyActivity updateDownloadViews - studyKey: %s, state: %d, percent: %d, errorCode: %d", studyKey, state, percent, errorCode));
        }
        super.updateDownloadViews(studyKey, state, percent, errorCode);
        if(state == Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_COMPLETE) {
            refreshDownloadedData(studyKey);
        }

        runOnUiThread(new Runnable() {
            public void run() {
                int position = mAdapter.getItemWithStudyKey(studyKey);
                if(position != -1) {
                    int idx = position + 2;
                    idx = idx - mLayoutManager.findFirstVisibleItemPosition();
                    View view = mListView.getChildAt(idx);
                    if (view != null) {
                        mAdapter.updateDownState(view, state, percent);
                    } else {
                        mAdapter.updateDownState(studyKey, state, percent);
                    }
                }
            }
        });
    }

    // =============================================================================

    private void showToastWithDownState(int state){
        switch (state){
            case Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_NONE:
                break;
            case Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_ERROR:
            case Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_API_ERROR:
                BraveUtils.showToast(StudyActivity.this, getString(R.string.toast_study_down_error));
                break;
            case Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_API_ING:
            case Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_API_COMPLETE:
            case Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_PENDING:
            case Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_ING:
            case Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_PAUSE:
                BraveUtils.showToast(StudyActivity.this, getString(R.string.toast_study_down_ing));
                break;
            case Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_COMPLETE:
                BraveUtils.showToast(StudyActivity.this, getString(R.string.toast_study_down_complete));
                break;
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(Tags.TAG_REFRESH, BraveUtils.toJson(mLecture));
        setResult(RESULT_OK, intent);

        super.onBackPressed();
    }

    private void showDefault(boolean show){
        if(show){
            layoutDefault.setVisibility(View.VISIBLE);
            layoutList.setVisibility(View.GONE);
            btnDown.setVisibility(View.GONE);
        }else{
            layoutDefault.setVisibility(View.GONE);
            layoutList.setVisibility(View.VISIBLE);
            btnDown.setVisibility(View.VISIBLE);
        }
    }

}
