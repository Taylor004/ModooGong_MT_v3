package kr.co.bravecompany.api.android.stdapp.api.data;

import java.util.ArrayList;

/**
 * Created by BraveCompany on 2017. 10. 31..
 */

public class StudyFileResult {
    private int pg;
    private int limit;
    private int total_rows;
    private int max_pg;
    private String is_last_pg;
    private ArrayList<StudyFileItemVO> board_list;

    public int getPg() {
        return pg;
    }

    public int getLimit() {
        return limit;
    }

    public int getTotal_rows() {
        return total_rows;
    }

    public int getMax_pg() {
        return max_pg;
    }

    public String getIs_last_pg() {
        return is_last_pg;
    }

    public ArrayList<StudyFileItemVO> getBoard_list() {
        return board_list;
    }
}
