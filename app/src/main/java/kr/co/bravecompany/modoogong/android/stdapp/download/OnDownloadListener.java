package kr.co.bravecompany.modoogong.android.stdapp.download;

/**
 * Created by BraveCompany on 2016. 12. 5..
 */

public interface OnDownloadListener {
    void onDownloadProgress(String mediaContentKey, int percent);
    void onDownloadPause(String mediaContentKey, int percent);
    void downloadStateChanged(String studyKey, int state, int errorCode);
    void downloadStateChangedContent(String mediaContentKey, int state, int errorCode);
}
