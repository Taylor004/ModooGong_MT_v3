package kr.co.bravecompany.modoogong.android.stdapp.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.concurrent.Executor;

import kr.co.bravecompany.modoogong.android.stdapp.BuildConfig;


public class MyFirebaseRemoteConfig {
    private static MyFirebaseRemoteConfig instance;

    public static MyFirebaseRemoteConfig getInstance() {
        if (instance == null) {
            instance = new MyFirebaseRemoteConfig();
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


    public class MarketVersionCheckHandler extends Handler {

        public static final int MESSAGE_SUCCESS = 1;
        public static final int MESSAGE_FAIL = 2;

        public MarketVersionCheckHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MyFirebaseRemoteConfig.MarketVersionCheckResult result = (MyFirebaseRemoteConfig.MarketVersionCheckResult) msg.obj;
            switch (msg.what) {
                case MESSAGE_SUCCESS:
                    if(result.listener != null)
                        result.listener.onSuccess(result.result);
                    break;
                case MESSAGE_FAIL:
                    if(result.listener != null)
                        result.listener.onFail();
                    break;
            }
        }
    }


    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private FirebaseRemoteConfigSettings mConfigSettings;

    private MarketVersionCheckHandler mHandler = new MarketVersionCheckHandler(Looper.getMainLooper());

    public MyFirebaseRemoteConfig()
    {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        mConfigSettings = new FirebaseRemoteConfigSettings.Builder()
            .setDeveloperModeEnabled(BuildConfig.DEBUG)
            .setMinimumFetchIntervalInSeconds(3600)
            .build();

        mFirebaseRemoteConfig.setConfigSettingsAsync(mConfigSettings);
    }


    public void getMarketVersion(Activity activity, String packageName, final OnMarketVersionCheckListener listener) {

        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(activity, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task)
                    {
                        MarketVersionCheckResult result = new MarketVersionCheckResult();
                        result.listener = listener;

                        if (task.isSuccessful())
                        {
                            boolean updated = task.getResult();

                            String marketMinVersion = mFirebaseRemoteConfig.getString("playstore_min_version");
                            String marketLatestVersion = mFirebaseRemoteConfig.getString("playstore_latest_version");

                            Log.d("Version", String.format("RemoteConfig Version minVersion:%s, latestVersion:%s", marketMinVersion, marketLatestVersion));

                            if (marketMinVersion != null && !marketMinVersion.isEmpty()) {
                                result.result = marketMinVersion;
                                mHandler.sendMessage(mHandler.obtainMessage(MarketVersionCheckHandler.MESSAGE_SUCCESS, result));
                                return;
                            }
                        }

                        mHandler.sendMessage(mHandler.obtainMessage(MarketVersionCheckHandler.MESSAGE_FAIL, result));
                    }
                });
    }


}
