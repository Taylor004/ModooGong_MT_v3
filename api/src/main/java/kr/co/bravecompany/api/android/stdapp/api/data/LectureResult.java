package kr.co.bravecompany.api.android.stdapp.api.data;

import java.util.ArrayList;

/**
 * Created by BraveCompany on 2016. 11. 9..
 */

public class LectureResult {
    private int totalCount;
    private int itemCount;
    private ArrayList<LectureItemVO> studies;

    public int getTotalCount() {
        return totalCount;
    }

    public int getItemCount() {
        return itemCount;
    }

    public ArrayList<LectureItemVO> getStudies() {
        return studies;
    }
}
