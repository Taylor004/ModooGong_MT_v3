package kr.co.bravecompany.modoogong.android.stdapp.pageStatis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import kr.co.bravecompany.api.android.stdapp.api.data.StatisPacket;
import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.db.MetaDataManager;
import kr.co.bravecompany.modoogong.android.stdapp.db.StatisDataManager;

public class StatisDailyTimeHistoryAdapter extends BaseAdapter {

    List<StatisPacket.DailyTime> dailyTimes = new ArrayList<>();

    public StatisDailyTimeHistoryAdapter(Context context)
    {

    }

    public void clearDatas()
    {
        dailyTimes.clear();
    }

    public void addData( StatisPacket.DailyTime data)
    {
        dailyTimes.add(data);
    }

    public void refresh()
    {
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dailyTimes.size();
    }

    @Override
    public StatisPacket.DailyTime getItem(int position)
    {
        return dailyTimes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return  null;
    }
}
