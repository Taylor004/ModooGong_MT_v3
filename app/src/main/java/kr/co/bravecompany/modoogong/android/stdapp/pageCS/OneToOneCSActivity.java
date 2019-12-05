package kr.co.bravecompany.modoogong.android.stdapp.pageCS;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.activity.BaseActivity;
import kr.co.bravecompany.modoogong.android.stdapp.adapter.OneToOneQAPagerAdapter;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.fragment.BaseFragment;
import kr.co.bravecompany.modoogong.android.stdapp.view.CustomViewPager;

public class OneToOneCSActivity extends BaseActivity {

    private TabLayout mTabMain;
    private CustomViewPager mPagerMain;
    private OneToOneQAPagerAdapter mPagerAdapter;

    private boolean isResumed = false;

    private FragmentManager fragmentManager = getSupportFragmentManager(); // 하단 메뉴에서 각 프래그먼트로 화면 전환을 하기 위한 프래그먼트 매니져.[2019.11.06 테일러]

    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cs_page);
        setSystemBar(true);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        initLayout();
        initListener();
        initData();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    protected void initLayout()
    {
        LayoutInflater inflater = getLayoutInflater();

        mTabMain = (TabLayout)findViewById(R.id.tabMain);
        mPagerMain = (CustomViewPager)findViewById(R.id.pagerMain);
        mPagerAdapter = new OneToOneQAPagerAdapter(fragmentManager);

        mPagerMain.setPagingEnabled(false);
        mPagerMain.setAdapter(mPagerAdapter);
        mTabMain.setupWithViewPager(mPagerMain);

        List<BaseFragment> fragments = new ArrayList<>();
        fragments.add(DoQAFragment.newInstance());
        fragments.add(QAListFragment.newInstance());
        mPagerAdapter.addAll(fragments);

        List<String> titles = Arrays.asList(getResources().getStringArray(R.array.one_to_one_qa_title));
        for(int i=0; i<titles.size(); i++){
            View tab = inflater.inflate(R.layout.view_custom_tab, null);
            ((TextView)tab.findViewById(R.id.tabTitle)).setText(titles.get(i));
            mTabMain.getTabAt(i).setCustomView(tab);
        }
    }

    public void movePage(int position, String data){
        mTabMain.getTabAt(position).select();

//        if(data != null){
//            mPagerAdapter.getItem(position).setData(data);
//        }
    }

    public int getSelectedPage(){
        return mTabMain.getSelectedTabPosition();
    }

    protected void initLayout(ViewGroup rootView) {

    }

    protected void initListener() {
        mPagerMain.addOnPageChangeListener(mOnPageChangeListener);
        mTabMain.addOnTabSelectedListener(mOnTabSelectedListener);
    }

    protected void initData() {
        mOnTabSelectedListener.onTabSelected(mTabMain.getTabAt(0));
    }

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

            switch (position) {
                case Tags.QA_PAGE_INDEX.QA_PAGE_Q_DO:
                    // ((MainActivity)getActivity()).setVisibilityOkButton(View.VISIBLE);
                    break;
                case Tags.QA_PAGE_INDEX.QA_PAGE_Q_LIST:
                    // ((MainActivity)getActivity()).setVisibilityOkButton(View.GONE);
                    break;
            }

            /*
            switch (position){
                case Tags.QA_PAGE_INDEX.QA_PAGE_Q_DO:
                   // ((MainActivity)getActivity()).setVisibilityOkButton(View.VISIBLE);
                    break;
                case Tags.QA_PAGE_INDEX.QA_PAGE_Q_LIST:
                   // ((MainActivity)getActivity()).setVisibilityOkButton(View.GONE);
                    break;
            }

            if(isResumed) {
                mPagerAdapter.getItem(position).initData();
                if(position == Tags.QA_PAGE_INDEX.QA_PAGE_Q_LIST){
                    ((DoQAFragment)mPagerAdapter.getItem(Tags.QA_PAGE_INDEX.QA_PAGE_Q_DO)).resetViews();
                }
            }
            */
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageScrollStateChanged(int state) {}
    };
}
