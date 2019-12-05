package kr.co.bravecompany.modoogong.android.stdapp.data;

import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import kr.co.bravecompany.modoogong.android.stdapp.db.model.Lecture;
import kr.co.bravecompany.modoogong.android.stdapp.db.model.Study;

/**
 * Created by BraveCompany on 2016. 10. 20..
 */

public class DownloadData {
    private boolean isSelected = false;
    private boolean isPaused = false;

    private int downState = Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_NONE;
    private int errorCode = -1;
    private int downPercents = 0;

    private Lecture lectureVO;
    private Study studyVO;


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public int getDownState() {
        return downState;
    }

    public void setDownState(int downState) {
        this.downState = downState;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getDownPercents() {
        return downPercents;
    }

    public void setDownPercents(int downPercents) {
        this.downPercents = downPercents;
    }

    public Lecture getLectureVO() {
        return lectureVO;
    }

    public void setLectureVO(Lecture lectureVO) {
        this.lectureVO = lectureVO;
    }

    public Study getStudyVO() {
        return studyVO;
    }

    public void setStudyVO(Study studyVO) {
        this.studyVO = studyVO;
    }
}
