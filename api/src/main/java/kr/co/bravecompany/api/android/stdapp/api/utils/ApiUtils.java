package kr.co.bravecompany.api.android.stdapp.api.utils;

import java.util.List;

/**
 * Created by BraveCompany on 2016. 12. 13..
 */

public class ApiUtils {

    // =============================================================================
    // JSON
    // =============================================================================

    public static String replaceBody(String body){
        if(body == null) {
            return null;
        }
        String result = replaceNull(body);
        return result;
    }

    public static String replaceNull(String body){
        if(body == null) {
            return null;
        }
        String result = body.replace("\"\"", "null");
        result = result.replace("\"[]\"", "null");
        result = result.replace("\"{}\"", "null");
        result = result.replace("\"null\"", "null");
        return result;
    }

    public static String replaceBackslash(String body){
        if(body == null) {
            return null;
        }
        String result = body.replace("\\", "\\\\");
        return result;
    }

    public static <T> boolean isNullOrEmpty(List<T> list)
    {
        return list ==null || list.size() ==0;
    }
}
