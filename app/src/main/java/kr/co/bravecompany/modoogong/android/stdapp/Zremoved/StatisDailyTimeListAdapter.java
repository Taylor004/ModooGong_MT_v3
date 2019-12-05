package kr.co.bravecompany.modoogong.android.stdapp.Zremoved;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import kr.co.bravecompany.api.android.stdapp.api.data.Packet;
import kr.co.bravecompany.api.android.stdapp.api.data.StatisPacket;
import kr.co.bravecompany.modoogong.android.stdapp.R;

public class StatisDailyTimeListAdapter extends BaseAdapter {

    public static class DailyTimeData
    {
        public StatisPacket.DailyTime timeData;
    }

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;

    List<DailyTimeData> mDailyTimeDataList = new ArrayList<>();


    public StatisDailyTimeListAdapter(Context context)
    {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void clearData()
    {
        mDailyTimeDataList.clear();
    }

    public void addData(StatisPacket.DailyTime data)
    {
        DailyTimeData timeData = new DailyTimeData();
        timeData.timeData = data;
        mDailyTimeDataList.add(timeData);
    }

    public void refresh()
    {
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDailyTimeDataList.size();
    }

    @Override
    public DailyTimeData getItem(int position)
    {
        return mDailyTimeDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if(v ==null)
            v = mLayoutInflater.inflate(R.layout.view_statis_subjects_history_item, null);
//
//        Packet.SubjectData data = subjectList.get(position);
//
//        Button name = (Button)v.findViewById(R.id.statis_subject_name);
//        name.setText(data.SUBJ_NM);

        return v;
    }
}
