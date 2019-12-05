package kr.co.bravecompany.modoogong.android.stdapp.db.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import kr.co.bravecompany.api.android.stdapp.api.data.StudyItemVO;
import kr.co.bravecompany.modoogong.android.stdapp.data.StudyData;

/**
 * Created by BraveCompany on 2016. 12. 5..
 */

public class Study extends RealmObject{

    @PrimaryKey
    private String studyKey;
    private int lctCode;
    private int studyOrder;
    private int studySubOrder;
    private String studyName;
    private int studyTime;
    private String studyVodKey;
    private int studyLectureNo;
    private String mediaContentKey;
    private int state;
    private int errorCode;
    private String vodInfo;

    //
    public String getStudyKey() {
        return studyKey;
    }

    public void setStudyKey(String studyKey) {
        this.studyKey = studyKey;
    }
    //

    public int getLctCode() {
        return lctCode;
    }

    public void setLctCode(int lctCode) {
        this.lctCode = lctCode;
    }

    public int getStudyOrder() {
        return studyOrder;
    }

    public void setStudyOrder(int studyOrder) {
        this.studyOrder = studyOrder;
    }

    public int getStudySubOrder() {
        return studySubOrder;
    }

    public void setStudySubOrder(int studySubOrder) {
        this.studySubOrder = studySubOrder;
    }

    public String getStudyName() {
        return studyName;
    }

    public void setStudyName(String studyName) {
        this.studyName = studyName;
    }

    public int getStudyTime() {
        return studyTime;
    }

    public void setStudyTime(int studyTime) {
        this.studyTime = studyTime;
    }

    public String getStudyVodKey() {
        return studyVodKey;
    }

    public void setStudyVodKey(String studyVodKey) {
        this.studyVodKey = studyVodKey;
    }

    //
    public int getStudyLectureNo() {
        return studyLectureNo;
    }

    public void setStudyLectureNo(int studyLectureNo) {
        this.studyLectureNo = studyLectureNo;
    }
    //

    public String getMediaContentKey() {
        return mediaContentKey;
    }

    public void setMediaContentKey(String mediaContentKey) {
        this.mediaContentKey = mediaContentKey;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getVodInfo() {
        return vodInfo;
    }

    public void setVodInfo(String vodInfo) {
        this.vodInfo = vodInfo;
    }

    public static Study create(StudyData studyData,
                               int studyLectureNo){
        StudyItemVO studyItem = studyData.getStudyItemVO();
        Study study = new Study();
        study.setStudyKey(studyData.getStudyKey());
        study.setLctCode(studyItem.getLctCode());
        study.setStudyOrder(studyItem.getStudyOrder());
        study.setStudySubOrder(studyItem.getStudySubOrder());
        study.setStudyName(studyItem.getStudyName());
        study.setStudyTime(studyItem.getStudyTime());
        study.setStudyVodKey(studyItem.getStudyVodKey());
        study.setStudyLectureNo(studyLectureNo);
        study.setMediaContentKey(null);
        study.setState(0);
        study.setErrorCode(-1);
        study.setVodInfo(null);
        return study;
    }

    public static String makeStudyKey(int studyLectureNo, int lctCode){
        return String.valueOf(studyLectureNo) + "-" + String.valueOf(lctCode);
    }
}
