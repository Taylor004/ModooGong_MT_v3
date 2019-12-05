package kr.co.bravecompany.api.android.stdapp.api.data;

import java.util.ArrayList;

/**
 * Created by BraveCompany on 2016. 12. 16..
 */

public class FreeExplainStudyResult {

    private int totalCount;
    private int itemCount;
    private ArrayList<FreeExplainStudyItemVO> explains;

    public int getTotalCount() {
        return totalCount;
    }

    public int getItemCount() {
        return itemCount;
    }

    public ArrayList<FreeExplainStudyItemVO> getExplains() {
        return explains;
    }
}
