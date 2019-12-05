package kr.co.bravecompany.api.android.stdapp.api.config;

/**
 * Created by BraveCompany on 2016. 11. 7..
 */

public class APIConfig {

    public static final int DEFAULT_PERPAGE = 20;
    public static final String CHARSET = "charset";
    public static final String BOARDKND_STUDY_QA = "03";
    public static final String BOARDKND_ONE_TO_ONE_QA = "05";
    public static final String BOARDTYP_SITE = "SITE";
    public static final int ISEDITER = 0;

    public static class LECTURE_STATE_TYPE{
        public static final String LECTURE_ING_ITEM = "Y";
        public static final String LECTURE_END_ITEM = "N";
    }

    public static class CATETYP_TYPE{
        public static final String CATETYP_Y = "Y";
        public static final String CATETYP_N = "N";
    }

    public static class BOARDCATE_TYPE{
        public static final String BOARDCATE_SITEBOARD = "Y";
        public static final String BOARDCATE_NOT_SITEBOARD = "N";
    }

    public static class CHRTYP_TYPE{
        public static final int CHRTYP_ALL = 0;
        public static final int CHRTYP_M_ING = 1;
        public static final int CHRTYP_M = 2;
    }

    public static class BKTYP_TYPE{
        public static final int BKTYP_ALL = 0;
        public static final int BKTYP_LECTURE = 1;
        public static final int BKTYP_LECTURE_ING = 2;
    }

    public static class TCHTYP_TYPE{
        public static final int TCHTYP_ALL = 0;
        public static final int TCHTYP_ING = 1;
    }

    public static class DISPLAYAREA_TYPE{
        public static final int DISPLAYAREA_3 = 3;
        public static final int DISPLAYAREA_4 = 4;
    }

    public static class STUDYMTHD_TYPE{
        public static final String STUDYMTHD_MS = "MS";
        public static final String STUDYMTHD_MD = "MD";
    }


}
