package kr.co.bravecompany.modoogong.android.stdapp.download;

/**
 * Created by BraveCompany on 2017. 6. 20..
 */

public interface OnDownloadDataListener<T> {
    public void onDataProgress(int progress);
    public void onDataComplete(T result);
    public void onDataError(int error);
}
