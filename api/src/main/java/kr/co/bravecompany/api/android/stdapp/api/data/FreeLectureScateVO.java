package kr.co.bravecompany.api.android.stdapp.api.data;

import java.util.ArrayList;

/**
 * Created by BraveCompany on 2016. 11. 10..
 */

public class FreeLectureScateVO {
    private String fvodCateNm;
    private int fvodCate;
    private String fvodSCate;
    private ArrayList<FreeStudyItemVO> bestFVod;

    public String getFvodCateNm() {
        return fvodCateNm;
    }

    public int getFvodCate() {
        return fvodCate;
    }

    public String getFvodSCate() {
        return fvodSCate;
    }

    public ArrayList<FreeStudyItemVO> getBestFVod() {
        return bestFVod;
    }
}
