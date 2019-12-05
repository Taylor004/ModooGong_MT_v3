package kr.co.bravecompany.api.android.stdapp.api.data;

/**
 * Created by BraveCompany on 2016. 10. 18..
 */

public class LectureItemVO {
    private int studyLectureNo;
    private int lectureCode;
    private String lectureName;
    private String studyState;
    private int lectureCnt;
    private int teacherCode;
    private String teacherName;
    private String State;
    private String lectureingDays;
    private int studyLessonCount;
    private String studyStartDay;
    private String studyEndDay;
    private String cateName;
    private String lectureStartDay;
    private String subjectName;
    private int subjectCode;



    public int getStudyLectureNo() {
        return studyLectureNo;
    }

    public int getLectureCode() {
        return lectureCode;
    }

    public String getLectureName() {
        return lectureName;
    }

    public String getStudyState() {
        return studyState;
    }

    public int getLectureCnt() {
        return lectureCnt;
    }

    public int getTeacherCode() {
        return teacherCode;
    }

    public int getSubjectCode()
    {
        return subjectCode;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public String getState() {
        return State;
    }

    public String getLectureingDays() {
        return lectureingDays;
    }

    public int getStudyLessonCount() {
        return studyLessonCount;
    }

    public void setStudyLessonCount(int studyLessonCount) {
        this.studyLessonCount = studyLessonCount;
    }

    public String getStudyStartDay() {
        return studyStartDay;
    }

    public String getStudyEndDay() {
        return studyEndDay;
    }

    public String getCateName() {
        return cateName;
    }

    public String getLectureStartDay() {
        return lectureStartDay;
    }

    public String getSubjectName() { return subjectName;}
}
