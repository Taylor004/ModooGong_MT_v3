package kr.co.bravecompany.api.android.stdapp.api.requests;

import java.io.IOException;

import kr.co.bravecompany.api.android.stdapp.api.NetworkHandler;
import kr.co.bravecompany.api.android.stdapp.api.NetworkManager;
import kr.co.bravecompany.api.android.stdapp.api.NetworkResult;
import kr.co.bravecompany.api.android.stdapp.api.OnResultListener;
import kr.co.bravecompany.api.android.stdapp.api.config.APIConfig;
import kr.co.bravecompany.api.android.stdapp.api.data.BoardCateResult;
import kr.co.bravecompany.api.android.stdapp.api.data.BoardDelFileRequest;
import kr.co.bravecompany.api.android.stdapp.api.data.PostResult;
import kr.co.bravecompany.api.android.stdapp.api.utils.ApiUtils;
import kr.co.bravecompany.api.android.stdapp.manager.APIPropertyManager;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by BraveCompany on 2016. 11. 15..
 */

public class BoardRequests extends NetworkManager{
    private static BoardRequests instance;

    public static BoardRequests getInstance() {
        if (mContext == null) {
            throw new IllegalStateException("Call `NetworkManager.init(Context..)` before calling this method.");
        }
        if (instance == null) {
            instance = new BoardRequests();
        }
        return instance;
    }

    public Request requestStudyQABoardCateList(OnResultListener<BoardCateResult> listener) {
        Request request = new Request.Builder()
                .url(api.getStudyQABoardCateUrl(mContext))
                .build();

        final NetworkResult<BoardCateResult> result = new NetworkResult<>();
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
                    BoardCateResult status = gson.fromJson(text, BoardCateResult.class);
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

    public Request requestOneToOneQABoardCateList(OnResultListener<BoardCateResult> listener) {
        Request request = new Request.Builder()
                .url(api.getOneToOneQABoardCateUrl(mContext))
                .build();

        final NetworkResult<BoardCateResult> result = new NetworkResult<>();
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
                    BoardCateResult status = gson.fromJson(text, BoardCateResult.class);
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

    public Request deleteStudyQAFile(BoardDelFileRequest vo,
                          OnResultListener<PostResult> listener) {

        FormBody.Builder builder = new FormBody.Builder();
        builder.add(APIConfig.CHARSET, CHARSET)
                .add("userKey", APIPropertyManager.getInstance(mContext).getUserKey())
                .add("boardTyp", APIConfig.BOARDTYP_SITE)
                .add("boardNo", APIConfig.BOARDKND_STUDY_QA)
                .add("listNo", String.valueOf(vo.getListNo()));

        RequestBody body = builder.build();

        Request request = new Request.Builder()
                .url(api.getDeleteBoardFileUrl(mContext))
                .post(body)
                .build();

        final NetworkResult<PostResult> result = new NetworkResult<>();
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
                    PostResult status = gson.fromJson(text, PostResult.class);
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

    public Request deleteOneToOneQAFile(BoardDelFileRequest vo,
                                     OnResultListener<PostResult> listener) {

        FormBody.Builder builder = new FormBody.Builder();
        builder.add(APIConfig.CHARSET, CHARSET)
                .add("userKey", APIPropertyManager.getInstance(mContext).getUserKey())
                .add("boardTyp", APIConfig.BOARDTYP_SITE)
                .add("boardNo", APIConfig.BOARDKND_ONE_TO_ONE_QA)
                .add("listNo", String.valueOf(vo.getListNo()));

        RequestBody body = builder.build();

        Request request = new Request.Builder()
                .url(api.getDeleteBoardFileUrl(mContext))
                .post(body)
                .build();

        final NetworkResult<PostResult> result = new NetworkResult<>();
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
                    PostResult status = gson.fromJson(text, PostResult.class);
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
