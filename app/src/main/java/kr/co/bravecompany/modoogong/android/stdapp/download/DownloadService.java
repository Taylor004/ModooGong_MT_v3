package kr.co.bravecompany.modoogong.android.stdapp.download;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.Looper;
import androidx.annotation.Nullable;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.kollus.sdk.media.KollusPlayerDRMListener;
import com.kollus.sdk.media.KollusStorage;
import com.kollus.sdk.media.content.KollusContent;
import com.kollus.sdk.media.util.ErrorCodes;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kr.co.bravecompany.api.android.stdapp.api.OnResultListener;
import kr.co.bravecompany.api.android.stdapp.api.data.LectureItemVO;
import kr.co.bravecompany.api.android.stdapp.api.data.StudyItemVO;
import kr.co.bravecompany.api.android.stdapp.api.requests.StudyRequests;
import kr.co.bravecompany.api.android.stdapp.api.data.StudyVodResult;
import kr.co.bravecompany.modoogong.android.stdapp.config.AnalysisTags;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.data.StudyData;
import kr.co.bravecompany.modoogong.android.stdapp.db.DataManager;
import kr.co.bravecompany.modoogong.android.stdapp.db.model.Study;
import kr.co.bravecompany.modoogong.android.stdapp.manager.PropertyManager;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import kr.co.bravecompany.modoogong.android.stdapp.utils.log;
import kr.co.bravecompany.player.android.stdapp.utils.PlayerUtils;
import okhttp3.Request;

/**
 * Created by BraveCompany on 2016. 12. 1..
 */

public class DownloadService extends Service{

    private final IBinder mBinder = new LocalBinder();

    private ExecutorService mThreadPool;
    private static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    private static final int MAXIMUM_POOL_SIZE = 1;
    private static final long THREAD_WAIT_TIME = 30 * 1000L; //wait 30 seconds

    private KollusStorage mStorage;
    //List of Downloads
    private ArrayList<KollusContent> mContentsList;
    private ArrayList<String> mDownloadingList = new ArrayList<>();

    public static final int DATA_ERROR = -1000;
    private ExecutorService mDataThreadPool;
    private DownloadDataHandler mHandler = new DownloadDataHandler(Looper.getMainLooper());

    public static final String ACTION_TASK_REMOVED = "kr.co.bravecompany.bravespk.android.stdapp.action.TASKREMOVED";

    public DownloadService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mStorage = KollusStorage.getInstance(getApplicationContext());
        mStorage.setOnKollusStorageListener(mKollusStorageListener);
        mStorage.setKollusPlayerDRMListener(mKollusPlayerDRMListener);

        mContentsList = mStorage.getDownloadContentList();

        mThreadPool = Executors.newFixedThreadPool(MAXIMUM_POOL_SIZE);
        mDataThreadPool = Executors.newFixedThreadPool(MAXIMUM_POOL_SIZE);

        return START_STICKY;
        //return super.onStartCommand(intent, flags, startId);
    }

    public class LocalBinder extends Binder {
        DownloadService getService() {
            return DownloadService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void startDownload(LectureItemVO lecture, ArrayList<StudyData> studies, OnDownloadDataListener<Integer> listener){
        mDataThreadPool.execute(new DownloadStarter(lecture, studies, listener));
    }

    public void removeDownloadAll(OnDownloadDataListener<String> listener){
        mDataThreadPool.execute(new DownloadAllRemover(listener));
    }

    public void loadVodData(int studyLectureNo, int lctCode, final String studyKey){
        downloadStateChanged(studyKey, Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_API_ING);
        StudyRequests.getInstance().requestStudyVod(studyLectureNo, lctCode, false,
                new OnResultListener<StudyVodResult>(){

                    @Override
                    public void onSuccess(Request request, StudyVodResult result) {
                        setVodData(result, studyKey);
                    }

                    @Override
                    public void onFail(Request request, Exception exception) {
                        downloadStateChanged(studyKey, Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_API_ERROR);

                        if(exception != null && exception.getMessage() != null) {
                            Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.REQUEST)
                                    .putCustomAttribute(AnalysisTags.ERROR, AnalysisTags.DOWNLOAD + ":: " + exception.getMessage()));
                        }
                    }
                });
    }

    private void setVodData(StudyVodResult result, String studyKey){
        if(result == null){
            downloadStateChanged(studyKey, Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_API_ERROR);
            return;
        }
        String vodInfo = result.getVodInfo();
        if(vodInfo != null){
            startDownload(BraveUtils.fromHTML(vodInfo), studyKey);
            updateVodInfo(studyKey, vodInfo);
        }else{
            downloadStateChanged(studyKey, Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_API_ERROR);
        }
    }

    // =============================================================================
    // Download
    // =============================================================================

    public KollusContent getDownloadContent(String mediaContentKey){
        synchronized(mContentsList) {
            for(KollusContent iter : mContentsList) {
                if(iter.getMediaContentKey().equals(mediaContentKey)) {
                    return iter;
                }
            }
        }
        return null;
    }

    public void removeDownloadContents(ArrayList<String> mediaContentKeys){
        for(String mediaContentKey : mediaContentKeys) {
            synchronized (mContentsList) {
                for (KollusContent iter : mContentsList) {
                    if (iter.getMediaContentKey().equals(mediaContentKey)) {
                        removeDownloadingContent(mediaContentKey);
                        if (iter.isDownloading()) {
                            mStorage.unload(mediaContentKey);
                            iter.setDownloading(false);
                        }
                        mStorage.remove(mediaContentKey);
                        mContentsList.remove(iter);
                        break;
                    }
                }
            }
        }
    }

    public void pauseDownloadingContent(String mediaContentKey){
        synchronized(mContentsList) {
            for(KollusContent iter : mContentsList) {
                if(iter.getMediaContentKey().equals(mediaContentKey)) {
                    if(iter.isDownloading()) {
                        onDownloadPause(mediaContentKey, iter.getDownloadPercent());
                        removeDownloadingContent(mediaContentKey);
                        mStorage.unload(mediaContentKey);
                        iter.setDownloading(false);
                    }
                    break;
                }
            }
        }
    }

    public void pauseDownloadingContents(ArrayList<String> mediaContentKeys){
        for(String mediaContentKey : mediaContentKeys) {
            synchronized (mContentsList) {
                for (KollusContent iter : mContentsList) {
                    if (iter.getMediaContentKey().equals(mediaContentKey)) {
                        if (iter.isDownloading()) {
                            removeDownloadingContent(mediaContentKey);
                            mStorage.unload(mediaContentKey);
                            iter.setDownloading(false);
                        }
                        break;
                    }
                }
            }
        }
    }

    public void removeDownloadContentAll(){
        mThreadPool.shutdownNow();
        mStorage.cancelLoad();
        synchronized(mContentsList) {
            for(KollusContent iter : mContentsList) {
                if(iter.isDownloading()){
                    mStorage.unload(iter.getMediaContentKey());
                    iter.setDownloading(false);
                }
                mStorage.remove(iter.getMediaContentKey());
            }
            mStorage.clearCache();
            mContentsList.clear();
            mDownloadingList.clear();
        }
    }

    public synchronized void startDownload(String vodInfo, String studyKey){
        downloadStateChanged(studyKey, Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_API_COMPLETE);

        DownloadTask down = new DownloadTask(vodInfo, studyKey);
        mThreadPool.execute(down);
    }

    private class DownloadTask implements Runnable {

        private String vodInfo;
        private String studyKey;

        public DownloadTask(String vodInfo, String studyKey) {
            this.vodInfo = vodInfo;
            this.studyKey = studyKey;
        }

        @Override
        public void run() {
            downloadStateChanged(studyKey, Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_PENDING);

            Uri uri = Uri.parse(vodInfo);
            if(uri != null) {
                log.d("handleIntent Intent Data : "+uri.toString());
                KollusContent content = new KollusContent();
                int index = -1;
                if(uri.getQueryParameterNames().isEmpty())
                    index = mStorage.load(uri.toString()+"?download", content);
                else
                    index = mStorage.load(uri.toString()+"&download", content);
                log.d("handleIntent index "+index);
                if(index >= 0) {
                    boolean bExist = false;
                    mStorage.download(content.getMediaContentKey());
                    for(KollusContent tmp : mContentsList) {
                        if(tmp.getMediaContentKey().equals(content.getMediaContentKey())) {
                            tmp.setDownloading(true);
                            bExist = true;
                            break;
                        }
                    }

                    if(!bExist) {
                        mContentsList.add(content);
                        content.setDownloading(true);
                    }
                    downloadStateChanged(studyKey, Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_ING);
                    updateMediaContentKey(studyKey, content.getMediaContentKey());

                    mDownloadingList.add(content.getMediaContentKey());
                    log.d("### content.getMediaContentKey() - " + content.getMediaContentKey());
                }
                else {
                    //showErrorMessage(index);
                    showErrorMessage(index, studyKey);
                }
            }else{
                downloadStateChanged(studyKey, Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_ERROR);
            }
        }
    }

    private void showErrorMessage(int errorCode) {
        String title = "Error" + " ("+errorCode+")";
        String message = mStorage.getLastError();
        if(message == null)
            message = ErrorCodes.getInstance(getApplicationContext()).getErrorString(errorCode);

        log.d("DownloadService showErrorMessage " + title + ": " + message);
    }

    private void showErrorMessage(int errorCode, String studyKey) {
        downloadStateChanged(studyKey, Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_ERROR, errorCode);

        showErrorMessage(errorCode);
    }

    private void showErrorMessageContent(int errorCode, String mediaContentKey) {
        downloadStateChangedContent(mediaContentKey, Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_ERROR, errorCode);

        showErrorMessage(errorCode);
    }

    // =============================================================================
    // Listener
    // =============================================================================

    private OnDownloadListener mDownloadListener;
    public void setOnDownloadListener(OnDownloadListener listener) {
        mDownloadListener = listener;
    }

    private KollusStorage.OnKollusStorageListener mKollusStorageListener = new KollusStorage.OnKollusStorageListener() {
        @Override
        public void onComplete(KollusContent content) {
            synchronized (mContentsList) {
                for(KollusContent iter : mContentsList) {
                    if(iter.getMediaContentKey().equals(content.getMediaContentKey())) {
                        content.setDownloading(false);
                        content.setDownloadCompleted(true);

                        downloadStateChangedContent(content.getMediaContentKey(),
                                Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_COMPLETE);
                        removeDownloadingContent(content.getMediaContentKey());
                        break;
                    }
                }
            }
        }

        @Override
        public void onProgress(KollusContent content) {
            synchronized (mContentsList) {
                for(KollusContent iter : mContentsList) {
                    if(iter.getMediaContentKey().equals(content.getMediaContentKey())) {
                        int percent = (int) (content.getReceivingSize()*100/content.getFileSize());
                        iter.setDownloading(true);
                        iter.setReceivedSize(content.getReceivingSize());
                        iter.setDownloadPercent(percent);

                        onDownloadProgress(content.getMediaContentKey(), percent);
                        break;
                    }
                }
            }
        }

        @Override
        public void onError(KollusContent content, int errorCode) {
            synchronized (mContentsList) {
                for(KollusContent iter : mContentsList) {
                    if(iter.getMediaContentKey().equals(content.getMediaContentKey())) {
                        iter.setDownloading(false);
                        iter.setDownloadError();

                        showErrorMessageContent(errorCode, content.getMediaContentKey());
                        removeDownloadingContent(content.getMediaContentKey());
                        break;
                    }
                }
            }
        }
    };

    private KollusPlayerDRMListener mKollusPlayerDRMListener = new KollusPlayerDRMListener() {
        @Override
        public void onDRM(String request, String response) {
            log.d(String.format("onDRM request '%s' response '%s'", request, response));
        }

        @Override
        public void onDRMInfo(KollusContent content, int nInfoCode) {
            if(nInfoCode == KollusPlayerDRMListener.DCB_INFO_DELETE) {
                removeDownloadMediaContent(content.getMediaContentKey());
                synchronized(mContentsList) {
                    for(KollusContent iter : mContentsList) {
                        if(iter.getMediaContentKey().equals(content.getMediaContentKey())) {
                            removeDownloadingContent(content.getMediaContentKey());
                            mContentsList.remove(iter);
                            break;
                        }
                    }
                }
            }
            log.d(String.format("onDRMInfo index %d info code %d message %s", content.getUriIndex(), nInfoCode, content.getServiceProviderMessage()));
        }
    };

    private void removeDownloadingContent(String mediaContentKey){
        synchronized(mDownloadingList) {
            for (int i = 0; i < mDownloadingList.size(); i++) {
                if (mDownloadingList.get(i).equals(mediaContentKey)) {
                    mDownloadingList.remove(i);
                    break;
                }
            }
        }
    }

    // =============================================================================
    // Update DB
    // =============================================================================

    public void onDownloadProgress(String mediaContentKey, int percent) {
        mDownloadListener.onDownloadProgress(mediaContentKey, percent);
    }

    public void onDownloadPause(String mediaContentKey, int percent) {
        mDownloadListener.onDownloadPause(mediaContentKey, percent);
    }

    public void downloadStateChanged(String studyKey, int state) {
        downloadStateChanged(studyKey, state, -1);
    }

    public void downloadStateChangedContent(String mediaContentKey, int state) {
        downloadStateChangedContent(mediaContentKey, state, -1);
    }

    public void downloadStateChanged(String studyKey, int state, int errorCode) {
        mDownloadListener.downloadStateChanged(studyKey, state, errorCode);
    }

    public void downloadStateChangedContent(String mediaContentKey, int state, int errorCode) {
        mDownloadListener.downloadStateChangedContent(mediaContentKey, state, errorCode);
    }

    public void updateMediaContentKey(String studyKey, String mediaContentKey) {
        DataManager.getInstance().updateMediaContentKey(studyKey, mediaContentKey);
    }

    public void updateVodInfo(String studyKey, String vodInfo) {
        DataManager.getInstance().updateVodInfo(studyKey, vodInfo);
    }

    public void removeDownloadMedia(String studyKey) {
        DataManager.getInstance().removeStudy(studyKey);
    }

    public void removeDownloadMediaContent(String mediaContentKey) {
        DataManager.getInstance().removeStudyContent(mediaContentKey);
    }

    // =============================================================================
    // Storage
    // =============================================================================

    public String getStorageDownloadSize() {
        if(mStorage != null) {
            return PlayerUtils.getStringSize(mStorage.getUsedSize(KollusStorage.TYPE_DOWNLOAD));
        }
        return null;
    }

    public String getStorageCacheSize() {
        if(mStorage != null) {
            return PlayerUtils.getStringSize(mStorage.getUsedSize(KollusStorage.TYPE_CACHE));
        }
        return null;
    }


    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mStorage != null) {
            mStorage.finish();
        }
        if(mThreadPool != null){
            mThreadPool.shutdownNow();
        }
    }

    // =============================================================================
    // Runnables
    // =============================================================================

    private class DownloadStarter implements Runnable {
        private DownloadDataResult<Integer> result = new DownloadDataResult<>();

        private LectureItemVO lecture;
        private ArrayList<StudyData> studies;
        private OnDownloadDataListener<Integer> listener;

        public DownloadStarter(LectureItemVO lecture, ArrayList<StudyData> studies, OnDownloadDataListener<Integer> listener) {
            this.lecture = lecture;
            this.studies = studies;
            this.listener = listener;
        }

        @Override
        public void run() {
            result.listener = listener;

            DataManager instance = DataManager.getInstance();
            instance.refreshRealm();

            instance.createLectureStudyModels(lecture, studies, mOnDataChangeListener);
        }

        private DataManager.OnDataChangeListener mOnDataChangeListener = new DataManager.OnDataChangeListener() {
            @Override
            public void onDataChangeProgress(int progress) {
            }

            @Override
            public void onDataChangeComplete() {
                startDownload(lecture, studies);
                mHandler.sendMessage(mHandler.obtainMessage(DownloadDataHandler.MESSAGE_COMPLETE, result));
            }

            @Override
            public void onDataChangeError() {
                mHandler.sendMessage(mHandler.obtainMessage(DownloadDataHandler.MESSAGE_ERROR, DATA_ERROR));
            }
        };

        private void startDownload(LectureItemVO lecture, ArrayList<StudyData> studies){
            int studyLectureNo = lecture.getStudyLectureNo();
            for(StudyData study : studies){
                if(study != null){
                    StudyItemVO item = study.getStudyItemVO();
                    loadVodData(studyLectureNo, item.getLctCode(), study.getStudyKey());
                }
            }
            result.result = studies.size();
        }
    }

    private class DownloadRemover implements Runnable {
        private DownloadDataResult<String> result = new DownloadDataResult<>();

        private ArrayList<String> studyKeys;
        private ArrayList<String> mediaContentKeys;
        private OnDownloadDataListener<String> listener;

        public DownloadRemover(ArrayList<String> studyKeys, ArrayList<String> mediaContentKeys, OnDownloadDataListener<String> listener) {
            this.studyKeys = studyKeys;
            this.mediaContentKeys = mediaContentKeys;
            this.listener = listener;
        }

        @Override
        public void run() {
            result.listener = listener;

            DataManager instance = DataManager.getInstance();
            instance.refreshRealm();

            removeDownloadContents(mediaContentKeys);
            instance.removeStudys(studyKeys, mediaContentKeys, mOnDataChangeListener);
        }

        private DataManager.OnDataChangeListener mOnDataChangeListener = new DataManager.OnDataChangeListener() {
            @Override
            public void onDataChangeProgress(int progress) {
            }

            @Override
            public void onDataChangeComplete() {
                mHandler.sendMessage(mHandler.obtainMessage(DownloadDataHandler.MESSAGE_COMPLETE, result));
            }

            @Override
            public void onDataChangeError() {
                mHandler.sendMessage(mHandler.obtainMessage(DownloadDataHandler.MESSAGE_ERROR, DATA_ERROR));
            }
        };
    }

    private class DownloadFinisher implements Runnable {
        private DownloadDataResult<String> result = new DownloadDataResult<>();

        private OnDownloadDataListener<String> listener;

        private ArrayList<String> removeStudyKeys;
        private ArrayList<String> removeMediaContentKeys;

        public DownloadFinisher(OnDownloadDataListener<String> listener) {
            this.listener = listener;
        }

        @Override
        public void run() {
            result.listener = listener;

            DataManager instance = DataManager.getInstance();
            instance.refreshRealm();

            ArrayList<Study> downloadStudies = instance.getDownloadingStudyList();
            ArrayList<String> pauseMediaContentKeys = new ArrayList<>();
            removeStudyKeys = new ArrayList<>();
            removeMediaContentKeys = new ArrayList<>();
            if(downloadStudies != null && downloadStudies.size() != 0){
                for (int i = 0; i < downloadStudies.size(); i++) {
                    Study downloadStudy = downloadStudies.get(i);
                    int state = downloadStudy.getState();
                    String mediaContentKey = downloadStudy.getMediaContentKey();
                    switch (state) {
                        case Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_ING:
                            pauseMediaContentKeys.add(mediaContentKey);
                            break;
                        case Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_API_ING:
                        case Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_API_COMPLETE:
                        case Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_PENDING:
                            if (mediaContentKey == null) {
                                removeStudyKeys.add(downloadStudy.getStudyKey());
                            } else {
                                removeMediaContentKeys.add(mediaContentKey);
                            }
                            break;
                    }
                }
                pauseDownloadingContents(pauseMediaContentKeys);
                removeDownloadContents(removeMediaContentKeys);
                instance.updateDownStateContents(pauseMediaContentKeys,
                        Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_PAUSE, -1, mOnPauseDataChangeListener);
            }else{
                mHandler.sendMessage(mHandler.obtainMessage(DownloadDataHandler.MESSAGE_COMPLETE, result));
            }
        }

        private DataManager.OnDataChangeListener mOnPauseDataChangeListener = new DataManager.OnDataChangeListener() {
            @Override
            public void onDataChangeProgress(int progress) {
            }

            @Override
            public void onDataChangeComplete() {
                DataManager.getInstance().removeStudys(removeStudyKeys, removeMediaContentKeys, mOnRemoveDataChangeListener);
            }

            @Override
            public void onDataChangeError() {
                mHandler.sendMessage(mHandler.obtainMessage(DownloadDataHandler.MESSAGE_ERROR, DATA_ERROR));
            }
        };

        private DataManager.OnDataChangeListener mOnRemoveDataChangeListener = new DataManager.OnDataChangeListener() {
            @Override
            public void onDataChangeProgress(int progress) {
            }

            @Override
            public void onDataChangeComplete() {
                mHandler.sendMessage(mHandler.obtainMessage(DownloadDataHandler.MESSAGE_COMPLETE, result));
            }

            @Override
            public void onDataChangeError() {
                mHandler.sendMessage(mHandler.obtainMessage(DownloadDataHandler.MESSAGE_ERROR, DATA_ERROR));
            }
        };
    }

    private class DownloadAllRemover implements Runnable {
        private DownloadDataResult<String> result = new DownloadDataResult<>();

        private OnDownloadDataListener<String> listener;

        public DownloadAllRemover(OnDownloadDataListener<String> listener) {
            this.listener = listener;
        }

        @Override
        public void run() {
            result.listener = listener;

            DataManager instance = DataManager.getInstance();
            instance.refreshRealm();

            removeDownloadContentAll();
            instance.removeAllLectureStudyModels(mOnDataChangeListener);
        }

        private DataManager.OnDataChangeListener mOnDataChangeListener = new DataManager.OnDataChangeListener() {
            @Override
            public void onDataChangeProgress(int progress) {
            }

            @Override
            public void onDataChangeComplete() {
                mHandler.sendMessage(mHandler.obtainMessage(DownloadDataHandler.MESSAGE_COMPLETE, result));
            }

            @Override
            public void onDataChangeError() {
                mHandler.sendMessage(mHandler.obtainMessage(DownloadDataHandler.MESSAGE_ERROR, DATA_ERROR));
            }
        };
    }

    // =============================================================================
    // Process killed
    // =============================================================================

    public void forceFinishDownload(boolean taskRemoved, OnDownloadDataListener<String> listener){
        try{
            if(mThreadPool != null && !taskRemoved) {
                mThreadPool.shutdownNow();
            }
            mDataThreadPool.execute(new DownloadFinisher(listener));
        }catch (Exception e){
            log.e(log.getStackTraceString(e));
        }
    }

    public void removeDownload(ArrayList<Study> studies, OnDownloadDataListener<String> listener){
        if(studies != null && studies.size() != 0) {
            ArrayList<String> studyKeys = new ArrayList<>();
            ArrayList<String> mediaContentKeys = new ArrayList<>();
            for (Study study : studies) {
                String mediaContentKey = study.getMediaContentKey();
                if (mediaContentKey == null) {
                    studyKeys.add(study.getStudyKey());
                } else {
                    mediaContentKeys.add(mediaContentKey);
                }
            }
            mDataThreadPool.execute(new DownloadRemover(studyKeys, mediaContentKeys, listener));
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        log.d("JJO onTaskRemoved");
        //super.onTaskRemoved(rootIntent);
        PropertyManager.getInstance().setTaskRemoved(true);
    }
}
