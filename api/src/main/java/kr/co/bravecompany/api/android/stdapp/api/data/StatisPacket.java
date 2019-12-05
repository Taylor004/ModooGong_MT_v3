package kr.co.bravecompany.api.android.stdapp.api.data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Created by Luke
 *      3.2버전, 수강 기록 데이타
 */

public final class StatisPacket {

    public static class ExamInfo {
        public String examName;
        public Date dueDate;
    }

    //Note. 시험 정보
    public static class ExamInfoResult extends Packet.ResponseResult {
        public List<ExamInfo> examInfos;
    }

    //Note. OnAir
    public static class OnAirResult extends  Packet.ResponseResult {
       // public Date lastUpdated;
        public int activeUsers;
    }

    //Note. 오늘의 순공시간
    public static class TodayRankResult extends  Packet.ResponseResult {
     //   public Date lastUpdated;
        public int hours;
        public int rank;
        public int rankDelta;
    }

    //Note. 오늘의 순공 시간
    public static class DailyTime  {
        public Date date;
        public int dailyTime;
    }

    //Note. 오늘의 순공 시간
    public static class DailyTimeResult extends  Packet.ResponseResult {
        public DailyTime today;
    }


    public static class DailyTimeHistoryResult extends  Packet.ResponseResult {
        public List<DailyTime> history;
    }

    /*
    //Note. 이번주 순공 시간
    public static class WeeklyTimeResult extends  Packet.ResponseResult {
        public Date lastUpdated;
        public float weeklyTime;
        public float previousWeeklyTime;
        public Date previousWeeklyDate;
    }
    */

    //Note.과목별 수강시간
    public static class SubjectTime {
        public String subjCD;
        public int hours;
    }

    //Note. 주간 과목별 순공시간
    public static class WeeklyTimeResult extends  Packet.ResponseResult {
        public Date date;
        public int weeklyTime;
        public List<SubjectTime> subjectList;
    }


    //Note. 과목별 순공 추이
    public static class HistoricalSubjectTime {
        public String subjCD;
        public Date date;
        public int[] hours; //! lastestDate가 속한 주 부터, 과거순으로 주단위 순공시간기록
    }

    //Note. 과목별 학습 추이
    public static class HistoricalSubjectTimeResult extends  Packet.ResponseResult {

        public String subjCD;       //! 강의 코드
        public int[] myHistory;     //!
        public int[] topHistory;    //!

        //public String lastUpdated; // 최근 업데이트 날짜
       // public HistoricalSubjectTime myHistory; // 내 공부시간
       // public HistoricalSubjectTime topHistory; // 상위 5% 공부시간

        //public Date baseDate;
        //public List<HistoricalSubjectTime> historicalSubjectTimeList;
        //public HistoricalSubjectTime meanHistory;
    }



    //Note. 과목별 완강수
    public static class SubjectView {
        public String subjCD; // 강의코드
       // public String subjNM; // 강의명
        public int view; // 총 완강수
        public int weeklyView; //주간 강의수
    }

    //Note. 누적 완강수
    public static class TotalSubjectViewResult extends  Packet.ResponseResult {
        //public Date queryDate;
        public List<SubjectView> subjectList;
    }

}

