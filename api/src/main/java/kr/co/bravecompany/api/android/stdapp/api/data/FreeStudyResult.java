package kr.co.bravecompany.api.android.stdapp.api.data;

import java.util.ArrayList;

/**
 * Created by BraveCompany on 2016. 11. 10..
 */

public class FreeStudyResult {

    private int totalCount;
    private int itemCount;
    private ArrayList<FreeStudyItemVO> fvods;

    public int getTotalCount() {
        return totalCount;
    }

    public int getItemCount() {
        return itemCount;
    }

    public ArrayList<FreeStudyItemVO> getFvods() {
        return fvods;
    }
}

/*
public class FreeStudyResult {

    public int totalCount;
    public int itemCount;
    public ArrayList<FreeStudyItemVO> fvods;

    public int getTotalCount() {
        return totalCount;
    }

    public int getItemCount() {
        return itemCount;
    }

    public ArrayList<FreeStudyItemVO> getFvods() {
        return fvods;
    }
}
*/