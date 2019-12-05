package kr.co.bravecompany.modoogong.android.stdapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import java.net.URISyntaxException;
import java.util.HashMap;

import kr.co.bravecompany.api.android.stdapp.api.data.ShopPacket;
import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.activity.MainActivity;
import kr.co.bravecompany.modoogong.android.stdapp.application.ModooGong;
import kr.co.bravecompany.modoogong.android.stdapp.db.BaseDataManager;
import kr.co.bravecompany.modoogong.android.stdapp.db.ShopDataManager;
import kr.co.bravecompany.modoogong.android.stdapp.manager.PropertyManager;


public class BookSalesFragment extends BaseFragment implements MainActivity.OnHomeActivityEventListener, ViewTreeObserver.OnScrollChangedListener
{
    private WebView webView;
    private Menu mMenu; //용감한 북스 처음 페이지시, 뒤로가기 버튼 이미지 수정 [2019.10.17 테일러]
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private boolean isInit = false; //용감한 북스 처음 페이지시, 뒤로가기 버튼 이미지 수정 [2019.10.17 테일러]
    public static String msGotoBookCode;


    private Animation fab_open, fab_close;
    private FloatingActionButton mCartButton; // 용감한 북스 플로팅 장바구니 [2019.10.22 테일러]
    private TextView mCartInfo;

    String getShoppingCartUrl()
    {
        return  ModooGong.storeURL+"/mypage/basket";
    }

    public static BookSalesFragment newInstance() {
        return new BookSalesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_book_sales, container, false);
        setHasOptionsMenu(true);

        initLayout(rootView);
        initWebView(rootView);

        initListener();
        initData();

        return rootView;
    }

    @Override
    public  void onPause()
    {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //! BaseFragment Override ....

    @Override
    protected void initListener()
    {

    }


    @Override
    protected void setData(String data)
    {

    }

    @Override
    protected void initData(){

        isInit = true; //용감한 북스 처음 페이지시, 뒤로가기 버튼 이미지 수정 [2019.10.17 테일러]
        String url = ModooGong.storeURL+"/newbooks";
        if(msGotoBookCode !=null)
        {
            url = url+"/book/"+msGotoBookCode;
            msGotoBookCode=null;
        }
        requestWebUrl(url,"교재구매"); //용감한 북스 -> 교재구매로 텍스트명 변경[2019.10.24 테일러]
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_bookstore_option, menu);

        //용감한 북스 처음 페이지시, 뒤로가기 버튼 이미지 수정 [2019.10.17 테일러]
        mMenu = menu;
        mMenu.getItem(1).setEnabled(false);
        mMenu.getItem(1).setVisible(false);
        mMenu.getItem(1).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
    }

    //용감한 북스 처음 페이지시, 뒤로가기 버튼 이미지 수정 [2019.10.17 테일러]
    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.menu_home)
        {
            //용감한 북스 처음 페이지시, 뒤로가기 버튼 이미지 수정 [2019.10.17 테일러]
            mMenu.getItem(1).setEnabled(false);
            mMenu.getItem(1).setVisible(false);
            mMenu.getItem(1).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
            initData();
        }
        else if(item.getItemId() == R.id.menu_back)
        {
            //용감한 북스 처음 페이지시, 뒤로가기 버튼 이미지 수정 [2019.10.17 테일러]
            if (webView.canGoBack())
                webView.goBack();
        }
        else
            return super.onOptionsItemSelected(item);

        return true;
    }

    @Override
    protected void initLayout(ViewGroup rootView) {
        ((MainActivity) getActivity()).setToolbarAuxButton(0);


        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);
        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        webView.reload();
                    }
                });

        mCartButton = (FloatingActionButton) rootView.findViewById(R.id.fab);// 용감한 북스 플로팅 장바구니 [2019.10.22 테일러]
        mCartInfo = (TextView) rootView.findViewById(R.id.cart_info);

        mCartButton.setOnClickListener(mFabClickListener);

        fab_open = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fab_close);

    }

    protected void initWebView(ViewGroup rootView)
    {
        webView = (WebView) rootView.findViewById(R.id.webView);

        webView.setWebViewClient(mWebViewClient);

        webView.setWebChromeClient(new WebChromeClient());
        webView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        // Long press block
        webView.setLongClickable(true);
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        webView.getSettings().setJavaScriptEnabled(true);          // Javascript를 실행할 수 있도록 설정
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);   // javascript 코드를 이용하여 윈도우를 새로 열기
        webView.getSettings().setBuiltInZoomControls(false);       // 안드로이드에서 제공하는 줌 아이콘을 사용할 수 있도록 설정
        webView.getSettings().setSupportZoom(false);               // 확대, 축소 기능을 사용할 수 있도록 설정
        webView.getSettings().setSupportMultipleWindows(false);    // 여러개의 윈도우를 사용할 수 있도록 설정

        webView.getSettings().setAllowFileAccess(true);            // webview 내에서 파일 접근 가능 여부
        webView.getSettings().setAppCacheEnabled(true);            // Cache API 사용 여부
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT); //캐쉬 설정으로 첫 진입에 초수가 단축됨
        webView.getSettings().setDatabaseEnabled(true);            // HTML5 에서 DataBase 허용
        webView.getSettings().setDomStorageEnabled(true);          // HTML5 DOM storage 허용여부
        webView.getSettings().setSaveFormData(false);              // 비밀번호 저장 사용 안함

        webView.getSettings().setUseWideViewPort(true);            // wide viewport 사용하도록 설정
        webView.getSettings().setLoadWithOverviewMode(true);       // 항상 전체 화면으로 보이도록 함
        //webView.getSettings().setMediaPlaybackRequiresUserGesture(false);  // 웹에서 미디어를 재생하기 위해 사용자의 제스처가 필요한지 여부 설정

        webView.getSettings().setGeolocationEnabled(false);        // Geo Location 사용 여부

        webView.getViewTreeObserver().addOnScrollChangedListener(this);

        //! 이 코드 삽입후 정상 작동 하기 시작함
        if (Build.VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        }

    }

    @Override
    public void onScrollChanged() {
        /*
        if (webView.getScrollY() == 0) {
            mSwipeRefreshLayout.setEnabled(true);
        } else {
            mSwipeRefreshLayout.setEnabled(false);
        }
        */
    }
    private Boolean mIsGotoFabOpen = false;

    private View.OnClickListener mFabClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            int id = v.getId();
            if(id == R.id.fab)
            {
                requestWebUrl(getShoppingCartUrl(),"교재구매"); //용감한 북스 -> 교재구매로 텍스트명 변경[2019.10.24 테일러]
            }
        }
    };


    String mGetUrl;
    private WebViewClient mWebViewClient = new WebViewClient()
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon)
        {
            mSwipeRefreshLayout.setRefreshing(true);

            if(mMenu !=null && mMenu.size() >= 2) {

                //용감한 북스 처음 페이지시, 뒤로가기 버튼이 느리게 사라져서 pagestart로 변경[2019.10.18 테일러]
                if (webView.canGoBack()) {

                    if (isInit) {
                        mMenu.getItem(1).setEnabled(false);
                        mMenu.getItem(1).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
                        mMenu.getItem(1).setVisible(false);
                        isInit = false;
                    } else {
                        mMenu.getItem(1).setEnabled(true);
                        mMenu.getItem(1).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                        mMenu.getItem(1).setVisible(true);
                    }

                } else {
                    mMenu.getItem(1).setEnabled(false);
                    mMenu.getItem(1).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
                    mMenu.getItem(1).setVisible(false);
                    isInit = false;
                }
            }

            refreshCartInfo();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            mSwipeRefreshLayout.setRefreshing(false);
            mCartButton.show();// 용감한 북스 플로팅 장바구니 [2019.10.22 테일러]
            mGetUrl = view.getUrl();

            String cookies = CookieManager.getInstance().getCookie(mGetUrl);
            Log.d("BOOK", "URL:"+mGetUrl+", Cookies:" + cookies);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                CookieSyncManager.getInstance().sync();
            } else {
                CookieManager.getInstance().flush();
            }

        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
        {
            hideErrorPage(view);
        }

        private void hideErrorPage(WebView view)
        {
            // Here we configurating our custom error page
            // It will be blank

            view.loadUrl("file:///android_asset/errorPage.html");
        }

        //Scheme 처리용
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Intent intent = parse(url);

            if (isIntent(url))
            {
                if (isExistInfo(intent, view.getContext()) || isExistPackage(intent, view.getContext()))
                    return start(intent, view.getContext());
                else {
                    gotoMarket(intent, view.getContext());
                    return true;
                }
            }
            else if (isMarket(url))
            {
                return start(intent, view.getContext());
            }

            return super.shouldOverrideUrlLoading(view, url);
        }

        private Intent parse(String url) {
            try {
                return Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
            } catch (URISyntaxException e) {
                e.printStackTrace();
                return null;
            }
        }

        private Boolean isIntent(String url) {
            return url.matches("^intent:?\\w*://\\S+$");
        }

        private Boolean isMarket(String url) {
            return url.matches("^market://\\S+$");
        }

        private Boolean isExistInfo(Intent intent, Context context) {
            try {
                return intent != null && context.getPackageManager().getPackageInfo(intent.getPackage(), PackageManager.GET_ACTIVITIES) != null;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                return false;
            }
        }

        private Boolean isExistPackage(Intent intent, Context context) {
            return intent != null && context.getPackageManager().getLaunchIntentForPackage(intent.getPackage()) != null;
        }

        private boolean start(Intent intent, Context context) {
            context.startActivity(intent);
            return true;
        }

        private boolean gotoMarket(Intent intent, Context context) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + intent.getPackage())));
            return true;
        }
    };

    @Override
    public boolean onHomeBackPressed()
    {
        if (webView.canGoBack()) {
            webView.goBack();
            return true;
        }

        return false;
    }

    @Override
    public void onHomeTitleBarPressed()
    {
        initData();
    }

    @Override
    public void onHomeMenuLecRegClicked() { }

    void refreshCartInfo()
    {
        ShopDataManager.getInstance().GetMyCartInfo(new BaseDataManager.OnDataUpdatedListener<ShopPacket.MyCountInfo>() {
            @Override
            public void onDataUpdated(ShopPacket.MyCountInfo data) {
                if(data !=null)
                {
                    mCartInfo.setVisibility(data.getCount() ==0 ? View.GONE : View.VISIBLE);
                    mCartInfo.setText(String.valueOf(data.getCount()));
                }
            }
        });
    }

    /**
     * request url
     *
     * @param url request url
     */
    private SharedPreferences mPreferences;

    public void requestWebUrl(final String url, String title)
    {
        ((MainActivity) getActivity()).setToolbarTitle(title);

        //https://stackoverflow.com/questions/5716898/set-a-cookie-to-a-webview-in-android

        /* 1. map value 설정
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getActivity().getApplicationContext());
        String token2= mPreferences.getString("auth_token","");

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("x-auth-token", token);
        webView.loadUrl(url, map);
        */

        /* 2. 단순 쿠기
        String baseUrl = "";
        String cookieString = "cookie_name=cookie_value; path=/";
        CookieManager.getInstance().setCookie(baseUrl, cookieString);
        */

        //3. 여러개의 쿠키 설정
        /*
        String cookies = "key1=someValue1;key2=someValue2;key3=someValue3";
        String [] cookiesList = cookies.split(";");
        for(int i=0; i < cookiesList.length ;i++)
        {
            String item = cookiesList[i];
            CookieManager.getInstance().setCookie(mHostDomainName, item);
        }
        */



        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                String urlWithKeepFlag = url + "?siteKeep=Y";

                String userKey = PropertyManager.getInstance().getUserKey();

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("x-userkey", userKey);
                map.put("x-platform", "app-android");
                map.put("x-siteseq", ModooGong.bookStoreSeq);
                webView.loadUrl(urlWithKeepFlag, map);

               // webView.loadUrl(url);
            }
        });

    }

}
