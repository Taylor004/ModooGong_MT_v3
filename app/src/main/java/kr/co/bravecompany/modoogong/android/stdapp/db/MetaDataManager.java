package kr.co.bravecompany.modoogong.android.stdapp.db;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import kr.co.bravecompany.api.android.stdapp.api.OnResultListener;
import kr.co.bravecompany.api.android.stdapp.api.data.LectureItemVO;
import kr.co.bravecompany.api.android.stdapp.api.data.LectureResult;
import kr.co.bravecompany.api.android.stdapp.api.data.Packet;
import kr.co.bravecompany.api.android.stdapp.api.data.ShopPacket;
import kr.co.bravecompany.api.android.stdapp.api.requests.SubscribeLectureRequests;
import okhttp3.Request;


public class MetaDataManager extends BaseDataManager {

    public enum GlobalDataType
    {
        TEACHER_LIST,
        SUBJECT_LIST
    }


    public  interface OnGlobalDataListener
    {
        void onGlobalDataUpdated(GlobalDataType dataType);
    }

    final String TAG = "MetaData";

    private static MetaDataManager instance;

    public static MetaDataManager getInstance() {

        if (instance == null) {
            instance = new MetaDataManager();
        }
        return instance;
    }


    public List<Packet.SubjectData> mSubjectDataList = new ArrayList<>();
    public List<Packet.TeacherData> mTeacherDataList = new ArrayList<>();


    public String GetSubjectName(String subjCD)
    {
        for (Packet.SubjectData subjectData : mSubjectDataList)
            if(subjectData.SUBJ_CD.equalsIgnoreCase(subjCD))
                return subjectData.SUBJ_NM;

        return "unknown";
    }

    public Packet.SubjectData findSubjectData(String subjCD)
    {
        for (Packet.SubjectData subjectData : mSubjectDataList)
            if(subjectData.SUBJ_CD.equalsIgnoreCase(subjCD))
                return subjectData;

        return null;
    }

    public void initMetaData()
    {
        loadTeacherData(new OnDataUpdatedListener<List<Packet.TeacherData>>() {
            @Override
            public void onDataUpdated(List<Packet.TeacherData> data) {

            }
        });

        loadSubjectData(new OnDataUpdatedListener<List<Packet.SubjectData>>() {
            @Override
            public void onDataUpdated(List<Packet.SubjectData> data) {

            }
        });
    }

    public void loadTeacherData(final OnDataUpdatedListener<List<Packet.TeacherData>> listener) {

        if (mTeacherDataList.size() == 0) {
            SubscribeLectureRequests.getInstance().requestTeacherList(new OnResultListener<Packet.ResGetTeacherList>() {
                @Override
                public void onSuccess(Request request, Packet.ResGetTeacherList result) {
                    if (result.resultCode >= 0) {
                        Log.d(TAG, String.format("requestTeacherList return count %d", result.tchList.size()));

                        mTeacherDataList.clear();
                        mTeacherDataList.addAll(result.tchList);
                        listener.onDataUpdated(mTeacherDataList);
                    } else {
                        Log.d(TAG, String.format("requestTeacherList return error %d", result.resultCode));
                    }
                }

                @Override
                public void onFail(Request request, Exception exception) {
                    Log.d(TAG, exception.getMessage());
                }
            });
        } else {
            listener.onDataUpdated(mTeacherDataList);
        }
    }

    public void loadSubjectData(final OnDataUpdatedListener<List<Packet.SubjectData>> listener) {
        if(mSubjectDataList.size() == 0) {

            SubscribeLectureRequests.getInstance().requestSubjectList(new OnResultListener<Packet.ResGetSubjectList>() {
                @Override
                public void onSuccess(Request request, Packet.ResGetSubjectList result) {
                    if (result.resultCode >= 0) {

                        Log.d(TAG, String.format("requestSubjectList return count %d", result.subjList.size()));

                        mSubjectDataList.clear();
                        mSubjectDataList.addAll(result.subjList);

                        listener.onDataUpdated(mSubjectDataList);

                    } else {
                        Log.d(TAG, String.format("requestSubjectList return error : %d", result.resultCode));
                    }
                }

                @Override
                public void onFail(Request request, Exception exception) {

                    Log.d(TAG, exception.getMessage());
                }
            });
        }
        else
        {
            listener.onDataUpdated(mSubjectDataList);
        }
    }

}
