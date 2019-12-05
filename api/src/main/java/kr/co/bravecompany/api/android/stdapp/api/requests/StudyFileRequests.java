package kr.co.bravecompany.api.android.stdapp.api.requests;

import java.io.IOException;

import kr.co.bravecompany.api.android.stdapp.api.NetworkHandler;
import kr.co.bravecompany.api.android.stdapp.api.NetworkManager;
import kr.co.bravecompany.api.android.stdapp.api.NetworkResult;
import kr.co.bravecompany.api.android.stdapp.api.OnResultListener;
import kr.co.bravecompany.api.android.stdapp.api.data.StudyFileDetailResult;
import kr.co.bravecompany.api.android.stdapp.api.data.StudyFileResult;
import kr.co.bravecompany.api.android.stdapp.api.utils.ApiUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by BraveCompany on 2017. 10. 31..
 */

public class StudyFileRequests extends NetworkManager {
    private static StudyFileRequests instance;

    public static StudyFileRequests getInstance() {
        if (mContext == null) {
            throw new IllegalStateException("Call `NetworkManager.init(Context..)` before calling this method.");
        }
        if (instance == null) {
            instance = new StudyFileRequests();
        }
        return instance;
    }

    public Request requestStudyFileList(int page, OnResultListener<StudyFileResult> listener) {
        Request request = new Request.Builder()
                .url(api.getStudyFileListUrl(mContext, page))
                .build();

        final NetworkResult<StudyFileResult> result = new NetworkResult<>();
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
                    StudyFileResult status = gson.fromJson(text, StudyFileResult.class);
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

    public Request requestStudyFileDetail(int no, OnResultListener<StudyFileDetailResult> listener) {
        Request request = new Request.Builder()
                .url(api.getStudyFileDetailUrl(mContext, no))
                .build();

        final NetworkResult<StudyFileDetailResult> result = new NetworkResult<>();
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
                    StudyFileDetailResult status = gson.fromJson(text, StudyFileDetailResult.class);
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