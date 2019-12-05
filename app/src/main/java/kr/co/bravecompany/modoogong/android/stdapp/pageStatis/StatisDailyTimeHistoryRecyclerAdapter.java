package kr.co.bravecompany.modoogong.android.stdapp.pageStatis;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dinuscxj.progressbar.CircleProgressBar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.co.bravecompany.api.android.stdapp.api.data.StatisPacket;
import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.db.BcDateUtils;

public class StatisDailyTimeHistoryRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    Context mContext = null;
    LayoutInflater mLayoutInflater = null;

    List<StatisPacket.DailyTime> dailyTimeList = new ArrayList<>();


    public static class StatisDailyTimeHistoryRecyclerViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        TextView weeklyTextView; // 주간 텍스트
        ImageView weeklyImageView; // 주간 둥근 흰색 이미지
        CircleProgressBar [] circleProgressBar; // 주간 원형 프로그래스

        StatisDailyTimeHistoryRecyclerViewHolder(View view){

            super(view);

            linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
            weeklyTextView = (TextView) view.findViewById(R.id.weekday_text); // 주간 텍스트 객체 생성
            weeklyImageView = (ImageView) view.findViewById(R.id.white_round_daily_image);
            circleProgressBar = new CircleProgressBar[3]; // 프로그래스 바 객체 생성
            circleProgressBar[0] = (CircleProgressBar) view.findViewById(R.id.daily_time_prog_1); //큰 원형
            circleProgressBar[1] = (CircleProgressBar) view.findViewById(R.id.daily_time_prog_2); //중간 원형
            circleProgressBar[2] = (CircleProgressBar) view.findViewById(R.id.daily_time_prog_3); //작은 원형

        }

    }


    StatisDailyTimeHistoryRecyclerAdapter(Context context) {
            this.mContext = context;
            this.mLayoutInflater = LayoutInflater.from(mContext);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_statis_daily_time_item,parent,false);

        return new StatisDailyTimeHistoryRecyclerAdapter.StatisDailyTimeHistoryRecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

        StatisDailyTimeHistoryRecyclerViewHolder statisDailyTimeHistoryRecyclerViewHolder = (StatisDailyTimeHistoryRecyclerViewHolder) holder;

        StatisPacket.DailyTime data = dailyTimeList.get(position);


        int day_of_week = BcDateUtils.getDayOfWeek(data.date);
        boolean today = BcDateUtils.IsToday(data.date);

        DrawDayOfWeekIcon(statisDailyTimeHistoryRecyclerViewHolder,day_of_week, today);

        int testTime =  (int) (60*60* 1.5); //1.5시간
        DrawTimeCircleProgressBar(statisDailyTimeHistoryRecyclerViewHolder.circleProgressBar, data.dailyTime);
    }

    void DrawDayOfWeekIcon(StatisDailyTimeHistoryRecyclerViewHolder holder, int day_of_week, boolean today) {

        String day_of_week_text = BcDateUtils.getDayOfWeekName(day_of_week);
        holder.weeklyTextView.setText(day_of_week_text);

        if(today) {
            holder.weeklyImageView.setVisibility(View.VISIBLE); // 흰색 텍스트 배경 보이게
            holder.weeklyTextView.setTextColor(Color.BLACK); // 텍스트 색상 검정색
            holder.weeklyTextView.setTypeface(null, Typeface.BOLD); // 굵기
        }
        else {
            holder.weeklyImageView.setVisibility(View.INVISIBLE); // 흰색 텍스트 배경 보이게
            holder.weeklyTextView.setTextColor(Color.GRAY); // 텍스트 색상 검정색
            holder.weeklyTextView.setTypeface(null, Typeface.NORMAL); // 굵기
        }
    }

    public static void DrawTimeCircleProgressBar(CircleProgressBar [] circleProgressBar,int totalTime)
    {
        float time = totalTime / (60.0f * 60.0f); // 시간 단위로 변경

        float [] times = new float [] {0,0,0};
        times[0] = Math.min(time, 1.0f); time -= times[0];
        times[1] = Math.min(time, 1.0f); time -= times[1];
        times[2] = Math.min(time, 1.0f); time -= times[2];

        for(int i=0; i < 3; i++) {

            int uiIndex = 2 - i;
            circleProgressBar[uiIndex].setMax(100);
            circleProgressBar[uiIndex].setProgress((int)Math.ceil(times[i] * 100));
        }
    }

    @Override
    public int getItemCount() {
        return dailyTimeList.size();
    }

    public void addDailyTimeDatas(List<StatisPacket.DailyTime> history)
    {
        // dailyTimeList.add(data);
        Date startOfWeek = BcDateUtils.GetStartOfWeek(0);

        for(int i=0; i < 7 ;i++)
        {
            Date nextDay = BcDateUtils.GetNextDay(startOfWeek, i);
            StatisPacket.DailyTime data=  GetDayData(nextDay,history);
            dailyTimeList.add(data);
        }
    }

    public void addDailyTimeDatasForReset()
    {
        // dailyTimeList.add(data);
        Date startOfWeek = BcDateUtils.GetStartOfWeek(0);

        for(int i=0; i < 7 ;i++)
        {
            StatisPacket.DailyTime data = new StatisPacket.DailyTime();
            data.dailyTime = 0;
            data.date =  BcDateUtils.GetNextDay(startOfWeek, i);
            dailyTimeList.add(data);
        }
    }

    StatisPacket.DailyTime GetDayData(Date day,List<StatisPacket.DailyTime> history)
    {
        for (StatisPacket.DailyTime h :history)
        {
            if(BcDateUtils.IsSameDay(h.date, day))
                return h;
        }

        StatisPacket.DailyTime n = new StatisPacket.DailyTime();
        n.dailyTime = 0;
        n.date = day;

        return n;
    }


    public void clearDatas()
    {
        dailyTimeList.clear();
    }

    public void refresh()
    {
        notifyDataSetChanged();
    }






}
