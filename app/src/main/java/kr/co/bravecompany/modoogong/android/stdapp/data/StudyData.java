package kr.co.bravecompany.modoogong.android.stdapp.data;

import kr.co.bravecompany.api.android.stdapp.api.data.StudyItemVO;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;

/**
 * Created by BraveCompany on 2016. 10. 18..
 */

public class StudyData {
    private boolean isSelected = false;
    private boolean isPlayed = false;

    private int downState = Tags.STUDY_DOWN_STATE_TYPE.STUDY_DOWN_NONE;
    private int downPercents = 0;

    private String studyKey;

    private StudyItemVO studyItemVO;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isPlayed() {
        return isPlayed;
    }

    public void setPlayed(boolean played) {
        isPlayed = played;
    }

    public int getDownState() {
        return downState;
    }

    public void setDownState(int downState) {
        this.downState = downState;
    }

    public int getDownPercents() {
        return downPercents;
    }

    public void setDownPercents(int downPercents) {
        this.downPercents = downPercents;
    }

    public String getStudyKey() {
        return studyKey;
    }

    public void setStudyKey(String studyKey) {
        this.studyKey = studyKey;
    }

    public StudyItemVO getStudyItemVO() {
        return studyItemVO;
    }

    public void setStudyItemVO(StudyItemVO studyItemVO) {
        this.studyItemVO = studyItemVO;
    }
}
