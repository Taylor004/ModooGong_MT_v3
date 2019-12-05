package kr.co.bravecompany.api.android.stdapp.api.requests;

import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.ArrayList;

import kr.co.bravecompany.api.android.stdapp.api.NetworkHandler;
import kr.co.bravecompany.api.android.stdapp.api.NetworkManager;
import kr.co.bravecompany.api.android.stdapp.api.NetworkResult;
import kr.co.bravecompany.api.android.stdapp.api.OnResultListener;
import kr.co.bravecompany.api.android.stdapp.api.data.FreeExplainStudyResult;
import kr.co.bravecompany.api.android.stdapp.api.data.FreeStudyItemVO;
import kr.co.bravecompany.api.android.stdapp.api.data.FreeStudyResult;
import kr.co.bravecompany.api.android.stdapp.api.data.LectureResult;
import kr.co.bravecompany.api.android.stdapp.api.data.MainResult;
import kr.co.bravecompany.api.android.stdapp.api.data.StudyResult;
import kr.co.bravecompany.api.android.stdapp.api.data.StudyVodResult;
import kr.co.bravecompany.api.android.stdapp.api.utils.ApiUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by BraveCompany on 2016. 11. 9..
 */

public class StudyRequests extends NetworkManager{
    private static StudyRequests instance;

    public static StudyRequests getInstance() {
        if (mContext == null) {
            throw new IllegalStateException("Call `NetworkManager.init(Context..)` before calling this method.");
        }
        if (instance == null) {
            instance = new StudyRequests();
        }
        return instance;
    }

    public Request requestLectureList(int page, String state, int perPage, OnResultListener<LectureResult> listener) {
        Request request = new Request.Builder()
                .url(api.getLectureListUrl(mContext, page, state, perPage))
                .build();

        final NetworkResult<LectureResult> result = new NetworkResult<>();
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
                        LectureResult status = gson.fromJson(text, LectureResult.class);
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

    public Request requestStudyList(int studyLectureNo, int lectureCode, OnResultListener<StudyResult> listener) {
        Request request = new Request.Builder()
                .url(api.getStudyListUrl(mContext, studyLectureNo, lectureCode))
                .build();

        final NetworkResult<StudyResult> result = new NetworkResult<>();
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
                        StudyResult status = gson.fromJson(text, StudyResult.class);
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

    public Request requestFreeLectureCateList(OnResultListener<MainResult> listener) {
        Request request = new Request.Builder()
                .url(api.getFreeLectureCateUrl(mContext))
                .build();

        final NetworkResult<MainResult> result = new NetworkResult<>();
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
                        MainResult status = gson.fromJson(text, MainResult.class);
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

    public Request requestFreeStudyList(int page, int cate, String scate, int perPage, OnResultListener<FreeStudyResult> listener) {
        Request request = new Request.Builder()
                .url(api.getFreeStudyListUrl(mContext, page, cate, scate, perPage))
                .build();

        final NetworkResult<FreeStudyResult> result = new NetworkResult<>();
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

                   // text = "{ \\”totalCount\\”:\\”1\\”, \\”itemCount\\”:\\”1\\”, \\”fvods\\”:[{\\”castNo\\”:\\”7\\”,\\”cate\\”:\\”2\\”,\\”category\\”:\\”1\\”,\\”title\\”:\\”test\\”,\\”best_yn\\”:\\”N\\”,\\”thumb\\”:\\”\\”, \\”thumb2\\”:\\”\\”,\\”type\\”:\\”04\\”,\\”video\\”:\\”\\”,\\”videoKey\\”:\\”0Of7wG61\\”,\\”views\\”:\\”3462\\”}]}";
                    //text = "{\"totalCount”:”1”,”itemCount”:”1”,”fvods\":[{\"castNo\":\"7\",\"cate\":\"2\",\"category\":\"1\",\"title\":\"육각암기법(1)\",\"best_yn\":\"N\",\"thumb\":\"http://img.drvoca.com/common/thum_img/freevod/7/thum_20161019193056_6127.png\",\"thumb2\":\"\",\"type\":\"04\",\"video\":\"\",\"videoKey\":\"0Of7wG61\",\"views\":\"3462\"}]}";
                    //text = "{\"totalCount\”:\”1\”,\”itemCount\”:\”1\”,\”fvods\":[{\"castNo\":\"7\",\"cate\":\"2\",\"category\":\"1\",\"title\":\"육각암기법(1)\",\"best_yn\":\"N\",\"thumb\":\"\",\"thumb2\":\"\",\"type\":\"04\",\"video\":\"\",\"videoKey\":\"0Of7wG61\",\"views\":\"3462\”}]}"
                    //text ="\{totalCount:1,itemCount:1,\”fvods\":[{\"castNo\":\"7\",\"cate\":\"2\",\"category\":\"1\",\"title\":\"육각암기법(1)\",\"best_yn\":\"N\",\"thumb\":\"http://img.drvoca.com/common/thum_img/freevod/7/thum_20161019193056_6127.png\",\"thumb2\":\"\",\"type\":\"04\",\"video\":\"\",\"videoKey\":\"0Of7wG61\",\"views\":\"3462”}]}";

                    //text = "{\"totalCount\u201D:\u201D1\u201D,\u201DitemCount\u201D:\u201D1\u201D,\u201Dfvods\":[{\"castNo\":\"7\",\"cate\":\"2\",\"category\":\"1\",\"title\":\"\uC721\uAC01\uC554\uAE30\uBC95(1)\",\"best_yn\":\"N\",\"thumb\":\"\",\"thumb2\":\"\",\"type\":\"04\",\"video\":\"\",\"videoKey\":\"0Of7wG61\",\"views\":\"3462\u201D}]}\r\n";
                    /*
                    FreeStudyResult tstatus = new FreeStudyResult();
                    tstatus.itemCount = 1;
                    tstatus.totalCount = 1;
                    FreeStudyItemVO t2 = new FreeStudyItemVO();
                    t2.castNo = 7;
                    t2.cate = 2;
                    t2.category = 1;
                    t2.title ="육각암기법";
                    t2.content = "heeloo";
                    t2.thumb = null;//"http://img.drvoca.com/common/thum_img/freevod/7/thum_20161019193056_6127.png";
                    t2.thumb2 =null;
                    t2.type = "04";
                    t2.video = null;
                    t2.videoKey = "0Of7wG61";
                    tstatus.fvods=new ArrayList<FreeStudyItemVO>();
                    tstatus.fvods.add(t2);
                    */

                    text = ApiUtils.replaceBody(text);
                    try {

                        FreeStudyResult status = gson.fromJson(text, FreeStudyResult.class);

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

    public Request requestStudyVod(int studyLectureNo, int lctCode, boolean isPlay, OnResultListener<StudyVodResult> listener) {
        Request request = new Request.Builder()
                .url(api.getStudyVodUrl(mContext, studyLectureNo, lctCode, isPlay))
                .build();

        final NetworkResult<StudyVodResult> result = new NetworkResult<>();
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
                        StudyVodResult status = gson.fromJson(text, StudyVodResult.class);
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

    public Request requestFreeExplainStudyList(int page, int examClass, int perPage, OnResultListener<FreeExplainStudyResult> listener) {
        Request request = new Request.Builder()
                .url(api.getFreeExplainStudyListUrl(mContext, page, examClass, perPage))
                .build();

        final NetworkResult<FreeExplainStudyResult> result = new NetworkResult<>();
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
                        FreeExplainStudyResult status = gson.fromJson(text, FreeExplainStudyResult.class);
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
