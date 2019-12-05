package kr.co.bravecompany.modoogong.android.stdapp.db;

import java.util.Calendar;
import java.util.Date;

public final class BcDateUtils {


    public static boolean IsOverDay(Date lastDate)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(lastDate);
        int lastDay = c.get(Calendar.DAY_OF_WEEK);

        c.setTime(new Date());
        int nowDay = c.get(Calendar.DAY_OF_WEEK);

        return lastDay != nowDay;
    }

    public static boolean IsToday(Date Date)
    {
        Calendar c = Calendar.getInstance();

        c.setTime(new Date());
        int nowYear = c.get(Calendar.YEAR);
        int nowDay = c.get(Calendar.DAY_OF_YEAR);

        c.setTime(Date);
        int lastYear = c.get(Calendar.YEAR);
        int lastDay = c.get(Calendar.DAY_OF_YEAR);

        return (lastDay == nowDay) && (nowYear == lastYear);
    }


    public static boolean IsSameWeek(Date d1, Date d2)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(d1);
        int lastDay = c.get(Calendar.WEEK_OF_YEAR);

        c.setTime(d2);
        int nowDay = c.get(Calendar.WEEK_OF_YEAR);

        return lastDay == nowDay;
    }



    public static Date GetNextDay(Date date , int addDay)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        c.set(Calendar.DATE, c.get(Calendar.DATE) + addDay);
        c.set(Calendar.HOUR_OF_DAY,0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        c.set(Calendar.MILLISECOND,0);

        return c.getTime();
    }

    public static boolean IsSameDay(Date date1, Date date2)
    {
        Calendar c = Calendar.getInstance();

        c.setTime(date1);
        int nowYear = c.get(Calendar.YEAR);
        int nowDay = c.get(Calendar.DAY_OF_YEAR);

        c.setTime(date2);
        int lastYear = c.get(Calendar.YEAR);
        int lastDay = c.get(Calendar.DAY_OF_YEAR);

        return (lastDay == nowDay) && (nowYear == lastYear);
    }

    public static int getDayOfWeek(Date date)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date); // Date 클래스 타입의 객체date를 Calendar 클래스 타입의 객체로 변환.
        int day_of_week = c.get(Calendar.DAY_OF_WEEK);
        return day_of_week;
    }

    public static String getDayOfWeekName(int dayOfWeek)
    {
        String name="";
        switch (dayOfWeek)
        {
            case 0: name = "Y"; break;
            case 1: name = "일"; break;
            case 2: name = "월"; break;
            case 3: name = "화"; break;
            case 4: name = "수"; break;
            case 5: name = "목"; break;
            case 6: name = "금"; break;
            case 7: name = "토"; break;
            default: name = "X";
        }

        return name;
    }

    public static class HMS
    {
        public int h;
        public int m;
        public int s;
    }

    public static HMS getHMS(int time)
    {
        int h= time / (60 *60);
        int m = time / (60) - h * 60;
        int s = time - m * 60 - h * 60 * 60;

        HMS hms = new HMS();
        hms.h=h;
        hms.m=m;
        hms.s=s;
        return hms;
    }

    //// 기준 시간

    public static Date GetStartOfWeek(int addWeek)
    {
        Calendar c = Calendar.getInstance();
        //c.set(Calendar.DATE, c.get(Calendar.DATE) + addWeek * 7);
        c.add(Calendar.DATE, 7 * addWeek);

        c.set(Calendar.DAY_OF_WEEK, 2);
        c.set(Calendar.HOUR_OF_DAY,0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        c.set(Calendar.MILLISECOND,0);

        return c.getTime();
    }

    public static Date GetStartOfDay(int addDay)
    {
        Calendar c = Calendar.getInstance();
        //c.set(Calendar.DATE, c.get(Calendar.DATE) + addDay);
        c.add(Calendar.DATE, addDay);

        c.set(Calendar.HOUR_OF_DAY,0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        c.set(Calendar.MILLISECOND,0);

        return c.getTime();
    }


    public static  String getMonday(int addWeek){

        int transDate = 1;
        transDate = transDate * addWeek;

        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy.MM.dd");

        Calendar c = Calendar.getInstance();

        c.add(Calendar.DATE, 7 * transDate);

        c.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);

        return formatter.format(c.getTime());

    }

    public static  String getSunday(int addWeek){

        int transDate = -1;
        transDate = transDate * addWeek;

        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy.MM.dd");

        Calendar c = Calendar.getInstance();

        c.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);

        if(transDate == 0)
        {
            c.add(c.DATE, 7);
        }
        else if(transDate == 1)
        {
            c.add(c.DATE,0);

        }
        else
        {
            c.add(c.DATE,-7 * (transDate - 1));
        }

        return formatter.format(c.getTime());
    }

}
