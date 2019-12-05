package kr.co.bravecompany.api.android.stdapp.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


/**
 * Created by BraveCompany on 2016. 11. 9..
 */

public class APIPropertyManager {
    private static APIPropertyManager instance;
    public static APIPropertyManager getInstance(Context context) {
        if (instance == null) {
            instance = new APIPropertyManager(context);
        }
        return instance;
    }
    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;

    private APIPropertyManager(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mPrefs.edit();
    }

    //User
    private static final String USER_KEY = "user_key";


    public String getUserKey() {
        return mPrefs.getString(USER_KEY, null);
    }

    public void setUserKey(String userKey) {
        mEditor.putString(USER_KEY, userKey);
        mEditor.commit();
    }

    // =============================================================================
    // Remove
    // =============================================================================

    public void removeUserKey(){
        mEditor.remove(USER_KEY);
        mEditor.commit();
    }
}
