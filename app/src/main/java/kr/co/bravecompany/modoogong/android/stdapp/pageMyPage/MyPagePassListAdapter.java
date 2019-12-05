package kr.co.bravecompany.modoogong.android.stdapp.pageMyPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import kr.co.bravecompany.api.android.stdapp.api.data.Packet;
import kr.co.bravecompany.api.android.stdapp.api.data.StatisPacket;
import kr.co.bravecompany.modoogong.android.stdapp.R;

public class MyPagePassListAdapter extends BaseAdapter {

    interface OnPassItemSelectListener
    {
        void onPassItemSelected(Packet.PassData item);
    }

    List<Packet.PassData> passItemList =new ArrayList<>();
    OnPassItemSelectListener mItemlistener;

    LayoutInflater mLayoutInflater = null;
    Context mContext;


    public MyPagePassListAdapter(Context context,  OnPassItemSelectListener listener )
    {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mItemlistener = listener;
    }

    public void refreshPassList(List<Packet.PassData> list)
    {
        passItemList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return passItemList.size();
    }

    @Override
    public Packet.PassData getItem(int position)
    {
        return passItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if(view ==null)
            view = mLayoutInflater.inflate(R.layout.view_mypage_mypass_item, null);

        TextView passName = (TextView) view.findViewById(R.id.passName);
        TextView subscribeDate = (TextView) view.findViewById(R.id.lectureDateName);
        TextView remainedDate = (TextView) view.findViewById(R.id.reminedLectureDayName);

        Button extendButton = (Button) view.findViewById(R.id.extendButton);

        Packet.PassData passData = getItem(position);
        passName.setText(passData.SAL_NM);
        subscribeDate.setText(String.format("%s ~ %s", passData.GRD_SDT,passData.GRD_EDT));
        remainedDate.setText(String.format("%s일", passData.ING_DAYS));

        //extendButton.setOnClickListener(new PassItemClickListener(getItem(position), mItemlistener));

        extendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemlistener.onPassItemSelected(getItem(position));
            }
        });

        return  view;
    }

    /*
    public class PassItemClickListener implements View.OnClickListener
    {
        Packet.PassData mPassData;
        OnPassItemSelectListener mListener;

        public PassItemClickListener(Packet.PassData passData, OnPassItemSelectListener listener)
        {
            mPassData = passData;
            mListener = listener;
        }

        @Override
        public void onClick(View v) {
            mItemlistener.onPassItemSelected(mPassData);
        }
    }
    */
}
