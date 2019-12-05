package kr.co.bravecompany.api.android.stdapp.api.requests;

import java.util.Date;

import kr.co.bravecompany.api.android.stdapp.R;
import kr.co.bravecompany.api.android.stdapp.api.NetworkManager;
import kr.co.bravecompany.api.android.stdapp.api.OnResultListener;
import kr.co.bravecompany.api.android.stdapp.api.data.StatisPacket.*;
import kr.co.bravecompany.api.android.stdapp.manager.APIPropertyManager;
import okhttp3.Request;

public class StatisRequests extends NetworkManager {

    private static StatisRequests instance;

    public static StatisRequests getInstance() {
        if (mContext == null) {
            throw new IllegalStateException("Call `NetworkManager.init(Context..)` before calling this method.");
        }
        if (instance == null) {
            instance = new StatisRequests();
        }
        return instance;
    }

    public class EmptyRequest
    {

    }

    public class UserKeyRequest
    {
        public String userKey;
    }

    public class DayTimeRequest
    {
        public String userKey;
        public Date baseDate;
    }

    public class WeeklyTimeRequest
    {
        public String userKey;
        public Date queryDate;
    }

    public class DailyTimeRequest
    {
        public String userKey;
        public int maxDays;
    }

    public Request test_requestExamInfo(OnResultListener<ExamInfoResult> listener)
    {
        String testJson = readRawResource(R.raw.exam_infos);
        return requestTestJson(testJson,ExamInfoResult.class, listener);
    }

    public Request requestExamInfo(OnResultListener<ExamInfoResult> listener)
    {
        String url = api.getExamInfoUrl(mContext);
        return request(url, new EmptyRequest(),  ExamInfoResult.class, listener );
    }


    public Request requestOnAirInfo(OnResultListener<OnAirResult> listener)
    {
//        String testJson = readRawResource(R.raw.onair_result);
//        return requestTestJson(testJson,OnAirResult.class, listener);

        String url = api.getOnAirUrl(mContext);
        return request(url, new EmptyRequest(), OnAirResult.class, listener );
    }

    public Request test_requestOnAirInfo(OnResultListener<OnAirResult> listener)
    {
        String testJson = readRawResource(R.raw.onair_result);
        return requestTestJson(testJson,OnAirResult.class, listener);
    }


    public Request requestTodayRank(OnResultListener<TodayRankResult> listener)
    {
        UserKeyRequest req = new UserKeyRequest();
        req.userKey =  APIPropertyManager.getInstance(mContext).getUserKey();

        String url = api.getTodayRankUrl(mContext);
        return request(url, req, TodayRankResult.class, listener );
    }


    public Request test_requestTodayRank(OnResultListener<TodayRankResult> listener)
    {
        String testJson = readRawResource(R.raw.today_rank_result);
        return requestTestJson(testJson, TodayRankResult.class, listener);
    }



    //Note. 오늘의 순공 시간
    public Request requestTodayTime(Date queryDate, OnResultListener<DailyTimeResult> listener)
    {
       // String testJson = readRawResource(R.raw.today_time_result);
       // return requestTestJson(testJson, DailyTimeResult.class, listener);

        DayTimeRequest req = new DayTimeRequest();
        req.userKey =  APIPropertyManager.getInstance(mContext).getUserKey();
        req.baseDate = queryDate;

        String url = api.getTodayTime(mContext);
        return request(url, req, DailyTimeResult.class, listener );
    }

    public Request test_requestTodayTime(Date queryDate, OnResultListener<DailyTimeResult> listener)
    {
        String testJson = readRawResource(R.raw.today_time_result);
        return requestTestJson(testJson, DailyTimeResult.class, listener);
    }

    //Note. 데일리 순공 시간 (7일간 하루단위 기록)
    public Request requestDailyTimeHistory(int maxDays, OnResultListener<DailyTimeHistoryResult> listener)
    {
       // String testJson = readRawResource(R.raw.daily_time_history_result);
       // return requestTestJson(testJson, DailyTimeHistoryResult.class, listener);

        DailyTimeRequest req = new DailyTimeRequest();
        req.userKey =  APIPropertyManager.getInstance(mContext).getUserKey();
        req.maxDays = maxDays;

        String url = api.getDailyTime(mContext);
        return request(url, req, DailyTimeHistoryResult.class, listener );

    }

    //Note. 데일리 순공 시간 (7일간 하루단위 기록)
    public Request test_requestDailyTimeHistory(int maxDays, OnResultListener<DailyTimeHistoryResult> listener)
    {
        String testJson = readRawResource(R.raw.daily_time_history_result);
        return requestTestJson(testJson, DailyTimeHistoryResult.class, listener);
    }


    //Note. 주간 순공 시간 + 과목별 순공 시간
    public Request requestWeeklyTime(Date queryDate, OnResultListener<WeeklyTimeResult> listener)
    {
        //String testJson = readRawResource(R.raw.weekly_subject_time_result);
        //return requestTestJson(testJson,WeeklyTimeResult.class, listener);

        WeeklyTimeRequest req = new WeeklyTimeRequest();
        req.userKey =  APIPropertyManager.getInstance(mContext).getUserKey();
        req.queryDate = queryDate;

        String url = api.getWeeklySubjectTimeUrl(mContext);
        return request(url, req, WeeklyTimeResult.class, listener );
    }

    //Note. 주간 순공 시간 + 과목별 순공 시간
    public Request test_requestWeeklyTime(Date queryDate, OnResultListener<WeeklyTimeResult> listener)
    {
        String testJson = readRawResource(R.raw.weekly_subject_time_result);
        return requestTestJson(testJson,WeeklyTimeResult.class, listener);
    }

    class SubjectRequest
    {
        public String userKey;
        public String subjectCode;
        public int period;
    }

    //Note. 과목별 순공 시간 추이
    public Request requestHistoricalSubjectTime(String subjCD, int period, OnResultListener<HistoricalSubjectTimeResult> listener)
    {
       // String testJson = readRawResource(R.raw.his_subject_time_result);
       // return requestTestJson(testJson,HistoricalSubjectTimeResult.class, listener);

        SubjectRequest req = new SubjectRequest();
        req.userKey = APIPropertyManager.getInstance(mContext).getUserKey();
        req.period = period;
        req.subjectCode = subjCD;

        String url = api.getHistoricalSubjectTimeUrl(mContext);
        return request(url, req, HistoricalSubjectTimeResult.class, listener);
    }

    //Note. 과목별 순공 시간 추이
    public Request test_requestHistoricalSubjectTime(String subjCD, int period, OnResultListener<HistoricalSubjectTimeResult> listener)
    {
        String testJson = readRawResource(R.raw.his_subject_time_result);
        return requestTestJson(testJson,HistoricalSubjectTimeResult.class, listener);
    }


    //Note. 모든 과목 완강 기록
    public Request test_requestTotalSubjectView(OnResultListener<TotalSubjectViewResult> listener)
    {
        String testJson = readRawResource(R.raw.total_subject_view_result);
        return requestTestJson(testJson,TotalSubjectViewResult.class, listener);
    }

    //Note. 모든 과목 완강 기록
    public Request requestTotalSubjectView(OnResultListener<TotalSubjectViewResult> listener)
    {
        UserKeyRequest  req = new UserKeyRequest();
        req.userKey = APIPropertyManager.getInstance(mContext).getUserKey();

        String url = api.getTotalSubjectViewUrl(mContext);
        return request(url, req, TotalSubjectViewResult.class, listener);
    }

}