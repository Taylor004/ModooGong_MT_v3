package kr.co.bravecompany.modoogong.android.stdapp.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import kr.co.bravecompany.api.android.stdapp.manager.APIPropertyManager;
import kr.co.bravecompany.modoogong.android.stdapp.application.ModooGong;
import kr.co.bravecompany.player.android.stdapp.manager.PlayerPropertyManager;

/**
 * Created by BraveCompany on 2016. 11. 9..
 */

public class PropertyManager {
    private Context mContext;
    private static PropertyManager instance;
    public static PropertyManager getInstance() {
        if (instance == null) {
            instance = new PropertyManager();
        }
        return instance;
    }
    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;

    private PropertyManager() {
        mContext = ModooGong.getContext();
        mPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        mEditor = mPrefs.edit();
    }

    private static final String APP_VER = "app_ver";
    //User
    private static final String USER_ID = "user_id";
    private static final String USER_PW = "user_pw";
    private static final String USER_NAME = "user_name";
    //Last play
    private static final String LAST_PLAY = "last_play";
    //Setting
    private static final String NOTICE_DATA = "notice_data";
    private static final String PUSH = "push";
    //Guide
    private static final String GO_DOWN_GUIDE = "go_down_guide";
    //
    private static final String TASK_REMOVED = "task_removed";

    private static final String PROFILE_PHOTO_PATH = "profile_photo_path";
    private static final String PROFILE_TODAY_MSG = "profile_today_msg";


    public String getAppVer() {
        return mPrefs.getString(APP_VER, null);
    }

    public void setAppVer(String appVer) {
        mEditor.putString(APP_VER, appVer);
        mEditor.commit();
    }
    public String getTodayMsg() {
        return mPrefs.getString(PROFILE_TODAY_MSG, "");
    }

    public void setTodayMsg(String msg) {
        mEditor.putString(PROFILE_TODAY_MSG, msg);
        mEditor.commit();
    }
    public String getProfilePhotoPath() {
        return mPrefs.getString(PROFILE_PHOTO_PATH, "");
    }

    public void setProfilePhotoPath(String path) {
        mEditor.putString(PROFILE_PHOTO_PATH, path);
        mEditor.commit();
    }

    public String getUserKey() {
        return APIPropertyManager.getInstance(mContext).getUserKey();
    }

    public void setUserKey(String userKey) {
        APIPropertyManager.getInstance(mContext).setUserKey(userKey);
    }

    public String getUserID() {
        return mPrefs.getString(USER_ID, "");
    }

    public void setUserID(String userID) {
        mEditor.putString(USER_ID, userID);
        mEditor.commit();
    }

    public String getUserPW() {
        return mPrefs.getString(USER_PW, "");
    }

    public void setUserPW(String userPW) {
        mEditor.putString(USER_PW, userPW);
        mEditor.commit();
    }

    public String getUserName() {
        return mPrefs.getString(USER_NAME, "");
    }

    public void setUserName(String userName) {
        mEditor.putString(USER_NAME, userName);
        mEditor.commit();
    }

    public String getLastPlay() {
        return mPrefs.getString(LAST_PLAY, "");
    }

    public void setLastPlay(String last) {
        mEditor.putString(LAST_PLAY, last);
        mEditor.commit();
    }

    public boolean isNoticeData() {
        return mPrefs.getBoolean(NOTICE_DATA, true);
    }

    public void setNoticeData(boolean noticeData) {
        mEditor.putBoolean(NOTICE_DATA, noticeData);
        mEditor.commit();
    }

    public boolean isPush() {
        return mPrefs.getBoolean(PUSH, true);
    }

    public void setPush(boolean push) {
        mEditor.putBoolean(PUSH, push);
        mEditor.commit();
    }

    public boolean isShowGoDownGuide() {
        return mPrefs.getBoolean(GO_DOWN_GUIDE, true);
    }

    public void setShowGoDownGuide(boolean show) {
        mEditor.putBoolean(GO_DOWN_GUIDE, show);
        mEditor.commit();
    }

    public boolean isAutoContinuePlay() {
        return PlayerPropertyManager.getInstance(mContext).isAutoContinuePlay();
    }

    public void setAutoContinuePlay(boolean autoContinuePlay) {
        PlayerPropertyManager.getInstance(mContext).setAutoContinuePlay(autoContinuePlay);
    }

    public boolean isSWCodec() {
        return PlayerPropertyManager.getInstance(mContext).isSWCodec();
    }

    public void setSWCodec(boolean isSWCodec) {
        PlayerPropertyManager.getInstance(mContext).setSWCodec(isSWCodec);
    }

    public boolean isTaskRemoved() {
        return mPrefs.getBoolean(TASK_REMOVED, false);
    }

    public void setTaskRemoved(boolean taskRemoved) {
        mEditor.putBoolean(TASK_REMOVED, taskRemoved);
        mEditor.commit();
    }

    public int getScreenOrientation() {
        return PlayerPropertyManager.getInstance(mContext).getScreenOrientation();
    }

    public void setScreenOrientation(int orientation) {
        PlayerPropertyManager.getInstance(mContext).setScreenOrientation(orientation);
    }

    public boolean isSaveBrightness() {
        return PlayerPropertyManager.getInstance(mContext).isSaveBrightness();
    }

    public void setSaveBrightness(boolean save) {
        PlayerPropertyManager.getInstance(mContext).setSaveBrightness(save);
    }

    public boolean isSaveRate() {
        return PlayerPropertyManager.getInstance(mContext).isSaveRate();
    }

    public void setSaveRate(boolean save) {
        PlayerPropertyManager.getInstance(mContext).setSaveRate(save);
    }

    // =============================================================================
    // Remove
    // =============================================================================

    public void removeUserKey(){
        APIPropertyManager.getInstance(mContext).removeUserKey();
    }

    public void removeSettings(){
        mEditor.remove(LAST_PLAY);
        mEditor.remove(NOTICE_DATA);
        mEditor.remove(PUSH);
        mEditor.remove(GO_DOWN_GUIDE);
        PlayerPropertyManager.getInstance(mContext).removePlayerPrefs();
        mEditor.commit();
    }

    public void removeAllPrefs(){
        APIPropertyManager.getInstance(mContext).removeUserKey();
        mEditor.remove(USER_ID);
        mEditor.remove(USER_PW);
        mEditor.remove(USER_NAME);
        mEditor.remove(LAST_PLAY);
        mEditor.remove(NOTICE_DATA);
        mEditor.remove(PUSH);
        mEditor.remove(GO_DOWN_GUIDE);
        PlayerPropertyManager.getInstance(mContext).removePlayerPrefs();
        mEditor.remove(TASK_REMOVED);
        mEditor.commit();
    }

    public void clearAllPrefs(){
        mEditor.clear();
        mEditor.commit();
    }

}
