package kr.co.bravecompany.modoogong.android.stdapp.pageStatis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.co.bravecompany.api.android.stdapp.api.data.Packet;
import kr.co.bravecompany.api.android.stdapp.api.data.StatisPacket;
import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.db.MetaDataManager;

public class StatisSubjectNameAdapter extends BaseAdapter {

    public interface OnSubjectNameSelectedListener
    {
        void onSubjectNameSelected(String name);
    }

    ArrayList<Packet.SubjectData> subjectList = new ArrayList<>();
    Context mContext = null;
    OnSubjectNameSelectedListener mListener;
    LayoutInflater mLayoutInflater = null;

    public StatisSubjectNameAdapter(Context context, OnSubjectNameSelectedListener listener)
    {
        this.mContext = context;
        this.mListener = listener;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void clearData()
    {
        subjectList.clear();
    }

    public void addSubjectData(Packet.SubjectData Data)
    {
        subjectList.add(Data);
    }


    public void refresh()
    {
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return subjectList.size();
    }


    @Override
    public Packet.SubjectData getItem(int position)
    {
        return subjectList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = null;

        if(v == null)
        v = mLayoutInflater.inflate(R.layout.view_statis_subjects_history_item, null);
        Button name = (Button)v.findViewById(R.id.statis_subject_name);
        Packet.SubjectData data = subjectList.get(position);

        name.setText(data.SUBJ_NM);
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Packet.SubjectData clickData = getItem(position);
                mListener.onSubjectNameSelected(clickData.SUBJ_CD);
            }
        });

        return v;
    }
}