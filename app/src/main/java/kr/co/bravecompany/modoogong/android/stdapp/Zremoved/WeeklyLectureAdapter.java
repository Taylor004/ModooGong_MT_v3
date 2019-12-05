package kr.co.bravecompany.modoogong.android.stdapp.Zremoved;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import kr.co.bravecompany.modoogong.android.stdapp.R;

public class WeeklyLectureAdapter extends BaseAdapter {


    Context mContext = null;

    LayoutInflater mLayoutInflater = null;

    ArrayList<WeeklyLectureListData> mWeeklyLectureListData;

    public WeeklyLectureAdapter(Context Context, ArrayList<WeeklyLectureListData> WeeklyLectureListData)
    {
            mContext = Context;

            mLayoutInflater = LayoutInflater.from(mContext);

            mWeeklyLectureListData = WeeklyLectureListData;

    }


    @Override
    public int getCount()
    { return mWeeklyLectureListData.size(); }

    @Override
    public Object getItem(int position)
    { return mWeeklyLectureListData.get(position); }

    @Override
    public long getItemId(int position)
    { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
         View view = mLayoutInflater.inflate(R.layout.view_statis_weekly_subjects_time_item,null);

        //텍스트
        TextView mSubjectNameTextView = (TextView)view.findViewById(R.id.subjectNameTextView);
        TextView mUserNameTextView = (TextView)view.findViewById(R.id.userNameTextView);
        TextView mTop5PercentTextView = (TextView)view.findViewById(R.id.top5PercentNameTextView);
        TextView mAverageTextView = (TextView)view.findViewById(R.id.averageTextView);

        // 총시간
        TextView mUserTotalLectureTimeTextView = (TextView)view.findViewById(R.id.userTotalLectureTimeTextView);
        TextView mTop5PercentTotalLectureTimeTextView = (TextView)view.findViewById(R.id.top5PercentTotalLectureTimeTextView);
        TextView mAverageTotalLectureTimeTextView = (TextView)view.findViewById(R.id.averageTotalLectureTimeTextView);

        // 프로그래스 바
        ProgressBar mUserLectureProgBar = (ProgressBar)view.findViewById(R.id.userLectureProgBar);
        ProgressBar mTop5PercentLectureProgBar = (ProgressBar)view.findViewById(R.id.top5PercentLectureProgBar);
        ProgressBar mAverageLectureProgBar = (ProgressBar)view.findViewById(R.id.averageLectureProgBar);


        mSubjectNameTextView.setText(mWeeklyLectureListData.get(position).getSubjectName());
        mUserNameTextView.setText(mWeeklyLectureListData.get(position).getUserName());
        mTop5PercentTextView.setText(mWeeklyLectureListData.get(position).getTop5PercentStr());
        mAverageTextView.setText(mWeeklyLectureListData.get(position).getAverageStr());

        mUserTotalLectureTimeTextView.setText(mWeeklyLectureListData.get(position).getUserTotalLectureTime());
        mTop5PercentTotalLectureTimeTextView.setText(mWeeklyLectureListData.get(position).getTop5PercentTotalLectureTime());
        mAverageTotalLectureTimeTextView.setText(mWeeklyLectureListData.get(position).getAverageTotalLectureTime());

        mUserLectureProgBar.setProgress(mWeeklyLectureListData.get(position).getUserProgBar());
        mTop5PercentLectureProgBar.setProgress(mWeeklyLectureListData.get(position).getTop5PercentProgBar());
        mAverageLectureProgBar.setProgress(mWeeklyLectureListData.get(position).getAverageProgBar());


         return view;

    }


}
