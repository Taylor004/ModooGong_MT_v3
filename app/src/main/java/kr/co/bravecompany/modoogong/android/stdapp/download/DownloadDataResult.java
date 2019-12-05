package kr.co.bravecompany.modoogong.android.stdapp.download;


/**
 * Created by BraveCompany on 2017. 6. 20..
 */

public class DownloadDataResult<T> {
    public OnDownloadDataListener<T> listener;
    public T result;
    public int progress;
    public int error;
}