package kr.co.bravecompany.modoogong.android.stdapp.db.model;

import androidx.annotation.Nullable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import kr.co.bravecompany.api.android.stdapp.api.data.LectureItemVO;

/**
 * Created by BraveCompany on 2016. 12. 5..
 */

public class Lecture extends RealmObject {

    @PrimaryKey
    private int studyLectureNo;
    private int lectureCode;
    private String teacherName;
    private String cateName;
    private String lectureName;
    private String studyStartDay;
    private String studyEndDay;
    private String studyState;

    public boolean favMark;
    public boolean newMark;
    public long updatedTime;

    public int getStudyLectureNo() {
        return studyLectureNo;
    }

    public void setStudyLectureNo(int studyLectureNo) {
        this.studyLectureNo = studyLectureNo;
    }

    public int getLectureCode() {
        return lectureCode;
    }

    public void setLectureCode(int lectureCode) {
        this.lectureCode = lectureCode;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }

    public String getLectureName() {
        return lectureName;
    }

    public void setLectureName(String lectureName) {
        this.lectureName = lectureName;
    }

    public String getStudyStartDay() {
        return studyStartDay;
    }

    public void setStudyStartDay(String studyStartDay) {
        this.studyStartDay = studyStartDay;
    }

    public String getStudyEndDay() {
        return studyEndDay;
    }

    public void setStudyEndDay(String studyEndDay) {
        this.studyEndDay = studyEndDay;
    }

    public String getStudyState() {
        return studyState;
    }

    public void setStudyState(String studyState) {
        this.studyState = studyState;
    }

    public static Lecture create(LectureItemVO lectureItem){
        Lecture lecture = new Lecture();
        lecture.setStudyLectureNo(lectureItem.getStudyLectureNo());
        lecture.setLectureCode(lectureItem.getLectureCode());
        lecture.setTeacherName(lectureItem.getTeacherName());
        lecture.setCateName(lectureItem.getCateName());
        lecture.setLectureName(lectureItem.getLectureName());
        lecture.setStudyStartDay(lectureItem.getStudyStartDay());
        lecture.setStudyEndDay(lectureItem.getStudyEndDay());
        lecture.setStudyState(lectureItem.getStudyState());
        return lecture;
    }
}
