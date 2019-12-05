package kr.co.bravecompany.modoogong.android.stdapp.pageStatis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import kr.co.bravecompany.api.android.stdapp.api.data.StatisPacket;
import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.db.MetaDataManager;
import kr.co.bravecompany.modoogong.android.stdapp.db.StatisDataManager;

public class StatisSubjectMedalAdapter extends BaseAdapter {

    private static class SubjectLectureListData {

        public String subjCD;   // 강의코드
        public String subjNM;   // 강의명
        public int view;        // 총 완강수
        public StatisDataManager.LevelInt viewLevel; // 총 완강수를 레벨로 변환
        public int weeklyView;

        public SubjectLectureListData(StatisPacket.SubjectView subjectView)
        {
            this.subjCD = subjectView.subjCD;
            this.subjNM = MetaDataManager.getInstance().GetSubjectName(subjectView.subjCD);
            this.view = subjectView.view;
            this.viewLevel = StatisDataManager.getInstance().GetSubjectViewLevel(subjectView.view);;
            this.weeklyView = subjectView.weeklyView;
        }
    }

    final int [] LEVEL_ICONS = {
            R.drawable.img_medal_default,
            R.drawable.img_medal_bronze,
            R.drawable.img_medal_silver,
            R.drawable.img_medal_gold}; // 목표 달성 별 이미지

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<SubjectLectureListData> subjectLectureListData = new ArrayList<>();

    public StatisSubjectMedalAdapter(Context context)
    {
        mContext = context;
        //subjectLectureListData = data;
        mLayoutInflater = LayoutInflater.from(mContext);

    }

    public void clearDataList()
    {
        subjectLectureListData.clear();
    }

    public void addData(StatisPacket.SubjectView data)
    {
        subjectLectureListData.add(new SubjectLectureListData(data));
    }

    public void refreshData()
    {
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return subjectLectureListData.size();
    }

    @Override
    public SubjectLectureListData getItem(int position) {
        return subjectLectureListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = mLayoutInflater.inflate(R.layout.view_statis_subject_medal_item, null);
        CircleImageView circleImageView = (CircleImageView)v.findViewById(R.id.subject_ImageView); // 수강 과목 목표 이미지
        TextView subjectMissionTextView = (TextView)v.findViewById(R.id.subjectMissionTextView); // 수강 과목 목표 달성 텍스트
        TextView remainedLectureSubjectTextView = (TextView)v.findViewById(R.id.remainedLectureSubjectTextView); // 과목 수강중 남은 강의 수 텍스트

        SubjectLectureListData data = subjectLectureListData.get(position);

        //최종 레벨이 아니라면...
        if(!data.viewLevel.completed)
        {
            circleImageView.setImageResource(LEVEL_ICONS[data.viewLevel.level]);
        }
        else //최종 레벨이면...
        {
            circleImageView.setImageResource((LEVEL_ICONS[LEVEL_ICONS.length-1]));// LEVEL_ICONS의 3번째 레벨 이미지로 설정.
        }

        String subjectText = String.format("%s 목표 %d강", data.subjNM, data.viewLevel.nextPoint);
        String reminedSubjectText = String.format("%s강 중 %s",data.viewLevel.nextPoint,data.view);

        subjectMissionTextView.setText(subjectText); // 과목명 및 총 완강수
        remainedLectureSubjectTextView.setText(reminedSubjectText); // 주간 강의 수

        return v;
    }
}
