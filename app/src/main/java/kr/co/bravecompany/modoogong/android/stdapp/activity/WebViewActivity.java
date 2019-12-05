package kr.co.bravecompany.modoogong.android.stdapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import java.net.URISyntaxException;
import java.util.HashMap;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.application.ModooGong;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.manager.PropertyManager;

public class WebViewActivity extends BaseActivity  implements  ViewTreeObserver.OnScrollChangedListener {

    private WebView webView;
    private TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        setSystemBar(true);

        initToolBar();

        initLayout();
        initListener();
        initData();
    }

    private void initToolBar()
    {
        Toolbar  mToolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle = mToolbar.findViewById(R.id.toolbar_title);

        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initLayout() {

        webView = (WebView) findViewById(R.id.webView);

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

    private void initListener(){
    }

    private void initData(){
        String url = getIntent().getStringExtra(Tags.TAG_URL);
        String title = getIntent().getStringExtra(Tags.TAGE_TITLE);

        if(title !=null) {
            toolbarTitle.setText(title);
        }

        if(url != null){
            requestWebUrl(url);
        }
    }

    /**
     * request url
     *
     * @param url request url
     */

    public void requestWebUrl(final String url) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                String urlWithKeepFlag = url + "?siteKeep=Y";

                String userKey = PropertyManager.getInstance().getUserKey();

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("x-userkey", userKey);
                map.put("x-platform", "app-android");
                map.put("x-siteseq", ModooGong.bookStoreSeq);

                webView.loadUrl(url);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
            return;
        }
        super.onBackPressed();
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

    String mGetUrl;
    private WebViewClient mWebViewClient = new WebViewClient()
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon)
        {
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
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
            //return url.contains("https://bootpaymark");
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
}
