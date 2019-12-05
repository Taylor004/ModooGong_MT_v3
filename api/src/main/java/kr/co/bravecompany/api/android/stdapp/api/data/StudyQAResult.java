package kr.co.bravecompany.api.android.stdapp.api.data;

import java.util.ArrayList;

/**
 * Created by BraveCompany on 2016. 11. 11..
 */

public class StudyQAResult {
    private int totalCount;
    private int itemCount;
    private ArrayList<StudyQAItemVO> inquiries;

    public int getTotalCount() {
        return totalCount;
    }

    public int getItemCount() {
        return itemCount;
    }

    public ArrayList<StudyQAItemVO> getInquiries() {
        return inquiries;
    }
}
