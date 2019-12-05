package kr.co.bravecompany.modoogong.android.stdapp.adapter;

import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.fragment.BaseFragment;

/**
 * Created by BraveCompany on 2016. 10. 26..
 */

public class StudyQAPagerAdapter extends FragmentPagerAdapter {

    private List<BaseFragment> mFragments;

    public StudyQAPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragments = new ArrayList<>();
    }


    public void add(BaseFragment fragment){
        mFragments.add(fragment);
        notifyDataSetChanged();
    }

    public void addAll(List<BaseFragment> fragments){
        mFragments.addAll(fragments);
        notifyDataSetChanged();
    }

    @Override
    public BaseFragment getItem(int position) {
        BaseFragment f = mFragments.get(position);
        if(!f.isAdded()){
            Bundle b = new Bundle();
            b.putInt(Tags.TAG_QA, Tags.QA_TYPE.QA_STUDY);
            f.setArguments(b);
        }
        return f;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
