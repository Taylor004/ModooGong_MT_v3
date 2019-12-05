package kr.co.bravecompany.modoogong.android.stdapp.data;

import kr.co.bravecompany.api.android.stdapp.api.data.LectureItemVO;

/**
 * Created by BraveCompany on 2016. 10. 21..
 */

public class LectureData {
    private int type;
    private LectureItemVO lectureItemVO;

    public boolean markNew = false;
    public boolean markFab = false;
    public long updatedTime =0;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public LectureItemVO getLectureItemVO() {
        return lectureItemVO;
    }

    public void setLectureItemVO(LectureItemVO lectureItemVO) {
        this.lectureItemVO = lectureItemVO;
    }
}
