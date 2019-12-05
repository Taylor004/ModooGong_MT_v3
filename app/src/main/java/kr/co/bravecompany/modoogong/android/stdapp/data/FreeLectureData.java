package kr.co.bravecompany.modoogong.android.stdapp.data;


import androidx.fragment.app.Fragment;

import kr.co.bravecompany.api.android.stdapp.api.data.FreeLectureScateVO;

/**
 * Created by BraveCompany on 2016. 11. 10..
 */

public class FreeLectureData {
    private Fragment fragment;
    private FreeLectureScateVO scateVO;

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public FreeLectureScateVO getScateVO() {
        return scateVO;
    }

    public void setScateVO(FreeLectureScateVO scateVO) {
        this.scateVO = scateVO;
    }
}
