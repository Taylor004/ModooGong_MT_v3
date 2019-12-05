package kr.co.bravecompany.modoogong.android.stdapp.data;

import kr.co.bravecompany.api.android.stdapp.api.data.LectureItemVO;

/**
 * Created by BraveCompany on 2016. 10. 19..
 */

public class StudyHeaderData {
    private int lectureCnt = 0;
    private int selectCnt = 0;
    private boolean isShowSelectAll = true;
    private LectureItemVO lectureItemVO;

    public int getLectureCnt() {
        return lectureCnt;
    }

    public void setLectureCnt(int lectureCnt) {
        this.lectureCnt = lectureCnt;
    }

    public int getSelectCnt() {
        return selectCnt;
    }

    public void setSelectCnt(int selectCnt) {
        this.selectCnt = selectCnt;
    }

    public boolean isShowSelectAll() {
        return isShowSelectAll;
    }

    public void setShowSelectAll(boolean showSelectAll) {
        isShowSelectAll = showSelectAll;
    }

    public LectureItemVO getLectureItemVO() {
        return lectureItemVO;
    }

    public void setLectureItemVO(LectureItemVO lectureItemVO) {
        this.lectureItemVO = lectureItemVO;
    }
}
