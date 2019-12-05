package kr.co.bravecompany.api.android.stdapp.api.requests;

import java.io.File;
import java.io.IOException;

import kr.co.bravecompany.api.android.stdapp.api.NetworkHandler;
import kr.co.bravecompany.api.android.stdapp.api.NetworkManager;
import kr.co.bravecompany.api.android.stdapp.api.NetworkResult;
import kr.co.bravecompany.api.android.stdapp.api.OnResultListener;
import kr.co.bravecompany.api.android.stdapp.api.config.APIConfig;
import kr.co.bravecompany.api.android.stdapp.api.data.OneToOneQADetailVO;
import kr.co.bravecompany.api.android.stdapp.api.data.OneToOneQARequest;
import kr.co.bravecompany.api.android.stdapp.api.data.OneToOneQAResult;
import kr.co.bravecompany.api.android.stdapp.api.data.PostResult;
import kr.co.bravecompany.api.android.stdapp.api.data.StudyQADetailVO;
import kr.co.bravecompany.api.android.stdapp.api.data.StudyQARequest;
import kr.co.bravecompany.api.android.stdapp.api.data.StudyQAResult;
import kr.co.bravecompany.api.android.stdapp.api.utils.ApiUtils;
import kr.co.bravecompany.api.android.stdapp.manager.APIPropertyManager;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by BraveCompany on 2016. 11. 11..
 */

public class QARequests extends NetworkManager {
    private static QARequests instance;

    public static QARequests getInstance() {
        if (mContext == null) {
            throw new IllegalStateException("Call `NetworkManager.init(Context..)` before calling this method.");
        }
        if (instance == null) {
            instance = new QARequests();
        }
        return instance;
    }

    // =============================================================================
    // Study QnA
    // =============================================================================

    public Request requestStudyQAList(int page, int perPage, OnResultListener<StudyQAResult> listener) {
        Request request = new Request.Builder()
                .url(api.getStudyQAListUrl(mContext, page, perPage))
                .build();

        final NetworkResult<StudyQAResult> result = new NetworkResult<>();
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
                    StudyQAResult status = gson.fromJson(text, StudyQAResult.class);
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

    public Request requestStudyQADetail(int qnaNo, OnResultListener<StudyQADetailVO> listener) {
        Request request = new Request.Builder()
                .url(api.getStudyQADetailUrl(mContext, qnaNo))
                .build();

        final NetworkResult<StudyQADetailVO> result = new NetworkResult<>();
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
                    StudyQADetailVO status = gson.fromJson(text, StudyQADetailVO.class);
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

    public Request updateStudyQA(StudyQARequest vo,
                                 OnResultListener<PostResult> listener) {

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM)
                .addFormDataPart(APIConfig.CHARSET, CHARSET)
                .addFormDataPart("userKey", APIPropertyManager.getInstance(mContext).getUserKey())
                .addFormDataPart("title", vo.getTitle())
                .addFormDataPart("content", vo.getContent())
                .addFormDataPart("lectureNo", vo.getLectureNo());

        String tchCd = vo.getTchCd();
        if(tchCd != null) {
            builder.addFormDataPart("tchCd", tchCd);
        }

        String category = vo.getCategory();
        if(category != null) {
            builder.addFormDataPart("category", category);
        }

        String cate = vo.getCate();
        if(cate != null) {
            builder.addFormDataPart("cate", cate);
        }

        File file = vo.getFile();
        if(file != null){
            builder.addFormDataPart("file", file.getName(),
                    RequestBody.create(MultipartBody.FORM, file));
            builder.addFormDataPart("fileFormName", "file");
        }

        String bookNo = vo.getBookNo();
        if(bookNo != null){
            builder.addFormDataPart("bookNo", bookNo);
        }
        String bookPage = vo.getBookPage();
        if(bookPage != null){
            builder.addFormDataPart("bookPage", bookPage);
        }

        int editNo = vo.getInquiryNo();
        if(editNo != -1){
            builder.addFormDataPart("inquiryNo", String.valueOf(editNo));
        }

        //isEditor
        builder.addFormDataPart("isEditer", String.valueOf(APIConfig.ISEDITER));

        RequestBody body = builder.build();

        Request request = new Request.Builder()
                .url(api.getUpdateStudyQAUrl(mContext))
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

    // =============================================================================
    // 1:1 QnA
    // =============================================================================

    public Request requestOneToOneQAList(int page, int perPage, OnResultListener<OneToOneQAResult> listener) {
        Request request = new Request.Builder()
                .url(api.getOneToOneQAListUrl(mContext, page, perPage))
                .build();

        final NetworkResult<OneToOneQAResult> result = new NetworkResult<>();
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
                    OneToOneQAResult status = gson.fromJson(text, OneToOneQAResult.class);
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

    public Request requestOneToOneQADetail(int inquiryNo, OnResultListener<OneToOneQADetailVO> listener) {
        Request request = new Request.Builder()
                .url(api.getOneToOneQADetailUrl(mContext, inquiryNo))
                .build();

        final NetworkResult<OneToOneQADetailVO> result = new NetworkResult<>();
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
                    OneToOneQADetailVO status = gson.fromJson(text, OneToOneQADetailVO.class);
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

    public Request updateOneToOneQA(OneToOneQARequest vo,
                                 OnResultListener<PostResult> listener) {

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM)
                .addFormDataPart(APIConfig.CHARSET, CHARSET)
                .addFormDataPart("userKey", APIPropertyManager.getInstance(mContext).getUserKey())
                .addFormDataPart("title", vo.getTitle())
                .addFormDataPart("content", vo.getContent())
                .addFormDataPart("category", vo.getCategory());

        File file = vo.getFile();
        if(file != null){
            builder.addFormDataPart("file", file.getName(),
                    RequestBody.create(MultipartBody.FORM, file));
            builder.addFormDataPart("fileFormName", "file");
        }

        int editNo = vo.getQnaNo();
        if(editNo != -1){
            builder.addFormDataPart("qnaNo", String.valueOf(editNo));
        }

        //isEditor
        builder.addFormDataPart("isEditer", String.valueOf(APIConfig.ISEDITER));

        RequestBody body = builder.build();

        Request request = new Request.Builder()
                .url(api.getUpdateOneToOneQAUrl(mContext))
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
