package kr.co.bravecompany.api.android.stdapp.api.data;

/**
 * Created by BraveCompany on 2016. 11. 7..
 */

public class LoginRequest {
    private String charset = "UTF-8";
    private String userAgent;
    private String id;
    private String password;
    private String os;
    private String os_ver;
    private String maddr1;
    private String maddr2;
    private String mobile_key;
    private String serial;
    private String mthd;
    private int isAuto;

    public String toString(){
        return "id: " + id + " password: " + password + " os: " + os + " os_ver: " + os_ver +
                " maddr1: " + maddr1 + " maddr2: " + maddr2 + " mobile_key: " + mobile_key +
                " mthd: " + mthd + "isAuto: " + isAuto;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getOs_ver() {
        return os_ver;
    }

    public void setOs_ver(String os_ver) {
        this.os_ver = os_ver;
    }

    public String getMaddr1() {
        return maddr1;
    }

    public void setMaddr1(String maddr1) {
        this.maddr1 = maddr1;
    }

    public String getMaddr2() {
        return maddr2;
    }

    public void setMaddr2(String maddr2) {
        this.maddr2 = maddr2;
    }

    public String getMobile_key() {
        return mobile_key;
    }

    public void setMobile_key(String mobile_key) {
        this.mobile_key = mobile_key;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getMthd() {
        return mthd;
    }

    public void setMthd(String mthd) {
        this.mthd = mthd;
    }

    public int getAuto() {
        return isAuto;
    }

    public void setAuto(int isAuto) {
        this.isAuto = isAuto;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
