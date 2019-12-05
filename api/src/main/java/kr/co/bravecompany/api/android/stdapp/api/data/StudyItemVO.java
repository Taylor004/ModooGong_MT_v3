package kr.co.bravecompany.api.android.stdapp.api.data;

/**
 * Created by BraveCompany on 2016. 10. 18..
 */

public class StudyItemVO {
    private int studyOrder;
    private int studySubOrder;
    private String studyName;
    private int studyTime;
    private String recentStudyDate;
    private int lctCode;
    private String studyVodPath;
    private String studyVodKey;

    public int getStudyOrder() {
        return studyOrder;
    }

    public int getStudySubOrder() {
        return studySubOrder;
    }

    public String getStudyName() {
        return studyName;
    }

    public int getStudyTime() {
        return studyTime;
    }

    public String getRecentStudyDate() {
        return recentStudyDate;
    }

    public void setRecentStudyDate(String recentStudyDate) {
        this.recentStudyDate = recentStudyDate;
    }

    public int getLctCode() {
        return lctCode;
    }

    public String getStudyVodPath() {
        return studyVodPath;
    }

    public String getStudyVodKey() {
        return studyVodKey;
    }
}
