package kr.co.bravecompany.modoogong.android.stdapp.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Pattern;


/**
 * Created by BraveCompany on 2017. 9. 4..
 */

public class MarketVersionChecker {

    private static MarketVersionChecker instance;

    public static MarketVersionChecker getInstance() {
        if (instance == null) {
            instance = new MarketVersionChecker();
        }
        return instance;
    }

    public interface OnMarketVersionCheckListener {
        void onSuccess(String version);
        void onFail();
    }

    public class MarketVersionCheckResult {
        public OnMarketVersionCheckListener listener;
        public String result;
    }

    private MarketVersionCheckHandler mHandler = new MarketVersionCheckHandler(Looper.getMainLooper());

    public class MarketVersionCheckHandler extends Handler {

        public static final int MESSAGE_SUCCESS = 1;
        public static final int MESSAGE_FAIL = 2;

        public MarketVersionCheckHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MarketVersionCheckResult result = (MarketVersionCheckResult) msg.obj;
            switch (msg.what) {
                case MESSAGE_SUCCESS:
                    if(result.listener != null) result.listener.onSuccess(result.result);
                    break;
                case MESSAGE_FAIL:
                    if(result.listener != null) result.listener.onFail();
                    break;
            }
        }
    }

    public void getMarketVersion(String packageName, OnMarketVersionCheckListener listener) {
        new VersionChecker(packageName, listener).start();
    }

    private class VersionChecker extends Thread {
        private MarketVersionCheckResult result = new MarketVersionCheckResult();

        private String packageName;
        private OnMarketVersionCheckListener listener;

        public VersionChecker(String packageName, OnMarketVersionCheckListener listener) {
            this.packageName = packageName;
            this.listener = listener;
        }

        /*
        @Override
        public void run() {
            result.listener = listener;

            try {
                Document doc = Jsoup.connect(
                        "https://play.google.com/store/apps/details?id="
                                + packageName).get();
                Elements Version = doc.select(".content");

                for (Element mElement : Version) {
                    if (mElement.attr("itemprop").equals("softwareVersion")) {
                        result.result = mElement.text().trim();
                        mHandler.sendMessage(mHandler.obtainMessage(MarketVersionCheckHandler.MESSAGE_SUCCESS, result));
                        return;
                    }
                }

            } catch (Exception e) {
                log.e(log.getStackTraceString(e));
            }
            mHandler.sendMessage(mHandler.obtainMessage(MarketVersionCheckHandler.MESSAGE_FAIL, result));
        }
        */

        @Override
        public void run() {
            result.listener = listener;

            try {
                Document doc = Jsoup.connect(
                        "https://play.google.com/store/apps/details?id="
                                + packageName).get();

                Elements Version = doc.select(".htlgb ");

                String VersionMarket="";

                for (int i = 0; i < Version.size() ; i++) {
                    VersionMarket = Version.get(i).text();
                    if (Pattern.matches("^[0-9]{1}.[0-9]{1}.[0-9]{1}$", VersionMarket)) {

                        result.result = VersionMarket;
                        mHandler.sendMessage(mHandler.obtainMessage(MarketVersionCheckHandler.MESSAGE_SUCCESS, result));
                        return;
                    }
                }


            } catch (Exception e) {
                log.e(log.getStackTraceString(e));
            }

            mHandler.sendMessage(mHandler.obtainMessage(MarketVersionCheckHandler.MESSAGE_FAIL, result));
        }
    }
}
