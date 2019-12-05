package kr.co.bravecompany.api.android.stdapp.api.requests;

import com.google.gson.JsonSyntaxException;

import java.io.IOException;

import kr.co.bravecompany.api.android.stdapp.api.NetworkHandler;
import kr.co.bravecompany.api.android.stdapp.api.NetworkManager;
import kr.co.bravecompany.api.android.stdapp.api.NetworkResult;
import kr.co.bravecompany.api.android.stdapp.api.OnResultListener;
import kr.co.bravecompany.api.android.stdapp.api.config.APIConfig;
import kr.co.bravecompany.api.android.stdapp.api.data.LoginRequest;
import kr.co.bravecompany.api.android.stdapp.api.data.LoginResult;
import kr.co.bravecompany.api.android.stdapp.api.utils.ApiUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by BraveCompany on 2016. 11. 8..
 */

public class AuthRequests extends NetworkManager{
    private static AuthRequests instance;

    public static AuthRequests getInstance() {
        if (mContext == null) {
            throw new IllegalStateException("Call `NetworkManager.init(Context..)` before calling this method.");
        }
        if (instance == null) {
            instance = new AuthRequests();
        }
        return instance;
    }

    public Request requestlogin(LoginRequest vo,
                             OnResultListener<LoginResult> listener) {

        FormBody.Builder builder = new FormBody.Builder();
        builder.add(APIConfig.CHARSET, CHARSET)
                .add("id", vo.getId())
                .add("password", vo.getPassword())
                .add("os", vo.getOs())
                .add("os_ver", vo.getOs_ver())
                .add("serial", vo.getSerial())
                .add("mthd", vo.getMthd())
                .add("isAuto", String.valueOf(vo.getAuto()));

        String mac = vo.getMaddr1();
        if(mac != null){
            builder.add("maddr1", mac);
        }
        String ip = vo.getMaddr2();
        if(ip != null){
            builder.add("maddr2", ip);
        }
        String key = vo.getMobile_key();
        if(key != null){
            builder.add("mobile_key", key);
        }

        RequestBody body = builder.build();

        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(api.getLoginUrl(mContext))
                .post(body);

        String userAgent = vo.getUserAgent();
        if(userAgent != null){
            requestBuilder.header("User-Agent", userAgent);
        }

        Request request = requestBuilder.build();
        /*
        Request request = new Request.Builder()
                .url(api.getLoginUrl(mContext))
                .post(body)
                .build();
        */
        final NetworkResult<LoginResult> result = new NetworkResult<>();
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
                    try {
                        LoginResult status = gson.fromJson(text, LoginResult.class);
                        result.result = status;
                        mHandler.sendMessage(mHandler.obtainMessage(NetworkHandler.MESSAGE_SUCCESS, result));
                    }catch (JsonSyntaxException e){
                        result.excpetion = new JsonSyntaxException(text);
                        mHandler.sendMessage(mHandler.obtainMessage(NetworkHandler.MESSAGE_FAIL, result));
                    }
                } else {
                    result.excpetion = new IOException(response.message());
                    mHandler.sendMessage(mHandler.obtainMessage(NetworkHandler.MESSAGE_FAIL, result));
                }
            }
        });
        return request;
    }
}
