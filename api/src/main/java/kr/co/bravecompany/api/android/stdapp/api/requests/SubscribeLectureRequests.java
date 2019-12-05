package kr.co.bravecompany.api.android.stdapp.api.requests;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kr.co.bravecompany.api.android.stdapp.api.NetworkHandler;
import kr.co.bravecompany.api.android.stdapp.api.NetworkManager;
import kr.co.bravecompany.api.android.stdapp.api.NetworkResult;
import kr.co.bravecompany.api.android.stdapp.api.OnResultListener;
import kr.co.bravecompany.api.android.stdapp.api.config.APIConfig;
import kr.co.bravecompany.api.android.stdapp.api.data.LectureResult;
import kr.co.bravecompany.api.android.stdapp.api.data.Packet;
import kr.co.bravecompany.api.android.stdapp.api.data.Packet.*;
import kr.co.bravecompany.api.android.stdapp.api.utils.APIManager;
import kr.co.bravecompany.api.android.stdapp.api.utils.ApiUtils;
import kr.co.bravecompany.api.android.stdapp.manager.APIPropertyManager;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SubscribeLectureRequests extends NetworkManager {

    private static SubscribeLectureRequests instance;

    public static SubscribeLectureRequests getInstance() {
        if (mContext == null) {
            throw new IllegalStateException("Call `NetworkManager.init(Context..)` before calling this method.");
        }
        if (instance == null) {
            instance = new SubscribeLectureRequests();
        }
        return instance;
    }

    public Request requestGetPassList(OnResultListener<ResGetPassList> listener)
    {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM)
                .addFormDataPart(APIConfig.CHARSET, CHARSET)
                .addFormDataPart("userKey", APIPropertyManager.getInstance(mContext).getUserKey());

        String url = api.getGetPassListUrl(mContext);

        return request(url, builder.build(),  ResGetPassList.class, listener );
    }

    public Request requestGetLectureList(String passKey, String payKey, OnResultListener<ResGetLectureList> listener) {

//        ReqGetLectureList req = new ReqGetLectureList();
//        req.userKey = APIPropertyManager.getInstance(mContext).getUserKey();
//        req.stlSeq = passKey;
//        req.salCd = payKey;


        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM)
                .addFormDataPart(APIConfig.CHARSET, CHARSET)
                .addFormDataPart("userKey", APIPropertyManager.getInstance(mContext).getUserKey())
                .addFormDataPart("stlSeq", passKey)
                .addFormDataPart("salCd", payKey);


        String url = api.getGetLectureListBelongToPassUrl(mContext)+"/"+passKey;

        return request(url, builder.build(), ResGetLectureList.class, listener);
    }

    public Request requestSubjectList( OnResultListener<ResGetSubjectList> listener)
    {

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM)
                .addFormDataPart(APIConfig.CHARSET, CHARSET);

        String url = api.getSubjectList(mContext);

        return request(url, builder.build(), ResGetSubjectList.class, listener);
    }

    public Request requestTeacherList( OnResultListener<ResGetTeacherList> listener)
    {

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM)
                .addFormDataPart(APIConfig.CHARSET, CHARSET);

        String url = api.getTeacherList(mContext);

        return request(url, builder.build(), ResGetTeacherList.class, listener);
    }

    public Request requestSubscribeLectureList(String passKey, String payKey, String passGradeKey, List<String> selected , OnResultListener<ResSubscribeLecture> listener)
    {
        String jsonLecList = gson.toJson(selected);

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM)
                .addFormDataPart(APIConfig.CHARSET, CHARSET)
                .addFormDataPart("userKey", APIPropertyManager.getInstance(mContext).getUserKey())
                .addFormDataPart("stlSeq", payKey)
                .addFormDataPart("stdGrdSeq", passGradeKey)
                .addFormDataPart("lectureCount", String.valueOf (selected.size()));

        int i=0;
        for (String code : selected)
        {
            String name = String.format("lectures[%d]", i++);
            String value = code;

            builder.addFormDataPart(name, value);
        }

        String url = api.getSubscribeLectureBelongToPassUrl(mContext) +"/" + passKey;

        return request(url, builder.build(), ResSubscribeLecture.class, listener );
    }


    public Request requestUnsubscribeLectureList( String stdChrSeq , OnResultListener<ResUnsubscribeLecture> listener)
    {
       // ReqUnsubscribeLecture req = new ReqUnsubscribeLecture();
       // req.userKey =  APIPropertyManager.getInstance(mContext).getUserKey();
        //req.salCd = "";
        //req.stlSeq = "";
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM)
                .addFormDataPart(APIConfig.CHARSET, CHARSET)
                .addFormDataPart("userKey", APIPropertyManager.getInstance(mContext).getUserKey())
                .addFormDataPart("stdChrSeq", stdChrSeq);


        return request(api.getUnsubscribeLectureBelongToPassUrl(mContext), builder.build(), ResUnsubscribeLecture.class, listener );
    }

    public void goLinkLectureDetail(Activity context, String lectureCode)
    {

        String url = api.getLectureDetailLink(lectureCode);

        try {
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(myIntent);

        } catch (ActivityNotFoundException e) {

            String msg = "No application can handle this request.";
            Log.d("SUBSCRIBE", msg);
            e.printStackTrace();
        }
    }
}
