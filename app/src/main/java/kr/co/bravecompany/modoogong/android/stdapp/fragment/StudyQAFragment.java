package kr.co.bravecompany.modoogong.android.stdapp.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.activity.MainActivity;
import kr.co.bravecompany.modoogong.android.stdapp.adapter.StudyQAPagerAdapter;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.pageCS.DoQAFragment;
import kr.co.bravecompany.modoogong.android.stdapp.pageCS.QAListFragment;
import kr.co.bravecompany.modoogong.android.stdapp.view.CustomViewPager;

/**
 * Created by BraveCompany on 2016. 10. 14..
 */

public class StudyQAFragment extends BaseFragment{

    private TabLayout mTabMain;
    private CustomViewPager mPagerMain;
    private StudyQAPagerAdapter mPagerAdapter;

    private boolean isResumed = false;

    public static StudyQAFragment newInstance() {
        return new StudyQAFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_qa, container, false);
        initLayout(inflater, rootView);
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

    protected void initLayout(LayoutInflater inflater, ViewGroup rootView) {
        mTabMain = (TabLayout)rootView.findViewById(R.id.tabMain);
        mPagerMain = (CustomViewPager)rootView.findViewById(R.id.pagerMain);
        mPagerAdapter = new StudyQAPagerAdapter(getChildFragmentManager());

        mPagerMain.setPagingEnabled(false);
        mPagerMain.setAdapter(mPagerAdapter);
        mTabMain.setupWithViewPager(mPagerMain);

        List<BaseFragment> fragments = new ArrayList<>();
        fragments.add(StudyDoQAFragment.newInstance());
        fragments.add(QAListFragment.newInstance());
        mPagerAdapter.addAll(fragments);

        List<String> titles = Arrays.asList(getResources().getStringArray(R.array.study_qa_title));
        for(int i=0; i<titles.size(); i++){
            View tab = inflater.inflate(R.layout.view_custom_tab, null);
            ((TextView)tab.findViewById(R.id.tabTitle)).setText(titles.get(i));
            mTabMain.getTabAt(i).setCustomView(tab);
        }

        ((MainActivity) getActivity()).setToolbarAuxButton(1);
    }

    public void movePage(int position, String data){
        mTabMain.getTabAt(position).select();
        if(data != null){
            mPagerAdapter.getItem(position).setData(data);
        }
    }

    public int getSelectedPage(){
        return mTabMain.getSelectedTabPosition();
    }

    @Override
    protected void initLayout(ViewGroup rootView) {

    }

    @Override
    protected void initListener() {
        mPagerMain.addOnPageChangeListener(mOnPageChangeListener);
        mTabMain.addOnTabSelectedListener(mOnTabSelectedListener);
    }

    @Override
    protected void initData() {
        mOnTabSelectedListener.onTabSelected(mTabMain.getTabAt(0));
    }

    @Override
    protected void setData(String data) {

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
            switch (position){
                case Tags.QA_PAGE_INDEX.QA_PAGE_Q_DO:
                    ((MainActivity)getActivity()).setVisibilityOkButton(View.VISIBLE);
                    break;
                case Tags.QA_PAGE_INDEX.QA_PAGE_Q_LIST:
                    ((MainActivity)getActivity()).setVisibilityOkButton(View.GONE);
                    break;
            }
            if(isResumed) {
                mPagerAdapter.getItem(position).initData();
                if(position == Tags.QA_PAGE_INDEX.QA_PAGE_Q_LIST){
                    ((DoQAFragment)mPagerAdapter.getItem(Tags.QA_PAGE_INDEX.QA_PAGE_Q_DO)).resetViews();
                }
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageScrollStateChanged(int state) {}
    };

}

