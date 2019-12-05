package kr.co.bravecompany.modoogong.android.stdapp.adapter;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import kr.co.bravecompany.api.android.stdapp.api.data.FreeStudyItemVO;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import kr.co.bravecompany.modoogong.android.stdapp.data.FreeLectureData;

/**
 * Created by BraveCompany on 2016. 10. 24..
 */

public class FreeLecturePagerAdapter extends FragmentPagerAdapter {
    private List<FreeLectureData> items = new ArrayList<FreeLectureData>();

    public FreeLecturePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void add(FreeLectureData item){
        this.items.add(item);
        notifyDataSetChanged();
    }

    public void addAll(List<FreeLectureData> items){
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        FreeLectureData item = items.get(position);
        Fragment f = item.getFragment();
        if(!f.isAdded()){
            Bundle b = new Bundle();
            b.putInt(Tags.TAG_CATE, item.getScateVO().getFvodCate());
            b.putString(Tags.TAG_SCATE, item.getScateVO().getFvodSCate());
            ArrayList<FreeStudyItemVO> bestFVods = item.getScateVO().getBestFVod();
            if(bestFVods != null && bestFVods.size() != 0) {
                b.putString(Tags.TAG_BESTFVOD, BraveUtils.toJson(item.getScateVO().getBestFVod().get(0)));
            }
            f.setArguments(b);
        }
        return f;
    }

    @Override
    public int getCount() {
        return items.size();
    }
}
