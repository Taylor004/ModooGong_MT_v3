package kr.co.bravecompany.modoogong.android.stdapp.data;

import androidx.fragment.app.Fragment;

/**
 * Created by BraveCompany on 2016. 12. 16..
 */

public class FreeExplainLectureData {
    private Fragment fragment;
    private int examClass;

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public int getExamClass() {
        return examClass;
    }

    public void setExamClass(int examClass) {
        this.examClass = examClass;
    }
}
