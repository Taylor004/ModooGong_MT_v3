package kr.co.bravecompany.modoogong.android.stdapp.activity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;

public class WebViewDialogActivity extends BaseActivity {

    private View layoutBack;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.66f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.activity_web_view_dialog);
        initLayout();
        initListener();
        initData();
    }

    private void initLayout() {
        layoutBack = findViewById(R.id.layoutBack);
        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(false);
        webView.getSettings().setLoadWithOverviewMode(false);
        webView.getSettings().setUseWideViewPort(true);
        int size = BraveUtils.convertPxToDp(getApplicationContext(), getResources().getDimension(R.dimen.text_small));
        webView.getSettings().setDefaultFontSize(size);
        //webView.setBackgroundColor(Color.TRANSPARENT);

        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        startLoading();
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                stopLoading();
            }
        });
    }

    private void initListener(){
        layoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData(){
        String html = getIntent().getStringExtra(Tags.TAG_HTML);
        int padding = BraveUtils.convertPxToDp(getApplicationContext(),
                getResources().getDimension(R.dimen.common_margin_medium));
        html = BraveUtils.makeHTMLTextGray(getApplicationContext(), html, padding, 1.5f);
        if(html != null){
            requestWebHtml(html);
        }
    }

    /**
     * request html
     *
     * @param html request url
     */
    public void requestWebHtml(final String html) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webView.loadDataWithBaseURL("", html, "text/html", "UTF-8", null);
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
}
