package kr.co.bravecompany.modoogong.android.stdapp.db;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.co.bravecompany.api.android.stdapp.api.OnResultListener;
import kr.co.bravecompany.api.android.stdapp.api.data.Packet;
import kr.co.bravecompany.api.android.stdapp.api.data.StatisPacket;
import kr.co.bravecompany.api.android.stdapp.api.requests.StatisRequests;
import okhttp3.Request;

public class StatisDataManager extends BaseDataManager {

    final String Tag = "StatisDataManager";

    private static StatisDataManager instance;

    public static StatisDataManager getInstance() {

        if (instance == null)
            instance = new StatisDataManager();
        return instance;
    }

    //시험 정보
    private Date mExamInfoLastUpdated;
    private StatisPacket.ExamInfoResult mExamInfoResult;

    //온에어
    private Date mOnAirLastUpdated;
    private StatisPacket.OnAirResult mOnAirResultCached;

    //투데이 랭커
    private Date mTodayRankLastUpdated;
    private StatisPacket.TodayRankResult mTodayRankResultCached;

    //! 일간 학습 시간 (오늘)
    private Date mTodayTimeLastUpdated;
    private StatisPacket.DailyTime mTodayTime;

    //! 일간 학습 시간 (어제)
    private Date mYesterdayLastUpdated;
    private StatisPacket.DailyTime mYesterdayTime;

    //! 일간 학습 시간 (7일간)
    private Date mDailyHistoryLastUpdated;
    private List<StatisPacket.DailyTime> mDailyTimeHistory= null;

    //! 주간 과목별 학습 시간 (이번주)
    private Date mThisWeekSubjectTimeUpdated;
    private StatisPacket.WeeklyTimeResult mThisWeekSubjectTimeData;

    //! 주간 과목별 학습시간 (이전주들)
    private List<StatisPacket.WeeklyTimeResult> mPreviousWeekSubjectTimes= null ;

    private List<StatisPacket.HistoricalSubjectTimeResult> mHistoricalSubjectTimes = new ArrayList<>();

    private Date mTotalSubjectViewUpdated;
    private StatisPacket.TotalSubjectViewResult mTotalSubjectViewResult;

    private Date mLastRankCheckDate = null;
    private int mLastRankCheckPoint = 0;

    final static long OVER_TIME_SEC = 60;

    public static boolean IsOverTime(Date lastDate)
    {
        Date now = new Date();
        long spanInSec = (now.getTime() - lastDate.getTime()) / 1000;

        return (spanInSec > OVER_TIME_SEC);
    }

    public static boolean IsOverTime(Date lastDate, long overTimeSec)
    {
        Date now = new Date();
        long spanInSec = (now.getTime() - lastDate.getTime()) / 1000;

        return (spanInSec > overTimeSec);
    }



    public void GetOnAir(final OnDataUpdatedListener<StatisPacket.OnAirResult> onListener)
    {
        if(mOnAirResultCached !=null && !IsOverTime(mOnAirLastUpdated)) {
            onListener.onDataUpdated(mOnAirResultCached);
        } else {
                        //테일러 테스트
            StatisRequests.getInstance().requestOnAirInfo(new OnResultListener<StatisPacket.OnAirResult>() {
                @Override
                public void onSuccess(Request request, StatisPacket.OnAirResult result) {

                    if(IsResultOk(result.resultCode)) {
                        mOnAirResultCached = result;
                        mOnAirLastUpdated = new Date();
                        onListener.onDataUpdated(result);
                    }
                    else
                        onListener.onDataUpdated(null);
                }

                @Override
                public void onFail(Request request, Exception exception) {
                    Log.d(Tag, "GetOnAir Failed:"+exception.getMessage());
                    onListener.onDataUpdated(null);
                }
            });
        }
    }


    public void GetTodayRank(final OnDataUpdatedListener<StatisPacket.TodayRankResult> onListener)
    {
        if(mTodayRankResultCached !=null && !IsOverTime(mTodayRankLastUpdated)) {
            onListener.onDataUpdated(mTodayRankResultCached);
        } else {
            // 테일러 테스트
            StatisRequests.getInstance().requestTodayRank(new OnResultListener<StatisPacket.TodayRankResult>() {
                @Override
                public void onSuccess(Request request, StatisPacket.TodayRankResult result) {

                    if(IsResultOk(result.resultCode)) {

                        if(mLastRankCheckPoint==0 ||
                                mLastRankCheckDate ==null ||
                                IsOverTime(mLastRankCheckDate, 60) ||
                                BcDateUtils.IsOverDay(mLastRankCheckDate)) {

                            mLastRankCheckPoint = result.rank;
                            mLastRankCheckDate = new Date();
                        }

                        result.rankDelta = (mLastRankCheckPoint - result.rank);

                        mTodayRankResultCached = result;
                        mTodayRankLastUpdated = new Date();
                        onListener.onDataUpdated(result);
                    }
                    else
                        onListener.onDataUpdated(null);
                }

                @Override
                public void onFail(Request request, Exception exception) {
                    Log.d(Tag, "GetTodayRank Failed:"+exception.getMessage());
                    onListener.onDataUpdated(null);
                }
            });
        }
    }


    public void GetExamInfoList(final OnDataUpdatedListener<StatisPacket.ExamInfoResult> onListener)
    {
        if(mExamInfoResult !=null && !BcDateUtils.IsOverDay(mExamInfoLastUpdated) ) {
            onListener.onDataUpdated(mExamInfoResult);
        } else {

            StatisRequests.getInstance().requestExamInfo(new OnResultListener<StatisPacket.ExamInfoResult>() {
                @Override
                public void onSuccess(Request request, StatisPacket.ExamInfoResult result) {

                    if(IsResultOk(result.resultCode)) {
                        mExamInfoResult = result;
                        mExamInfoLastUpdated = new Date();
                        onListener.onDataUpdated(result);
                    }
                    else
                        onListener.onDataUpdated(null);
                }

                @Override
                public void onFail(Request request, Exception exception) {
                    Log.d(Tag, "GetExamInfoList Failed:"+exception.getMessage());
                    onListener.onDataUpdated(null);

                }
            });
        }
    }

    //! 오늘의 순공 시간
    public void GetTodayTime(final OnDataUpdatedListener<StatisPacket.DailyTime> onListener)
    {
        if(mTodayTime !=null && !IsOverTime(mTodayTimeLastUpdated)) {
            onListener.onDataUpdated(mTodayTime);
        } else {
                //테일러 테스트
            StatisRequests.getInstance().requestTodayTime(BcDateUtils.GetStartOfDay(0), new OnResultListener<StatisPacket.DailyTimeResult>() {
                @Override
                public void onSuccess(Request request, StatisPacket.DailyTimeResult result) {

                    if(IsResultOk(result.resultCode)) {
                        mTodayTime = result.today;
                        mTodayTimeLastUpdated = new Date();

                        onListener.onDataUpdated(mTodayTime);
                    }
                    else
                        onListener.onDataUpdated(null);
                }

                @Override
                public void onFail(Request request, Exception exception) {
                    Log.d(Tag, "GetTodayTime Failed:"+exception.getMessage());
                    onListener.onDataUpdated(null);
                }
            });
        }
    }

    //! 어제의 순공 시간
    public void GetYesterdayTime(final OnDataUpdatedListener<StatisPacket.DailyTime> onListener)
    {
        if(mYesterdayTime !=null && !IsOverTime(mYesterdayLastUpdated)) {
            onListener.onDataUpdated(mYesterdayTime);
        } else {

            StatisRequests.getInstance().requestTodayTime(BcDateUtils.GetStartOfDay(-1), new OnResultListener<StatisPacket.DailyTimeResult>() {
                @Override
                public void onSuccess(Request request, StatisPacket.DailyTimeResult result) {

                    if(IsResultOk(result.resultCode)) {
                        mYesterdayTime = result.today;
                        mYesterdayLastUpdated = new Date();

                        onListener.onDataUpdated(mYesterdayTime);
                    }
                }

                @Override
                public void onFail(Request request, Exception exception) {
                    Log.d(Tag, "GetYesterdayTime Failed:"+exception.getMessage());
                }
            });
        }
    }

    //! 7일간의 순공시간
    public void GetDailyTimeHistory(final OnDataUpdatedListener<List<StatisPacket.DailyTime>> onListener)
    {
        if(mDailyTimeHistory !=null && !BcDateUtils.IsOverDay(mDailyHistoryLastUpdated)) {
            onListener.onDataUpdated(mDailyTimeHistory);
        } else {

            StatisRequests.getInstance().requestDailyTimeHistory(7, new OnResultListener<StatisPacket.DailyTimeHistoryResult>() {
                @Override
                public void onSuccess(Request request, StatisPacket.DailyTimeHistoryResult result) {

                    if(IsResultOk(result.resultCode)) {
                        mDailyTimeHistory = result.history;
                        mDailyHistoryLastUpdated = new Date();
                        onListener.onDataUpdated(mDailyTimeHistory);
                    }
                }

                @Override
                public void onFail(Request request, Exception exception) {
                    Log.d(Tag, "GetDailyTimeHistory Failed:"+exception.getMessage());
                }
            });
        }
    }


    //! 이번주 과목별 순공 시간
    public void GetThisWeeklySubjectTime(final OnDataUpdatedListener<StatisPacket.WeeklyTimeResult> onListener)
    {
        if(mThisWeekSubjectTimeData !=null && !IsOverTime(mThisWeekSubjectTimeUpdated)) {
            onListener.onDataUpdated(mThisWeekSubjectTimeData);
        }else {

            StatisRequests.getInstance().requestWeeklyTime(BcDateUtils.GetStartOfDay(0), new OnResultListener<StatisPacket.WeeklyTimeResult>() {
                @Override
                public void onSuccess(Request request, StatisPacket.WeeklyTimeResult result) {

                    if(IsResultOk(result.resultCode)) {
                        mThisWeekSubjectTimeData = result;
                        mThisWeekSubjectTimeUpdated = new Date();

                        onListener.onDataUpdated(mThisWeekSubjectTimeData);
                    }
                }

                @Override
                public void onFail(Request request, Exception exception) {
                    Log.d(Tag, "GetThisWeeklySubjectTime Failed:"+exception.getMessage());
                }
            });
        }
    }

    private StatisPacket.WeeklyTimeResult FindWeeklySubjectTime(Date startOfWeek)
    {
        if(mPreviousWeekSubjectTimes !=null)
            for (StatisPacket.WeeklyTimeResult w: mPreviousWeekSubjectTimes) {
                if(BcDateUtils.IsSameWeek(w.date, startOfWeek))
                    return  w;
            }

        return null;
    }

    private void UpdateWeeklySubjectTime(StatisPacket.WeeklyTimeResult result)
    {
        StatisPacket.WeeklyTimeResult w = FindWeeklySubjectTime(result.date);

        if(mPreviousWeekSubjectTimes ==null)
            mPreviousWeekSubjectTimes =new ArrayList<StatisPacket.WeeklyTimeResult>();

        if( w==null) {
            mPreviousWeekSubjectTimes.add(result);
        } else {
            mPreviousWeekSubjectTimes.remove(w);
            mPreviousWeekSubjectTimes.add(result);
        }
    }

    //! 특정 주의 과목별 순공 시간
    public void GetWeeklySubjectTime(int addWeek, final OnDataUpdatedListener<StatisPacket.WeeklyTimeResult> onListener)
    {
        Date queryDate = BcDateUtils.GetStartOfWeek(addWeek);
        StatisPacket.WeeklyTimeResult w  = FindWeeklySubjectTime(queryDate);

        if(w !=null) {
            onListener.onDataUpdated(w);
        } else {    //테일러 테스트
            StatisRequests.getInstance().requestWeeklyTime(queryDate, new OnResultListener<StatisPacket.WeeklyTimeResult>() {
                @Override
                public void onSuccess(Request request, StatisPacket.WeeklyTimeResult result) {

                    UpdateWeeklySubjectTime(result);
                    onListener.onDataUpdated(result);
                }

                @Override
                public void onFail(Request request, Exception exception) {
                    Log.d(Tag, "GetWeeklySubjectTime Failed:"+exception.getMessage());
                }
            });
        }
    }


    private StatisPacket.HistoricalSubjectTimeResult FindHistoricalSubjectTime(String subjCD)
    {
        for ( StatisPacket.HistoricalSubjectTimeResult w: mHistoricalSubjectTimes)
            if(w.subjCD.equalsIgnoreCase(subjCD))
                return  w;

        return null;
    }

    private void UpdateHistoricalSubjectTime(StatisPacket.HistoricalSubjectTimeResult result)
    {
        StatisPacket.HistoricalSubjectTimeResult w = FindHistoricalSubjectTime(result.subjCD);

        if( w==null) {
            mHistoricalSubjectTimes.add(result);
        } else {
            mHistoricalSubjectTimes.remove(w);
            mHistoricalSubjectTimes.add(result);
        }
    }


//
//
//    //! 강의를 한번이라도 들은 과목 리스트
//    public void GetSubjectList(ShopDataManager.OnDataUpdatedListener<List<Packet.SubjectData>> listener)
//    {
//        ArrayList<Packet.SubjectData> list = new  ArrayList<Packet.SubjectData>();
//        Packet.SubjectData data= new Packet.SubjectData();
//        data.SUBJ_CD = "key_1";
//        data.SUBJ_NM= "국어";
//        list.add(data);
//
//        listener.onDataUpdated(list);
//    }


    //! 과목별 학습 추이
    public void GetHistoricalSubjectTime(String subjCD, final OnDataUpdatedListener<StatisPacket.HistoricalSubjectTimeResult> onListener)
    {
        StatisPacket.HistoricalSubjectTimeResult h = FindHistoricalSubjectTime(subjCD);

        if(h !=null) {
            onListener.onDataUpdated(h);
        } else {
            StatisRequests.getInstance().requestHistoricalSubjectTime(subjCD, 7,
                    new OnResultListener<StatisPacket.HistoricalSubjectTimeResult>() {
                        @Override
                        public void onSuccess(Request request, StatisPacket.HistoricalSubjectTimeResult result) {

                            UpdateHistoricalSubjectTime(result);
                            onListener.onDataUpdated(result);
                        }

                        @Override
                        public void onFail(Request request, Exception exception) {
                            Log.d(Tag, "GetHistoricalSubjectTime Failed:"+exception.getMessage());
                        }
                    });
        }
    }

    //! 과목별 완강 기록
    public void GetTotalSubjectView(final OnDataUpdatedListener<StatisPacket.TotalSubjectViewResult> onListener)
    {
        if(mTotalSubjectViewResult !=null && !IsOverTime(mTotalSubjectViewUpdated)) {
            onListener.onDataUpdated(mTotalSubjectViewResult);
        } else {

            StatisRequests.getInstance().requestTotalSubjectView(new OnResultListener<StatisPacket.TotalSubjectViewResult>() {
                @Override
                public void onSuccess(Request request, StatisPacket.TotalSubjectViewResult result) {
                    mTotalSubjectViewResult = result;
                    mTotalSubjectViewUpdated = new Date();

                    onListener.onDataUpdated(mTotalSubjectViewResult);
                }

                @Override
                public void onFail(Request request, Exception exception) {
                    Log.d(Tag, "GetTotalSubjectView Failed:"+exception.getMessage());
                }
            });
        }
    }

    /*****************************************************
     *
     *
     *
     *****************************************************/
    public static class DataConstant
    {
        public static final int [] viewLevels = new int [] {1,10,30};
        public static final float [] dailyTimeLevels = new float [] { 1.0f, 2.0f, 3.0f};
        public static final int [] weeklyViewRange = new int[] {0, -1,-2,-3,-4}; // 이번주, 지난주,지지난주....
    }

    public static class LevelInt
    {
        public int level;           //! start 0
        public int nextPoint;       //! next level point (누적 포인트 기준)
        public boolean completed;   //! 최종 레벨 달성 완료
    }

    public static class LevelFloat
    {
        public int level;
        public float nextPoint;
        public boolean completed;   //! 최종 레벨 달성 완료
    }


    //! 주간 기록, 과거 주 리스트
    public int [] GetWeeklyViewRange()
    {
        return DataConstant.weeklyViewRange;
    }

    //! 완강 기록을 레벨로 변환
    public LevelInt GetSubjectViewLevel(int viewCount)
    {
        int level =0;
        for(level=0; level < DataConstant.viewLevels.length ; level++) {
            if (viewCount < DataConstant.viewLevels[level])
                break;
        }

        level = Math.min(level, DataConstant.viewLevels.length-1);

        LevelInt g= new LevelInt();
        g.level = level;
        g.nextPoint =DataConstant.viewLevels[level];
        g.completed = level >=  (DataConstant.viewLevels.length -1) && viewCount >= g.nextPoint;

        return g;
    }

    //! 일간 수강시간을 레벨로 변환
    public LevelFloat GetDailyTimeLevel(float time)
    {
        int level =0;
        for(level=0; level < DataConstant.dailyTimeLevels.length ; level++) {
            if (time < DataConstant.dailyTimeLevels[level])
                break;
        }

        level = Math.min(level, DataConstant.dailyTimeLevels.length-1);

        LevelFloat g= new LevelFloat();
        g.level = level;
        g.nextPoint =DataConstant.dailyTimeLevels[level];
        g.completed = level >=  (DataConstant.dailyTimeLevels.length -1) && time >= g.nextPoint;

        return g;
    }

    /*****************************************************
     *
     * Test Code!!
     *
     *****************************************************/

    protected Gson gson = new GsonBuilder().serializeNulls().create();

    public void PrintPacketObject(String title, Object obj)
    {
        String json = gson.toJson(obj);
        Log.d("PacketTest", String.format("%s, result_json: %s", title, json));
    }

    public void DoTest()
    {
        //GetStartOfDay "Thu Nov 14 00:00:00 GMT+09:00 2019"
        Log.d("PacketTest", String.format("GetStartOfDay %s", BcDateUtils.GetStartOfDay(0)));


        GetExamInfoList(new OnDataUpdatedListener<StatisPacket.ExamInfoResult>() {
            @Override
            public void onDataUpdated(StatisPacket.ExamInfoResult data) {
                PrintPacketObject("GetExamInfoList", data);
            }
        });

        GetOnAir(new OnDataUpdatedListener<StatisPacket.OnAirResult>() {
            @Override
            public void onDataUpdated(StatisPacket.OnAirResult data) {
                PrintPacketObject("GetOnAir", data);
            }
        });

        GetTodayRank(new OnDataUpdatedListener<StatisPacket.TodayRankResult>() {
            @Override
            public void onDataUpdated(StatisPacket.TodayRankResult data) {
                PrintPacketObject("GetTodayRank", data);
            }
        });

        GetTodayTime(new OnDataUpdatedListener<StatisPacket.DailyTime>() {
            @Override
            public void onDataUpdated(StatisPacket.DailyTime data) {
                PrintPacketObject("GetTodayTime", data);
            }
        });

        GetYesterdayTime(new OnDataUpdatedListener<StatisPacket.DailyTime>() {
            @Override
            public void onDataUpdated(StatisPacket.DailyTime data) {
                PrintPacketObject("GetYesterdayTime", data);
            }
        });

        GetDailyTimeHistory(new OnDataUpdatedListener<List<StatisPacket.DailyTime>>() {
            @Override
            public void onDataUpdated(List<StatisPacket.DailyTime> data) {
                PrintPacketObject("GetDailyTimeHistory", data);
            }
        });

        GetThisWeeklySubjectTime(new OnDataUpdatedListener<StatisPacket.WeeklyTimeResult>() {
            @Override
            public void onDataUpdated(StatisPacket.WeeklyTimeResult data) {
                PrintPacketObject("GetThisWeeklySubjectTime", data);
            }
        });

        GetTotalSubjectView(new OnDataUpdatedListener<StatisPacket.TotalSubjectViewResult>() {
            @Override
            public void onDataUpdated(StatisPacket.TotalSubjectViewResult data) {
                PrintPacketObject("GetTotalSubjectView",  data);
            }
        });

        String subjCD = "1";
        GetHistoricalSubjectTime(subjCD, new OnDataUpdatedListener<StatisPacket.HistoricalSubjectTimeResult>() {
            @Override
            public void onDataUpdated(StatisPacket.HistoricalSubjectTimeResult data) {
                PrintPacketObject("GetHistoricalSubjectTime", data);
            }
        });
    }
}
