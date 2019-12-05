package kr.co.bravecompany.modoogong.android.stdapp.data;

import kr.co.bravecompany.api.android.stdapp.api.data.NoticeItemVO;

/**
 * Created by BraveCompany on 2016. 10. 25..
 */

public class NoticeData {
    private boolean isNew = false;
    private NoticeItemVO noticeItemVO;

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public NoticeItemVO getNoticeItemVO() {
        return noticeItemVO;
    }

    public void setNoticeItemVO(NoticeItemVO noticeItemVO) {
        this.noticeItemVO = noticeItemVO;
    }
}
