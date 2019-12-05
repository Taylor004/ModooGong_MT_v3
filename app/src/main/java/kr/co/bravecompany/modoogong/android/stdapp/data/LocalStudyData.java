package kr.co.bravecompany.modoogong.android.stdapp.data;

import kr.co.bravecompany.modoogong.android.stdapp.db.model.Study;

/**
 * Created by BraveCompany on 2016. 12. 6..
 */

public class LocalStudyData {
    private boolean isSelected = false;
    private boolean isPlayed = false;

    private Study studyVO;

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

    public Study getStudyVO() {
        return studyVO;
    }

    public void setStudyVO(Study studyVO) {
        this.studyVO = studyVO;
    }
}
