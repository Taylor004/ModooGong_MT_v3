package kr.co.bravecompany.modoogong.android.stdapp.Zremoved;

public class WeeklyLectureButtonListData {

    private String ButtonText;
    private int week;

    public WeeklyLectureButtonListData(int range)
    {
        this.week = range;

        switch (this.week)
        {
            case 0:
                this.ButtonText = "이번주";
                break;
            case -1:
                this.ButtonText = "-1주전";
                break;
            case -2:
                this.ButtonText = "-2주전";
                break;
            case -3:
                this.ButtonText = "-3주전";
                break;
            case -4:
                this.ButtonText = "-4주전";
                break;
        }

    }

    public String getmButtonText(){ return this.ButtonText; }

    public int getWeekValue(){ return  this.week; }
}
