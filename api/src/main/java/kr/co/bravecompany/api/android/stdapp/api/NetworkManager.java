package kr.co.bravecompany.api.android.stdapp.api;

import android.content.Context;
import android.os.Build;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.RawRes;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import kr.co.bravecompany.api.android.stdapp.api.utils.APIManager;
import kr.co.bravecompany.api.android.stdapp.api.utils.ApiUtils;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.JavaNetCookieJar;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;


public class NetworkManager {
    public static boolean isDebug = false;
    protected static Context mContext;

    public static final String CHARSET = "UTF-8";

    private static final int DEFAULT_CACHE_SIZE = 50 * 1024 * 1024;
    private static final String DEFAULT_CACHE_DIR = "miniapp";
    protected OkHttpClient mClient;

    protected NetworkManager() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        CookieManager cookieManager = new CookieManager(new PersistentCookieStore(mContext), CookiePolicy.ACCEPT_ALL);
        builder.cookieJar(new JavaNetCookieJar(cookieManager));

        File dir = new File(mContext.getExternalCacheDir(), DEFAULT_CACHE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        builder.cache(new Cache(dir, DEFAULT_CACHE_SIZE));

        builder.connectTimeout(30, TimeUnit.SECONDS);
        builder.readTimeout(30, TimeUnit.SECONDS);
        builder.writeTimeout(30, TimeUnit.SECONDS);

        if (isDebug) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);

            builder.addNetworkInterceptor(new StethoInterceptor());
        }

        //Lee Android 5 (API 21), HTTPS 오류에 대한 대응

        try {
            TrustManager[] trustAllCerts = getTrustAllCerts();

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());

            builder.sslSocketFactory(sc.getSocketFactory(), (X509TrustManager) trustAllCerts[0] );

        } catch (Exception e) {
            e.printStackTrace();
        }

        mClient = builder.build();

        //!Lee Android 5 (API 21)
        // Make and set trust manager(s) to solve SSLHandshakeException exception.
        // The exception occurs from Kit Kat or lower versions of Android.
        /*
        if ((Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)) {
            try {
                TrustManager[] trustAllCerts = getTrustAllCerts();
                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                mClient.sslSocketFactory().setSslSocketFactory(sc.getSocketFactory());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        */
    }

    /**
     * Get trust manager to permit all of certifications.
     * @return TrustManager array with one X509TrustManager element
     */
    private static TrustManager[] getTrustAllCerts() {
        return new TrustManager[] { new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[] {};
            }
        }};
    }

    public static void init(Context context, boolean debug, String host){
        if (context == null) {
            throw new IllegalArgumentException("Non-null context required.");
        }
        if (host == null) {
            throw new IllegalArgumentException("Non-null host required.");
        }
        mContext = context;
        isDebug = debug;
        APIManager.isDebug = debug;
        APIManager.host = host;
    }

    protected NetworkHandler mHandler = new NetworkHandler(Looper.getMainLooper());

    protected Gson gson = new GsonBuilder().serializeNulls().create();

    protected APIManager api = APIManager.getInstance();


    protected  <T> Request request(final String url, Object reqObject, final Type resultType, OnResultListener<T> listener)
    {
        String json= gson.toJson(reqObject);

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MediaType.parse("application/json"),json ))
                .build();

        final NetworkResult<T> result = new NetworkResult<>();
        result.request = request;
        result.listener = listener;

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Log.d("Network", String.format("request failed url:%s, exception:%s", url, e.getMessage()));

                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(NetworkHandler.MESSAGE_FAIL, result));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {

                    if (response.isSuccessful()) {
                        String text = response.body().string();
                        text = ApiUtils.replaceBody(text);

                        result.result = (T) gson.fromJson(text, resultType);//result.result.getClass()) ;

                        mHandler.sendMessage(mHandler.obtainMessage(NetworkHandler.MESSAGE_SUCCESS, result));
                    } else {

                        Log.d("Network", String.format("request failed url:%s, exception:%s", url, response.message()));

                        result.excpetion = new IOException(response.message());
                        mHandler.sendMessage(mHandler.obtainMessage(NetworkHandler.MESSAGE_FAIL, result));
                    }
                }
                catch (Exception ex)
                {
                    result.excpetion = new IOException(ex.getMessage());
                    mHandler.sendMessage(mHandler.obtainMessage(NetworkHandler.MESSAGE_FAIL, result));
                }
            }
        });

        return request;
    }

    protected  <T extends Object> Request request(String url, RequestBody reqBody, final Type resultType, OnResultListener<T> listener)
    {

        Request request = new Request.Builder()
                .url(url)
                .post(reqBody)
                .build();

        final NetworkResult<T> result = new NetworkResult<>();
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

                try {
                    if (response.isSuccessful()) {
                        String text = response.body().string();
                        text = ApiUtils.replaceBody(text);

                        result.result = (T) gson.fromJson(text, resultType);
                        mHandler.sendMessage(mHandler.obtainMessage(NetworkHandler.MESSAGE_SUCCESS, result));

                    } else {

                        result.excpetion = new IOException(response.message());
                        mHandler.sendMessage(mHandler.obtainMessage(NetworkHandler.MESSAGE_FAIL, result));
                    }

                }catch (Exception ex)
                {
                    result.excpetion = new IOException(ex.getMessage());
                    mHandler.sendMessage(mHandler.obtainMessage(NetworkHandler.MESSAGE_FAIL, result));
                }
            }
        });

        return request;
    }


    protected  <T> Request request(Request request, OnResultListener<T> listener)
    {
        final NetworkResult<T> result = new NetworkResult<>();
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

                    result.result =  (T) gson.fromJson(text, result.result.getClass()) ;

                    mHandler.sendMessage(mHandler.obtainMessage(NetworkHandler.MESSAGE_SUCCESS, result));
                } else {
                    result.excpetion = new IOException(response.message());
                    mHandler.sendMessage(mHandler.obtainMessage(NetworkHandler.MESSAGE_FAIL, result));
                }
            }
        });

        return request;
    }

    protected <T extends Object> Request requestTestJson(String testJson, final Type resultType, OnResultListener<T> listener)
    {
        final NetworkResult<T> result = new NetworkResult<>();
        result.request = null;
        result.listener = listener;

        try {

            result.result = (T) gson.fromJson(testJson, resultType);
            mHandler.sendMessage(mHandler.obtainMessage(NetworkHandler.MESSAGE_SUCCESS, result));

        } catch (Exception ex)
        {
            result.excpetion = new IOException(ex.getMessage());
            mHandler.sendMessage(mHandler.obtainMessage(NetworkHandler.MESSAGE_FAIL, result));
        }

        return null;
    }

    protected <T extends Object> Request requestTestObj(T resultObj, final Type resultType, OnResultListener<T> listener)
    {
        final NetworkResult<T> result = new NetworkResult<>();
        result.request = null;
        result.listener = listener;

        try {

            result.result = resultObj;
            mHandler.sendMessage(mHandler.obtainMessage(NetworkHandler.MESSAGE_SUCCESS, result));

        } catch (Exception ex)
        {
            result.excpetion = new IOException(ex.getMessage());
            mHandler.sendMessage(mHandler.obtainMessage(NetworkHandler.MESSAGE_FAIL, result));
        }

        return null;
    }


    public String readRawResource(@RawRes int res) {
        InputStream is = mContext.getResources().openRawResource(res);
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
