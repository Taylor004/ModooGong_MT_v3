package kr.co.bravecompany.api.android.stdapp.api.data;

import java.util.ArrayList;

/**
 * Created by BraveCompany on 2016. 11. 11..
 */

public class NoticeResult {
    private int totalCount;
    private int itemCount;
    private ArrayList<NoticeItemVO> notices;

    public int getTotalCount() {
        return totalCount;
    }

    public int getItemCount() {
        return itemCount;
    }

    public ArrayList<NoticeItemVO> getNotices() {
        return notices;
    }
}
