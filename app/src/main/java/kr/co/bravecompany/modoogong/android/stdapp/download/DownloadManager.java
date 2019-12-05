package kr.co.bravecompany.modoogong.android.stdapp.download;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.kollus.sdk.media.content.KollusContent;

import java.util.ArrayList;

import kr.co.bravecompany.modoogong.android.stdapp.config.AnalysisTags;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.data.DownloadData;
import kr.co.bravecompany.modoogong.android.stdapp.db.DataManager;
import kr.co.bravecompany.modoogong.android.stdapp.db.model.Lecture;
import kr.co.bravecompany.modoogong.android.stdapp.db.model.Study;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import kr.co.bravecompany.api.android.stdapp.api.data.LectureItemVO;
import kr.co.bravecompany.modoogong.android.stdapp.data.StudyData;

/**
 * Created by BraveCompany on 2016. 12. 2..
 */

public class DownloadManager {

    public static final String ACTION_DOWNLOAD_START = "kr.co.bravecompany.bravespk.android.stdapp.action.start";
    public static final String ACTION_DOWNLOAD_STATE = "kr.co.bravecompany.bravespk.android.stdapp.action.state";

    private Context mContext;

    private DownloadService mService;
    private boolean mBound = false;

    public DownloadManager(Context context) {
        mContext = context;
    }

    public void bindDownloadService(){
        Intent intent = new Intent(mContext, DownloadService.class);
        mContext.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    public void unBindDownloadService(){
        if (mBound) {
            mContext.unbindService(mConnection);
        }
    }

    public void startDownload(LectureItemVO lecture, ArrayList<StudyData> studies, OnDownloadDataListener<Integer> listener){
        if(mBound){
            mService.startDownload(lecture, studies, listener);

            String lectureName = lecture.getLectureName();

            ContentViewEvent event = new ContentViewEvent()
                    .putCustomAttribute(AnalysisTags.ACTION, "start_download");
            if(lectureName != null){
                event.putContentType(lectureName);
            }
            Answers.getInstance().logContentView(event);
        }
    }

    public StudyData getDownloadStudyVO(String studyKey){
        StudyData study = null;
        Study downloadStudy = DataManager.getInstance().getStudyModel(studyKey);
        if(downloadStudy != null) {
            study = new StudyData();
            study.setDownState(downloadStudy.getState());
            if (mBound) {
                KollusContent content = mService.getDownloadContent(downloadStudy.getMediaContentKey());
                if(content != null) {
                    study.setDownPercents(content.getDownloadPercent());
                }
            }
        }
        return study;
    }

    public DownloadData getDownloadingDownloadVO(String studyKey){
        DownloadData download = null;
        Study downloadStudy = DataManager.getInstance().getStudyModel(studyKey);
        if(downloadStudy != null) {
            download = new DownloadData();
            download.setDownState(downloadStudy.getState());
            download.setErrorCode(downloadStudy.getErrorCode());
            if (mBound) {
                KollusContent content = mService.getDownloadContent(downloadStudy.getMediaContentKey());
                if(content != null) {
                    download.setDownPercents(content.getDownloadPercent());
                }
            }
        }
        return download;
    }


    public int getDownloadingStudyCount(){
        ArrayList<Study> downs = DataManager.getInstance().getDownloadingStudyList();
        if(downs != null && downs.size() != 0){
            return downs.size();
        }
        return -1;
    }

    public int getDownloadPauseStudyCount(){
        DataManager instance = DataManager.getInstance();
        instance.refreshRealm();

        ArrayList<Study> all_pauses = instance.getDownloadAllPauseStudyList();
        if(all_pauses != null){
            int count = all_pauses.size();
            if(count != 0){
                return count;
            }
        }
        return -1;
    }

    public int getDownloadStudyCount(int studyLectureNo){
        ArrayList<Study> downs = DataManager.getInstance().getStudyList(studyLectureNo);
        if(downs != null){
            return downs.size();
        }
        return -1;
    }

    public Lecture getDownloadLecture(int studyLectureNo){
        return DataManager.getInstance().getLectureModel(studyLectureNo);
    }

    public ArrayList<Lecture> getDownloadLectureList(){
        return DataManager.getInstance().getLectureList();
    }

    public void updateDownloadLectureStudyEndDay(int studyLectureNo, String studyEndDay){
        DataManager.getInstance().updateStudyEndDay(studyLectureNo, studyEndDay);
    }

    public void updateDownloadLectureStudyState(int studyLectureNo, String studyState){
        DataManager.getInstance().updateStudyState(studyLectureNo, studyState);
    }

    public Study getDownloadStudy(String studyKey){
        return DataManager.getInstance().getStudyModel(studyKey);
    }

    public ArrayList<Study> getDownloadingStudyList(){
        return DataManager.getInstance().getDownloadingStudyList();
    }

    public ArrayList<Study> getDownloadCompleteStudyList(int studyLectureNo){
        return DataManager.getInstance().getDownloadCompleteStudyList(studyLectureNo);
    }

    public void removeDownloadStudyList(ArrayList<Study> studies, OnDownloadDataListener<String> listener){
        if(mBound){
            mService.removeDownload(studies, listener);
        }
    }

    public void removeDownloadLecture(int studyLectureNo){
        DataManager.getInstance().removeLecture(studyLectureNo);
        ArrayList<Study> removeStudies = DataManager.getInstance().getStudyList(studyLectureNo);
        if(removeStudies != null && removeStudies.size() != 0) {
            removeDownloadStudyList(removeStudies, null);
        }
    }

    public void pauseDownloadingStudy(String mediaContentKey){
        if(mBound){
            mService.pauseDownloadingContent(mediaContentKey);
        }
    }

    public void reStartDownload(){
        ArrayList<Study> all_pauses = DataManager.getInstance().getDownloadAllPauseStudyList();
        if(all_pauses != null && all_pauses.size() != 0){
            for(int i=0; i<all_pauses.size(); i++){
                Study study = all_pauses.get(i);
                reStartDownload(BraveUtils.fromHTML(study.getVodInfo()), study.getStudyKey());
            }
        }
    }

    public void reStartDownload(String vodInfo, String studyKey){
        if(mBound) {
            Study study = DataManager.getInstance().getStudyModel(studyKey);
            if(study != null){
                if (vodInfo == null) {
                    mService.loadVodData(study.getStudyLectureNo(), study.getLctCode(), studyKey);
                }else{
                    mService.startDownload(vodInfo, studyKey);
                }
            }
        }
    }

    public void forceFinishDownload(boolean taskRemoved, OnDownloadDataListener<String> listener){
        if(mBound){
            mService.forceFinishDownload(taskRemoved, listener);
        }
    }

    public void removeDownloadAll(OnDownloadDataListener<String> listener){
        if(mBound){
            mService.removeDownloadAll(listener);
        }
    }

    public boolean checkDownloadedStudy(String studyVodKey){
        Study study = DataManager.getInstance().getStudyModelVodKey(studyVodKey);
        return (study != null);
    }

    public void updateDownloadLecture(ArrayList<LectureItemVO> lectures) {
        ArrayList<Lecture> downloadLectures = getDownloadLectureList();
        for (int i = 0; i < downloadLectures.size(); i++) {
            Lecture downloadLecture = downloadLectures.get(i);
            if (lectures != null && lectures.size() != 0) {
                LectureItemVO lecture = getLectureItem(lectures, downloadLecture.getStudyLectureNo());
                if (lecture != null) {
                    String newStudyEndDay = lecture.getStudyEndDay();
                    String newStudyState = lecture.getStudyState();
                    if (newStudyEndDay != null && newStudyState != null) {
                        if (!newStudyEndDay.equals(downloadLecture.getStudyEndDay())) {
                            //Update study end day
                            updateDownloadLectureStudyEndDay(downloadLecture.getStudyLectureNo(), newStudyEndDay);
                        }
                        if (!newStudyState.equals(downloadLecture.getStudyState())) {
                            //Update study state
                            updateDownloadLectureStudyState(downloadLecture.getStudyLectureNo(), newStudyState);
                        }
                    }
                } else {
                    removeDownloadLecture(downloadLecture.getStudyLectureNo());
                }
            }else{
                removeDownloadLecture(downloadLecture.getStudyLectureNo());
            }
        }
    }

    private LectureItemVO getLectureItem(ArrayList<LectureItemVO> lectures, int downStudyLectureNo){
        for(int i=0; i<lectures.size(); i++){
            if(lectures.get(i).getStudyLectureNo() == downStudyLectureNo){
                return lectures.get(i);
            }
        }
        return null;
    }

    // =============================================================================
    // Storage
    // =============================================================================

    public String getStorageDownloadSize() {
        if(mBound){
            return mService.getStorageDownloadSize();
        }
        return null;
    }

    public String getStorageCacheSize() {
        if(mBound){
            return mService.getStorageCacheSize();
        }
        return null;
    }

    // =============================================================================
    // Send Broadcast
    // =============================================================================

    public void sendStartDownload(){
        Intent intent = new Intent(ACTION_DOWNLOAD_START);
        LocalBroadcastManager.getInstance(mContext).sendBroadcastSync(intent);
    }

    private void sendStateDownloadWithPercent(String mediaContentKey, int percent){
        Intent intent = new Intent(ACTION_DOWNLOAD_STATE);
        Study downloadStudy = DataManager.getInstance().getStudyModelContent(mediaContentKey);
        if(downloadStudy != null){
            intent.putExtra(Tags.TAG_STUDY_KEY, downloadStudy.getStudyKey());
            intent.putExtra(Tags.TAG_DOWN_STATE, downloadStudy.getState());
            intent.putExtra(Tags.TAG_DOWN_PERCENT, percent);
            LocalBroadcastManager.getInstance(mContext).sendBroadcastSync(intent);
        }
    }

    private void sendStateDownloadContent(String mediaContentKey, int state, int errorCode){
        String studyKey = DataManager.getInstance().getStudyKey(mediaContentKey);
        if(studyKey != null){
            sendStateDownload(studyKey, state, errorCode);
        }
    }

    private void sendStateDownload(String studyKey, int state, int errorCode){
        Intent intent = new Intent(ACTION_DOWNLOAD_STATE);
        if(studyKey != null){
            intent.putExtra(Tags.TAG_STUDY_KEY, studyKey);
            intent.putExtra(Tags.TAG_DOWN_STATE, state);
            intent.putExtra(Tags.TAG_ERROR_CODE, errorCode);
            LocalBroadcastManager.getInstance(mContext).sendBroadcastSync(intent);

            ContentViewEvent event = new ContentViewEvent()
                    .putCustomAttribute(AnalysisTags.ACTION, "error_download")
                    .putContentId(studyKey)
                    .putCustomAttribute(AnalysisTags.VALUE, String.valueOf(errorCode));
            switch (state){
                case Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_API_ERROR:
                    event.putCustomAttribute(AnalysisTags.ERROR, "api_error");
                    break;
                case Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_ERROR:
                    event.putCustomAttribute(AnalysisTags.ERROR, "down_error");
                    break;
            }
            Answers.getInstance().logContentView(event);
        }
    }

    // =============================================================================
    // Listener
    // =============================================================================

    private OnDownloadBindListener mDownloadBindListener;
    public void setOnDownloadBindListener(OnDownloadBindListener listener) {
        mDownloadBindListener = listener;
    }

    private OnDownloadListener mDownloadListener = new OnDownloadListener() {
        @Override
        public void onDownloadProgress(String mediaContentKey, int percent) {
            sendStateDownloadWithPercent(mediaContentKey, percent);
        }

        @Override
        public void onDownloadPause(String mediaContentKey, int percent) {
            DataManager.getInstance().updateDownStateContent(mediaContentKey,
                    Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_PAUSE, -1);
            sendStateDownloadWithPercent(mediaContentKey, percent);
        }

        @Override
        public void downloadStateChanged(String studyKey, int state, int errorCode) {
            DataManager.getInstance().updateDownState(studyKey, state, errorCode);
            sendStateDownload(studyKey, state, errorCode);
        }

        @Override
        public void downloadStateChangedContent(String mediaContentKey, int state, int errorCode) {
            DataManager.getInstance().updateDownStateContent(mediaContentKey, state, errorCode);
            sendStateDownloadContent(mediaContentKey, state, errorCode);
        }
    };

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            DownloadService.LocalBinder binder = (DownloadService.LocalBinder) service;
            mService = binder.getService();
            mService.setOnDownloadListener(mDownloadListener);
            mBound = true;
            if(mDownloadBindListener != null) {
                mDownloadBindListener.onBindComplete();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
}
