package kr.co.bravecompany.modoogong.android.stdapp.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.view.inputmethod.InputMethodManager;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;

/**
 * Created by BraveCompany on 2016. 10. 12..
 */

public class SystemUtils {
    /**
     * Destroy app
     */
    public static void onDestroyApp() {
        System.exit(0);
    }

    /**
     * Process to shut down apps
     */
    public static void killProcess(Activity activity) {
        activity.moveTaskToBack(true);
        activity.finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    // =============================================================================
    // Device
    // =============================================================================

    /**
     * Get width pixel of the screen
     *
     * @param context context
     * @return width pixel
     */
    public static int getScreenWidthPx(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * Get height pixel of the screen
     *
     * @param context context
     * @return height pixel
     */
    public static int getScreenHeightPx(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * Get pixel short side of the screen
     *
     * @param context context
     * @return screen short side px
     */
    public static int getScreenWidth(Context context) {
        int width = getScreenWidthPx(context);
        int height = getScreenHeightPx(context);
        if (width > height) {
            return height;
        } else {
            return width;
        }
    }

    /**
     * Get pixel long side of the screen
     *
     * @param context context
     * @return screen long side px
     */
    public static int getScreenHeight(Context context) {
        int width = getScreenWidthPx(context);
        int height = getScreenHeightPx(context);
        if (width > height) {
            return width;
        } else {
            return height;
        }
    }

    /**
     * Show keyboard
     *
     * @param activity activity
     */
    public static void showKeyboard(Activity activity) {
        if (activity != null) {
            if(!KeyboardVisibilityEvent.isKeyboardVisible(activity)) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        }
    }

    /**
     * Hide keyboard
     *
     * @param activity activity
     */
    public static void hideSoftKeyboard(Activity activity) {
        if (activity != null) {
            if(KeyboardVisibilityEvent.isKeyboardVisible(activity)) {
                InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    // =============================================================================
    // Device info
    // =============================================================================

    public static String getOS(){
        return "Android";
    }

    public static String getMthd(){
        return "A";
    }

    public static String getMacAddress(Context context){
        boolean bIsWifiOff=false;
        WifiManager wimanager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        if(!wimanager.isWifiEnabled()){
            wimanager.setWifiEnabled(true);
            bIsWifiOff = true;
        }
        String macAddress = wimanager.getConnectionInfo().getMacAddress();
        if(bIsWifiOff){
            wimanager.setWifiEnabled(false);
            bIsWifiOff = false;
        }
        return macAddress;
    }

    public static String getIpAddress(Context context){
        boolean bIsWifiOff=false;
        WifiManager wimanager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        if(!wimanager.isWifiEnabled()){
            wimanager.setWifiEnabled(true);
            bIsWifiOff = true;
        }
        int ipAddress = wimanager.getConnectionInfo().getIpAddress();
        // Convert little-endian to big-endianif needed
        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            ipAddress = Integer.reverseBytes(ipAddress);
        }
        byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();
        String ipAddressString;
        try {
            ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
        } catch (UnknownHostException ex) {
            ipAddressString = null;
        }
        if(bIsWifiOff){
            wimanager.setWifiEnabled(false);
            bIsWifiOff = false;
        }
        return ipAddressString;
    }

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return Tags.NETWORK_TYPE.NETWORK_WIFI;

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return Tags.NETWORK_TYPE.NETWORK_MOBILE;
        }
        return Tags.NETWORK_TYPE.NETWORK_NOT_CONNECTED;
    }

    public static boolean isNetworkConnected(Context context){
        return (SystemUtils.getConnectivityStatus(context) != Tags.NETWORK_TYPE.NETWORK_NOT_CONNECTED);
    }

    public static String getAndroidID(Context context){
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getDeviceInfo(Context context){
        return String.format(context.getString(R.string.login_device_info),
                getAppVersionName(context),
                getAppPackageName(context),
                getVersionCode(context),
                getOS(), Build.VERSION.RELEASE,
                Build.MANUFACTURER, Build.MODEL);
    }

    /**
     * Get app version name
     *
     * @param context context
     * @return app version
     */
    public static String getAppVersionName(Context context) {
        String version = null;

        try {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            log.e(log.getStackTraceString(e));
        }

        return version;
    }

    /**
     * Get app version code
     *
     * @param context context
     * @return app version
     */
    public static int getVersionCode(Context context) {
        int version = 0;
        try {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            log.e(log.getStackTraceString(e));
        }
        return version;
    }

    /**
     * Get app package name
     *
     * @param context context
     * @return app package name
     */
    public static String getAppPackageName(Context context) {
        String name = null;

        try {
            name = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.packageName;
        } catch (Exception e) {
            log.e(log.getStackTraceString(e));
        }

        return name;
    }

    /**
     * Get app name
     *
     * @param context context
     * @return app name
     */
    public static String getAppName(Context context) {
        String name = null;

        try {
            name = context.getPackageManager().getApplicationLabel(context.getApplicationInfo()).toString();
        } catch (Exception e) {
            log.e(log.getStackTraceString(e));
        }

        return name;
    }
}
