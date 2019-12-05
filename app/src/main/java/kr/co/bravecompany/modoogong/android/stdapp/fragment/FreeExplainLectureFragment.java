package kr.co.bravecompany.modoogong.android.stdapp.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.adapter.FreeExplainLecturePagerAdapter;
import kr.co.bravecompany.modoogong.android.stdapp.view.CustomViewPager;
import kr.co.bravecompany.modoogong.android.stdapp.data.FreeExplainLectureData;

/**
 * Created by BraveCompany on 2016. 12. 16..
 */

public class FreeExplainLectureFragment extends BaseFragment{
    
    private TabLayout mTabMain;
    private CustomViewPager mPagerMain;
    private FreeExplainLecturePagerAdapter mPagerAdapter;

    private boolean isResumed = false;

    public static FreeExplainLectureFragment newInstance() {
        return new FreeExplainLectureFragment();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_free_lecture, container, false);
        initLayout(rootView);
        initListener();
        initData();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        isResumed = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isResumed = false;
    }

    @Override
    protected void initLayout(ViewGroup rootView) {
        mTabMain = (TabLayout)rootView.findViewById(R.id.tabMain);
        mPagerMain = (CustomViewPager)rootView.findViewById(R.id.pagerMain);
        mPagerAdapter = new FreeExplainLecturePagerAdapter(getChildFragmentManager());

        mPagerMain.setPagingEnabled(true);
        mPagerMain.setAdapter(mPagerAdapter);
        mTabMain.setupWithViewPager(mPagerMain);
    }

    @Override
    protected void initListener() {
        mPagerMain.addOnPageChangeListener(mOnPageChangeListener);
        mTabMain.addOnTabSelectedListener(mOnTabSelectedListener);
    }

    @Override
    protected void setData(String data) {

    }

    @Override
    protected void initData(){
        ArrayList<String> explains = new ArrayList<>();
        explains.addAll(Arrays.asList(getResources().getStringArray(R.array.free_explain)));

        ArrayList<FreeExplainLectureData> items = new ArrayList<>();
        if(explains.size() != 0){
            mPagerMain.setOffscreenPageLimit(explains.size());
            for(int i=explains.size(); i>0; i--){
                FreeExplainLectureData explain = new FreeExplainLectureData();
                explain.setExamClass(i);
                explain.setFragment(FreeExplainStudyFragment.newInstance());
                items.add(explain);
            }
            mPagerAdapter.addAll(items);

            for(int i=0; i<explains.size(); i++){
                View tab = LayoutInflater.from(getContext()).inflate(R.layout.view_custom_tab, null);
                TextView title = (TextView) tab.findViewById(R.id.tabTitle);
                title.setText(explains.get(i));
                mTabMain.getTabAt(i).setCustomView(tab);
            }

            mOnTabSelectedListener.onTabSelected(mTabMain.getTabAt(0));
        }
    }

    private TabLayout.OnTabSelectedListener mOnTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            if(tab.getCustomView() != null){
                ((TextView)tab.getCustomView().findViewById(R.id.tabTitle)).setTypeface(Typeface.DEFAULT_BOLD);
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            if(tab.getCustomView() != null){
                ((TextView)tab.getCustomView().findViewById(R.id.tabTitle)).setTypeface(Typeface.DEFAULT);
            }
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            if(isResumed) {
                Fragment select = mPagerAdapter.getItem(position);
                if(select instanceof BaseFragment){
                    //((BaseFragment)select).initData();
                    //((BaseFragment)select).setData(null);
                }
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageScrollStateChanged(int state) {}
    };
}
