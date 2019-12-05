package kr.co.bravecompany.api.android.stdapp.api.data;

import java.util.ArrayList;

/**
 * Created by BraveCompany on 2016. 11. 10..
 */

public class StudyResult {
    private int studyLectureNo;
    private int lectureCode;
    private String lectureName;
    private int totalLessonCount;
    private float studyLessonpercents;
    private int studyLessonCount;
    private int studyLectureState;
    private String studyStartDate;
    private String studyEndDate;
    private int studyState;
    private String recentStudyDate;
    private String lectureStartDay;
    private ArrayList<StudyItemVO> lesson;

    public int getStudyLectureNo() {
        return studyLectureNo;
    }

    public int getLectureCode() {
        return lectureCode;
    }

    public String getLectureName() {
        return lectureName;
    }

    public int getTotalLessonCount() {
        return totalLessonCount;
    }

    public float getStudyLessonpercents() {
        return studyLessonpercents;
    }

    public int getStudyLessonCount() {
        return studyLessonCount;
    }

    public int getStudyLectureState() {
        return studyLectureState;
    }

    public String getStudyStartDate() {
        return studyStartDate;
    }

    public String getStudyEndDate() {
        return studyEndDate;
    }

    public int getStudyState() {
        return studyState;
    }

    public String getRecentStudyDate() {
        return recentStudyDate;
    }

    public String getLectureStartDay() {
        return lectureStartDay;
    }

    public ArrayList<StudyItemVO> getLesson() {
        return lesson;
    }
}
