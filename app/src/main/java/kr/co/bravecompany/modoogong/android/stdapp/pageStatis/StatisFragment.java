package kr.co.bravecompany.modoogong.android.stdapp.pageStatis;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kr.co.bravecompany.api.android.stdapp.api.data.Packet;
import kr.co.bravecompany.api.android.stdapp.api.data.StatisPacket;
import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.activity.MainActivity;
import kr.co.bravecompany.modoogong.android.stdapp.db.BaseDataManager;
import kr.co.bravecompany.modoogong.android.stdapp.db.BcDateUtils;
import kr.co.bravecompany.modoogong.android.stdapp.db.MetaDataManager;
import kr.co.bravecompany.modoogong.android.stdapp.db.StatisDataManager;
import kr.co.bravecompany.modoogong.android.stdapp.fragment.BaseFragment;
import kr.co.bravecompany.modoogong.android.stdapp.utils.UIUtils;

public class StatisFragment extends BaseFragment {

    //오늘 및 어제 수강 시간 비교 timeBar
    public static class TimeBar
    {
        public ViewGroup layout;
        public CircleProgressBar [] circleProgressBar;
    }

    //일간 순공시간 히스토리
    public static class DailyTimeHistoryLayout
    {
        public ViewGroup layout;
        public StatisDailyTimeHistoryRecyclerAdapter adapter;
        public RecyclerView recyclerView;
        public LinearLayoutManager linearLayoutManager;
    }

    //오늘 및 어제 수강 시간
    public static class TodayTimeLayout
    {
        public ViewGroup layout; // 레이아웃
        public TextView title; // 일일 수강시간 타이틀명
        public TextView time; // 시간
//        public TextView rank; // 등수
//        public ImageView rankDeltaImage;
//        public TextView rankDelta;
        public TimeBar timeBar; // 수강한 시간을 bar 표현
    }

    //주간 과목별 학습 시간 차트
    public static class WeeklyChartViewLayoutView
    {
        public ViewGroup layout; // 레이아웃

        public ViewGroup emptyLayout;
        public ViewGroup watingLayout;
        public ViewGroup chartLayout;

        //public CircleProgressBar waitingBar;

        public PieChart pieChart; // 파이 차트
        public int activeWeek; // 주간 활동
        public ImageButton nextWeek; // 다음 주 이미지 버튼
        public ImageButton prevWeek; // 전주 이미지 버튼
        public Calendar calendar;
        public TextView simpleWeeklyText; // 주단위 텍스트
        public TextView detailWeeklyText; // 년월일 주단위 텍스트
    }

    //과목별 학습 시간 추이
    public static class LayoutSubjectHistoryView
    {
        public ViewGroup layout; // 과목별 학습 추이 레이아웃
        public RecyclerView subjectButtonList; // 과목별 버튼 리스트
        public StatisSubjectNameRecyclerAdapter subjectNameAdapter; // 과목별 어댑터
        public LinearLayoutManager linearLayoutManager; // 가로 버튼 리스트를 위한 리니어 레이아웃 매니져

        public BarChart barChart; // 과목의 학습 추이를 보여주는 막대 그래프

        public TextView meanStudyTime;
        public TextView studyPeriod;

        // public ArrayList<BarEntry> barEntry1; // 막대 그래프의 설정될 데이터 리스트.
       // public ArrayList<BarEntry> barEntry2;
        //public BarData barData;

        //public ArrayList<String> barEntryLabels1; // 막대 그래프의 라벨.
        ///public ArrayList<String> barEntryLabels2;
       // public BarDataSet barDataSet1;
       // public BarDataSet barDataSet2;
       // public BarData barData;
        public String selectedSubjCD; // 선택된 과목 코드
    }

    // 과목별 누적 수강 기록 클래스
    public static class LayoutSubjectMedalView
    {
        public ViewGroup layout; // 레이아웃
        public GridView gridView; // 누적된 수강 기록을 보여줄 그리드 리스트
        public StatisSubjectMedalAdapter adapter; // 그리드 뷰에 사용될 어댑터
    }

    public DailyTimeHistoryLayout mLayoutDailyTimeHistory;

    public TodayTimeLayout mLayoutTodayTime; // 오늘의 수강시간 객체
    public TodayTimeLayout mLayoutYesterdayTime; // 어제의 수강시간 객체

    public WeeklyChartViewLayoutView mLayoutWeeklyChartView;
    public LayoutSubjectHistoryView mLayoutSubjectHistoryView;
    public LayoutSubjectMedalView mLayoutSubjectMedalView;


    public static StatisFragment newInstance() { return new StatisFragment();}


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_statis_record, container, false);

        initDailyTimeHistory(rootView);
        initTodayTimeView(rootView);
        initWeeklySubjectTimeView(rootView);
        initSubjectTimeHistory(rootView);
        initSubjectMedalView(rootView);

        return rootView;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        refreshDailyTimeHistory(false);
        refreshTodayTimeView();
    }

    @Override
    public void onStop()
    {
        super.onStop();
    }

    @Override
    protected void initLayout(ViewGroup rootView) {
    }

    @Override
    protected void initListener() {
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void setData(String data) {
    }

    /*********************************************
     *
     * 일간 학습 기록
     *
     *********************************************/

    private void initDailyTimeHistory(ViewGroup rootView)
    {
        mLayoutDailyTimeHistory = new DailyTimeHistoryLayout();
        mLayoutDailyTimeHistory.layout = (ViewGroup) rootView.findViewById(R.id.content_statis_dailyhistory_layout);
        mLayoutDailyTimeHistory.linearLayoutManager = new LinearLayoutManager(this.getContext(),LinearLayoutManager.HORIZONTAL,false);
        mLayoutDailyTimeHistory.recyclerView = mLayoutDailyTimeHistory.layout.findViewById(R.id.content_statis_daily_history_recyclerView);
        mLayoutDailyTimeHistory.recyclerView.setLayoutManager(mLayoutDailyTimeHistory.linearLayoutManager);
        mLayoutDailyTimeHistory.adapter = new StatisDailyTimeHistoryRecyclerAdapter(getContext());
        mLayoutDailyTimeHistory.recyclerView.setAdapter(mLayoutDailyTimeHistory.adapter);

        refreshDailyTimeHistory(true);
    }

    private void refreshDailyTimeHistory(boolean reset)
    {
        if(reset)
        {
            mLayoutDailyTimeHistory.adapter.clearDatas();
            mLayoutDailyTimeHistory.adapter.addDailyTimeDatasForReset();
            mLayoutDailyTimeHistory.adapter.refresh();
        }

        StatisDataManager.getInstance().GetDailyTimeHistory(new BaseDataManager.OnDataUpdatedListener<List<StatisPacket.DailyTime>>() {
            @Override
            public void onDataUpdated(List<StatisPacket.DailyTime> data) {

                if(data !=null) {
                    mLayoutDailyTimeHistory.adapter.clearDatas();
                    mLayoutDailyTimeHistory.adapter.addDailyTimeDatas(data);
                    mLayoutDailyTimeHistory.adapter.refresh();
                }
            }
        });
    }

    /*********************************************
     *
     * 오늘 vs 내일 수강 시간 비교
     *
     *********************************************/

    private void initTodayTimeView(ViewGroup rootView)
    {
        mLayoutYesterdayTime = new TodayTimeLayout();
        mLayoutYesterdayTime.layout = (ViewGroup) rootView.findViewById(R.id.content_statis_todaytime_yesterday_item); // 어제 수강 시간 레이아웃
        BuildTodayTimeLayout(mLayoutYesterdayTime);

        mLayoutTodayTime = new TodayTimeLayout();
        mLayoutTodayTime.layout = (ViewGroup) rootView.findViewById(R.id.content_statis_todaytime_today_item); // 오늘 수강 시간 레이아웃
        BuildTodayTimeLayout(mLayoutTodayTime);


        refreshTodayTimeView();
    }

    void BuildTodayTimeLayout(TodayTimeLayout layout)
    {
        ViewGroup view = layout.layout;

        layout.timeBar = new TimeBar();
        layout.timeBar.circleProgressBar = new CircleProgressBar[3];
        layout.timeBar.circleProgressBar[0] = (CircleProgressBar) view.findViewById(R.id.prog_1);
        layout.timeBar.circleProgressBar[1] = (CircleProgressBar) view.findViewById(R.id.prog_2);
        layout.timeBar.circleProgressBar[2] = (CircleProgressBar) view.findViewById(R.id.prog_3);

        layout.title = (TextView) view.findViewById(R.id.title);
        layout.time = (TextView) view.findViewById(R.id.time);

        layout.time.setVisibility(View.INVISIBLE);
        StatisDailyTimeHistoryRecyclerAdapter.DrawTimeCircleProgressBar(layout.timeBar.circleProgressBar, 0);
    }

    private void refreshTodayTimeView() {

        StatisDataManager.getInstance().GetTodayTime(new BaseDataManager.OnDataUpdatedListener<StatisPacket.DailyTime>() {
            @Override
            public void onDataUpdated(StatisPacket.DailyTime data) {
                if(data !=null)
                    refreshTodayTimeProgressBar(mLayoutTodayTime, data);
            }
        });

        StatisDataManager.getInstance().GetYesterdayTime(new BaseDataManager.OnDataUpdatedListener<StatisPacket.DailyTime>() {
            @Override
            public void onDataUpdated(StatisPacket.DailyTime data) {
                if(data !=null)
                    refreshTodayTimeProgressBar(mLayoutYesterdayTime, data);
            }
        });
    }

    private void refreshTodayTimeProgressBar(TodayTimeLayout layout, StatisPacket.DailyTime data)
    {
        StatisDailyTimeHistoryRecyclerAdapter.DrawTimeCircleProgressBar(layout.timeBar.circleProgressBar, data.dailyTime);

        layout.time.setVisibility(View.VISIBLE);
        BcDateUtils.HMS hms= BcDateUtils.getHMS(data.dailyTime);
        String timeText =String.format("%02d시간 %02d분", hms.h, hms.m);
        layout.time.setText(timeText);
    }

    /*********************************************
     *
     * 주간 과목별 학습 시간 (파이차트)
     *
     *********************************************/

    private void initWeeklySubjectTimeView(ViewGroup rootView)
    {
        mLayoutWeeklyChartView = new WeeklyChartViewLayoutView();

        mLayoutWeeklyChartView.layout = (ViewGroup) rootView.findViewById(R.id.content_statis_weekly_subject_chart);
        mLayoutWeeklyChartView.chartLayout = (ViewGroup) rootView.findViewById(R.id.subject_time_chart_layout);
        mLayoutWeeklyChartView.watingLayout = (ViewGroup) rootView.findViewById(R.id.subject_time_waiting_layout);
        mLayoutWeeklyChartView.emptyLayout = (ViewGroup) rootView.findViewById(R.id.subject_time_empty_layout);

       // mLayoutWeeklyChartView.waitingBar = (CircleProgressBar) rootView.findViewById(R.id.subject_time_waiting_bar) ;

        mLayoutWeeklyChartView.pieChart = (PieChart)rootView.findViewById(R.id.subject_time_piechart);
        mLayoutWeeklyChartView.nextWeek = (ImageButton) rootView.findViewById(R.id.subject_time_next_week);
        mLayoutWeeklyChartView.prevWeek = (ImageButton) rootView.findViewById(R.id.subject_time_prev_week);
        mLayoutWeeklyChartView.simpleWeeklyText = (TextView) rootView.findViewById(R.id.weeklyTextView1);
        mLayoutWeeklyChartView.detailWeeklyText = (TextView) rootView.findViewById(R.id.weeklyTextView2);
        mLayoutWeeklyChartView.calendar = Calendar.getInstance();
        mLayoutWeeklyChartView.calendar.get(Calendar.MONTH+1);
        mLayoutWeeklyChartView.calendar.get(Calendar.WEEK_OF_MONTH);

        PieChart pieChart = mLayoutWeeklyChartView.pieChart;
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setRotationEnabled(false);
        pieChart.setDragDecelerationFrictionCoef(1.0f);
        pieChart.setTransparentCircleColor(Color.BLACK);
        pieChart.setHoleColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(0);
        pieChart.setExtraOffsets(5,5,5,5);
        pieChart.setTransparentCircleRadius(0.1f);
        pieChart.setHoleRadius(55.0f);


        mLayoutWeeklyChartView.prevWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshWeeklySubjectTimeView(-1);// 클릭 이전주 deltaWeek = -1
            }
        });

        mLayoutWeeklyChartView.nextWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshWeeklySubjectTimeView(1);// 클릭 다음주 deltaWeek = 1
            }
        });

        refreshWeeklySubjectTimeView(0); // 클릭을 하지 않았으면 이번주 deltaWeek = 0
    }

    private void refreshWeeklySubjectTimeView(int deltaWeek) {

        mLayoutWeeklyChartView.watingLayout.setVisibility(View.VISIBLE);
        mLayoutWeeklyChartView.chartLayout.setVisibility(View.GONE);
        mLayoutWeeklyChartView.emptyLayout.setVisibility(View.GONE);
        //mLayoutWeeklyChartView.waitingBar.startAnimation();

        mLayoutWeeklyChartView.activeWeek += deltaWeek;
        mLayoutWeeklyChartView.activeWeek = Math.min(mLayoutWeeklyChartView.activeWeek,0);
        mLayoutWeeklyChartView.activeWeek = Math.max(mLayoutWeeklyChartView.activeWeek,-4);

        boolean hasPrev = mLayoutWeeklyChartView.activeWeek > -4;
        boolean hasNext = mLayoutWeeklyChartView.activeWeek < 0;

        mLayoutWeeklyChartView.nextWeek.setVisibility(hasNext? View.VISIBLE : View.INVISIBLE);
        mLayoutWeeklyChartView.prevWeek.setVisibility(hasPrev? View.VISIBLE : View.INVISIBLE);

        Date activeWeekDate = BcDateUtils.GetStartOfWeek( mLayoutWeeklyChartView.activeWeek);
        Calendar cal = Calendar.getInstance();
        cal.setTime(activeWeekDate);

        String simpleDateText = String.format("%d월 %d주", cal.get(Calendar.MONTH)+1, cal.get(Calendar.WEEK_OF_MONTH));

        String monday_Date = BcDateUtils.getMonday(mLayoutWeeklyChartView.activeWeek);
        String sunday_Date = BcDateUtils.getSunday(mLayoutWeeklyChartView.activeWeek);
        String detailText = String.format("%s ~ %s",monday_Date,sunday_Date);

        mLayoutWeeklyChartView.simpleWeeklyText.setText(simpleDateText);
        mLayoutWeeklyChartView.detailWeeklyText.setText(detailText);

       // startLoading();

        StatisDataManager.getInstance().GetWeeklySubjectTime(mLayoutWeeklyChartView.activeWeek,
                new BaseDataManager.OnDataUpdatedListener<StatisPacket.WeeklyTimeResult>() {
                    @Override
                    public void onDataUpdated(StatisPacket.WeeklyTimeResult data) {

                       // stopLoading();

                        if(data ==null || data.subjectList ==null || data.subjectList.size() ==0) {
                            //TODO : 데이타가 없을 경우에 대한 처리 필요
                            mLayoutWeeklyChartView.watingLayout.setVisibility(View.GONE);
                            mLayoutWeeklyChartView.chartLayout.setVisibility(View.GONE);
                            mLayoutWeeklyChartView.emptyLayout.setVisibility(View.VISIBLE);

                        } else {
                            mLayoutWeeklyChartView.watingLayout.setVisibility(View.GONE);
                            mLayoutWeeklyChartView.chartLayout.setVisibility(View.VISIBLE);
                            mLayoutWeeklyChartView.emptyLayout.setVisibility(View.GONE);

                            refreshWeeklySubjectTimeChart(data.subjectList);
                        }
                    }
                });
    }


    private void refreshWeeklySubjectTimeChart(List<StatisPacket.SubjectTime> subjectTimeList)
    {
        List<PieEntry> yValue = new ArrayList<>();
        List<LegendEntry> legendEntries = new ArrayList<>();

        int colorIndex = 0;
        List<Integer> colors = new ArrayList<>();

        PieDataSet dataSet = new PieDataSet(yValue,"");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        int totalStudyTime = 0;

        for (StatisPacket.SubjectTime subjTime :subjectTimeList) {
            totalStudyTime +=subjTime.hours;
        }

        for (StatisPacket.SubjectTime subjTime :subjectTimeList)
        {
            if(subjTime.hours <= 0)
                continue;

            String name = MetaDataManager.getInstance().GetSubjectName(subjTime.subjCD);
            int percent =(int) Math.round (subjTime.hours * 100.0f / totalStudyTime);

            yValue.add(new PieEntry((int)subjTime.hours,name));

            LegendEntry legendEntry = new LegendEntry();
            legendEntry.form = Legend.LegendForm.CIRCLE;
            legendEntry.label = String.format("%s %d%%",name,percent);
            legendEntry.formColor = dataSet.getColor(colorIndex++);
            legendEntries.add(legendEntry);
        }

        if(yValue.size() > 0)
        {
            //Note. Legend 설정을 먼저해줘야, setData에서 크래쉬가 발생하지 않는다!
            Legend legend = mLayoutWeeklyChartView.pieChart.getLegend();
            legend.setTextColor(Color.WHITE);
            legend.setCustom(legendEntries);
            legend.setEnabled(true);
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);

            dataSet.setSliceSpace(0.0f);
            dataSet.setSelectionShift(10);
            dataSet.setValueTextSize(0.0f);

            dataSet.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return String.format("%d%%", (int) Math.round(value));
                }
            });

            PieData data = new PieData(dataSet);
            data.setValueTextSize(10f);
            data.setValueTextColor(Color.WHITE);

            /*
            data.setValueFormatter(new ValueFormatter() {
                @Override
                public String getAxisLabel(float value, AxisBase axis) {
                    return String.format("%d%%", (int) Math.round(value));
                }

                @Override
                public String getPointLabel(Entry entry)
                {
                    return String.format("%d%%", (int) Math.round(entry.getY()));
                }

            });
            */
            mLayoutWeeklyChartView.pieChart.setData(data);


            BcDateUtils.HMS hms = BcDateUtils.getHMS(totalStudyTime);
            String centerText = "";

            if(hms.h == 0 && hms.m ==0)
                centerText.format("누적 시간\n--");
            else if(hms.h == 0 )
                centerText.format("누적 시간\n%02d분", hms.m);
            else
                centerText.format("누적 시간\n%02d시간", hms.h);

            mLayoutWeeklyChartView.pieChart.setCenterText(centerText);
            mLayoutWeeklyChartView.pieChart.setCenterTextSize(15f);
            mLayoutWeeklyChartView.pieChart.setCenterTextColor(Color.WHITE);

            mLayoutWeeklyChartView.pieChart.animateY(1000);
            mLayoutWeeklyChartView.pieChart.invalidate();
        }
        else
        {
            mLayoutWeeklyChartView.watingLayout.setVisibility(View.GONE);
            mLayoutWeeklyChartView.chartLayout.setVisibility(View.GONE);
            mLayoutWeeklyChartView.emptyLayout.setVisibility(View.VISIBLE);
        }
    }



    /*********************************************
     *
     * 과목별 학습 시간 추이
     *
     * *******************************************/

    private void initSubjectTimeHistory(ViewGroup rootView)
    {
        mLayoutSubjectHistoryView = new LayoutSubjectHistoryView();
        mLayoutSubjectHistoryView.layout = (ViewGroup) rootView.findViewById(R.id.content_statis_subjects_history);
        mLayoutSubjectHistoryView.subjectButtonList = (RecyclerView) rootView.findViewById(R.id.subject_name_recyclerview);
        mLayoutSubjectHistoryView.linearLayoutManager = new LinearLayoutManager(this.getContext(),LinearLayoutManager.HORIZONTAL,false);
        mLayoutSubjectHistoryView.barChart = (BarChart) rootView.findViewById(R.id.subject_history_barchart);

        mLayoutSubjectHistoryView.meanStudyTime = (TextView) rootView.findViewById(R.id.content_statis_subjects_history_time);
        mLayoutSubjectHistoryView.studyPeriod = (TextView) rootView.findViewById(R.id.content_statis_subjects_history_date);


        StatisSubjectNameRecyclerAdapter adapter = new StatisSubjectNameRecyclerAdapter(getContext(),
                new StatisSubjectNameRecyclerAdapter.OnSubjectNameSelectedListener() {
                    @Override
                    public void onSubjectNameSelected(String subjCD) {

                        refreshSubjectTimeHistory (subjCD);
                    }
                });

        mLayoutSubjectHistoryView.subjectNameAdapter = adapter;
        mLayoutSubjectHistoryView.subjectButtonList.setLayoutManager(mLayoutSubjectHistoryView.linearLayoutManager);
        mLayoutSubjectHistoryView.subjectButtonList.setAdapter(adapter);

        setHistoryChart();


        //초기화 및 디폴트 선택
        refreshSubjectNameListExInit();
        refreshSubjectTimeHistory("0");

        refreshSubjectNameListEx();
    }

    /* 사용하지 않는 버전
    private void refreshSubjectNameList()
    {
        MetaDataManager.getInstance().loadSubjectData(new BaseDataManager.OnDataUpdatedListener<List<Packet.SubjectData>>() {
            @Override
            public void onDataUpdated(List<Packet.SubjectData> data) {

                if(data !=null) {
                    mLayoutSubjectHistoryView.subjectNameAdapter.clearData(); //현재 에러 발생 여기서 나타남.
                    for (int i = 0; i < data.size(); i++) {
                        //TODO: 한 강의라도 신청이 되어 있어야 표현이 된다!
                        Packet.SubjectData subjectData = data.get(i);
                        mLayoutSubjectHistoryView.subjectNameAdapter.addSubjectData(subjectData);
                    }

                    mLayoutSubjectHistoryView.subjectNameAdapter.refresh();
                }

            }

        });
    }
    */

    private void refreshSubjectNameListExInit()
    {
        mLayoutSubjectHistoryView.subjectNameAdapter.clearData();
        mLayoutSubjectHistoryView.subjectNameAdapter.addSubjectData0();
        mLayoutSubjectHistoryView.subjectNameAdapter.refresh();
    }

    private void refreshSubjectNameListEx()
    {
        StatisDataManager.getInstance().GetTotalSubjectView(new StatisDataManager.OnDataUpdatedListener<StatisPacket.TotalSubjectViewResult>() {
            @Override
            public void onDataUpdated(StatisPacket.TotalSubjectViewResult data) {

                if(data !=null)
                {
                    mLayoutSubjectHistoryView.subjectNameAdapter.clearData();
                    mLayoutSubjectHistoryView.subjectNameAdapter.addSubjectData0();

                    for(int i=0 ; i < data.subjectList.size();i++)
                    {
                        StatisPacket.SubjectView subjectView  = data.subjectList.get(i);
                        Packet.SubjectData subjectData =  MetaDataManager.getInstance().findSubjectData(subjectView.subjCD);
                        if(subjectData !=null)
                            mLayoutSubjectHistoryView.subjectNameAdapter.addSubjectData(subjectData);
                    }

                    mLayoutSubjectHistoryView.subjectNameAdapter.refresh();
                }
            }});
    }

    private void refreshSubjectTimeHistory(final String selected)
    {
        mLayoutSubjectHistoryView.selectedSubjCD = selected;
        mLayoutSubjectHistoryView.subjectNameAdapter.Selected(selected);

        StatisDataManager.getInstance().GetHistoricalSubjectTime(selected,
                new BaseDataManager.OnDataUpdatedListener<StatisPacket.HistoricalSubjectTimeResult>() {
                    @Override
                    public void onDataUpdated(StatisPacket.HistoricalSubjectTimeResult data) {

                        //data.subjCD = mLayoutSubjectHistoryView.selectedSubjCD;
                        refreshSubjectTimeHistoryChart(data);
                    }
                });
    }

/*
    void refreshSubjectTimeHistoryChart_old(StatisPacket.HistoricalSubjectTimeResult data)
    {
        //바 차트 드로잉 참고 자료!!
        //https://stackoverflow.com/questions/35240289/how-to-create-a-barchart-with-grouped-bars-with-mpandroidchart

        //TODO : Bar Chart!!
        float groupSpace = 0.36f;
        float barSpace = 0.02f;
        float barWidth = 0.30f;

       // List<LegendEntry> legendEntries = new ArrayList<>();
        List<String> xLabels = new ArrayList<>();

        mLayoutSubjectHistoryView.barEntry1 = new ArrayList<BarEntry>();
        mLayoutSubjectHistoryView.barEntry2 = new ArrayList<BarEntry>();
        //mLayoutSubjectHistoryView.barEntryLabels1 = new ArrayList<String>();
       // mLayoutSubjectHistoryView.barEntryLabels2 = new ArrayList<String>();

        setHistoryLegends();


        int cols=   AddValueToBarEntry(data, xLabels); // 엔트리 추가 하는 메소드
        //AddValueToBarEntryLabel(); // 엔트리 라벨 추가하는 메소드

        BarDataSet barDataSet1 = new BarDataSet(mLayoutSubjectHistoryView.barEntry1,"내 공부시간");
        BarDataSet barDataSet2 = new BarDataSet(mLayoutSubjectHistoryView.barEntry2,"상위 5% 공부시간");

        barDataSet1.setColor(Color.GREEN);
        barDataSet1.setValueTextColor(Color.WHITE);
        barDataSet2.setColor(Color.GRAY);
        barDataSet2.setValueTextColor(Color.WHITE);

        BarData barData = new BarData(barDataSet1, barDataSet2);
        barData.setBarWidth(barWidth);

        mLayoutSubjectHistoryView.barChart.setData(barData);
        mLayoutSubjectHistoryView.barChart.setVisibleXRangeMaximum(4);

       // mLayoutSubjectHistoryView.barChart.moveViewToX(0);
       // mLayoutSubjectHistoryView.barChart.groupBars(0,groupSpace,barSpace);

        XAxis xAxis = mLayoutSubjectHistoryView.barChart.getXAxis();
        xAxis.setTextColor(Color.GRAY);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //
        xAxis.setGranularity(1);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularityEnabled(true);

        int groupCount = data.myHistory.length;
        float unitWitdh = mLayoutSubjectHistoryView.barChart.getBarData().getGroupWidth(groupSpace, barSpace);
        float maxWidth =unitWitdh * groupCount;

       // xAxis.setAxisMinimum(0);
       // xAxis.setAxisMaximum(cols);

        xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(0 + maxWidth);
        mLayoutSubjectHistoryView.barChart.groupBars(0, groupSpace, barSpace);

       // String[] months = new String[] {"A", "B", "C", "D","E","F","G","H"};
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels.toArray(new String[0])));

        YAxis yAxisL = mLayoutSubjectHistoryView.barChart.getAxisLeft();
      //  yAxisL.setEnabled(false);

        yAxisL.setDrawAxisLine(false);
        yAxisL.setDrawGridLines(false);
        yAxisL.setAxisMinimum(0);
        yAxisL.setAxisMaximum(12);

        YAxis yAxisR = mLayoutSubjectHistoryView.barChart.getAxisRight();
        yAxisR.setTextColor(Color.GRAY);
        yAxisR.setDrawAxisLine(false);
        yAxisR.setDrawGridLines(false);
        yAxisR.setAxisMinimum(0);
        yAxisR.setAxisMaximum(12);

        mLayoutSubjectHistoryView.barChart.setDescription(null);
        mLayoutSubjectHistoryView.barChart.setFitBars(true);
        mLayoutSubjectHistoryView.barChart.invalidate();
    }
*/

    void setHistoryChart()
    {
        mLayoutSubjectHistoryView.barChart.setVisibleXRangeMaximum(4);

        XAxis xAxis = mLayoutSubjectHistoryView.barChart.getXAxis();
        xAxis.setTextColor(Color.GRAY);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        xAxis.setGranularity(1);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularityEnabled(true);

        YAxis yAxisL = mLayoutSubjectHistoryView.barChart.getAxisLeft();
        //yAxisL.setEnabled(false);
        yAxisL.setDrawAxisLine(false);
        yAxisL.setDrawGridLines(false);
        yAxisL.setAxisMinimum(0);
        yAxisL.setAxisMaximum(12);

        YAxis yAxisR = mLayoutSubjectHistoryView.barChart.getAxisRight();
        yAxisR.setTextColor(Color.GRAY);
        yAxisR.setDrawAxisLine(false);
        yAxisR.setDrawGridLines(false);
        yAxisR.setAxisMinimum(0);
        yAxisR.setAxisMaximum(12);
        yAxisR.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {

                //return super.getAxisLabel(value, axis);
                return String.format("%dH", (int)value);
            }
        });

        mLayoutSubjectHistoryView.barChart.setDescription(null);
        mLayoutSubjectHistoryView.barChart.setFitBars(true);

        setHistoryLegends();
    }

    void setHistoryLegends()
    {
        String myHistoryText = "내 공부시간";
        String topHistoryText = "상위 5% 공부시간";
        List<LegendEntry> legendEntries = new ArrayList<>();

        LegendEntry legendEntry1 = new LegendEntry();
        legendEntry1.label = myHistoryText;
        legendEntry1.form = Legend.LegendForm.CIRCLE;
        legendEntry1.formColor = Color.GREEN;
        legendEntry1.formSize = 5.0f;
        legendEntries.add(legendEntry1);

        LegendEntry legendEntry2 = new LegendEntry();
        legendEntry2.label = topHistoryText;
        legendEntry2.form = Legend.LegendForm.CIRCLE;
        legendEntry2.formColor = Color.GRAY;
        legendEntry2.formSize = 5.0f;
        legendEntries.add(legendEntry2);

        Legend legend = mLayoutSubjectHistoryView.barChart.getLegend();
        legend.setEnabled(true);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setTextColor(Color.WHITE);
        legend.setFormToTextSpace(5);
        legend.setXEntrySpace(20);
        legend.setYEntrySpace(0);
        legend.setCustom(legendEntries);
    }


    void refreshSubjectTimeHistoryChart(StatisPacket.HistoricalSubjectTimeResult data)
    {
        //바 차트 드로잉 참고 자료!!
        //https://stackoverflow.com/questions/35240289/how-to-create-a-barchart-with-grouped-bars-with-mpandroidchart

        //TODO : Bar Chart!!
        final float groupSpace = 0.36f;
        final float barSpace = 0.02f;
        final float barWidth = 0.30f;

        List<String> xLabels = new ArrayList<>();
        List<BarEntry> barEntry1 = new ArrayList<BarEntry>();
        List<BarEntry> barEntry2 = new ArrayList<BarEntry>();

        int itemCount = AddValueToBarEntry(data, barEntry1, barEntry2, xLabels); // 엔트리 추가 하는 메소드

        BarDataSet barDataSet1 = new BarDataSet(barEntry1,"내 공부시간");
        BarDataSet barDataSet2 = new BarDataSet(barEntry2,"상위 5% 공부시간");

        barDataSet1.setColor(Color.GREEN);
        barDataSet1.setValueTextColor(Color.WHITE);
        barDataSet2.setColor(Color.GRAY);
        barDataSet2.setValueTextColor(Color.WHITE);

        ValueFormatter valueFormatter = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if(value < 0.001f)
                    return "";
                else
                    return String.format("%.1fH",value);
            }
        };

        barDataSet1.setValueFormatter(valueFormatter);
        barDataSet2.setValueFormatter(valueFormatter);

        BarData barData = new BarData(barDataSet1, barDataSet2);
        barData.setBarWidth(barWidth);

        mLayoutSubjectHistoryView.barChart.setData(barData);
        mLayoutSubjectHistoryView.barChart.setVisibleXRangeMaximum(4);

        XAxis xAxis = mLayoutSubjectHistoryView.barChart.getXAxis();

        //int groupCount = data.myHistory.length;
        float unitWitdh = mLayoutSubjectHistoryView.barChart.getBarData().getGroupWidth(groupSpace, barSpace);
        float maxWidth = unitWitdh * itemCount;

        xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(maxWidth);
        mLayoutSubjectHistoryView.barChart.groupBars(0, groupSpace, barSpace);

        xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels.toArray(new String[0])));

        mLayoutSubjectHistoryView.barChart.animateY(300);
        mLayoutSubjectHistoryView.barChart.notifyDataSetChanged();
        mLayoutSubjectHistoryView.barChart.invalidate();
    }


    private int AddValueToBarEntry(StatisPacket.HistoricalSubjectTimeResult data,
                                   List<BarEntry> barEntry1 , List<BarEntry> barEntry2, List<String> xLabels)
    {
        float meanTime = 0.0f;
        float totalTime = 0.0f;
        int period = data.myHistory.length;

        int[] myHistoryHours = data.myHistory; //내 공부 시간
        int[] topHistoryHours = data.topHistory; //상위 공부 시간

        for(int i = 0; i < data.myHistory.length; i++)
        {
            int deltaWeek = data.myHistory.length - i;

//            float my = i+ 2.0f;// (float) myHistoryHours[i] / (60.0f * 60.0f);
//            float top = i + 4.0f;// (float) topHistoryHours[i] / (60.0f * 60.0f);

            totalTime += myHistoryHours[i];

            float my = (float) myHistoryHours[i] / (60.0f * 60.0f);
            float top = (float) topHistoryHours[i] / (60.0f * 60.0f);

            barEntry1.add(new BarEntry(deltaWeek-1, my));
            barEntry2.add(new BarEntry(deltaWeek-1, top));

            String e = String.format("%d주전", deltaWeek);
            xLabels.add(e);
        }

        meanTime = totalTime / period;
        BcDateUtils.HMS hms = BcDateUtils.getHMS((int)meanTime);

        String periodText = String.format("%s ~ %s",BcDateUtils.getMonday(-(period+1)), BcDateUtils.getSunday(-1));
        mLayoutSubjectHistoryView.meanStudyTime.setText(String.format("%d시간 %d분", hms.h, hms.m));
        mLayoutSubjectHistoryView.studyPeriod.setText(periodText);

        return period;
    }


    /***********************************************
     *
     * 과목별 수강 기록
     *
     * *********************************************/

    private void initSubjectMedalView(ViewGroup rootView)
    {
        //과목별 누적수강기록 초기화
        mLayoutSubjectMedalView = new LayoutSubjectMedalView();

        mLayoutSubjectMedalView.adapter = new StatisSubjectMedalAdapter(getContext());

        mLayoutSubjectMedalView.layout = (ViewGroup) rootView.findViewById(R.id.content_statis_subject_medal_layout);

        mLayoutSubjectMedalView.gridView = (GridView) rootView.findViewById(R.id.subject_medal_grid);

        //mLayoutSubjectMedalView.gridView.removeAllViews();  현재 에러 발생 여기서 나타남.
        mLayoutSubjectMedalView.gridView.setAdapter(mLayoutSubjectMedalView.adapter);

        refreshSubjectMedalView();
    }

    private void refreshSubjectMedalView()
    {
        StatisDataManager.getInstance().GetTotalSubjectView(new StatisDataManager.OnDataUpdatedListener<StatisPacket.TotalSubjectViewResult>() {
            @Override
            public void onDataUpdated(StatisPacket.TotalSubjectViewResult data) {

                mLayoutSubjectMedalView.adapter.clearDataList();

                for(int i = 0; i < data.subjectList.size(); i++)
                {
                    StatisPacket.SubjectView subjView = data.subjectList.get(i);
                    mLayoutSubjectMedalView.adapter.addData(subjView);
                }
                // GridView에서 Adapter가 가지고 있는 Column수에 따라서 GridView의 높이가 달라지도록 처리.
                UIUtils.ResizeGridViewDynamicHeight(mLayoutSubjectMedalView.gridView);
                mLayoutSubjectMedalView.adapter.refreshData();
            }
        });
    }

}


