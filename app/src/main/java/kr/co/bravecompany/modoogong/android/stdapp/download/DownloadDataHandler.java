package kr.co.bravecompany.modoogong.android.stdapp.download;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;


/**
 * Created by BraveCompany on 2017. 6. 20..
 */

public class DownloadDataHandler extends Handler {

    public static final int MESSAGE_ING = 1;
    public static final int MESSAGE_COMPLETE = 2;
    public static final int MESSAGE_ERROR = 3;

    public DownloadDataHandler(Looper looper) {
        super(looper);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        DownloadDataResult result = (DownloadDataResult) msg.obj;
        switch (msg.what) {
            case MESSAGE_ING:
                if(result.listener != null) result.listener.onDataProgress(result.progress);
                break;
            case MESSAGE_COMPLETE:
                if(result.listener != null) result.listener.onDataComplete(result.result);
                break;
            case MESSAGE_ERROR:
                if(result.listener != null) result.listener.onDataError(result.error);
                break;
        }
    }
}
