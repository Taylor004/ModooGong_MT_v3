package kr.co.bravecompany.modoogong.android.stdapp.Zremoved;

public class WeeklyLectureListData {

    private String subjectName; // 과목명
    private String userName; // 유저이름
    private String top5PercentStr; // 상위 5%
    private String averageStr; // 평균

    //Date와 DateFormat을 이용하기 위해서 String으로 데이터 구조화
    private String userTotalLectureTime; // 유저 총 수강시간
    private String top5PercentTotalLectureTime; // 상위 5%의 총 수강시간
    private String averageTotalLectureTime; // 평균 총 수강시간

    //프로그래스바의 대한 값(임시)
    private int userProgBar;
    private int top5PercentProgBar;
    private int averageProgBar;

    public WeeklyLectureListData(String subjectName, String userName, String top5PercentStr, String averageStr,
                                 String userTotalLectureTime, String top5PercentTotalLectureTime, String averageTotalLectureTime,
                                 int userProgressBar, int top5PercentProgressBar, int averageProgressBar)
    {

        this.subjectName = subjectName;

        this.userName = userName;

        this.top5PercentStr = top5PercentStr;

        this.averageStr = averageStr;

        this.userTotalLectureTime = userTotalLectureTime;

        this.top5PercentTotalLectureTime = top5PercentTotalLectureTime;

        this.averageTotalLectureTime = averageTotalLectureTime;

        this.userProgBar = userProgressBar;

        this.top5PercentProgBar = top5PercentProgressBar;

        this.averageProgBar = averageProgressBar;
    }

    public String getSubjectName() { return this.subjectName; }

    public String getUserName(){ return  this.userName; }

    public String getTop5PercentStr(){ return  this.top5PercentStr; }

    public String getAverageStr() { return  this.averageStr; }

    public String getUserTotalLectureTime() { return this.userTotalLectureTime; }

    public String getTop5PercentTotalLectureTime() { return  this.top5PercentTotalLectureTime; }

    public String getAverageTotalLectureTime() { return this.averageTotalLectureTime; }

    public int getUserProgBar(){ return this.userProgBar; }

    public int getTop5PercentProgBar() { return this.top5PercentProgBar; }

    public int getAverageProgBar() { return  this.averageProgBar; }
}
