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

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.adapter.FreeLecturePagerAdapter;
import kr.co.bravecompany.api.android.stdapp.api.data.MainResult;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import kr.co.bravecompany.modoogong.android.stdapp.view.CustomViewPager;
import kr.co.bravecompany.api.android.stdapp.api.data.FreeLectureScateVO;
import kr.co.bravecompany.modoogong.android.stdapp.data.FreeLectureData;

/**
 * Created by BraveCompany on 2016. 10. 14..
 */

public class FreeLectureFragment extends BaseFragment{

    private String cate = null;

    private TabLayout mTabMain;
    private CustomViewPager mPagerMain;
    private FreeLecturePagerAdapter mPagerAdapter;

    private boolean isResumed = false;

    public static FreeLectureFragment newInstance() {
        return new FreeLectureFragment();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            cate = args.getString(Tags.TAG_CATE);
        }
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
        mPagerAdapter = new FreeLecturePagerAdapter(getChildFragmentManager());

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
        if(cate != null) {
            MainResult.FreeLectureCate cate =
                    (MainResult.FreeLectureCate) BraveUtils.toJsonString(this.cate,
                            MainResult.FreeLectureCate.class);
            if (cate != null) {
                ArrayList<FreeLectureData> items = new ArrayList<>();
                ArrayList<FreeLectureScateVO> scates = cate.getFvodScates();
                if (scates != null && scates.size() != 0) {
                    mPagerMain.setOffscreenPageLimit(scates.size());
                    for (int i = 0; i < scates.size(); i++) {
                        FreeLectureData scate = new FreeLectureData();
                        scate.setScateVO(scates.get(i));
                        scate.setFragment(FreeStudyFragment.newInstance());
                        items.add(scate);
                    }
                    mPagerAdapter.addAll(items);

                    for (int i = 0; i < scates.size(); i++) {
                        View tab = LayoutInflater.from(getContext()).inflate(R.layout.view_custom_tab, null);
                        TextView title = (TextView) tab.findViewById(R.id.tabTitle);
                        title.setText(scates.get(i).getFvodCateNm());
                        int size = BraveUtils.convertPxToDp(getContext(), getResources().getDimension(R.dimen.text_small));
                        title.setTextSize(size);
                        mTabMain.getTabAt(i).setCustomView(tab);
                    }

                    mOnTabSelectedListener.onTabSelected(mTabMain.getTabAt(0));
                }
            }
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
                }
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageScrollStateChanged(int state) {}
    };

}
