package kr.co.bravecompany.modoogong.android.stdapp.pageProfile;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import kr.co.bravecompany.api.android.stdapp.api.data.StatisPacket;
import kr.co.bravecompany.modoogong.android.stdapp.R;

public class ProfileExamInfoRecyclerAdapter extends SliderViewAdapter<ProfileExamInfoRecyclerAdapter.ProfileExamInfoAdapterVH> {

    Context mContext;
    LayoutInflater mLayoutInflater;
    List<StatisPacket.ExamInfo> examInfoResultList = new ArrayList<>();



    public ProfileExamInfoRecyclerAdapter(Context context)
    {
            this.mContext = context;
            this.mLayoutInflater.from(mContext);
    }


    public static class ProfileExamInfoAdapterVH extends SliderViewAdapter.ViewHolder {

        // 레이아웃에 있는 view들 선언
        View itemView;
        TextView dueDateTextView; // 마감 날짜 텍스트뷰
        TextView examNameTextView; // 시험 이름 텍스트뷰
        TextView examDdaysTextView; // 시험 디데이 텍스트뷰


        public ProfileExamInfoAdapterVH(View itemView) {
            super(itemView);

            dueDateTextView = (TextView) itemView.findViewById(R.id.date_TextView);
            examNameTextView = (TextView) itemView.findViewById(R.id.exam_Name_TextView);
            examDdaysTextView = (TextView) itemView.findViewById(R.id.exam_Ddays_TextView);
            this.itemView = itemView;


        }
    }


    @Override
    public ProfileExamInfoAdapterVH onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_exam_schedule_item,null);
        return new ProfileExamInfoAdapterVH(v);
    }




    @Override
    public void onBindViewHolder(ProfileExamInfoAdapterVH viewHolder, int position) {

        ProfileExamInfoAdapterVH profileExamInfoAdapterVH = (ProfileExamInfoAdapterVH) viewHolder;
        String examNameText = "";

        //데이터 처리할 부분
        StatisPacket.ExamInfo ExamInfoData = examInfoResultList.get(position);
        examNameText = ExamInfoData.examName;
        viewHolder.examNameTextView.setText(examNameText);

        String dateFormat = "yyyy년 MM월 dd일";
        String finalDateText = "";
        String dDayText = "";

        Calendar nowDay = Calendar.getInstance(); // 오늘 날짜
        Calendar d_day = Calendar.getInstance(); // 마감 날짜
        d_day.setTime(ExamInfoData.dueDate); // 디데이 날짜 설정

        Long l_nowDate = nowDay.getTimeInMillis()/(24*60*60*1000); // 오늘의 날짜 정보를 Millis값에서 일 단위 값으로 변경.
        Long l_d_day = d_day.getTimeInMillis()/(24*60*60*1000); // d_day 날짜 정보를 Millis값에서 일 단위 값으로 변경.
        Long l_substract_d_day = (l_nowDate - l_d_day) - 1; // 오늘 날짜 - 마감 날짜에서 추출한 디데이 값

        dDayText = l_substract_d_day.toString(); // long 타입 디데이 값을 string으로 형변환
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);

        finalDateText = simpleDateFormat.format(ExamInfoData.dueDate);
        viewHolder.dueDateTextView.setText(finalDateText); // 시험 마감 날짜
        viewHolder.examDdaysTextView.setText("D" + dDayText); // 시험 디데이

    }


    @Override
    public int getCount() {
        return examInfoResultList.size();
    }

    public void addExamInfoDatas(StatisPacket.ExamInfo data)
    {
        examInfoResultList.add(data);
    }

    public void clearDatas()
    {
        examInfoResultList.clear();
    }

    public void refresh()
    {
        notifyDataSetChanged();
    }



}
