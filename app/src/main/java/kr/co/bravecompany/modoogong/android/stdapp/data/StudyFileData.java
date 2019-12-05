package kr.co.bravecompany.modoogong.android.stdapp.data;

import kr.co.bravecompany.api.android.stdapp.api.data.StudyFileItemVO;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;

/**
 * Created by BraveCompany on 2017. 10. 20..
 */

public class StudyFileData {
    private int type = Tags.STUDY_FILE_VIEW_TYPE.STUDY_FILE_ITEM;
    private int number = 0;
    private StudyFileItemVO studyFileItemVO;

    public StudyFileData(StudyFileItemVO studyFileItemVO) {
        this.studyFileItemVO = studyFileItemVO;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public StudyFileItemVO getStudyFileItemVO() {
        return studyFileItemVO;
    }
}
