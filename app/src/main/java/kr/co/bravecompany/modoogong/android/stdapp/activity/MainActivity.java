package kr.co.bravecompany.modoogong.android.stdapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import kr.co.bravecompany.api.android.stdapp.api.data.ShopPacket;
import kr.co.bravecompany.api.android.stdapp.api.data.StatisPacket;
import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.api.android.stdapp.api.OnResultListener;
import kr.co.bravecompany.api.android.stdapp.api.config.APIConfig;
import kr.co.bravecompany.api.android.stdapp.api.requests.StudyRequests;
import kr.co.bravecompany.api.android.stdapp.api.data.LectureItemVO;
import kr.co.bravecompany.api.android.stdapp.api.data.LectureResult;
import kr.co.bravecompany.api.android.stdapp.api.data.MainResult;
import kr.co.bravecompany.api.android.stdapp.api.data.NoticeItemVO;
import kr.co.bravecompany.modoogong.android.stdapp.application.MyFirebaseMessagingService;
import kr.co.bravecompany.modoogong.android.stdapp.config.AnalysisTags;
import kr.co.bravecompany.modoogong.android.stdapp.config.RequestCode;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.db.BaseDataManager;
import kr.co.bravecompany.modoogong.android.stdapp.db.BcDateUtils;
import kr.co.bravecompany.modoogong.android.stdapp.db.MetaDataManager;
import kr.co.bravecompany.modoogong.android.stdapp.db.ShopDataManager;
import kr.co.bravecompany.modoogong.android.stdapp.db.StatisDataManager;
import kr.co.bravecompany.modoogong.android.stdapp.download.DownloadService;
import kr.co.bravecompany.modoogong.android.stdapp.download.OnDownloadDataListener;
import kr.co.bravecompany.modoogong.android.stdapp.download.OnDownloadBindListener;
import kr.co.bravecompany.modoogong.android.stdapp.fragment.BookSalesFragment;
import kr.co.bravecompany.modoogong.android.stdapp.fragment.NoLoginFragment;
import kr.co.bravecompany.modoogong.android.stdapp.fragment.NoNetworkFragment;
import kr.co.bravecompany.modoogong.android.stdapp.pageProfile.ProfileExamInfoRecyclerAdapter;
import kr.co.bravecompany.modoogong.android.stdapp.pageSetting.SettingActivity;
import kr.co.bravecompany.modoogong.android.stdapp.pageStatis.StatisFragment;
import kr.co.bravecompany.modoogong.android.stdapp.pageLocalLecture.LocalLectureFragment;
import kr.co.bravecompany.modoogong.android.stdapp.pageMyPage.MyPageFragment;
import kr.co.bravecompany.modoogong.android.stdapp.pageLectureList.LectureFragment;
import kr.co.bravecompany.modoogong.android.stdapp.fragment.OneToOneQAFragment;
import kr.co.bravecompany.modoogong.android.stdapp.manager.PropertyManager;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BackPressedHandler;
import kr.co.bravecompany.modoogong.android.stdapp.utils.PhotoSelector;
import kr.co.bravecompany.modoogong.android.stdapp.utils.SystemUtils;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import kr.co.bravecompany.modoogong.android.stdapp.utils.UIUtils;
import okhttp3.Request;

public class MainActivity extends DownBaseActivity {

    //! Home Activity에서 발생한 이벤트를 받기 위한 장치
    public interface OnHomeActivityEventListener
    {
        boolean onHomeBackPressed();
        void onHomeTitleBarPressed();
        void onHomeMenuLecRegClicked();
    }

    public enum PageId {
        MAIN_BOOKSTORE,
        MAIN_STATIS,
        MAIN_DOWNLOAD_LECTURE,
        MAIN_MYPAGES,
        MAIN_MY_LECTURE,
        AUX_NO_LOGIN,
        AUX_NO_NETWORK
    }

    public static class ChildPage
    {
        public PageId pageId;
        public String title;
        public int resMenuId;
        public Fragment childView;
        public boolean mustLogin;
        public boolean mustNetwork;

        public View badgeView;
    }

    public static class ToolbarLayout
    {
        private Toolbar mToolbar;
        private TextView btnOk;
        //private TextView btnLecReg;
    }

    public static class NoLoginLayout
    {
        public LinearLayout layout;
        public TextView btnLogin;
    }

    public static class LoginLayout
    {
        public LinearLayout layout;

        private TextView txtName;
        public CircleImageView mProfileImageView;
        public Button mProfileEditorButton;
        public TextView txtTodayMessage; //UIUtils 클래스에서 사용하기 위해서 static으로 선언.[2019.10.03 테일러]
        public Button mTodayMessageButton; //UIUtils 클래스에서 사용하기 위해서 static으로 선언.
        public View mViewTodayMessageDivider; //UIUtils 클래스에서 사용하기 위해서 static으로 선언.
    }

    public static class RefreshLayout
    {
        public LinearLayout layout;
        public ImageView btnMenuRefresh;
    }

    public static class ExtraLayout
    {
        public ViewGroup layout;
        //private FrameLayout btnSetting;
        private TextView mCustomCenterTextView; // 좌측 슬라이드 메뉴의 고객센터 TextView 선언[2019.11.06 테일러]
        private TextView mSetUpTextView; // 좌측 슬라이드 메뉴의 설정 TextView 선언[2019.11.06 테일러]
    }

    public static class OnAirLayout // 온에어 클래스
    {
        public ViewGroup layout; // 온에어 레이아웃
        public TextView activeUsers; // 활동중인 사용자들 텍스트뷰
    }

    public static class TodayRankLayout // 오늘의 순공시간 랭크 클래스
    {
        public ViewGroup layout; // 오늘의 순공시간 레이아웃
        public TextView todayRank; // 오늘의 랭크
        public TextView rankDelta; // 랭크 델타
        public TextView todayTime; // 오늘 수강한 총 시간

        public ImageView rankUpMark; // 랭크 델타에 따른 이미지뷰
        public ImageView rankDownMark; // 랭크 델타에 따른 이미지뷰
    }

    public static class ExamInfoLayout // 시험 정보 클래스
    {
        public LinearLayout layout; // 시험 정보 레이아웃
        public TextView dueDate; // 오늘의 날짜 텍스트 뷰
        public TextView examName; // 시험 이름 텍스트 뷰
        public TextView d_day; // 시험 디데이 텍스트뷰
        public SliderView examInfoSliderView; // 시험 정보 슬라이드 뷰
        public ProfileExamInfoRecyclerAdapter examInfosliderViewAdapter; //시험 정보 슬라이드 어뎁터
    }

    private DrawerLayout mDrawer;
    private NavigationView mNavigationView;

    private ToolbarLayout mToolbarLayout = new ToolbarLayout();
    private LoginLayout mLoginLayout = new LoginLayout();
    private NoLoginLayout mNoLoginLayout = new NoLoginLayout();
    private RefreshLayout mRefreshLayout = new RefreshLayout();
    private ExtraLayout mExtraLayout = new ExtraLayout();

    private OnAirLayout mOnAirLayout        = new OnAirLayout();
    private TodayRankLayout mTodayRankLayout = new TodayRankLayout();
    private ExamInfoLayout mExamInfoLayout  = new ExamInfoLayout();


    //! 구버전 네비게이션
//    ArrayList<Fragment> mMenus;
//    ArrayList<String> mTitles;
//    private RecyclerView mListView;
//    private MenuAdapter mAdapter;
//    private LinearLayoutManager mLayoutManager;

    //Fragment Pages
    private FragmentManager fragmentManager = getSupportFragmentManager(); // 하단 메뉴에서 각 프래그먼트로 화면 전환을 하기 위한 프래그먼트 매니져.[2019.11.06 테일러]

    //! 신 버전 네비게이션
    private BottomNavigationView bottomNavigationView; // 하단 메뉴를 사용하기 위해서 선언[2019.11.06 테일러]
    private ArrayList<ChildPage> mBottomNaviPages;
    private ChildPage mActivePage;

    private MainResult mMainResult = null;
    private LectureResult mLectureResult = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = new Intent(this, DownloadService.class);
        startService(intent);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initLayout();
        initExamInfoLayout();
        initLoginLayout();
        initNoLoginLayout();

        initExtraMenuLayout();

        initNaviMenus();

        initInfoLayout();

        startDataLoadingProcess();

        openMenuPage(R.id.navigation_my_lecture);


        MetaDataManager.getInstance().initMetaData();

        //for test
        StatisDataManager.getInstance().DoTest();
        ShopDataManager.getInstance().DoTest();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Intent intent = new Intent(this, DownloadService.class);
        stopService(intent);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        startLiveStatus();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        stopLiveStatus();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        mPhotoSelector.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if(requestCode == RequestCode.REQUEST_LOGIN){
                boolean isNewId = data.getBooleanExtra(Tags.TAG_NEW_ID, false);
                if(isNewId) {
                    startLoading();
                    mDownloadManager.removeDownloadAll(new OnDownloadDataListener<String>() {
                        @Override
                        public void onDataProgress(int progress) {
                        }

                        @Override
                        public void onDataComplete(String result) {
                            stopLoading();
                            PropertyManager.getInstance().removeSettings();
                            refreshMenuPage();
                        }

                        @Override
                        public void onDataError(int error) {
                            stopLoading();
                            BraveUtils.showLoadOnFailToast(MainActivity.this);
                        }
                    });
                }else{
                    refreshMenuPage();
                }
            }
            else
            {
                List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
                if (fragmentList != null)
                {
                    for(Fragment fragment : fragmentList){
                        fragment.onActivityResult(requestCode, resultCode, data);
                    }
                }
            }
        }
    }

    void initNaviMenus()
    {

        bottomNavigationView = findViewById(R.id.bottom_navigation_view); // BottomNavigationView View 객체화

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                openMenuPage(menuItem.getItemId());
                return true;
            }
        });

        mBottomNaviPages = new ArrayList<>();

        ChildPage frag =null;

        frag = new ChildPage();
        frag.pageId = PageId.MAIN_STATIS;
        frag.resMenuId = R.id.navigation_lecture_record;
        frag.childView = StatisFragment.newInstance();
        frag.mustLogin = true;
        frag.mustNetwork = true;
        frag.title = "학습리포트";
        frag.badgeView = addBadgeView(0);
        mBottomNaviPages.add(frag);


        frag = new ChildPage();
        frag.pageId = PageId.MAIN_DOWNLOAD_LECTURE;
        frag.resMenuId = R.id.navigation_download;
        frag.mustLogin = false;
        frag.mustNetwork = false;
        frag.title = "다운로드함";
        frag.childView = LocalLectureFragment.newInstance();
        frag.badgeView = addBadgeView(1);
        mBottomNaviPages.add(frag);


        frag = new ChildPage();
        frag.pageId = PageId.MAIN_MY_LECTURE;
        frag.resMenuId = R.id.navigation_my_lecture;
        frag.title = "내강의실";
        frag.mustLogin = true;
        frag.mustNetwork = true;
        frag.childView = LectureFragment.newInstance();
        frag.badgeView = addBadgeView(2);
        mBottomNaviPages.add(frag);


        frag = new ChildPage();
        frag.pageId = PageId.MAIN_BOOKSTORE;
        frag.resMenuId =R.id.navigation_bravebooks;
        frag.childView = BookSalesFragment.newInstance();
        frag.mustLogin = true;
        frag.mustNetwork = true;
        frag.title = "교재판매";
        frag.badgeView = addBadgeView(3);
        mBottomNaviPages.add(frag);

        frag = new ChildPage();
        frag.pageId = PageId.MAIN_MYPAGES;
        frag.resMenuId = R.id.navigation_mypage;
        frag.childView = MyPageFragment.newInstance();
        frag.mustLogin = true;
        frag.mustNetwork = true;
        frag.title = "마이페이지";
        frag.badgeView = addBadgeView(4);
        mBottomNaviPages.add(frag);

        frag = new ChildPage();
        frag.pageId = PageId.AUX_NO_LOGIN;
        frag.resMenuId = 0;//R.id.navigation_mypage;
        frag.childView = NoLoginFragment.newInstance();
        frag.title = "로그인 필요";
        mBottomNaviPages.add(frag);

        frag = new ChildPage();
        frag.pageId = PageId.AUX_NO_NETWORK;
        frag.resMenuId =0;// R.id.navigation_mypage;
        frag.childView = NoNetworkFragment.newInstance();
        frag.title = "네트웍 연결 필요";
        mBottomNaviPages.add(frag);

    }

    private View addBadgeView(int index)
    {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(index);

        View notificationBadge = LayoutInflater.from(this).inflate(R.layout.view_notification_badge, menuView, false);

        itemView.addView(notificationBadge);
        notificationBadge.setVisibility( View.GONE);

        return notificationBadge;
    }

    private void refreshMenuBadge(PageId pageId, boolean on)
    {
        ChildPage page= findPage(pageId);
        page.badgeView.setVisibility(on? View.VISIBLE : View.GONE);
    }

    @Override
    protected void initLayout()
    {
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setItemIconTintList(null);

        mToolbarLayout = new ToolbarLayout();
        mToolbarLayout.mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbarLayout.mToolbar.setOnClickListener(mToolbarClickListener);
        setSupportActionBar(mToolbarLayout.mToolbar);
        getSupportActionBar().setTitle("");


        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbarLayout.mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();


        mToolbarLayout.mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TOOLBARCLICK","메뉴열림...");
                //hide keyboard
                SystemUtils.hideSoftKeyboard(MainActivity.this);

                mDrawer.openDrawer(GravityCompat.START);
            }
        });


        mToolbarLayout.btnOk = (TextView) mToolbarLayout.mToolbar.findViewById(R.id.btnOk);
        mToolbarLayout.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnOkBtnClickListener.onClick(v);
            }
        });


//        mToolbarLayout.btnLecReg.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                onLegRegClicked(v);
//            }
//        });

    }

    protected void initNoLoginLayout()
    {
        //mNoLoginLayout = new NoLoginLayout();

        mNoLoginLayout.layout = (LinearLayout)mNavigationView.findViewById(R.id.layoutNoLogin);
        mNoLoginLayout.btnLogin = (TextView)mNavigationView.findViewById(R.id.btnLogin);


        mRefreshLayout =new RefreshLayout();
        mRefreshLayout.layout = (LinearLayout)mNavigationView.findViewById(R.id.layoutMenuRefresh);
        mRefreshLayout.btnMenuRefresh = (ImageView)mNavigationView.findViewById(R.id.btnMenuRefresh);
        mRefreshLayout.btnMenuRefresh.setActivated(false);

        mNoLoginLayout.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BraveUtils.goLogin(MainActivity.this);
                closeDrawer();

                Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.MAIN)
                        .putCustomAttribute(AnalysisTags.ACTION, "go_login"));
            }
        });

        mRefreshLayout.btnMenuRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRefreshLayout.btnMenuRefresh.setActivated(true);
                refreshMenuPage();

                Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.MAIN)
                        .putCustomAttribute(AnalysisTags.ACTION, "refresh_menu"));
            }
        });
    }


    protected  void initLoginLayout()
    {
       // mLoginLayout = new LoginLayout();
        mLoginLayout.layout = (LinearLayout) mNavigationView.findViewById(R.id.layoutLogin);

        mLoginLayout.txtName = (TextView) mNavigationView.findViewById(R.id.txtName);
        mLoginLayout.txtTodayMessage = (TextView) mNavigationView.findViewById(R.id.todayMessage);
        mLoginLayout.mProfileImageView = (CircleImageView) mNavigationView.findViewById(R.id.profile_image);
        mLoginLayout.mProfileEditorButton = (Button) mNavigationView.findViewById(R.id.profile_image_editor);
        mLoginLayout.mTodayMessageButton = (Button) mNavigationView.findViewById(R.id.profile_todayMessage_image_editor); // 오늘의 메시지 펜 버튼
        mLoginLayout.mViewTodayMessageDivider = (View) mNavigationView.findViewById(R.id.todayMessageDividerID); // 오늘의 메시지 구분선

        mLoginLayout.txtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doEditTodayMessage();
            }
        });

        //펜 버튼 누르면 오늘의 메시지 수정할수있게 설정
        mLoginLayout.mTodayMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doEditTodayMessage();
            }
        });

        mLoginLayout.txtTodayMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doEditTodayMessage();
            }
        });


        mLoginLayout.mProfileEditorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doEditProfile();
            }
        });

        mLoginLayout.mProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doEditProfile();
            }
        });
    }


    protected void initExamInfoLayout()
    {
        // 시험 정보 레이아웃 초기화
        mExamInfoLayout.layout = (LinearLayout) mNavigationView.findViewById(R.id.layout_exam_info);
        mExamInfoLayout.dueDate = (TextView) mNavigationView.findViewById(R.id.date_TextView);
        mExamInfoLayout.examName = (TextView) mNavigationView.findViewById(R.id.exam_Name_TextView);
        mExamInfoLayout.d_day = (TextView) mNavigationView.findViewById(R.id.exam_Ddays_TextView);
        mExamInfoLayout.examInfoSliderView = (SliderView) mNavigationView.findViewById(R.id.SliderView);
        mExamInfoLayout.examInfosliderViewAdapter = new ProfileExamInfoRecyclerAdapter(this);
        mExamInfoLayout.examInfoSliderView.setSliderAdapter(mExamInfoLayout.examInfosliderViewAdapter);
        mExamInfoLayout.examInfoSliderView.setIndicatorAnimation(IndicatorAnimations.WORM);
        mExamInfoLayout.examInfoSliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        mExamInfoLayout.examInfoSliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        mExamInfoLayout.examInfoSliderView.setIndicatorSelectedColor(Color.BLACK);
        mExamInfoLayout.examInfoSliderView.setIndicatorUnselectedColor(Color.GRAY);
        mExamInfoLayout.examInfoSliderView.setScrollTimeInSec(4);
        mExamInfoLayout.examInfoSliderView.startAutoCycle();
    }


    protected void initInfoLayout()
    {
        // 온에어 레이아웃 초기화
        mOnAirLayout.layout = (ViewGroup) mNavigationView.findViewById(R.id.content_main_onair_layout);
        mOnAirLayout.activeUsers =  mOnAirLayout.layout.findViewById(R.id.main_onair_activeusers);

        // 오늘의 순공시간 랭크 레이아웃 초기화
        mTodayRankLayout.layout = (ViewGroup) mNavigationView.findViewById(R.id.content_main_todayrank_layout);
        mTodayRankLayout.todayTime = (TextView) mTodayRankLayout.layout.findViewById(R.id.todayrank_current_time);
        mTodayRankLayout.todayRank = (TextView) mTodayRankLayout.layout.findViewById(R.id.todayrank_rank_text);
        mTodayRankLayout.rankUpMark = (ImageView) mTodayRankLayout.layout.findViewById(R.id.todayrank_up_mark);
        mTodayRankLayout.rankDownMark = (ImageView) mTodayRankLayout.layout.findViewById(R.id.todayrank_down_mark);
        mTodayRankLayout.rankDelta = (TextView) mTodayRankLayout.layout.findViewById(R.id.main_todayrank_updown_text);

    }

    protected void initExtraMenuLayout()
    {
      //  mExtraLayout = new ExtraLayout();
        mExtraLayout.layout = (ViewGroup) mNavigationView.findViewById(R.id.content_main_extra_menu);
        //mExtraLayout.btnSetting = (FrameLayout)mNavigationView.findViewById(R.id.btnSetting);
        mExtraLayout.mCustomCenterTextView = (TextView) mNavigationView.findViewById(R.id.customerCenterView); // 고객센터 텍스트 뷰 객체화.[2019.11.06 테일러]
        mExtraLayout.mSetUpTextView = (TextView) mNavigationView.findViewById(R.id.setUpView); // 설정 텍스트 뷰 객체화. [2019.11.06 테일러]

//        mExtraLayout.btnSetting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(BraveUtils.checkUserInfo()) {
//                    startActivity(new Intent(MainActivity.this, SettingActivity.class));
//                    closeDrawer();
//
//                    Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.MAIN)
//                            .putCustomAttribute(AnalysisTags.ACTION, "go_setting"));
//                }else{
//                    BraveUtils.showToast(MainActivity.this, getString(R.string.toast_no_login));
//                }
//            }
//        });

        // 슈퍼 수강앱 고객센터 텍스트 클릭시 2019.11.01 [테일러]
        mExtraLayout.mCustomCenterTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BraveUtils.checkUserInfo())
                {
                    //startActivity(new Intent(MainActivity.this, QADetailActivity.class));
                    closeDrawer();
                    Fragment fragment = new OneToOneQAFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,fragment).commitNowAllowingStateLoss();
                }
                else
                {
                    BraveUtils.showToast(MainActivity.this,getString(R.string.toast_no_login));
                }
            }
        });

        // 슈퍼 수강앱 환경설정 텍스트 클릭시 2019.11.01 [테일러]
        mExtraLayout.mSetUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BraveUtils.checkUserInfo()) {
                    startActivity(new Intent(MainActivity.this, SettingActivity.class));
                    closeDrawer();

                    Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.MAIN)
                            .putCustomAttribute(AnalysisTags.ACTION, "go_setting"));
                }else{
                    BraveUtils.showToast(MainActivity.this, getString(R.string.toast_no_login));
                }
            }
        });
    }

    /*
    protected void initLegacyMenuLayout()
    {
        mAdapter = new MenuAdapter();
        mListView = (RecyclerView)mNavigationView.findViewById(R.id.recyclerMenu);
        mListView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(getApplicationContext()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mListView.setLayoutManager(mLayoutManager);

        mAdapter.setOnItemClickListener(new OnItemClickListener(){

            @Override
            public void onItemClick(View view, int position) {
                goMenuItemSelected(position);
            }
        });
    }
    */

    @Override
    protected void initListener() {}


    /****************************************************************
     *
     * Manage Page
     *
     ****************************************************************/

    void openMenuPage(int resMenuId)
    {
        for (ChildPage page : mBottomNaviPages)
            if (page.resMenuId == resMenuId)
                openMenuPage(page.pageId);
    }

    ChildPage findPage(PageId menuId)
    {
        for (ChildPage page : mBottomNaviPages)
            if (page.pageId == menuId) {
                return page;
            }

        return  null;
    }

    public void openMenuPage(PageId menuId) {

        FragmentTransaction transaction1 = fragmentManager.beginTransaction(); // 화면을 전환할 프래그먼트트렌젝션 선언 및 초기화

        ChildPage page = findPage(menuId);

        if(page !=null) {

           mActivePage = page;
           setToolbarTitle(page.title);

           if (page.mustNetwork && !checkNetworkStatus()) {
               ChildPage noNetworkPage = findPage(PageId.AUX_NO_NETWORK);
               transaction1.replace(R.id.frame_layout, noNetworkPage.childView).commitAllowingStateLoss();
           } else if (page.mustLogin && !checkLoginStatus()) {
               ChildPage noLogin = findPage(PageId.AUX_NO_LOGIN);
               transaction1.replace(R.id.frame_layout, noLogin.childView).commitAllowingStateLoss();
           } else
               transaction1.replace(R.id.frame_layout, page.childView).commitAllowingStateLoss();
        }

        mActivePage = null;
    }

    public void refreshMenuPage()
    {
        if(mActivePage !=null)
            openMenuPage(mActivePage.pageId);
    }

    boolean checkNetworkStatus()
    {
        return SystemUtils.isNetworkConnected(getApplicationContext());
    }

    boolean checkLoginStatus()
    {
        return BraveUtils.checkUserInfo();
    }

    /****************************************************************
     *
     * Data Loading Process
     *
     ****************************************************************/

    void startDataLoadingProcess()
    {
        if (mDownloadManager != null) {
            mDownloadManager.setOnDownloadBindListener(new OnDownloadBindListener() {
                @Override
                public void onBindComplete() {
                    boolean taskRemoved = PropertyManager.getInstance().isTaskRemoved();
                    if(taskRemoved) {

                        startLoading();

                        mDownloadManager.forceFinishDownload(true, new OnDownloadDataListener<String>() {
                            @Override
                            public void onDataProgress(int progress) {
                            }

                            @Override
                            public void onDataComplete(String result) {
                                stopLoading();
                                PropertyManager.getInstance().setTaskRemoved(false);
                                nextDataLoadingProcess();

                                //initLayout();
                                //nitLoginLayout();
                                //initListener();
                                //initData();
                            }

                            @Override
                            public void onDataError(int error) {
                                stopLoading();
                                BraveUtils.showLoadOnFailToast(MainActivity.this);
                            }
                        });
                    }else{
                        nextDataLoadingProcess();
                        //initLayout();
                        //initLoginLayout();
                        //initListener();
                        //initData();
                    }
                }
            });
        }
    }

    void nextDataLoadingProcess()
    {
        if(mDownloadManager != null) {
            boolean isNewId = getIntent().getBooleanExtra(Tags.TAG_NEW_ID, false);
            if(isNewId) {
                startLoading();
                mDownloadManager.removeDownloadAll(new OnDownloadDataListener<String>() {
                    @Override
                    public void onDataProgress(int progress) {
                    }

                    @Override
                    public void onDataComplete(String result) {
                        stopLoading();
                        PropertyManager.getInstance().removeSettings();
                        loadData();
                    }

                    @Override
                    public void onDataError(int error) {
                        stopLoading();
                        BraveUtils.showLoadOnFailToast(MainActivity.this);
                    }
                });
            }else {
                loadData();
            }
        }
    }

//
//    private void initData(){
//        loadData();
//    }

    /* 레거시 메뉴 체계
    private void initNavigationMenu(){
        ArrayList<MenuData> menus = new ArrayList<MenuData>();
        ArrayList<String> menu_icon = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.menu_icon)));

        if(ModooGong.hasStudyQA){
            menu_icon.add(getString(R.string.menu_study_qa));
        }

        if(ModooGong.hasStudyFile){
            menu_icon.add(getString(R.string.menu_study_file));
        }

        for(int i=0; i<menu_icon.size(); i++)
        {
            String menu = menu_icon.get(i);
            MenuData m = new MenuData();
            m.setType(Tags.MENU_VIEW_TYPE.MENU_ICON);
            m.setName(menu);

            if(i == Tags.MENU_INDEX.MENU_DOWN_LECTURE){
                m.setImage(R.drawable.down_icon);
            }else if(ModooGong.hasStudyFile && i == Tags.MENU_INDEX.MENU_STUDY_FILE){
                m.setImage(R.drawable.file_icon);
            }else{
                m.setImage(R.drawable.pen_icon);
            }

            m.menuIndex = menus.size();
            menus.add(m);
        }

        //Lee, 교재판매 페이지!
        {
            String menuName = "교재구매"; //용감한 북스 -> 교재구매로 텍스트명 변경[2019.10.24 테일러]
            MenuData m = new MenuData();
            m.setType(Tags.MENU_VIEW_TYPE.MENU_ICON);
            m.setName(menuName);
            m.setImage(R.drawable.ic_menu_book);
            m.menuIndex = menus.size();
            Tags.MENU_INDEX.MENU_BOOK_SALES =  m.menuIndex;
            menus.add(m);
        }


        ArrayList<String> menu_text = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.menu_text)));

        if(ModooGong.hasExplainStudy){
            menu_text.add(0, getString(R.string.menu_explain_study));
            if(ModooGong.hasExplainStudyBottom) {
                Tags.FREE_MENU_ADD_POSITION = menus.size();
            }else{
                Tags.FREE_MENU_ADD_POSITION = menus.size() + 1;
            }
        }else{
            Tags.FREE_MENU_ADD_POSITION = menus.size();
        }

        for(String menu : menu_text){
            MenuData m = new MenuData();
            m.setType(Tags.MENU_VIEW_TYPE.MENU_TEXT);
            m.setName(menu);
            m.menuIndex = menus.size();
            menus.add(m);
        }

        mAdapter.addAll(menus);
    }
    */

    /*
    private void initNavigationMenu(ArrayList<MainResult.FreeLectureCate> cates, ArrayList<NoticeItemVO> notices)
    {
        if(mAdapter.getItemCount() == 0 || Tags.FREE_MENU_ADD_POSITION == -1){
            return;
        }

        int freeCnt = 0;
        if(cates != null && cates.size() != 0) {
            ArrayList<MenuData> free_menus = new ArrayList<>();
            for (MainResult.FreeLectureCate cate : cates) {
                MenuData m = new MenuData();
                m.setType(Tags.MENU_VIEW_TYPE.MENU_TEXT);
                m.setName(cate.getFvodCateNm());
                m.menuIndex = Tags.FREE_MENU_ADD_POSITION + free_menus.size();
                free_menus.add(m);
            }
            mAdapter.addAll(Tags.FREE_MENU_ADD_POSITION, free_menus);
            freeCnt = free_menus.size();
        }

        if(ModooGong.hasExplainStudyBottom){
            Tags.MENU_INDEX.MENU_NOTICE = Tags.FREE_MENU_ADD_POSITION + freeCnt + 1;
        }else{
            Tags.MENU_INDEX.MENU_NOTICE = Tags.FREE_MENU_ADD_POSITION + freeCnt;
        }

        Tags.MENU_INDEX.MENU_ONE_TO_ONE_QA = Tags.MENU_INDEX.MENU_NOTICE + 1;
       // Tags.MENU_INDEX.MENU_BOOK_SALES = Tags.MENU_INDEX.MENU_ONE_TO_ONE_QA + 1;

        int select = mAdapter.getSelect();
        if(select != -1 && select >= Tags.FREE_MENU_ADD_POSITION){
            mAdapter.setSelect(select + freeCnt);
        }

        if(notices != null && notices.size() != 0){
            mAdapter.updateNew(Tags.MENU_INDEX.MENU_NOTICE,
                                    BraveUtils.isNewBoard(notices.get(0).getWriteDate()));
        }
    }
    */


    /*
    private void initMenuFragment()
    {
        if(mMenus == null){
            mMenus = new ArrayList<>();
        }else{
            mMenus.clear();
        }

        mMenus.add(LectureFragment.newInstance());
        mMenus.add(LocalLectureFragment.newInstance());
        mMenus.add(BookSalesFragment.newInstance());

        if(ModooGong.hasStudyQA) {
            mMenus.add(StudyQAFragment.newInstance());
        }

        if(ModooGong.hasStudyFile){
            mMenus.add(StudyFileFragment.newInstance());
        }

        if(ModooGong.hasExplainStudy){
            if(ModooGong.hasExplainStudyNoTab){
                mMenus.add(FreeExplainStudyFragment.newInstance());
            }else{
                mMenus.add(FreeExplainLectureFragment.newInstance());
            }
        }

        mMenus.add(NoticeFragment.newInstance());
        mMenus.add(OneToOneQAFragment.newInstance());

    }
    */

    /*
    private void initMenuFragment(ArrayList<MainResult.FreeLectureCate> cates){

        if(mMenus == null || Tags.FREE_MENU_ADD_POSITION == -1){
            return;
        }

        if(cates != null && cates.size() != 0) {
            ArrayList<Fragment> free_menus = new ArrayList<>();
            for (MainResult.FreeLectureCate cate : cates) {
                Fragment f;
                Bundle b = new Bundle();
                if(cate.getFvodScates() != null && cate.getFvodScates().size() != 0){
                    f = FreeLectureFragment.newInstance();
                    b.putString(Tags.TAG_CATE, BraveUtils.toJson(cate));
                }else{
                    f = FreeStudyFragment.newInstance();
                    b.putInt(Tags.TAG_CATE, cate.getFvodCate());
                }
                f.setArguments(b);
                free_menus.add(f);
            }
            mMenus.addAll(Tags.FREE_MENU_ADD_POSITION, free_menus);
        }
    }

    private void initTitle(){
        if(mTitles == null){
            mTitles = new ArrayList<>();
        }else{
            mTitles.clear();
        }
        ArrayList<String> toolbar_title = new ArrayList<>();
        for(int i=0; i<mAdapter.getItemCount(); i++){
            MenuData menu = mAdapter.getItem(i);
            toolbar_title.add(menu.getName());
        }
        mTitles.addAll(toolbar_title);
    }

    private void initTitle(ArrayList<MainResult.FreeLectureCate> cates){
        if(mTitles == null || Tags.FREE_MENU_ADD_POSITION == -1){
            return;
        }
        if(cates != null && cates.size() != 0) {
            ArrayList<String> free_titles = new ArrayList<>();
            for (MainResult.FreeLectureCate cate : cates) {
                free_titles.add(cate.getFvodCateNm());
            }
            mTitles.addAll(Tags.FREE_MENU_ADD_POSITION, free_titles);
        }
    }
*/


    private void loadData(){

        startLoading();

        StudyRequests.getInstance().requestFreeLectureCateList(new OnResultListener<MainResult>(){

            @Override
            public void onSuccess(Request request, MainResult result)
            {
                stopLoading();
                setData(result);

                MyFirebaseMessagingService.initPushSystem(getApplicationContext());
            }

            @Override
            public void onFail(Request request, Exception exception) {
                stopLoading();
                BraveUtils.showRequestOnFailToast(MainActivity.this, exception);

                if(exception != null && exception.getMessage() != null) {
                    Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.REQUEST)
                            .putCustomAttribute(AnalysisTags.ERROR, AnalysisTags.MAIN + ":: " + exception.getMessage()));
                }

                setData(null);
            }
        });
    }

    private void setData(MainResult result)
    {
        if(result == null){
            refreshLoginStatus();
        }
        else if(!BraveUtils.checkUserInfo()) {
            refreshLoginStatus();
        } else {

            mMainResult = result;
            ArrayList<NoticeItemVO> notices = result.getNotiLists().getEpilogues();
            ArrayList<MainResult.FreeLectureCate> cates = result.getFvodCates();

            MyFirebaseMessagingService.PushActionData startData = MyFirebaseMessagingService.PopPushStartData();
            if (startData != null)
                handlePushStartData(startData);

            refreshLoginStatus();
            loadLectureData();
        }
    }

    private void refreshLoginStatus()
    {
        if(mMainResult == null){
            setNavigationHeaderState(Tags.NAV_HEADER_TYPE.NAV_HEADER_MENU_REFRESH);
        }else if(!BraveUtils.checkUserInfo()){
            setNavigationHeaderState(Tags.NAV_HEADER_TYPE.NAV_HEADER_NO_LOGIN);
        }else{
            setNavigationHeaderState(Tags.NAV_HEADER_TYPE.NAV_HEADER_LOGIN);

            refreshProfileLayout();
            refreshExamInfoList();
        }
    }

    private void setNavigationHeaderState(int state){
        switch (state){
            case Tags.NAV_HEADER_TYPE.NAV_HEADER_LOGIN:

                mLoginLayout.layout.setVisibility(View.VISIBLE);
                mNoLoginLayout.layout.setVisibility(View.GONE);
                mRefreshLayout.layout.setVisibility(View.GONE);
                mExtraLayout.layout.setVisibility(View.VISIBLE);

                break;
            case Tags.NAV_HEADER_TYPE.NAV_HEADER_NO_LOGIN:

                mLoginLayout.layout.setVisibility(View.GONE);
                mNoLoginLayout.layout.setVisibility(View.VISIBLE);
                mRefreshLayout.layout.setVisibility(View.GONE);
                mExtraLayout.layout.setVisibility(View.GONE);

                break;

            case Tags.NAV_HEADER_TYPE.NAV_HEADER_MENU_REFRESH:

                mLoginLayout.layout.setVisibility(View.GONE);
                mNoLoginLayout.layout.setVisibility(View.GONE);
                mRefreshLayout.layout.setVisibility(View.VISIBLE);
                mExtraLayout.layout.setVisibility(View.GONE);

                break;
        }
    }

    void handlePushStartData(MyFirebaseMessagingService.PushActionData startData)
    {
        Log.d("Push", String.format("handlePushStartData action:%s, param:%s, body:%s", startData.action, startData.param, startData.body));

        if(startData.action !=null) {
            if (startData.action.equals("open")) {

            } else if (startData.action.equals("store")) {

                BookSalesFragment.msGotoBookCode = startData.param;
                openMenuPage(PageId.MAIN_BOOKSTORE);
            }
        }
    }

    private void loadLectureData(){

        startLoading();
        StudyRequests.getInstance().requestLectureList(1, APIConfig.LECTURE_STATE_TYPE.LECTURE_ING_ITEM,
                1000, new OnResultListener<LectureResult>(){

            @Override
            public void onSuccess(Request request, LectureResult result) {
                stopLoading();
                setLectureData(result);
            }

            @Override
            public void onFail(Request request, Exception exception) {
                stopLoading();
                //BraveUtils.showToast(MainActivity.this, getString(R.string.toast_common_network_fail));
                if(exception != null && exception.getMessage() != null) {
                    Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.REQUEST)
                            .putCustomAttribute(AnalysisTags.ERROR, AnalysisTags.MAIN + ":: " + exception.getMessage()));
                }

                // resetFragment(mAdapter.getSelect());
                refreshMenuPage();
            }
        });
    }

    private void setLectureData(LectureResult result) {

        if(result == null){
            return;
        }

        mLectureResult = result;

        if(mDownloadManager != null) {
            ArrayList<LectureItemVO> lectures = result.getStudies();
            mDownloadManager.updateDownloadLecture(lectures);
            updateDownloadViews();
        }

        //resetFragment(mAdapter.getSelect());
        refreshMenuPage();
        reStartDownloadWithDialog();
        refreshLiveStatus();
    }

    private void reStartDownloadWithDialog(){

        if(mDrawer.isDrawerOpen(GravityCompat.START)){
            return;
        }

        boolean askDownloadPage = mActivePage !=null &&
                (mActivePage.resMenuId == R.id.navigation_download ||mActivePage.resMenuId == R.id.navigation_my_lecture);

        if(askDownloadPage) {
            if (mDownloadManager != null) {
                int count = mDownloadManager.getDownloadPauseStudyCount();
                if (count != -1) {
                    String message = getString(R.string.dialog_restart_download);
                    BraveUtils.showAlertDialogOkCancel(MainActivity.this, message,
                            new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    mDownloadManager.reStartDownload();

                                    Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.MAIN)
                                            .putCustomAttribute(AnalysisTags.ACTION, "restart_download"));
                                }
                            }, null);
                }
            }
        }
    }


    /*
    private void goMenuItemSelected(int position){
        mAdapter.setSelect(position);

        setToolbar(position);
        setFragment(position);
        if(!mRefreshLayout.btnMenuRefresh.isActivated()) {
            closeDrawer();
        }else{
            mRefreshLayout.btnMenuRefresh.setActivated(false);
        }
    }
*/


//
//    private void setToolbar(int position){
//        if(mTitles == null){
//            return;
//        }
//        //set Toolbar
//        TextView title = (TextView)mToolbarLayout.mToolbar.findViewById(R.id.toolbar_title);
//        title.setText(mTitles.get(position));
//
//        /*
//        if(checkNetwork(position) && checkLogin(position)) {
//            if(position == Tags.MENU_INDEX.MENU_STUDY_QA ||
//                    position == Tags.MENU_INDEX.MENU_ONE_TO_ONE_QA){
//                btnOk.setVisibility(View.VISIBLE);
//            }else{
//                btnOk.setVisibility(View.GONE);
//            }
//        }else{
//            btnOk.setVisibility(View.GONE);
//        }
//        */
//    }

    /*
    public String getToolbarTitle(){
        if(mTitles == null || mAdapter.getSelect() == -1){
            return null;
        }
        return mTitles.get(mAdapter.getSelect());
    }

    private void setFragment(int position)
    {
        setToolbarAuxButton(0);

        //set Fragment
        Fragment menuFragment = null;
        CustomEvent event = new CustomEvent(AnalysisTags.MAIN);

        if(!checkNetwork(position))
        {
            if(position == Tags.MENU_INDEX.MENU_DOWN_LECTURE){
                menuFragment = LocalLectureFragment.newInstance();

                event.putCustomAttribute(AnalysisTags.ACTION, "go_local_lecture_no_network");
            }else{
                menuFragment = NoNetworkFragment.newInstance();

                event.putCustomAttribute(AnalysisTags.ACTION, "go_no_network");
            }
        }
        else if(!checkLogin(position))
        {
            if(position == Tags.MENU_INDEX.MENU_DOWN_LECTURE){
                menuFragment = LocalLectureFragment.newInstance();

                event.putCustomAttribute(AnalysisTags.ACTION, "go_local_lecture_no_login");
            }else{
                menuFragment = NoLoginFragment.newInstance();

                event.putCustomAttribute(AnalysisTags.ACTION, "go_no_login");
            }
        }
        else
        {

            if(mMenus != null)
            {
                menuFragment = mMenus.get(position);

                String action;

                if (position == Tags.MENU_INDEX.MENU_LECTURE) {
                    action = "go_lecture";
                } else if (position == Tags.MENU_INDEX.MENU_DOWN_LECTURE) {
                    action = "go_down_lecture";
                } else if (position == Tags.MENU_INDEX.MENU_STUDY_QA) {
                    action = "go_study_qa";
                }else if(position == Tags.MENU_INDEX.MENU_STUDY_FILE){
                    action = "go_study_file";
                }else if(position == Tags.MENU_INDEX.MENU_NOTICE){
                    action = "go_notice";
                }else if(position == Tags.MENU_INDEX.MENU_ONE_TO_ONE_QA) {
                    action = "go_one_to_one_qa";
                }else if(position == Tags.MENU_INDEX.MENU_BOOK_SALES) {
                    action = "go_book_sales";
                }else{
                    action = "go_free_lecture";
                }

                event.putCustomAttribute(AnalysisTags.ACTION, action);
            }
        }

        Answers.getInstance().logCustom(event);

        if(menuFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, menuFragment)
                    .commitAllowingStateLoss();
                    //.commit();

            //Adhoc. Jongok, 이미 활성화된 프래그먼트에 대한 리프래쉬 이벤트를 발생시키기 위한 장치
            if( menuFragment instanceof BaseFragment ) {
                if(menuFragment.getHost() !=null)
                    ((BaseFragment) menuFragment).refreshFragment();
            }
        }
    }

    public void resetFragment(){
        if(mMainResult == null){
            initData();
        }else {
            if(mLectureResult == null){
                loadLectureData();
            }else{
                resetFragment(mAdapter.getSelect());
            }
        }
    }

    public void resetFragment(int position){
        refreshLoginStatus();
        if(position == -1){
            position = Tags.MENU_INDEX.MENU_LECTURE;
        }
        goMenuItemSelected(position);
    }
*/
/*
    private boolean checkLogin(int position){
        // 사용자의 로그인 정보를 체크하여서 로그인이 안되있다면 로그인 후 이용하라는 페이지 나오도록 하는 부분[2019.10.18 테일러]
        if(!BraveUtils.checkUserInfo()) {
            if(position == Tags.MENU_INDEX.MENU_LECTURE ||
                    position == Tags.MENU_INDEX.MENU_DOWN_LECTURE ||
                    position == Tags.MENU_INDEX.MENU_STUDY_QA ||
                    position == Tags.MENU_INDEX.MENU_STUDY_FILE ||
                    position == Tags.MENU_INDEX.MENU_BOOK_SALES ||
                    position == Tags.MENU_INDEX.MENU_ONE_TO_ONE_QA){
                return false;
            }else{
                return true;
            }
        }

        return true;
    }

    private boolean checkNetwork(int position){

        if(!SystemUtils.isNetworkConnected(getApplicationContext())) {
            return false;
//
//            if(position == Tags.MENU_INDEX.MENU_DOWN_LECTURE){
//                return true;
//            }else{
//                return false;
//            }
//
        }

        return true;
    }
*/


    /********************************************************************
     *
     * ToolBar 네비케이션 아이템 변경
     *
     ********************************************************************/

    @Override
    public void onBackPressed()
    {
        //! 교재판매 페이지 웹뷰에 백키 이벤트를 전달하기 위한 장치!
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if (fragmentList != null)
        {
            //TODO: Perform your logic to pass back press here
            for(Fragment fragment : fragmentList){
                if(fragment instanceof OnHomeActivityEventListener){
                    if(((OnHomeActivityEventListener)fragment).onHomeBackPressed())
                    {
                        // super.onHomeBackPressed();
                        return;
                    }
                }
            }
        }

        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        }else {
            if(BraveUtils.checkUserInfo()) {
                if (mDownloadManager != null) {
                    int count = mDownloadManager.getDownloadingStudyCount();
                    if (count != -1) {
                        BraveUtils.showAlertDialogOkCancel(MainActivity.this, getString(R.string.dialog_download_exit_guide),
                                new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        startLoading();
                                        mDownloadManager.forceFinishDownload(false, new OnDownloadDataListener<String>() {
                                            @Override
                                            public void onDataProgress(int progress) {
                                            }

                                            @Override
                                            public void onDataComplete(String result) {
                                                stopLoading();
                                                callBackPressed();
                                            }

                                            @Override
                                            public void onDataError(int error) {
                                                stopLoading();
                                                BraveUtils.showLoadOnFailToast(MainActivity.this);
                                            }
                                        });
                                    }
                                }, null);
                    } else {
                        BackPressedHandler.onBackPressed(MainActivity.this, getString(R.string.toast_common_guide_finish));
                    }
                }
            }else{
                BackPressedHandler.onBackPressed(MainActivity.this, getString(R.string.toast_common_guide_finish));
            }
        }
    }

    private void callBackPressed(){
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void closeDrawer(){
        if(mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        }
    }


    public void setToolbarTitle(String title)
    {
        TextView titleWidget = (TextView)mToolbarLayout.mToolbar.findViewById(R.id.toolbar_title);
        titleWidget.setText(title);
    }

    @Override
    protected void updateDownloadViews(String studyKey, int state, int percent, int errorCode)
    {

    }

    //! Depricated ...

    View.OnClickListener mToolbarClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
            if (fragmentList != null)
            {
                //TODO: Perform your logic to pass back press here
                for(Fragment fragment : fragmentList){
                    if(fragment instanceof OnHomeActivityEventListener){
                        ((OnHomeActivityEventListener)fragment).onHomeTitleBarPressed();
                    }
                }
            }
        }
    };

    public void setVisibilityOkButton(int visible){
        mToolbarLayout.btnOk.setVisibility(visible);
    }

    private View.OnClickListener mOnOkBtnClickListener;

    public void setOnOkBtnClickListener(View.OnClickListener listener) {
        mOnOkBtnClickListener = listener;
    }


    private void onLegRegClicked(View view)
    {
       List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
       if (fragmentList != null)
       {
           //TODO: Perform your logic to pass back press here
           for(Fragment fragment : fragmentList){
               if(fragment instanceof OnHomeActivityEventListener){ ((OnHomeActivityEventListener)fragment).onHomeMenuLecRegClicked();
               }
           }
       }
    }

    public void setToolbarAuxButton(int idx)
    {
        if(idx ==0)
        {
            mToolbarLayout.btnOk.setVisibility(View.GONE);
            //mToolbarLayout.btnLecReg.setVisibility(View.GONE);
        }
        else if(idx ==1)
        {
            mToolbarLayout.btnOk.setVisibility(View.VISIBLE);
            //mToolbarLayout.btnLecReg.setVisibility(View.GONE);
        }
        else if(idx ==2)
        {
            mToolbarLayout.btnOk.setVisibility(View.GONE);
           // mToolbarLayout.btnLecReg.setVisibility(View.VISIBLE);
        }
    }

    /********************************************
     *
     * Photo Profile
     *
     ********************************************/

    PhotoSelector mPhotoSelector=new PhotoSelector();

    String [] todayMsgHints = {
            "당신의 용감한 한마디를 적어주세요." // 메시지 힌트 내용 변경 [19.10.14 테일러]
    };

    String getTodayHint()
    {
        Random r = new Random();
        int i= r.nextInt(todayMsgHints.length);
        return todayMsgHints[i];
    }

    void refreshProfileLayout() {

        mLoginLayout.txtName.setText(PropertyManager.getInstance().getUserName());

        String msg = PropertyManager.getInstance().getTodayMsg();
        if (msg.length() == 0) // 메시지 내용이 없으면
        {
            msg = "오늘의 용감한 한마디"; // "오늘의 화이팅 한마디" -> "오늘의 용감한 한마디"으로 변경 [19.10.14 테일러]
            mLoginLayout.txtTodayMessage.setTextColor(Color.parseColor("#c0c0c0")); // 접속시, "오늘의 화이팅 한마디"가 입력되어
                                                                                // 회색으로 색상 처리 하고 오늘의 한마디 작성 유도
            mLoginLayout.mViewTodayMessageDivider.setVisibility(View.VISIBLE); // 구분선만 보이게 하여 오늘의 한마디 작성하게 유도하는 바탕 표현
            mLoginLayout.mTodayMessageButton.setVisibility(View.VISIBLE);// 오늘의 한미디 편집 아이콘 상시 노출로 수정[2019.10.17 테일러]
        }
        else
        {
            mLoginLayout.txtTodayMessage.setTextColor(Color.parseColor("#000000"));
            mLoginLayout.mViewTodayMessageDivider.setVisibility(View.INVISIBLE); // 오늘의 한마디에 한글자라도 작성되면 구분선 안나오도록 처리
            mLoginLayout.mTodayMessageButton.setVisibility(View.VISIBLE);
        }


        mLoginLayout.txtTodayMessage.setText(msg);

        String photoPath =   PropertyManager.getInstance().getProfilePhotoPath();
        if(photoPath != null && photoPath.length() > 0 ) {
            mLoginLayout.mProfileImageView.setImageURI(Uri.parse(photoPath));
        }
    }

    void doEditTodayMessage()
    {
      //  String prevMsg = txtTodayMessage.getText().toString();
        String prevMsg = getTodayHint();

        UIUtils.InputText(this, "오늘의 용감한 한마디", "", prevMsg, new UIUtils.InputResultListener(){
            @Override
            public void onInputText(String msg)
            {
                mLoginLayout.txtTodayMessage.setText(msg);
                PropertyManager.getInstance().setTodayMsg(msg);

                if(msg.length() == 0) //입력된 텍스트가 하나도 없다면
                {
                    //result = "오늘의 화이팅 한마디";
                    //mainActivity.txtTodayMessage.setTextColor(Color.parseColor("#c0c0c0"));
                    mLoginLayout.mViewTodayMessageDivider.setVisibility(View.VISIBLE);
                    mLoginLayout.mTodayMessageButton.setVisibility(View.VISIBLE); // 오늘의 한미디 편집 아이콘 상시 노출로 수정[2019.10.17 테일러]
                }
                else //입력된 텍스트가 하나라도 있다면
                {
                    mLoginLayout.txtTodayMessage.setTextColor(Color.parseColor("#000000")); // 텍스트의 색상을 하얀색으로 변경
                    mLoginLayout.mViewTodayMessageDivider.setVisibility(View.INVISIBLE);
                    mLoginLayout.mTodayMessageButton.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    void doEditProfile()
    {
        mPhotoSelector.requestPhoto(this, mOnPhotoSelectedListener);
    }

    PhotoSelector.OnPhotoSelectedListener mOnPhotoSelectedListener = new PhotoSelector.OnPhotoSelectedListener()
    {
        @Override
        public void onPhotoSelected(String path, Bitmap selectedBitmap)
        {
            if(path != null && path.length() > 0 ) {
                mLoginLayout.mProfileImageView.setImageURI(Uri.parse(path));
                PropertyManager.getInstance().setProfilePhotoPath(path);
            }
        }
    };

    /********************************************
     *
     * OnAir Data
     *
     ********************************************/

    Handler mLiveUpdateHandler = new Handler(Looper.getMainLooper());
    int mLiveUpdateDelayTime = 10;

    void startLiveStatus()
    {
        mLiveUpdateHandler.removeCallbacksAndMessages(null);
        refreshLiveStatus();
    }

    void stopLiveStatus()
    {
        mLiveUpdateHandler.removeCallbacksAndMessages(null);
    }

    void scheduleNextLiveUpdate()
    {
        mLiveUpdateHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLiveStatus();
            }

        }, mLiveUpdateDelayTime * 1000) ;
    }

    private void refreshLiveStatus()
    {
        scheduleNextLiveUpdate();

        StatisDataManager.getInstance().GetTodayRank(new BaseDataManager.OnDataUpdatedListener<StatisPacket.TodayRankResult>() {
            @Override
            public void onDataUpdated(StatisPacket.TodayRankResult data) {
                refreshTodayRank(mTodayRankLayout,data);
            }
        });

        StatisDataManager.getInstance().GetOnAir(new BaseDataManager.OnDataUpdatedListener<StatisPacket.OnAirResult>() {
            @Override
            public void onDataUpdated(StatisPacket.OnAirResult data) {

                String msg = String.format("%d명", data.activeUsers);
                mOnAirLayout.activeUsers.setText(msg);
            }
        });

        ShopDataManager.getInstance().GetMyCartInfo(new BaseDataManager.OnDataUpdatedListener<ShopPacket.MyCountInfo>() {
            @Override
            public void onDataUpdated(ShopPacket.MyCountInfo data) {
                if(data !=null)
                {
                    refreshMenuBadge(PageId.MAIN_BOOKSTORE, data.getCount() >0);
                }
            }
        });


    }

    private void refreshTodayRank(TodayRankLayout mTodayRankLayout,StatisPacket.TodayRankResult data)
    {
        if(data ==null)
        {
            mTodayRankLayout.todayTime.setText(String.format("00 : 00 : 00"));
            mTodayRankLayout.todayRank.setText(String.format("00등"));
            mTodayRankLayout.rankDelta.setVisibility(View.GONE);
            mTodayRankLayout.rankUpMark.setVisibility(View.GONE);
            mTodayRankLayout.rankDownMark.setVisibility(View.GONE);
        }
        else {
            BcDateUtils.HMS hms = BcDateUtils.getHMS(data.hours);

            mTodayRankLayout.todayTime.setText(String.format("%02d : %02d : %02d", hms.h, hms.m, hms.s));
            mTodayRankLayout.todayRank.setText(String.format("%d등", data.rank));

            if (data.rankDelta == 0) {
                mTodayRankLayout.rankDelta.setText("");
                mTodayRankLayout.rankDelta.setVisibility(View.GONE);
                mTodayRankLayout.rankUpMark.setVisibility(View.GONE);
                mTodayRankLayout.rankDownMark.setVisibility(View.GONE);

            } else if (data.rankDelta > 0) {
                mTodayRankLayout.rankDelta.setText(String.format("%d", Math.abs(data.rankDelta)));
                mTodayRankLayout.rankDelta.setTextColor(Color.GREEN);
                mTodayRankLayout.rankDelta.setVisibility(View.VISIBLE);
                mTodayRankLayout.rankUpMark.setVisibility(View.VISIBLE);
                mTodayRankLayout.rankDownMark.setVisibility(View.GONE);

            } else if (data.rankDelta < 0) {
                mTodayRankLayout.rankDelta.setText(String.format("%d", Math.abs(data.rankDelta)));
                mTodayRankLayout.rankDelta.setTextColor(Color.RED);
                mTodayRankLayout.rankDelta.setVisibility(View.VISIBLE);
                mTodayRankLayout.rankUpMark.setVisibility(View.GONE);
                mTodayRankLayout.rankDownMark.setVisibility(View.VISIBLE);
            }
        }
    }

    private void refreshExamInfoList()
    {
        StatisDataManager.getInstance().GetExamInfoList(
                new BaseDataManager.OnDataUpdatedListener<StatisPacket.ExamInfoResult>() {
                    @Override
                    public void onDataUpdated(StatisPacket.ExamInfoResult data) {
                        //TODO Show Display!!
                        // 시험 정보 데이터
                        if(data !=null &&  data.examInfos.size() > 0) {

                            mExamInfoLayout.examInfoSliderView.setVisibility(View.VISIBLE);

                            mExamInfoLayout.examInfosliderViewAdapter.clearDatas();

                            for (int i = 0; i < data.examInfos.size(); i++) {
                                mExamInfoLayout.examInfosliderViewAdapter.addExamInfoDatas(data.examInfos.get(i));
                            }

                            mExamInfoLayout.examInfosliderViewAdapter.refresh();
                        }
                        else
                        {
                            mExamInfoLayout.examInfoSliderView.setVisibility(View.GONE);
                        }
                    }
                });
    }

}
