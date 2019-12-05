package kr.co.bravecompany.api.android.stdapp.api.data;

import java.util.ArrayList;

/**
 * Created by BraveCompany on 2016. 11. 10..
 */

public class MainResult {

    private NoticeList notiLists;
    private ArrayList<FreeLectureCate> fvodCates;

    public NoticeList getNotiLists() {
        return notiLists;
    }

    public ArrayList<FreeLectureCate> getFvodCates() {
        return fvodCates;
    }

    public class NoticeList {
        private int totalCount;
        private int itemCount;
        private ArrayList<NoticeItemVO> epilogues;

        public int getTotalCount() {
            return totalCount;
        }

        public int getItemCount() {
            return itemCount;
        }

        public ArrayList<NoticeItemVO> getEpilogues() {
            return epilogues;
        }
    }

    public class FreeLectureCate {
        private int fvodCate;
        private String fvodCateNm;
        private String fvodCateIntro;
        private ArrayList<FreeLectureScateVO> fvodScates;

        public int getFvodCate() {
            return fvodCate;
        }

        public String getFvodCateNm() {
            return fvodCateNm;
        }

        public String getFvodCateIntro() {
            return fvodCateIntro;
        }

        public ArrayList<FreeLectureScateVO> getFvodScates() {
            return fvodScates;
        }
    }
}
