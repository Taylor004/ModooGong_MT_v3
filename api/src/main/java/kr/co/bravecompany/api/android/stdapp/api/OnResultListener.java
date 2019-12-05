package kr.co.bravecompany.api.android.stdapp.api;

import okhttp3.Request;


public interface OnResultListener<T> {
    public void onSuccess(Request request, T result);
    public void onFail(Request request, Exception exception);
}

