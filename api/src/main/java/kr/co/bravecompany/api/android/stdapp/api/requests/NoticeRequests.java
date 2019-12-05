package kr.co.bravecompany.api.android.stdapp.api.requests;

import java.io.IOException;

import kr.co.bravecompany.api.android.stdapp.api.NetworkHandler;
import kr.co.bravecompany.api.android.stdapp.api.NetworkManager;
import kr.co.bravecompany.api.android.stdapp.api.NetworkResult;
import kr.co.bravecompany.api.android.stdapp.api.OnResultListener;
import kr.co.bravecompany.api.android.stdapp.api.data.NoticeDetailResult;
import kr.co.bravecompany.api.android.stdapp.api.data.NoticeResult;
import kr.co.bravecompany.api.android.stdapp.api.utils.ApiUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by BraveCompany on 2016. 11. 11..
 */

public class NoticeRequests extends NetworkManager{
    private static NoticeRequests instance;

    public static NoticeRequests getInstance() {
        if (mContext == null) {
            throw new IllegalStateException("Call `NetworkManager.init(Context..)` before calling this method.");
        }
        if (instance == null) {
            instance = new NoticeRequests();
        }
        return instance;
    }

    public Request requestNoticeList(int page, int perPage, OnResultListener<NoticeResult> listener) {
        Request request = new Request.Builder()
                .url(api.getNoticeListUrl(mContext, page, perPage))
                .build();

        final NetworkResult<NoticeResult> result = new NetworkResult<>();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(NetworkHandler.MESSAGE_FAIL, result));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String text = response.body().string();
                    text = ApiUtils.replaceBody(text);
                    NoticeResult status = gson.fromJson(text, NoticeResult.class);
                    result.result = status;
                    mHandler.sendMessage(mHandler.obtainMessage(NetworkHandler.MESSAGE_SUCCESS, result));
                } else {
                    result.excpetion = new IOException(response.message());
                    mHandler.sendMessage(mHandler.obtainMessage(NetworkHandler.MESSAGE_FAIL, result));
                }
            }
        });
        return request;
    }

    public Request requestNoticeDetail(int noticeNo, OnResultListener<NoticeDetailResult> listener) {
        Request request = new Request.Builder()
                .url(api.getNoticeDetailUrl(mContext, noticeNo))
                .build();

        final NetworkResult<NoticeDetailResult> result = new NetworkResult<>();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(NetworkHandler.MESSAGE_FAIL, result));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String text = response.body().string();
                    text = ApiUtils.replaceBody(text);
                    NoticeDetailResult status = gson.fromJson(text, NoticeDetailResult.class);
                    result.result = status;
                    mHandler.sendMessage(mHandler.obtainMessage(NetworkHandler.MESSAGE_SUCCESS, result));
                } else {
                    result.excpetion = new IOException(response.message());
                    mHandler.sendMessage(mHandler.obtainMessage(NetworkHandler.MESSAGE_FAIL, result));
                }
            }
        });
        return request;
    }
}
