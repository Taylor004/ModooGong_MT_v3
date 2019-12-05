package kr.co.bravecompany.modoogong.android.stdapp.pageMyPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kr.co.bravecompany.api.android.stdapp.api.data.StatisPacket;
import kr.co.bravecompany.modoogong.android.stdapp.R;

import static android.view.View.GONE;

public class MyPageMenuListAdapter extends BaseAdapter {

    List<MyPageFragment.MenuItem> menuItemList = new ArrayList<>();

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;

    public MyPageMenuListAdapter(Context context)
    {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void refreshMenus(List<MyPageFragment.MenuItem> menuList)
    {
       // menuItemList = menuList;
        menuItemList.clear();
        for(MyPageFragment.MenuItem item : menuList)
        {
            if(!item.top)
                menuItemList.add(item);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return menuItemList.size();
    }

    @Override
    public MyPageFragment.MenuItem getItem(int position)
    {
        return menuItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if(view ==null)
            view = mLayoutInflater.inflate(R.layout.view_mypage_menu_item, null);

        TextView name = (TextView) view.findViewById(R.id.menuItemName);
        TextView badge = (TextView) view.findViewById(R.id.menuItemAlarmTextView);

        MyPageFragment.MenuItem item = getItem(position);
        name.setText(item.title);
        badge.setText( String.valueOf(item.badge));

        if(item.badge ==0)
            badge.setVisibility(GONE);
        else
            badge.setVisibility(View.VISIBLE);

        return view;
    }
}
