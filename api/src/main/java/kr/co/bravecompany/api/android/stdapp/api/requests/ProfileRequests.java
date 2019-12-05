package kr.co.bravecompany.api.android.stdapp.api.requests;

import java.io.File;
import java.io.IOException;

import kr.co.bravecompany.api.android.stdapp.api.NetworkHandler;
import kr.co.bravecompany.api.android.stdapp.api.NetworkManager;
import kr.co.bravecompany.api.android.stdapp.api.NetworkResult;
import kr.co.bravecompany.api.android.stdapp.api.OnResultListener;
import kr.co.bravecompany.api.android.stdapp.api.config.APIConfig;
import kr.co.bravecompany.api.android.stdapp.api.data.Packet.*;
import kr.co.bravecompany.api.android.stdapp.api.data.PostResult;
import kr.co.bravecompany.api.android.stdapp.api.utils.ApiUtils;
import kr.co.bravecompany.api.android.stdapp.manager.APIPropertyManager;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProfileRequests extends NetworkManager {

    private static ProfileRequests instance;


    public static ProfileRequests getInstance() {
        if (mContext == null) {
            throw new IllegalStateException("Call `NetworkManager.init(Context..)` before calling this method.");
        }
        if (instance == null) {
            instance = new ProfileRequests();
        }
        return instance;
    }


    public Request requestSetUserPhoto(String userKey, File photoFile, OnResultListener<ResponseResult> listener)
    {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM)
                .addFormDataPart(APIConfig.CHARSET, CHARSET)
                .addFormDataPart("userKey", APIPropertyManager.getInstance(mContext).getUserKey());

        if(photoFile != null){
            builder.addFormDataPart("photo", photoFile.getName(),
                    RequestBody.create(MultipartBody.FORM, photoFile));
        }


        RequestBody body = builder.build();

        Request request = new Request.Builder()
                .url(api.getSetUserPhotoUrl(mContext))
                .post(body)
                .build();

        return request(request, listener);

        /*

        final NetworkResult<ResponseResult> result = new NetworkResult<>();
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
                    ResponseResult status = gson.fromJson(text, ResponseResult.class);
                    result.result = status;
                    mHandler.sendMessage(mHandler.obtainMessage(NetworkHandler.MESSAGE_SUCCESS, result));
                } else {
                    result.excpetion = new IOException(response.message());
                    mHandler.sendMessage(mHandler.obtainMessage(NetworkHandler.MESSAGE_FAIL, result));
                }
            }
        });

        return request;
        */
    }

    public Request requestSetPushKey(String pushKey, String mobileKey, OnResultListener<ResponseResult> listener)
    {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM)
                .addFormDataPart(APIConfig.CHARSET, CHARSET)
                .addFormDataPart("userKey", APIPropertyManager.getInstance(mContext).getUserKey())
                .addFormDataPart("pushKey", pushKey)
                .addFormDataPart("mobileKey", mobileKey);


        return request(api.getSetUserPushKeyUrl(mContext), builder.build(), ResponseResult.class, listener );
    }

    public Request requestSetTodayMessage(String msg, OnResultListener<ResponseResult> listener)
    {
        /*
        ReqSetUserMsg req = new ReqSetUserMsg();
        req.userKey =  APIPropertyManager.getInstance(mContext).getUserKey();
        req.msg  = msg;

        return request(api.getSetUserMsgUrl(mContext), req, listener );
        */

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM)
                .addFormDataPart(APIConfig.CHARSET, CHARSET)
                .addFormDataPart("userKey", APIPropertyManager.getInstance(mContext).getUserKey())
                .addFormDataPart("msg", msg);


        return request(api.getSetUserMsgUrl(mContext), builder.build(), ResponseResult.class, listener );
    }

}