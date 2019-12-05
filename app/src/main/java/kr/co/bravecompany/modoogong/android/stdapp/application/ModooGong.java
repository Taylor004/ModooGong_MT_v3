package kr.co.bravecompany.modoogong.android.stdapp.application;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.kollus.sdk.media.util.Utils;

import java.io.File;

import kr.co.bravecompany.api.android.stdapp.api.NetworkManager;
import kr.co.bravecompany.modoogong.android.stdapp.BuildConfig;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.utils.log;
import kr.co.bravecompany.player.android.stdapp.config.KollusConstant;

/**
 * Created by BraveCompany on 2016. 10. 11..
 */

public class ModooGong extends Application{
    private static ModooGong instance;
    private static Context context;

    public static final boolean isDebug = false;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        context = this;

        AppConfig.SetupApplicationConfig(context.getPackageName());

        //! Lee
//        String kollusKey = BuildConfig.kollusKey;
//        int kollusPort = BuildConfig.kollusPort;
//        String apiURL = BuildConfig.apiURL;
//        host = apiURL;

        //// Application Setting ///////////////////////////////////////////////////////
        NetworkManager.init(context, isDebug, host);

        // true : develop, false : release
       // KollusConstant.init("9a31d34c8d4499fa5ec27b1b4faee4485619d758", 7492, isDebug);
        KollusConstant.init(kollusKey, kollusPort, isDebug);


        // true : show log, false : hide log
        log.init(isDebug);

/*
        // default: -1000
        Tags.STUDY_QA_MENU_INDEX.QA_MENU_SUBJECT = -1000;
        Tags.STUDY_QA_MENU_INDEX.QA_MENU_TYPE = -1000;
        Tags.STUDY_QA_MENU_INDEX.QA_MENU_TEACHER = -1000;
        Tags.STUDY_QA_MENU_INDEX.QA_MENU_LECTURE = -1000;
        Tags.STUDY_QA_MENU_INDEX.QA_MENU_BOOK = -1000;
        Tags.STUDY_QA_MENU_INDEX.QA_MENU_PAGE = -1000;

        // default: "-1000"
        Tags.QA_TYPE_CD.QA_TYPE_LECTURE = "-1000"; //학습상담
        Tags.QA_TYPE_CD.QA_TYPE_STUDY = "-1000"; //수업(강의)내용
        Tags.QA_TYPE_CD.QA_TYPE_ETC = "-1000"; //기타
        Tags.QA_TYPE_CD.QA_TYPE_FEEDBACK = "-1000"; //답변첨삭

        if(hasStudyQA){
            Tags.MENU_INDEX.MENU_STUDY_QA = 2;
        }
        if(hasStudyFile){
            Tags.MENU_INDEX.MENU_STUDY_FILE = 3;
        }
 */
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static ModooGong getInstance(){
        return instance;
    }

    public static Context getContext() {
        return context;
    }

    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        clearData(appDir);

        File sdCardDir = new File(Utils.getStoragePath(this));
        clearData(sdCardDir);
    }

    private void clearData(File dir){
        if(dir.exists()){
            String[] children = dir.list();
            for(String s : children){
                if(!s.equals("lib")){
                    if(deleteDir(new File(dir, s))){
                        log.i("File " + dir.getPath() + "/" + s +" DELETED");
                    }else {
                        log.i("File " + dir.getPath() + "/" + s +" DELETED FAILED");
                    }
                }
            }
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        if(dir != null){
            return dir.delete();
        }
        return false;
    }

    // =============================================================================
    // Application Setting
    // =============================================================================

    public static String host = "aaa.com";
    public static String kollusKey = "";
    public static int kollusPort =0;
    public static String storeURL = "https://t-m.modoogong.com";

    public static String bookStoreSeq = "32";

    public static String splashDescription = null;
    public static boolean isShowCateName = false;
    public static boolean isShowQAMenuSubject = false;
    public static boolean isShowQAMenuType = false;
    public static boolean isShowQAMenuTeacher = false;
    public static boolean isRequiredQAMenuLecture = false;
    public static boolean isShowQAMenuBook = false;
    public static boolean isRequiredQAMenuBook = false;
    public static boolean isShowQAVoiceRecoder = false;
    public static boolean hasMypage = false;
    public static boolean hasExplainStudy = false;
    public static boolean hasExplainStudyBottom = false;
    public static boolean hasExplainStudyNoTab = false;
    public static boolean hasMobileWeb = false;
    public static boolean isShowJoinLogin = true;
    public static boolean hasStudyQA = false;
    public static boolean isModoo = true;
    public static boolean isShowMenuTextColorGray = false;
    public static boolean hasStudyFile = false;


}
