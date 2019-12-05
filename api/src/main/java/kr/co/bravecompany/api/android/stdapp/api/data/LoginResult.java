package kr.co.bravecompany.api.android.stdapp.api.data;

/**
 * Created by BraveCompany on 2016. 11. 7..
 */

public class LoginResult {
    private String rstState;
    private String userKey;
    private String userId;
    private String userName;
    private String userType;
    private String userState;
    private String email;

    public String getRstState() {
        return rstState;
    }

    public String getUserKey() {
        return userKey;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserType() {
        return userType;
    }

    public String getUserState() {
        return userState;
    }

    public String getEmail() {
        return email;
    }
}
