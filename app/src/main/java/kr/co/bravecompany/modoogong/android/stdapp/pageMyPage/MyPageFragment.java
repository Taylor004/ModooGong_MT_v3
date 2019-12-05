package kr.co.bravecompany.modoogong.android.stdapp.pageMyPage;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import kr.co.bravecompany.api.android.stdapp.api.data.Packet;
import kr.co.bravecompany.api.android.stdapp.api.data.ShopPacket;
import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.activity.MainActivity;
import kr.co.bravecompany.modoogong.android.stdapp.application.ModooGong;
import kr.co.bravecompany.modoogong.android.stdapp.db.BaseDataManager;
import kr.co.bravecompany.modoogong.android.stdapp.db.ShopDataManager;
import kr.co.bravecompany.modoogong.android.stdapp.fragment.BaseFragment;
import kr.co.bravecompany.modoogong.android.stdapp.manager.PropertyManager;
import kr.co.bravecompany.modoogong.android.stdapp.pageCS.OneToOneCSActivity;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import kr.co.bravecompany.modoogong.android.stdapp.utils.UIUtils;

public class MyPageFragment extends BaseFragment implements MainActivity.OnHomeActivityEventListener {

    public enum MenuType
    {
        OPEN_URL,
        OPEN_ACTIVITY,
    }

    public final static class MenuIds
    {
        final static int ONE_TO_ONE = 1;
        final static int CART =2;
        final static int FAQ =3;
        final static int ORDER =4;
        final static int NOTICE =5;

        final static int COUPON =6;
        final static int PUSH =7;

    }

    public static class MenuItem
    {
        public MenuType pageType;
        public int menuId;
        public String title;
        public String openURL;
        public int badge;

        public boolean top;
    }



    public static class StaticMenuItem
    {
        public ViewGroup layout;
        public TextView title;
        public TextView badge;

        public MenuItem menuItem;
    }

    public static class ProfileLayout
    {
        public ViewGroup layout;
        public CircleImageView photo;
        public TextView name;
        public TextView message;
    }

    public static class StaticMenuLayout
    {
        public ViewGroup layout;
        public StaticMenuItem couponMenu = new StaticMenuItem();
        public StaticMenuItem notiMenu = new StaticMenuItem();
    }

    public static class PassListLayout
    {
        public ViewGroup layout;
        public ListView passList;
        public MyPagePassListAdapter adapter;
    }

    public static class MenuListLayout
    {
        public ViewGroup layout;
        public ListView menuList;
        public MyPageMenuListAdapter adapter;
    }

    private List<MenuItem>  mMenuList = new ArrayList<>();

    private ProfileLayout mProfileLayout = new ProfileLayout();
    private StaticMenuLayout mStaticMenuLayout = new StaticMenuLayout();
    private PassListLayout mPassListLayout = new PassListLayout();
    private MenuListLayout mMenuListLayout = new MenuListLayout();

    public static MyPageFragment newInstance() { return new MyPageFragment();}

    private MyPageFragment()
    {
        super();
        setupMenus();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_mypage, container, false);

        initLayout(rootView); // 레이아웃 초기화

        initProfileLayout(rootView);
        initStaticMenuLayout(rootView);
        initPassListLayout(rootView);
        initMenuListLayout(rootView);

        refreshMyProfile();
        refreshMyPageInfo();
        refreshMyPassList();

        return rootView;
    }

//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        refreshMyPassList();
//        refreshMyPageInfo();
//
//        refreshStaticMenu(mStaticMenuLayout.couponMenu);
//        refreshStaticMenu(mStaticMenuLayout.notiMenu);
//    }

    @Override
    public void onResume()
    {
        super.onResume();

        refreshMyPassList();
        refreshMyPageInfo();

        refreshStaticMenu(mStaticMenuLayout.couponMenu);
        refreshStaticMenu(mStaticMenuLayout.notiMenu);
    }

    @Override
    protected void initLayout(ViewGroup rootView) {}
    @Override
    protected void initListener(){}
    @Override
    protected void initData() {}
    @Override
    protected void setData(String data) {}


    void setupMenus()
    {
        MenuItem item;

        item = new MenuItem();
        item.menuId = MenuIds.ONE_TO_ONE;
        item.title = "1:1문의내역";
        item.badge = 0;
        item.pageType = MenuType.OPEN_ACTIVITY;// MenuType.OPEN_URL;
        item.openURL = "cs/qna";
        mMenuList.add(item);

        item = new MenuItem();
        item.menuId = MenuIds.CART;
        item.title = "장바구니";
        item.badge = 0;
        item.pageType = MenuType.OPEN_URL;
        item.openURL = "mypage/basket";
        mMenuList.add(item);


        item = new MenuItem();
        item.menuId = MenuIds.ORDER;
        item.title = "주문/배송 조회";
        item.badge = 0;
        item.pageType = MenuType.OPEN_URL;
        item.openURL = "mypage/payHis";
        mMenuList.add(item);

        item = new MenuItem();
        item.menuId = MenuIds.NOTICE;
        item.title = "공지사항";
        item.badge = 0;
        item.pageType = MenuType.OPEN_URL;
        item.openURL = "cs/notice";
        mMenuList.add(item);

        item = new MenuItem();
        item.menuId = MenuIds.FAQ;
        item.title = "FAQ 자주 묻는 질문";
        item.badge = 0;
        item.pageType = MenuType.OPEN_URL;
        item.openURL = "cs/faq";
        mMenuList.add(item);


        item = new MenuItem();
        item.menuId = MenuIds.COUPON;
        item.title = "쿠폰함";
        item.badge = 0;
        item.pageType = MenuType.OPEN_URL;
        item.openURL = "mypage/bank";
        item.top = true;
        mMenuList.add(item);

        item = new MenuItem();
        item.menuId = MenuIds.PUSH;
        item.title = "공지사항";
        item.badge = 0;
        item.pageType = MenuType.OPEN_URL;
        item.openURL = "push_notice";
        item.top = true;
        mMenuList.add(item);
    }

    private void  initProfileLayout(ViewGroup rootView)
    {
        ViewGroup layout= (ViewGroup) rootView.findViewById(R.id.mypage_profile_layout);

        mProfileLayout.layout = layout;
        mProfileLayout.photo = (CircleImageView) layout.findViewById(R.id.mypage_profile_photo);
        mProfileLayout.name = (TextView) layout.findViewById(R.id.mypage_profile_name);
        mProfileLayout.message = (TextView) layout.findViewById(R.id.mypage_profile_message);
    }

    private void initStaticMenuLayout(ViewGroup rootView)
    {
        ViewGroup layout= (ViewGroup) rootView.findViewById(R.id.mypage_alarm_layout);

        mStaticMenuLayout.layout = layout;
        mStaticMenuLayout.couponMenu.layout = layout.findViewById(R.id.mypage_coupon_layout);
        mStaticMenuLayout.couponMenu.title = layout.findViewById(R.id.mypage_coupon_title);
        mStaticMenuLayout.couponMenu.badge = layout.findViewById(R.id.mypage_coupon_badge);
        mStaticMenuLayout.couponMenu.menuItem = findMenuById(MenuIds.COUPON);

        mStaticMenuLayout.notiMenu.layout = layout.findViewById(R.id.mypage_noti_layout);
        mStaticMenuLayout.notiMenu.title = layout.findViewById(R.id.mypage_noti_title);
        mStaticMenuLayout.notiMenu.badge = layout.findViewById(R.id.mypage_noti_badge);
        mStaticMenuLayout.notiMenu.menuItem = findMenuById(MenuIds.PUSH);


        mStaticMenuLayout.couponMenu.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doMenuAction(mStaticMenuLayout.couponMenu.menuItem);
            }
        });

        mStaticMenuLayout.notiMenu.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doMenuAction(mStaticMenuLayout.notiMenu.menuItem);
            }
        });

        refreshStaticMenu(mStaticMenuLayout.couponMenu);
        refreshStaticMenu(mStaticMenuLayout.notiMenu);
    }

    private void initPassListLayout(ViewGroup rootView)
    {
        ViewGroup layout  = (ViewGroup) rootView.findViewById(R.id.mypage_mypass_layout);

        mPassListLayout.layout = layout;
        mPassListLayout.passList = (ListView) layout.findViewById(R.id.mypage_mypass_list);
        mPassListLayout.adapter = new MyPagePassListAdapter(getContext(),
                new MyPagePassListAdapter.OnPassItemSelectListener() {
                    @Override
                    public void onPassItemSelected(Packet.PassData item) {
                        doPassClicked(item);
                    }
                });

        mPassListLayout.passList.setAdapter(mPassListLayout.adapter);

//        mPassListLayout.passList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });
    }

    private void initMenuListLayout(ViewGroup rootView)
    {
        ViewGroup layout  = (ViewGroup) rootView.findViewById(R.id.mypage_menu_layout);
        mMenuListLayout.layout = layout;
        mMenuListLayout.menuList = (ListView) layout.findViewById(R.id.mypage_menu_list);
        mMenuListLayout.adapter = new MyPageMenuListAdapter(getContext());
        mMenuListLayout.menuList.setAdapter(mMenuListLayout.adapter);

        //Note 이것으로 안됨
        mMenuListLayout.menuList.setScrollContainer(false);

        mMenuListLayout.adapter.refreshMenus(mMenuList);

        UIUtils.ResizeListViewHeightBasedOnChildren(mMenuListLayout.menuList);

        mMenuListLayout.menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MenuItem item = mMenuList.get(position);
                doMenuAction(item);
            }
        });
    }



    MenuItem findMenuById(int menuId)
    {
        for(MenuItem item : mMenuList)
        {
            if(item.menuId == menuId)
                return item;
        }

        return null;
    }

    private void refreshMyProfile()
    {
        mProfileLayout.name.setText(PropertyManager.getInstance().getUserName());

        String msg = PropertyManager.getInstance().getTodayMsg();
        if (msg.length() == 0) // 메시지 내용이 없으면
        {
            msg = "오늘의 용감한 한마디"; // "오늘의 화이팅 한마디" -> "오늘의 용감한 한마디"으로 변경 [19.10.14 테일러]
            mProfileLayout.message.setTextColor(Color.parseColor("#c0c0c0")); // 접속시, "오늘의 화이팅 한마디"가 입력되어
        }

//        else
//        {
//            mLoginLayout.mViewTodayMessageDivider.setVisibility(View.INVISIBLE); // 오늘의 한마디에 한글자라도 작성되면 구분선 안나오도록 처리
//            mLoginLayout.mTodayMessageButton.setVisibility(View.VISIBLE);
//        }


        mProfileLayout.message.setText(msg);

        String photoPath =   PropertyManager.getInstance().getProfilePhotoPath();
        if(photoPath != null && photoPath.length() > 0 ) {
            mProfileLayout.photo.setImageURI(Uri.parse(photoPath));
        }
    }

    private void refreshMyPageInfo()
    {

        ShopDataManager.getInstance().GetCouponInfo(new BaseDataManager.OnDataUpdatedListener<ShopPacket.MyCountInfo>() {
            @Override
            public void onDataUpdated(ShopPacket.MyCountInfo data) {

                mStaticMenuLayout.couponMenu.menuItem.badge = data.getCount();
                refreshStaticMenu(mStaticMenuLayout.couponMenu);
            }
        });

        ShopDataManager.getInstance().GetNotificationInfo(new BaseDataManager.OnDataUpdatedListener<ShopPacket.MyCountInfo>() {
            @Override
            public void onDataUpdated(ShopPacket.MyCountInfo data) {
                mStaticMenuLayout.notiMenu.menuItem.badge = data.getCount();
                refreshStaticMenu(mStaticMenuLayout.notiMenu);
            }
        });

        ShopDataManager.getInstance().GetMyCartInfo(new BaseDataManager.OnDataUpdatedListener<ShopPacket.MyCountInfo>() {
            @Override
            public void onDataUpdated(ShopPacket.MyCountInfo data) {
                MenuItem cartMenuItem= findMenuById(MenuIds.CART);
                cartMenuItem.badge = data.getCount();
                mMenuListLayout.adapter.notifyDataSetChanged();
            }
        });
    }

    private void refreshMyPassList()
    {
        ShopDataManager.getInstance().GetMyPassList(new BaseDataManager.OnDataUpdatedListener<List<Packet.PassData>>()
        {
            @Override
            public void onDataUpdated(List<Packet.PassData> result)
            {
                mPassListLayout.adapter.refreshPassList(result);
                UIUtils.ResizeListViewHeightBasedOnChildren(mPassListLayout.passList);
            }
        });
    }

    private void refreshStaticMenu(StaticMenuItem item)
    {
        item.title.setText(item.menuItem.title);
        item.badge.setText(String.valueOf(item.menuItem.badge));
        item.badge.setVisibility((item.menuItem.badge==0) ? View.INVISIBLE : View.VISIBLE);
    }

    private void doMenuAction(MenuItem menuItem)
    {
        if(menuItem.pageType == MenuType.OPEN_URL)
        {
            String openURL = ModooGong.storeURL + "/" + menuItem.openURL;
            BraveUtils.goWebView(this.getActivity(), openURL, menuItem.title);
        }
        else if(menuItem.menuId == MenuIds.ONE_TO_ONE)
        {
            Intent intent = new Intent(getContext(), OneToOneCSActivity.class);
            startActivity(intent);
        }
    }

    private void doPassClicked(Packet.PassData passData)
    {
        //int k=0;

    }

    @Override
    public boolean onHomeBackPressed() {
        return false;
    }

    @Override
    public void onHomeTitleBarPressed() {

    }

    @Override
    public void onHomeMenuLecRegClicked() {

    }

}
