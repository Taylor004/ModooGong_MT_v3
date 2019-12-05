package kr.co.bravecompany.modoogong.android.stdapp.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.crashlytics.android.answers.LoginEvent;
import com.facebook.stetho.Stetho;
import com.google.gson.JsonSyntaxException;
import com.gun0912.tedpermission.PermissionListener;
import com.kollus.sdk.media.KollusStorage;
import com.kollus.sdk.media.util.ErrorCodes;
//import com.kollus.sdk.media.util.LibraryChecker;
import com.kollus.sdk.media.util.Utils;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.api.android.stdapp.api.OnResultListener;
import kr.co.bravecompany.api.android.stdapp.api.config.ErrorType;
import kr.co.bravecompany.api.android.stdapp.api.requests.AuthRequests;
import kr.co.bravecompany.api.android.stdapp.api.data.LoginRequest;
import kr.co.bravecompany.api.android.stdapp.api.data.LoginResult;
import kr.co.bravecompany.modoogong.android.stdapp.application.ModooGong;
import kr.co.bravecompany.modoogong.android.stdapp.application.MyFirebaseMessagingService;
import kr.co.bravecompany.modoogong.android.stdapp.config.AnalysisTags;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.db.DataManager;
import kr.co.bravecompany.modoogong.android.stdapp.manager.PropertyManager;
import kr.co.bravecompany.modoogong.android.stdapp.utils.BraveUtils;
import kr.co.bravecompany.modoogong.android.stdapp.utils.MyFirebaseRemoteConfig;
import kr.co.bravecompany.modoogong.android.stdapp.utils.SystemUtils;
import kr.co.bravecompany.modoogong.android.stdapp.utils.log;
import kr.co.bravecompany.player.android.stdapp.config.KollusConstant;
import okhttp3.Request;

public class SplashActivity extends BaseActivity {

    private final int DEVICE_SETTING_START 	= 100;
    private final int DEVICE_SETTING_END 	= 101;
    private final int DEVICE_SETTING_ERROR 	= 102;

    private final int DOWNLOAD_CHECKING	= 200;
    private final int DOWNLOAD_CHECK_COMPLETE = 201;
    private final int DOWNLOAD_START 	= 202;
    private final int DOWNLOAD_PROGRESS = 203;
    private final int DOWNLOAD_COMPLETE = 204;
    private final int DOWNLOAD_ERROR 	= 205;
    private final int UNSUPPORT_DEVICE	= 206;

    private final int AUTO_LOGIN_START = 300;
    private final int AUTO_LOGIN_FAIL = 301;

    private int mState = -1;

    //private View layoutLoading;
    private LinearLayout layoutSplash;
    private ImageView imgLoading;
    private TextView txtCheckLibrary;
    private TextView txtDesc;
    //private LibraryChecker mLibraryChecker;
    private KollusStorage mStorage;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initLayout();
        initListener();
        //goActivity();
        BraveUtils.checkPermission(SplashActivity.this, mStoragePermissionListener,
                getString(R.string.check_permission_external_storage),
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        handleIntent(getIntent());

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }


    void handleIntent(Intent intent)
    {
        MyFirebaseMessagingService.InitPushStartData(intent);
    }

    private void initLayout() {
        //layoutLoading = findViewById(R.id.layoutLoading);
        layoutSplash = (LinearLayout)findViewById(R.id.layoutSplash);
        imgLoading = (ImageView)findViewById(R.id.imgLoading);
        txtCheckLibrary = (TextView)findViewById(R.id.txtCheckLibrary);
        txtDesc = (TextView)findViewById(R.id.txtDesc);

        if( Utils.isTablet(getApplicationContext())){
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)layoutSplash.getLayoutParams();
            layoutParams.bottomMargin = getResources().getDimensionPixelSize(R.dimen.common_default_bottom_margin);
            layoutSplash.setLayoutParams(layoutParams);
        }
        String desc = ModooGong.splashDescription;
        if(desc != null && desc.length() != 0){
            txtDesc.setText(desc);
            txtDesc.setVisibility(View.VISIBLE);
        }
    }

/*
    public static boolean isTablet_Ad(Context context) {
        Resources r = context.getResources();
        DisplayMetrics mat =  r.getDisplayMetrics();


        int portrait_width_pixel = Math.min(mat.widthPixels, mat.heightPixels);
        int dots_per_virtual_inch = mat.densityDpi;
        float virutal_width_inch = (float)(portrait_width_pixel / dots_per_virtual_inch);
        Configuration config = r.getConfiguration();
        boolean isTablet = virutal_width_inch > 2.0F || (config.screenLayout & 15) >= 4;
        if (isTablet) {
           // CpuInfo cpuInfo = CpuInfo.getInstance();
           // return cpuInfo.getFrequenceWithInt() >= 1500000 && cpuInfo.getCpuCount() >= 2;
            return true;
        } else {
            return false;
        }
    }
 */

    private void initListener() {
    }

    private void initData(){
        startLoading();

        //check rooting
        if(Utils.isRooting()){
            stopLoading();
            String message = getString(R.string.dialog_error_rooting);
            BraveUtils.showAlertDialogOk(SplashActivity.this, message,
                    new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.SPLASH)
                                    .putCustomAttribute(AnalysisTags.ACTION, "finish_rooting_app"));

                            finish();
                        }
                    });
        }else{
            /*
            //check market version
            MarketVersionChecker.getInstance().getMarketVersion(
                    getPackageName(), new MarketVersionChecker.OnMarketVersionCheckListener() {
                        @Override
                        public void onSuccess(String version) {
                            initUpdate(version);
                        }

                        @Override
                        public void onFail() {
                            initClearData();
                        }
                    });
             */

            //Note. 파이어베이스 리모트 Config를 이용한 설정
            MyFirebaseRemoteConfig.getInstance().getMarketVersion( this,
                    getPackageName(), new MyFirebaseRemoteConfig.OnMarketVersionCheckListener() {
                        @Override
                        public void onSuccess(String version) {
                            initUpdate(version);
                        }

                        @Override
                        public void onFail() {
                            initClearData();
                        }
                    });
        }
    }

    private void initUpdate(String version){

        if(BraveUtils.checkAppUpdate(getApplicationContext(), version))
        {
            String message = getString(R.string.dialog_app_update);
            String positive = getString(R.string.go_google_play_store);
            String negative = getString(R.string.cancel_google_play_store);
            String title  = getString(R.string.dialog_app_update_title);

            BraveUtils.showAlertDialog(SplashActivity.this,title, message, positive,negative,
                    new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            String url = String.format(getString(R.string.google_play_store_url), getPackageName());
                            BraveUtils.goWeb(SplashActivity.this, url);
                            finishActivity();
                        }
                    },
                    new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            initClearData();
                        }
                    }
                    );
        }else{
            initClearData();
        }
    }

    private void initClearData(){
        //check app version
        if(BraveUtils.checkAppClearData(getApplicationContext())) {
            String message = getString(R.string.dialog_app_clear_data);
            BraveUtils.showAlertDialogOk(SplashActivity.this, message,
                    new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            ModooGong.getInstance().clearApplicationData();
                            PropertyManager.getInstance().clearAllPrefs();

                            initLibrary();
                        }
                    });
        }else{
            initLibrary();
        }
    }

    private void initLibrary(){
        PropertyManager.getInstance().setAppVer(SystemUtils.getAppVersionName(getApplicationContext()));

        Context context = ModooGong.getContext();
        Fabric.with(context, new Answers(), new Crashlytics());

        //Realm.init(context);
        DataManager.getInstance().initDatabase(context);
        JodaTimeAndroid.init(context);

        if(ModooGong.isDebug) {
            Stetho.initialize(Stetho.newInitializerBuilder(context)
                    .enableDumpapp(Stetho.defaultDumperPluginsProvider(context))
                    .enableWebKitInspector(RealmInspectorModulesProvider.builder(context).build())
                    .build());
        }

        initStorage();
        //setupLibrary();
    }


    private void goActivity(){
        if(!BraveUtils.checkUserInfo()){
            goLoginActivity();
        }else{
            //goMainActivity();
            showMessage(getString(R.string.splash_auto_login_ing));
            braveLogin();
        }
    }

    private void braveLogin(){
        mState = AUTO_LOGIN_START;
        LoginRequest request = new LoginRequest();
        request.setId(PropertyManager.getInstance().getUserID());
        request.setPassword(PropertyManager.getInstance().getUserPW());
        request.setOs(SystemUtils.getOS());
        request.setOs_ver(Build.VERSION.RELEASE);
        request.setMaddr1(SystemUtils.getMacAddress(getApplicationContext()));
        request.setMaddr2(SystemUtils.getIpAddress(getApplicationContext()));
        request.setMobile_key(SystemUtils.getAndroidID(getApplicationContext()));
        request.setSerial(Build.SERIAL);
        request.setMthd(SystemUtils.getMthd());
        request.setAuto(Tags.LOGIN_TYPE.LOGIN_AUTO);
        request.setUserAgent(SystemUtils.getDeviceInfo(getApplicationContext()));

        AuthRequests.getInstance().requestlogin(request, new OnResultListener<LoginResult>() {
            @Override
            public void onSuccess(Request request, LoginResult result) {
                checkUser(result);
            }

            @Override
            public void onFail(Request request, Exception exception) {
                mState = AUTO_LOGIN_FAIL;
                stopLoading();
                if(exception instanceof JsonSyntaxException){
                    //BraveUtils.showToast(SplashActivity.this, getString(R.string.toast_common_json_fail));
                }else{
                    BraveUtils.showToast(SplashActivity.this, getString(R.string.toast_common_network_fail));
                }

                if(exception != null && exception.getMessage() != null) {
                    Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.REQUEST)
                            .putCustomAttribute(AnalysisTags.ERROR, AnalysisTags.SPLASH + ":: " + exception.getMessage()));
                }

                showMessage(getString(R.string.splash_auto_login_fail));
                goReLoginActivity();
            }
        });
    }

    private void checkUser(LoginResult result){
        if(result == null){
            return;
        }
        LoginEvent event = new LoginEvent().putMethod("login_auto");
        String userId = result.getUserId();
        if(userId != null){
            event.putCustomAttribute(AnalysisTags.VALUE, userId);
        }

        String state = result.getRstState();
        if(state != null){
            if(ErrorType.LOGIN_USER_STATE.S2.equals(state)){
                PropertyManager.getInstance().setUserKey(result.getUserKey());
                PropertyManager.getInstance().setUserName(result.getUserName());
                goMainActivity();

                event.putSuccess(true);
            }else{
                goReLoginActivity();

                event.putCustomAttribute(AnalysisTags.ERROR, "mobile_error");
                event.putSuccess(false);
            }
        }else{
            goReLoginActivity();

            event.putSuccess(false);
        }
        Answers.getInstance().logLogin(event);
    }

    private void goReLoginActivity(){
        BraveUtils.showToast(this, getString(R.string.toast_login_error));
        PropertyManager.getInstance().removeUserKey();
        goLoginActivity();
    }

    private void goMainActivity() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                BraveUtils.goMain(SplashActivity.this);
                finishActivity();
            }
        }, 2000);
    }

    private void goLoginActivity() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                intent.putExtra(Tags.TAG_SPLASH, true);
                startActivity(intent);
                finishActivity();
            }
        }, 2000);
    }

    private void finishActivity(){
        stopLoading();
        finish();
    }

    private void finishActivity(String msg){
        if(msg != null){
            showMessage(msg);
        }else {
            showMessage(getString(R.string.toast_splash_guide_finish));
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finishActivity();
            }
        }, 1000);
    }

    @Override
    public void onBackPressed() {
        if(imgLoading.getVisibility() != View.VISIBLE){
            finish();
        }
    }

    // =============================================================================
    //
    // Kollus Player Setting
    //
    // =============================================================================

    private void showMessage(String msg) {
        txtCheckLibrary.setText(msg);
    }

//    private void setupLibrary(){
//        mLibraryChecker = new LibraryChecker(this, mOnCheckerListener);
//        mLibraryChecker.check(!KollusConstant.isDebug);
//    }

    private void initStorage() {
        Context mContext= getApplicationContext();

        // mStorage = KollusStorage.getInstance(mContext, true);
        mStorage = KollusStorage.getInstance(mContext);

        mStorage.initialize(KollusConstant.KEY, KollusConstant.EXPIRE_DATE, mContext.getPackageName());

        mStorage.setDeviceASync(
                Utils.getStoragePath(mContext),
                Utils.createUUIDSHA1(mContext),
                Utils.createUUIDMD5(mContext),
                Utils.isTablet(mContext),
                mDeviceListener);
    }

//        private void setupStorage(){
//        mStorage = KollusStorage.getInstance(getApplicationContext(), true);
//        mStorage.initialize(KollusConstant.KEY, KollusConstant.EXPIRE_DATE,
//                SplashActivity.this.getPackageName());
//        mStorage.setDeviceASync(
//                Utils.getStoragePath(SplashActivity.this),
//                Utils.createUUIDSHA1(SplashActivity.this),
//                Utils.createUUIDMD5(SplashActivity.this),
//                Utils.isTablet(SplashActivity.this),
//                mDeviceListener);
//    }
//
//    private Handler mLibraryHandler = new Handler() {
//
//        @Override
//        public void handleMessage(Message msg) {
//            mState = msg.what;
//            switch (msg.what) {
//                case DOWNLOAD_CHECKING:
//                    showMessage(getString(R.string.kollus_resource_check));
//                    break;
//                case DOWNLOAD_CHECK_COMPLETE:
//                    showMessage(getString(R.string.kollus_resource_check_complete));
//                    int nCount = mLibraryChecker.getDownloadCount();
//                    int nTotalSize = mLibraryChecker.getDownloadTotalSize();
//                    if (nCount > 0 && nTotalSize > 0) {
//                        String message = String.format(getString(R.string.dialog_kollus_resource_download),
//                                nCount, nTotalSize / (1024. * 1024.));
//                        BraveUtils.showAlertDialogOkCancel(SplashActivity.this, message,
//                                mLibraryOkDialogListener, mLibraryCancelDialogListener);
//                    } else {
//                        setupStorage();
//                    }
//                    break;
//                case DOWNLOAD_START:
//                    showMessage(getString(R.string.kollus_resource_download_ing));
//                    break;
//                case DOWNLOAD_PROGRESS:
//                    showMessage(
//                            String.format(getString(R.string.kollus_resource_download_ing_progress),
//                                    msg.arg1, mLibraryChecker.getDownloadCount(),
//                                    msg.arg2));
//                    break;
//                case DOWNLOAD_COMPLETE:
//                    setupStorage();
//                    break;
//                case DOWNLOAD_ERROR:
//                    stopLoading();
//                    showMessage(getString(R.string.kollus_resource_download_error));
//
//                    Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.SPLASH)
//                            .putCustomAttribute(AnalysisTags.ERROR, "resource_download_error"));
//                    break;
//                case UNSUPPORT_DEVICE:
//                    stopLoading();
//                    showMessage(getString(R.string.kollus_resource_not_support));
//
//                    Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.SPLASH)
//                            .putCustomAttribute(AnalysisTags.ERROR, "not_supported_device"));
//                    break;
//                //Device Setting
//                case DEVICE_SETTING_START:
//                    //showMessage(getString(R.string.kollus_device_setting_start));
//                    break;
//                case DEVICE_SETTING_END:
//                    goActivity();
//                    break;
//                case DEVICE_SETTING_ERROR:
//                    //showMessage(getString(R.string.kollus_device_setting_error));
//                    finishActivity(getString(R.string.kollus_device_setting_error));
//
//                    Answers.getInstance().logCustom(new CustomEvent(AnalysisTags.SPLASH)
//                            .putCustomAttribute(AnalysisTags.ERROR, "device_setting_error"));
//                    break;
//            }
//        }
//
//    };
//
//    private MaterialDialog.SingleButtonCallback mLibraryOkDialogListener = new MaterialDialog.SingleButtonCallback() {
//        @Override
//        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//            mLibraryChecker.download();
//        }
//    };
//
//    private MaterialDialog.SingleButtonCallback mLibraryCancelDialogListener = new MaterialDialog.SingleButtonCallback() {
//        @Override
//        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//            finishActivity(null);
//        }
//    };
//
//    private LibraryChecker.OnCheckerListener mOnCheckerListener = new LibraryChecker.OnCheckerListener() {
//
//        @Override
//        public void onDownloadChecking() {
//            //log.d(String.format("LibraryChecker Checking (ver.%s)", Utils.getVersion()));
//            log.d("LibraryChecker Checking");
//            mLibraryHandler.sendEmptyMessage(DOWNLOAD_CHECKING);
//        }
//
//        @Override
//        public void onDownloadCheckComplete(int nCount, int nTotalSize) {
//            log.d(String.format("LibraryChecker CheckComplete (Count : %d, TotalSize : %dByte", nCount, nTotalSize));
//            mLibraryHandler.sendEmptyMessage(DOWNLOAD_CHECK_COMPLETE);
//        }
//
//        @Override
//        public void onDownloadStart() {
//            log.d("LibraryChecker Download Start");
//            mLibraryHandler.sendEmptyMessage(DOWNLOAD_START);
//        }
//
//        @Override
//        public void onDownloadComplete() {
//            log.d("LibraryChecker Download Complete");
//            mLibraryHandler.sendEmptyMessage(DOWNLOAD_COMPLETE);
//        }
//
//        @Override
//        public void onDownloadError() {
//            log.d("LibraryChecker Download Error");
//            mLibraryHandler.sendEmptyMessage(DOWNLOAD_ERROR);
//        }
//
//        @Override
//        public void onDownloadProgress(int index, int progress) {
//            log.d(String.format("LibraryChecker Downloading %dth --> %d%%", index, progress));
//            Message msg = mHandler.obtainMessage(DOWNLOAD_PROGRESS, index, progress);
//            mLibraryHandler.sendMessage(msg);
//        }
//
//        @Override
//        public void onUnSupportDevice() {
//            log.e("LibraryChecker Unsupported Device!!");
//            mLibraryHandler.sendEmptyMessage(UNSUPPORT_DEVICE);
//        }
//    };

    private KollusStorage.OnKollusStorageDeviceListener mDeviceListener = new KollusStorage.OnKollusStorageDeviceListener() {

        @Override
        public void onDeviceSettingEnd() {
            log.d("Kollus Device Setting End");
            mLibraryHandler.sendEmptyMessage(DEVICE_SETTING_END);
        }

        @Override
        public void onDeviceSettingStart() {
            log.d("Kollus Device Setting Start");
            mLibraryHandler.sendEmptyMessage(DEVICE_SETTING_START);
        }

        @Override
        public void onDeviceSettingError(int nErrorCode) {
            String message = String.format("ErrorCode(%d) -- %s",
                    nErrorCode,
                    ErrorCodes.getInstance(SplashActivity.this).getErrorString(nErrorCode));
            log.d(message);
            mLibraryHandler.sendMessage(mHandler.obtainMessage(DEVICE_SETTING_ERROR, nErrorCode, 0));
        }

    };


    private Handler mLibraryHandler =new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            mState = msg.what;
            switch (msg.what)
            {
                //Device Setting
                case DEVICE_SETTING_START:
                    showMessage(getString(R.string.kollus_resource_download_ing));
                    break;
                case DEVICE_SETTING_END:
                    goActivity();

                    break;
                case DEVICE_SETTING_ERROR:
                    finishActivity(getString(R.string.kollus_device_setting_error));

                    break;
            }
        }

    };

    @Override
    public void startLoading() {
        AnimationDrawable anim = (AnimationDrawable)imgLoading.getDrawable();
        anim.start();
        imgLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void stopLoading() {
        AnimationDrawable anim = (AnimationDrawable)imgLoading.getDrawable();
        anim.stop();
        imgLoading.setVisibility(View.GONE);
    }

    // =============================================================================
    // Check Permission
    // =============================================================================

    private PermissionListener mStoragePermissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            if(Build.VERSION.SDK_INT >= 23 && PropertyManager.getInstance().getAppVer() == null){
                String message = getString(R.string.dialog_check_permission_external_storage);
                BraveUtils.showAlertDialogOk(SplashActivity.this, message,
                        new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                initData();
                             }
                        });
            }else{
                initData();
            }
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            finishActivity(null);
        }
    };
}
