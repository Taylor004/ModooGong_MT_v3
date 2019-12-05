package kr.co.bravecompany.api.android.stdapp.api;

import okhttp3.Request;

public class NetworkResult<T> {
    public Request request;
    public OnResultListener<T> listener;
    public Exception excpetion;
    public T result;
    public T result2;
}