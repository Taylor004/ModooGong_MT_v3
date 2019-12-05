package kr.co.bravecompany.modoogong.android.stdapp.config;

/**
 * Created by BraveCompany on 2016. 10. 12..
 */

public class Tags {
    public static final String TAG_SPLASH = "splash";
    public static final String TAG_QA = "qa";
    public static final String TAG_IMAGE = "image";
    public static final String TAG_VOICE = "voice";
    public static final String TAG_QA_TYPE = "qa_type";
    public static final String TAG_LECTURE = "lecture";
    public static final String TAG_CATE = "cate";
    public static final String TAG_SCATE = "scate";
    public static final String TAG_BESTFVOD = "bestFVod";
    public static final String TAG_QA_NO = "qna_no";
    public static final String TAG_QA_DATA = "qna_data";
    public static final String TAG_NOTICE_NO = "notice_no";
    public static final String TAG_URL = "url";
    public static final String TAGE_TITLE = "title";

    public static final String TAG_HTML = "html";
    public static final String TAG_REFRESH = "refresh";
    public static final String TAG_EXAMCLASS = "examClass";
    public static final String TAG_NEW_ID = "new_id";
    public static final String TAG_GO_DOWN = "go_down";
    public static final String TAG_STUDY_FILE_NO = "study_file_no";
    //play
    public static final String TAG_PLAY = "play";
    public static final String TAG_MEDIA_CONTENT_KEY = "media_content_key";
    //Download
    public static final String TAG_DOWN_PERCENT = "down_percent";
    public static final String TAG_DOWN_STATE = "down_state";
    public static final String TAG_STUDY_KEY = "study_key";
    public static final String TAG_ERROR_CODE = "error_code";
    public static final String TAG_TASK_REMOVED = "task_removed";


    public static class LOGIN_TYPE {
        public static final int LOGIN_USER = 0;
        public static final int LOGIN_AUTO = 1;
    }

    public static class MENU_VIEW_TYPE {
        public static final int MENU_ICON = 1;
        public static final int MENU_TEXT = 2;
    }

    public static class MENU_INDEX{
        public static final int MENU_LECTURE = 0;
        public static final int MENU_DOWN_LECTURE = 1;
        public static int MENU_STUDY_QA = -1;
        public static int MENU_STUDY_FILE = -1;
        ////application setting
        public static int MENU_NOTICE = -1;
        ////application setting
        public static int MENU_ONE_TO_ONE_QA = -1;

        public static int MENU_BOOK_SALES = 2;
    }

    ////application setting
    public static int FREE_MENU_ADD_POSITION = -1;
    /*
    public static class MENU_INDEX{
        public static final int MENU_LECTURE = 0; //내강의실
        public static final int MENU_DOWN_LECTURE = 1; //다운로드 보관함
        public static final int MENU_STUDY_QA = 2; //학습문의
        public static final int MENU_FREE_LECTURE = 3; //무료강의
        public static final int MENU_NOTICE = 4; //공지사항
        public static final int MENU_ONE_TO_ONE_QA = 5; //고객센터
    }
    */

    public static class NAV_HEADER_TYPE {
        public static final int NAV_HEADER_LOGIN = 0;
        public static final int NAV_HEADER_NO_LOGIN = 1;
        public static final int NAV_HEADER_MENU_REFRESH = 2;
    }

    public static class LECTURE_MENU_GROUP_INDEX {
        public static final int LECTURE_TYPE = 0;
    }

    public static class LECTURE_MENU_INDEX{
        public static final int LECTURE_ING = 0;
        public static final int LECTURE_END = 1;
    }

    public static class STUDY_VIEW_TYPE{
        public static final int STUDY_HEADER = 1;
        public static final int STUDY_SELECT_HEADER = 2;
        public static final int STUDY_ITEM = 3;
    }

    public static class LECTURE_VIEW_TYPE{
        public static final int LECTURE_ING_ITEM = 1;
        public static final int LECTURE_END_ITEM = 2;
    }

    public static class STUDY_DOWN_STATE_TYPE{
        public static final int STUDY_DOWN_NONE = 0;
        public static final int STUDY_DOWN_ING = 1;
        public static final int STUDY_DOWN_PENDING = 2;
        public static final int STUDY_DOWN_COMPLETE = 3;
        public static final int STUDY_DOWN_PAUSE = 4;
        public static final int STUDY_DOWN_ERROR = 5;
        public static final int STUDY_DOWN_API_ING = 6;
        public static final int STUDY_DOWN_API_COMPLETE = 7;
        public static final int STUDY_DOWN_API_ERROR = 8;
    }

    public static class STUDY_BTN_TYPE {
        public static final int STUDY_DOWN = 0;
        public static final int STUDY_SELECT = 1;
        public static final int STUDY_DO_DOWN = 2;
    }

    public static class STUDY_DOWN_FAB_TYPE{
        public static final int STUDY_DOWN_DOWN = 0;
        public static final int STUDY_DOWN_PAUSE = 1;
    }

    public static class DOWNLOAD_HEADER_TYPE{
        public static final int DOWNLOAD_HEADER_NONE = 0;
        public static final int DOWNLOAD_HEADER_SELECT = 1;
        public static final int DOWNLOAD_HEADER_PAUSE = 2;
    }

    public static class DOWNLOAD_BTN_TYPE{
        public static final int DOWNLOAD_DELETE = 0;
        public static final int DOWNLOAD_SELECT = 1;
        public static final int DOWNLOAD_DO_DELETE = 2;
    }

    public static class LOCAL_STUDY_BTN_TYPE {
        public static final int LOCAL_STUDY_DELETE = 0;
        public static final int LOCAL_STUDY_SELECT = 1;
        public static final int LOCAL_STUDY_DO_DELETE = 2;
    }

    public static class STUDY_FILE_VIEW_TYPE{
        public static final int STUDY_FILE_HEADER = 1;
        public static final int STUDY_FILE_ITEM = 2;
        public static final int STUDY_FILE_FOOTER = 3;
    }

    public static class FREE_LECTURE_VIEW_TYPE{
        public static final int FREE_LECTURE_DIVIDER = 0;
        public static final int FREE_LECTURE_HEADER = 1;
        public static final int FREE_LECTURE_ITEM = 2;
        public static final int FREE_LECTURE_FOOTER = 3;
    }

    public static class FREE_EXPLAIN_VIEW_TYPE{
        public static final int FREE_EXPLAIN_ITEM = 2;
        public static final int FREE_EXPLAIN_FOOTER = 3;
    }

    public static class FREE_EXPLAIN_EXAM_CLASS{
        public static final int FREE_EXPLAIN_EXAM_CLASS_1 = 1;
        public static final int FREE_EXPLAIN_EXAM_CLASS_2 = 2;
        public static final int FREE_EXPLAIN_EXAM_CLASS_3 = 3;
    }

    public static class NOTICE_TYPE_CD {
        public static final int NOTICE_NONE = 1;
        public static final int NOTICE_EVENT = 2;
        public static final int NOTICE_FIRST = 3;
    }

    public static class SETTING_TYPE{
        public static final int SETTING_DIVIDER = 0;
        public static final int SETTING_SWITCH = 1;
        public static final int SETTING_TEXT = 2;
    }

    public static class SETTING_SWITCH_INDEX {
        public static final int SETTING_NOTICE_DATA = 1;
        public static final int SETTING_PUSH = 2;
    }

    public static class SETTING_PLAYER_INDEX {
        public static final int SETTING_AUTO_CONTINUE_PLAY= 4;
        public static final int SETTING_SCREEN_ORIENTATION = 5;
        public static final int SETTING_SAVE_RATE = -1;
        public static final int SETTING_SAVE_BRIGHTNESS = 6;
        public static final int SETTING_SW_CODEC = 7;
    }

    public static class SETTING_TEXT_INDEX {
        public static final int SETTING_MEMORY_TOTAL = 9;
        public static final int SETTING_MEMORY_AVAILABLE = 10;
        public static final int SETTING_MEMORY_STUDY = 11;
    }

    public static class QA_TYPE {
        public static final int QA_STUDY = 0;
        public static final int QA_ONE_TO_ONE = 1;
    }

    public static class QA_PAGE_INDEX {
        public static final int QA_PAGE_Q_DO = 0;
        public static final int QA_PAGE_Q_LIST = 1;
    }

    public static class STUDY_QA_MENU_GROUP_INDEX {
        public static final int LECTURE_TYPE = 0;
        public static final int BOOK_TYPE = 1;
    }

    ////application setting
    public static class STUDY_QA_MENU_INDEX {
        public static int QA_MENU_SUBJECT = -1000;
        public static int QA_MENU_TYPE = -1000;
        public static int QA_MENU_TEACHER = -1000;
        public static int QA_MENU_LECTURE = -1000;
        public static int QA_MENU_BOOK = -1000;
        public static int QA_MENU_PAGE = -1000;
    }

    /*
    public static class STUDY_QA_MENU_INDEX {
        public static final int QA_MENU_SUBJECT = 0; //과목
        public static final int QA_MENU_TYPE = 1; //질문유형
        public static int QA_MENU_TEACHER = 2; //강사명
        public static final int QA_MENU_LECTURE = 3; //강좌
        public static int QA_MENU_BOOK = 4; //교재
        public static int QA_MENU_PAGE = 5; //교재페이지
    }
    */

    public static class ONE_TO_ONE_QA_MENU_INDEX {
        public static final int QA_MENU_TYPE = 0;
    }

    ////application setting
    public static class QA_TYPE_CD {
        public static String QA_TYPE_LECTURE = "-1000";
        public static String QA_TYPE_STUDY = "-1000";
        public static String QA_TYPE_ETC = "-1000";
        public static String QA_TYPE_FEEDBACK = "-1000";
    }

    /*
    public static class QA_TYPE_CD {
        public static final String QA_TYPE_LECTURE = "1"; //학습상담
        public static final String QA_TYPE_STUDY = "2"; //수업(강의)내용
        public static final String QA_TYPE_ETC = "3"; //기타
        public static final String QA_TYPE_FEEDBACK = "4"; //답변첨삭
    }
     */

    public static class QA_UPLOAD_TYPE {
        public static final int QA_UPLOAD_NONE = -1;
        public static final int QA_UPLOAD_IMAGE = 0;
        public static final int QA_UPLOAD_VOICE = 1;
        public static final int QA_UPLOAD_FILE = 2;
    }

    public static class VOICE_RECORD_BTN_TYPE{
        public static final int VOICE_RECORD_DO_RECORD = 0;
        public static final int VOICE_RECORD_STOP = 1;
        public static final int VOICE_RECORD_PLAY = 2;
        public static final int VOICE_RECORD_PLAY_RESETUP = 3;
    }

    public static class QA_STATE_TYPE {
        public static final int QA_STATE_ING = 0;
        public static final int QA_STATE_END = 1;
    }

    public static class QA_DETAIL_VIEW_TYPE{
        public static final int QA_DETAIL_ITEM = 1;
        public static final int QA_DETAIL_ANSWER = 2;
    }

    public static class QA_VIEW_TYPE{
        public static final int QA_ITEM = 2;
        public static final int QA_FOOTER = 3;
    }

    public static class NETWORK_TYPE{
        public static final int NETWORK_NOT_CONNECTED = 0;
        public static final int NETWORK_WIFI = 1;
        public static final int NETWORK_MOBILE = 2;
    }
}
