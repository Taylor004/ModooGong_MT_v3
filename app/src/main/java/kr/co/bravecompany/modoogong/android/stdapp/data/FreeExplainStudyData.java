package kr.co.bravecompany.modoogong.android.stdapp.data;

import kr.co.bravecompany.api.android.stdapp.api.data.FreeExplainStudyItemVO;

/**
 * Created by BraveCompany on 2016. 12. 16..
 */

public class FreeExplainStudyData {
    private boolean isPlayed = false;
    private FreeExplainStudyItemVO freeExplainStudyItemVO;

    public boolean isPlayed() {
        return isPlayed;
    }

    public void setPlayed(boolean played) {
        isPlayed = played;
    }

    public FreeExplainStudyItemVO getFreeExplainStudyItemVO() {
        return freeExplainStudyItemVO;
    }

    public void setFreeExplainStudyItemVO(FreeExplainStudyItemVO freeExplainStudyItemVO) {
        this.freeExplainStudyItemVO = freeExplainStudyItemVO;
    }
}
