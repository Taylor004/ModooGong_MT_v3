package kr.co.bravecompany.modoogong.android.stdapp.pageLocalLecture;

import android.content.Intent;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import java.util.ArrayList;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.pageLocalStudy.LocalStudyActivity;
import kr.co.bravecompany.modoogong.android.stdapp.activity.MainActivity;
import kr.co.bravecompany.api.android.stdapp.api.OnResultListener;
import kr.co.bravecompany.api.android.stdapp.api.config.APIConfig;
import kr.co.bravecompany.api.android.stdapp.api.requests.StudyRequests;
import kr.co.bravecompany.api.android.stdapp.api.data.LectureItemVO;
import kr.co.bravecompany.api.android.stdapp.api.data.LectureResult;
import kr.co.bravecompany.modoogong.android.stdapp.config.AnalysisTags;
import kr.co.bravecompany.modoogong.android.stdapp.config.RequestCode;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.db.model.Lecture;
import kr.co.bravecompany.modoogong.android.stdapp.download.OnDownloadBindListener;
import kr.co.bravecompany.modoogong.android.stdapp.fragment.DownBaseFragment;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import kr.co.bravecompany.modoogong.android.stdapp.utils.SystemUtils;
import kr.co.bravecompany.modoogong.android.stdapp.utils.log;
import kr.co.bravecompany.modoogong.android.stdapp.viewholder.OnItemClickListener;
import kr.co.bravecompany.modoogong.android.stdapp.data.LocalLectureData;
import okhttp3.Request;

/**
 * Created by BraveCompany on 2016. 10. 12..
 */

public class LocalLectureFragment extends DownBaseFragment {

    private RecyclerView mListView;
    private LocalLectureAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private SwipeRefreshLayout refreshView;

    private View layoutDefault;

    //header
    private TextView txtLectureCount;
    //end
    private TextView txtMemory;


    public static LocalLectureFragment newInstance() {
        return new LocalLectureFragment();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RequestCode.REQUEST_STUDY){
            initLocalData();
        }else if(requestCode == RequestCode.REQUEST_DOWN){
            initLocalData();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_local_lecture, container, false);
        initLayout(rootView);
        initListener();
        //initLocalData();
        return rootView;
    }

    @Override
    protected void initLayout(ViewGroup rootView) {
        if(BraveUtils.checkUserInfo()) {
            super.initLayout(rootView);
        }

        ((MainActivity) getActivity()).setToolbarAuxButton(0);

        mAdapter = new LocalLectureAdapter();
        mListView = (RecyclerView)rootView.findViewById(R.id.recyclerDownLecture);
        mListView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(getContext());
        mListView.setLayoutManager(mLayoutManager);

        refreshView = (SwipeRefreshLayout)rootView.findViewById(R.id.refreshView);
        int color = getResources().getColor(R.color.colorPrimary);
        refreshView.setColorSchemeColors(color, color, color, color);

        layoutDefault = rootView.findViewById(R.id.layoutDefault);

        //header
        txtLectureCount = (TextView)rootView.findViewById(R.id.txtLectureCount);
        //end
        txtMemory = (TextView)rootView.findViewById(R.id.txtMemory);
    }

    @Override
    protected void initListener() {
        if(BraveUtils.checkUserInfo()) {
            super.initListener();
        }

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                LocalLectureData lecture = mAdapter.getItem(position);
                Lecture item = lecture.getLectureVO();
                String studyState = item.getStudyState();
                if(studyState != null){
                    if (BraveUtils.checkPrevLecture(studyState)) {
                        BraveUtils.showToast(getActivity(), getString(R.string.toast_lecture_prev));
                    } else if (BraveUtils.checkStopLecture(studyState)) {
                        BraveUtils.showToast(getActivity(), getString(R.string.toast_lecture_stop));
                    } else {
                        Intent intent = new Intent(getActivity(), LocalStudyActivity.class);
                        intent.putExtra(Tags.TAG_LECTURE, item.getStudyLectureNo());
                        //intent.putExtra(Tags.TAG_LECTURE, BraveUtils.toJson(item.getLectureVO()));
                        startActivityForResult(intent, RequestCode.REQUEST_STUDY);
                    }
                }
            }
        });

        refreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();

                Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.LOCALLECTURE)
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

    @Override
    protected void setData(String data) {

    }

    @Override
    protected void initData() {
        if(SystemUtils.isNetworkConnected(getContext()) && BraveUtils.checkUserInfo()){
            loadData();
        }else{
            initLocalData();
        }
    }

    private void loadData(){
        if(!refreshView.isRefreshing()) {
            startLoading();
        }
        StudyRequests.getInstance().requestLectureList(1, APIConfig.LECTURE_STATE_TYPE.LECTURE_ING_ITEM,
                1000, new OnResultListener<LectureResult>(){

            @Override
            public void onSuccess(Request request, LectureResult result) {
                if(!refreshView.isRefreshing()) {
                    stopLoading();
                }else{
                    refreshView.setRefreshing(false);
                }
                setData(result);
            }

            @Override
            public void onFail(Request request, Exception exception) {
                if(!refreshView.isRefreshing()) {
                    stopLoading();
                }else{
                    refreshView.setRefreshing(false);
                }
                //BraveUtils.showToast(getActivity(), getString(R.string.toast_common_network_fail));
                if(exception != null && exception.getMessage() != null) {
                    Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.REQUEST)
                            .putCustomAttribute(AnalysisTags.ERROR, AnalysisTags.LOCALLECTURE + ":: " + exception.getMessage()));
                }

                initLocalData();
            }
        });
    }

    private void setData(LectureResult result) {
        if(result == null){
            return;
        }
        if(mDownloadManager != null) {
            ArrayList<LectureItemVO> lectures = result.getStudies();
            mDownloadManager.updateDownloadLecture(lectures);
            updateDownloadViews();
        }
        initLocalData();
    }

    private void initLocalData(){
        mAdapter.clear();
        if(mDownloadManager != null) {
            ArrayList<LocalLectureData> items = new ArrayList<>();
            ArrayList<Lecture> downloadLectures = mDownloadManager.getDownloadLectureList();
            if (downloadLectures != null && downloadLectures.size() != 0) {
                for(int i=0; i<downloadLectures.size(); i++) {
                    LocalLectureData lecture = new LocalLectureData();
                    Lecture downloadLecture = downloadLectures.get(i);
                    if(!checkRemoveItem(downloadLecture)){
                        lecture.setLectureVO(downloadLecture);
                        items.add(lecture);
                    }
                }
                mAdapter.addAll(items);
            }
            showDefault();

            txtLectureCount.setText(String.format(getResources().getString(R.string.lecture_count),
                    mAdapter.getItemCount()));

            txtMemory.setText(BraveUtils.getAvailableMemorySize(getContext()));

        }else{
            log.d("mDownloadManager == NULL");
        }
        if(refreshView.isRefreshing()){
            refreshView.setRefreshing(false);
        }
    }

    private boolean checkRemoveItem(Lecture downloadLecture){
        //Expiration
        int days = BraveUtils.getLectureingDays(downloadLecture.getStudyEndDay());
        if(days == -1){
            mDownloadManager.removeDownloadLecture(downloadLecture.getStudyLectureNo());
            return true;
        }
        //Removed all studies
        int count = mDownloadManager.getDownloadStudyCount(downloadLecture.getStudyLectureNo());
        if(count == 0){
            mDownloadManager.removeDownloadLecture(downloadLecture.getStudyLectureNo());
            return true;
        }
        return false;
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
            log.d(String.format("LocalLectureFragment updateDownloadViews - studyKey: %s, state: %d, percent: %d, errorCode: %d", studyKey, state, percent, errorCode));
        }
        super.updateDownloadViews(studyKey, state, percent, errorCode);
        if(isResumed && state == Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_COMPLETE){
            BraveUtils.showToast(getActivity(), getString(R.string.toast_local_download_complete));
        }
    }

    // =============================================================================

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
