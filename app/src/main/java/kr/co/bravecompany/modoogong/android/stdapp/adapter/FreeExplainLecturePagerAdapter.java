package kr.co.bravecompany.modoogong.android.stdapp.adapter;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.data.FreeExplainLectureData;

/**
 * Created by BraveCompany on 2016. 12. 16..
 */

public class FreeExplainLecturePagerAdapter extends FragmentPagerAdapter {
    private List<FreeExplainLectureData> items = new ArrayList<FreeExplainLectureData>();

    public FreeExplainLecturePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void add(FreeExplainLectureData item){
        this.items.add(item);
        notifyDataSetChanged();
    }

    public void addAll(List<FreeExplainLectureData> items){
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        FreeExplainLectureData item = items.get(position);
        Fragment f = item.getFragment();
        if(!f.isAdded()){
            Bundle b = new Bundle();
            b.putInt(Tags.TAG_EXAMCLASS, item.getExamClass());
            f.setArguments(b);
        }
        return f;
    }

    @Override
    public int getCount() {
        return items.size();
    }
}

