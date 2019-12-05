package kr.co.bravecompany.api.android.stdapp.api;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class NetworkHandler extends Handler {

    public static final int MESSAGE_SUCCESS = 1;
    public static final int MESSAGE_FAIL = 2;

    public NetworkHandler(Looper looper) {
        super(looper);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        NetworkResult result = (NetworkResult) msg.obj;
        switch (msg.what) {
            case MESSAGE_SUCCESS:
                result.listener.onSuccess(result.request, result.result);
                break;
            case MESSAGE_FAIL:
                result.listener.onFail(result.request, result.excpetion);
                break;
        }
    }
}

