package kr.co.bravecompany.api.android.stdapp.api.utils;

import android.content.Context;
import android.util.Log;

import kr.co.bravecompany.api.android.stdapp.R;
import kr.co.bravecompany.api.android.stdapp.api.config.APIAddress;
import kr.co.bravecompany.api.android.stdapp.api.config.APIConfig;
import kr.co.bravecompany.api.android.stdapp.api.config.APIParamValue;
import kr.co.bravecompany.api.android.stdapp.manager.APIPropertyManager;

/**
 * Created by BraveCompany on 2016. 11. 7..
 */

public class APIManager {
    private static final String TAG = "APIManager";
    /**
     * true : develop, false : release
     */
    public static boolean isDebug = false;
    public static String host = "api_host";

    private static APIManager mInstance;

    public static APIManager getInstance() {
        if (mInstance == null) {
            mInstance = new APIManager();
        }
        return mInstance;
    }

    // =============================================================================
    // Auth
    // =============================================================================

    public String getLoginUrl(Context context){
        return getBaseUrl(context) + APIAddress.USERS + APIAddress.LOGIN;
    }

    // =============================================================================
    // Study
    // =============================================================================

    public String getLectureListUrl(Context context, int page, String state, int perPage){
        String url = getBaseUrl(context) + APIAddress.USERS + APIAddress.STUDY + "?" +
                        APIParamValue.CHARSET_UTF8 +
                        APIParamValue.USERKEY + APIPropertyManager.getInstance(context).getUserKey() +
                        APIParamValue.MTHD_MS +
                        APIParamValue.PAGE + String.valueOf(page);
        if(perPage == -1){
            perPage = APIConfig.DEFAULT_PERPAGE;
        }
        url = url + APIParamValue.PERPAGE + String.valueOf(perPage);
        if(state != null){
            url = url + APIParamValue.STATE + state;
        }
        return url;
    }

    public String getStudyListUrl(Context context, int studyLectureNo, int lectureCode){
        return getBaseUrl(context) + APIAddress.USERS + APIAddress.STUDY +
                "/" + String.valueOf(studyLectureNo) + "?" +
                APIParamValue.CHARSET_UTF8 +
                APIParamValue.USERKEY + APIPropertyManager.getInstance(context).getUserKey() +
                APIParamValue.MTHD_MS +
                APIParamValue.LECTURECODE + String.valueOf(lectureCode);
    }

    public String getFreeLectureCateUrl(Context context){
        return getBaseUrl(context) + APIAddress.ETCS + APIAddress.APPMAIN + "?" +
                APIParamValue.CHARSET_UTF8 +
                APIParamValue.MTHD_MS;
    }

    public String getFreeStudyListUrl(Context context, int page, int cate, String scate, int perPage){
        String url = getBaseUrl(context) + APIAddress.VIDEOS + APIAddress.FVODS + "?" +
                        APIParamValue.CHARSET_UTF8 +
                        APIParamValue.MTHD_MS +
                        APIParamValue.CATE + String.valueOf(cate) +
                        APIParamValue.PAGE + String.valueOf(page);
        if(perPage == -1){
            perPage = APIConfig.DEFAULT_PERPAGE;
        }
        url = url + APIParamValue.PERPAGE + String.valueOf(perPage);
        if(scate != null){
            url = url + APIParamValue.SCATE + scate;
        }
        return url;
    }


/*
    public String getFreeLectureCateUrl(Context context){

        String baseUrl = getBaseUrl(context);

       // String testURL = String.format(context.getString(R.string.develop_base_url), "bravespk.com");

        return baseUrl + APIAddress.ETCS + APIAddress.APPMAIN + "?" +
                APIParamValue.CHARSET_UTF8 +
                APIParamValue.MTHD_MS;
    }

    public String getFreeStudyListUrl(Context context, int page, int cate, String scate, int perPage){

        String baseUrl = getBaseUrl(context);

        //String testURL = String.format(context.getString(R.string.develop_base_url), "bravespk.com");

        String url = baseUrl + APIAddress.VIDEOS + APIAddress.FVODS + "?" +
                APIParamValue.CHARSET_UTF8 +
                APIParamValue.MTHD_MS +
                APIParamValue.CATE + String.valueOf(cate) +
                APIParamValue.PAGE + String.valueOf(page);
        if(perPage == -1){
            perPage = APIConfig.DEFAULT_PERPAGE;
        }
        url = url + APIParamValue.PERPAGE + String.valueOf(perPage);
        if(scate != null){
            url = url + APIParamValue.SCATE + scate;
        }
        return url;
    }
*/
    public String getStudyVodUrl(Context context, int studyLectureNo, int lctCode, boolean isPlay){
        String url = getBaseUrl(context) + APIAddress.USERS + APIAddress.STUDYVOD +
                        "/" + String.valueOf(studyLectureNo) + "?" +
                        APIParamValue.CHARSET_UTF8 +
                        APIParamValue.USERKEY + APIPropertyManager.getInstance(context).getUserKey() +
                        APIParamValue.MTHD_MS +
                        APIParamValue.LECTURECODE + String.valueOf(lctCode);
        Log.d("TAG","AAAAAA-"+url);
        if(isPlay){
            url = url + APIParamValue.STUDYMTHD + APIConfig.STUDYMTHD_TYPE.STUDYMTHD_MS;


        }else{
            url = url + APIParamValue.STUDYMTHD + APIConfig.STUDYMTHD_TYPE.STUDYMTHD_MD;
        }
        return url;
    }

    public String getFreeExplainStudyListUrl(Context context, int page, int examClass, int perPage){
        String url = getBaseUrl(context) + APIAddress.VIDEOS + APIAddress.EXPLAIN + "?" +
                APIParamValue.CHARSET_UTF8 +
                APIParamValue.MTHD_MS +
                APIParamValue.PAGE + String.valueOf(page);
        if(perPage == -1){
            perPage = APIConfig.DEFAULT_PERPAGE;
        }
        url = url + APIParamValue.PERPAGE + String.valueOf(perPage);
        if(examClass != -1){
            url = url + APIParamValue.EXAMCLASS + String.valueOf(examClass);
        }
        return url;
    }

    // =============================================================================
    // Notice
    // =============================================================================

    public String getNoticeListUrl(Context context, int page, int perPage){
        String url = getBaseUrl(context) + APIAddress.BOARDS + APIAddress.NOTICES + "?" +
                        APIParamValue.CHARSET_UTF8 +
                        APIParamValue.MTHD_MS +
                        APIParamValue.DISPLAYAREA + APIConfig.DISPLAYAREA_TYPE.DISPLAYAREA_3 +
                        APIParamValue.PAGE + String.valueOf(page);
        if(perPage == -1){
            perPage = APIConfig.DEFAULT_PERPAGE;
        }
        url = url + APIParamValue.PERPAGE + String.valueOf(perPage);
        return url;
    }

    public String getNoticeDetailUrl(Context context, int noticeNo){
        return getBaseUrl(context) + APIAddress.BOARDS + APIAddress.NOTICES +
                "/" + String.valueOf(noticeNo) + "?" +
                APIParamValue.CHARSET_UTF8 +
                APIParamValue.MTHD_MS;
    }

    // =============================================================================
    // Study QA
    // =============================================================================

    public String getStudyQAListUrl(Context context, int page, int perPage){
        String url = getBaseUrl(context) + APIAddress.BOARDS + APIAddress.INQUIRIES + "?" +
                APIParamValue.CHARSET_UTF8 +
                APIParamValue.USERKEY + APIPropertyManager.getInstance(context).getUserKey() +
                APIParamValue.MTHD_MS +
                APIParamValue.PAGE + String.valueOf(page);
        if(perPage == -1){
            perPage = APIConfig.DEFAULT_PERPAGE;
        }
        url = url + APIParamValue.PERPAGE + String.valueOf(perPage);
        return url;
    }

    public String getStudyQADetailUrl(Context context, int qnaNo){
        return getBaseUrl(context) + APIAddress.BOARDS + APIAddress.INQUIRIES +
                "/" + String.valueOf(qnaNo) + "?" +
                APIParamValue.CHARSET_UTF8 +
                APIParamValue.USERKEY + APIPropertyManager.getInstance(context).getUserKey() +
                APIParamValue.MTHD_MS;
    }

    public String getUpdateStudyQAUrl(Context context){
        return getBaseUrl(context) + APIAddress.BOARDS + APIAddress.INQUIRIES;
    }

    // =============================================================================
    // 1:1 QA
    // =============================================================================

    public String getOneToOneQAListUrl(Context context, int page, int perPage){
        String url = getBaseUrl(context) + APIAddress.BOARDS + APIAddress.QNAS + "?" +
                APIParamValue.CHARSET_UTF8 +
                APIParamValue.USERKEY + APIPropertyManager.getInstance(context).getUserKey() +
                APIParamValue.MTHD_MS +
                APIParamValue.PAGE + String.valueOf(page);
        if(perPage == -1){
            perPage = APIConfig.DEFAULT_PERPAGE;
        }
        url = url + APIParamValue.PERPAGE + String.valueOf(perPage);
        return url;
    }

    public String getOneToOneQADetailUrl(Context context, int inquiryNo){
        return getBaseUrl(context) + APIAddress.BOARDS + APIAddress.QNAS +
                "/" + String.valueOf(inquiryNo) + "?" +
                APIParamValue.CHARSET_UTF8 +
                APIParamValue.USERKEY + APIPropertyManager.getInstance(context).getUserKey() +
                APIParamValue.MTHD_MS;
    }

    public String getUpdateOneToOneQAUrl(Context context){
        return getBaseUrl(context) + APIAddress.BOARDS + APIAddress.QNAS;
    }

    // =============================================================================
    // Study File
    // =============================================================================

    public String getStudyFileListUrl(Context context, int page){
        return getBaseUrl(context) + APIAddress.BOARDS + APIAddress.FILEBOARDLIST + "?" +
                APIParamValue.CHARSET_UTF8 +
                APIParamValue.USER_KEY + APIPropertyManager.getInstance(context).getUserKey() +
                APIParamValue.MTHD_MS +
                APIParamValue.PG + String.valueOf(page);
    }

    public String getStudyFileDetailUrl(Context context, int no){
        return getBaseUrl(context) + APIAddress.BOARDS + APIAddress.FILEBOARDVIEW + "?" +
                APIParamValue.CHARSET_UTF8 +
                APIParamValue.USER_KEY + APIPropertyManager.getInstance(context).getUserKey() +
                APIParamValue.MTHD_MS +
                APIParamValue.NO + String.valueOf(no);
    }

    // =============================================================================
    // Board
    // =============================================================================

    public String getStudyQABoardCateUrl(Context context){
        return getSiteBoardCateUrl(context, APIConfig.BOARDKND_STUDY_QA);
                //+
                //APIParamValue.CATETYP + APIConfig.CATETYP_TYPE.CATETYP_Y +
                //APIParamValue.CHRTYP + String.valueOf(APIConfig.CHRTYP_TYPE.CHRTYP_ALL);
    }

    public String getOneToOneQABoardCateUrl(Context context){
        return getSiteBoardCateUrl(context, APIConfig.BOARDKND_ONE_TO_ONE_QA);
    }

    public String getSiteBoardCateUrl(Context context, String boardKnd){
        return getBaseUrl(context) + APIAddress.BOARDS + APIAddress.SITEBOARD + "?" +
                APIParamValue.CHARSET_UTF8 +
                APIParamValue.USERKEY + APIPropertyManager.getInstance(context).getUserKey() +
                APIParamValue.MTHD_MS +
                APIParamValue.BOARDKND + boardKnd +
                APIParamValue.BOARDCATE + APIConfig.BOARDCATE_TYPE.BOARDCATE_SITEBOARD +
                APIParamValue.CATETYP + APIConfig.CATETYP_TYPE.CATETYP_Y +
                APIParamValue.CHRTYP + String.valueOf(APIConfig.CHRTYP_TYPE.CHRTYP_M_ING +
                APIParamValue.BKTYP + APIConfig.BKTYP_TYPE.BKTYP_ALL +
                APIParamValue.TCHTYP + APIConfig.TCHTYP_TYPE.TCHTYP_ALL);
    }

    public String getCodeUrl(Context context, String codeKnd, String codeCd){
        return getBaseUrl(context) + APIAddress.COMMON + APIAddress.CODES +
                "/" + codeKnd + "?" +
                APIParamValue.CHARSET_UTF8 +
                APIParamValue.CODECD + codeCd;
    }

    public String getDeleteBoardFileUrl(Context context){
        return getBaseUrl(context) + APIAddress.BOARDS + APIAddress.DELFILE;
    }

    // =============================================================================
    // Base
    // =============================================================================

    public static String getFileUrl(Context context, String filePath){
        String base = APIManager.getInstance().getBaseFileUrl(context);
        if(filePath != null && filePath.length() != 0){
            return base + filePath;
        }
        return null;
    }

    public String getBaseFileUrl(Context context) {
        return String.format(context.getString(R.string.base_file_url), host);
    }

    public static String getFreePlayUrl(Context context, String vedioKey){
        String base = APIManager.getInstance().getBaseFreePlayUrl(context);
        if(vedioKey != null && vedioKey.length() != 0){
            return base + vedioKey;
        }
        return null;
    }

    public String getBaseFreePlayUrl(Context context){
        return context.getString(R.string.base_free_play_url);
    }

    /**
     * Get base api url
     *
     * @return base api rul
     */
    private String getBaseUrl(Context context) {
        if (isDebug)
            return String.format(context.getString(R.string.develop_base_url), host);
        else
            return String.format(context.getString(R.string.release_base_url), host);
    }


    // =============================================================================
    // Profile
    // =============================================================================

    public String getSetUserPhotoUrl(Context context){
        return getBaseUrl(context) + "/users/regPhoto";
    }

    public String getSetUserPushKeyUrl(Context context){
        return getBaseUrl(context) + "/users/regPush";
    }

    public String getSetUserMsgUrl(Context context){
        return getBaseUrl(context) + "/users/regMsg";
    }

    public String getGetPassListUrl(Context context){
        return getBaseUrl(context) + "/users/stdPass";
    }

    public String getGetLectureListBelongToPassUrl(Context context){
        //return getBaseUrl(context) + "/users/stdPassChrList";
        return getBaseUrl(context) + "/users/stdPass";
    }


    public String getSubscribeLectureBelongToPassUrl(Context context){
        return getBaseUrl(context) + "/users/getAddChr";
    }


    public String getUnsubscribeLectureBelongToPassUrl(Context context){
        return getBaseUrl(context) + "/users/getDelChr";
    }


    public String getSubjectList(Context context){
        return getBaseUrl(context) + "/users/subjTch";
    }

    public String getTeacherList(Context context){
        return getBaseUrl(context) + "/users/subjTch/tch";
    }

    public String getLectureDetailLink(String lectureCode)
    {
        return String.format("https://m.%s/prod/chr/%s", host, lectureCode);
    }


    // =============================================================================
    // 프로필 페이지
    // =============================================================================

    public String getExamInfoUrl(Context context) {
        return getBaseUrl(context) + "/infos/examList";
    }


    public String getTodayRankUrl(Context context) {
        return getBaseUrl(context) + "/statis/todayRank";
    }

    public String getOnAirUrl(Context context) {
        return getBaseUrl(context) + "/statis/onAir";
    }


    // =============================================================================
    // 학습 기록 페이지
    // =============================================================================

   public String getTodayTime(Context context) {
        return getBaseUrl(context) + "/statis/todayStudyTime";
    }

    public String getDailyTime(Context context) {
        return getBaseUrl(context) + "/statis/dailyStudyTime";
    }

    //public String getWeeklyTimeUrl(Context context) {
    // return getBaseUrl(context) + "/statis/weekly_time";
    //}

    public String getWeeklySubjectTimeUrl(Context context) {
        return getBaseUrl(context) + "/statis/weeklyStudyTime";
    }

    public String getHistoricalSubjectTimeUrl(Context context) {
        return getBaseUrl(context) + "/statis/subjectStudyTime";
    }

    public String getTotalSubjectViewUrl(Context context) {
        return getBaseUrl(context) + "/statis/subjectStudyLecture";
    }


    // =============================================================================
    // 마이 페이지
    // =============================================================================

//    public String getMyCouponInfoUrl(Context context) {
//        return getBaseUrl(context) + "/my_pages/my_coupon_info";
//    }
//
//    public String getMyCartInfoUrl(Context context) {
//        return getBaseUrl(context) + "/my_pages/my_cart_info";
//    }

    public String getMyCountInfoUrl(Context context) {
        return getBaseUrl(context) + "/users/countInfo";
    }

}
